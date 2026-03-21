<template>
  <div class="chat-container">
    <!-- 聊天消息区域 -->
    <div class="chat-messages" ref="messagesRef">
      <div
        v-for="(message, index) in sseMessages"
        :key="index"
        :class="[
          'message',
          message.role === 'user' ? 'user-message' : 'bot-message'
        ]"
      >
        <el-avatar
          v-if="message.role === 'bot'"
          :size="40"
          src="https://pic.yupi.icu/5563/202502081502809.png"
          class="avatar"
        />
        <MdPreview
          class="message-content"
          editor-id="mdPreview"
          :modelValue="message.content"
          previewTheme="github"
          showCodeRowNumber
        />
        <el-avatar
          v-if="message.role === 'user'"
          :size="40"
          :src="GET_AVATAR()"
          class="avatar"
        />
      </div>
    </div>

    <!-- 用户输入区域 -->
    <div class="chat-input">
      <el-input
        type="textarea"
        :disabled="isLoading"
        v-model="inputMessage"
        placeholder="请输入您的景点偏好信息(自然风光、历史文化、休闲娱乐，身处位置（北京、浙江），喜欢的交通方式等等)"
        @keyup.enter="sendMessage"
      >
      </el-input>
      <div class="action-buttons">
        <el-button type="primary" @click="sendMessage" :loading="isLoading"
          >发送
        </el-button>
        <el-button
          type="success"
          @click="sendMultiAgentPlan"
          :loading="isLoading"
          >协作规划
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref } from "vue";
import { GET_AVATAR, GET_ID } from "@/utils/token";
import { MdPreview } from "md-editor-v3";
import "md-editor-v3/lib/style.css";
import { addUserAiMessageUsingPost } from "@/api/userAiMessageController";
import { generateTourismMultiAgentPlanUsingPost } from "@/api/tourismAgentController";
import { ElMessage } from "element-plus";

// 消息类型
interface Message {
  role: "user" | "bot";
  content: string;
}

// 加载中状态
const isLoading = ref(false);
// 用户输入的消息
const inputMessage = ref("");
// 消息容器的引用
const messagesRef = ref<HTMLElement | null>(null);
// SSE消息
const sseMessages = ref<Message[]>([]);

const extractErrorMessage = (error: any, fallback: string) => {
  return error?.response?.data?.message || error?.message || fallback;
};

// 发送消息
const sendMessage = async () => {
  console.log("[AITalk] sendMessage triggered", inputMessage.value);
  isLoading.value = true;
  ElMessage.success({
    duration: 3000,
    message: "人多时，AI 接口调用缓慢，一般为 15-30 秒，注意等待"
  });
  if (!inputMessage.value.trim()) {
    console.warn("[AITalk] input is empty");
    isLoading.value = false;
    return;
  }

  // 添加用户消息
  sseMessages.value.push({ role: "user", content: inputMessage.value });

  scrollToBottom();

  // 添加机器人消息占位符
  sseMessages.value.push({ role: "bot", content: "" });

  try {
    console.log("[AITalk] requesting /api/userAiMessage/add", {
      userInputText: inputMessage.value,
      userId: GET_ID()
    });
    const res = await addUserAiMessageUsingPost({
      userInputText: inputMessage.value,
      userId: GET_ID()
    });
    console.log("[AITalk] /api/userAiMessage/add response", res);
    if (res.code !== 200) {
      sseMessages.value.pop();
      return ElMessage.error({
        duration: 3500,
        message: res.message || "调用 AI 接口失败"
      });
    }
    // 更新现有的 bot 消息内容
    const lastMessage = sseMessages.value[sseMessages.value.length - 1];
    if (lastMessage && lastMessage.role === "bot") {
      lastMessage.content += res.data.aiGenerateText;
    } else {
      sseMessages.value.push({ role: "bot", content: res.data.aiGenerateText });
    }
    scrollToBottom();
    inputMessage.value = "";
  } catch (error: any) {
    sseMessages.value.pop();
    ElMessage.error({
      duration: 5000,
      message: extractErrorMessage(error, "调用 AI 接口失败")
    });
    console.error("[AITalk] /api/userAiMessage/add error", error);
  } finally {
    isLoading.value = false;
  }
};

const sendMultiAgentPlan = async () => {
  console.log("[AITalk] sendMultiAgentPlan triggered", inputMessage.value);
  if (!inputMessage.value.trim()) {
    console.warn("[AITalk] multi-agent input is empty");
    return;
  }
  isLoading.value = true;
  sseMessages.value.push({ role: "user", content: inputMessage.value });
  scrollToBottom();
  sseMessages.value.push({ role: "bot", content: "" });
  try {
    console.log("[AITalk] requesting /api/tourismAgent/plan", {
      userInputText: inputMessage.value,
      recommendSize: 6
    });
    const res = await generateTourismMultiAgentPlanUsingPost({
      userInputText: inputMessage.value,
      recommendSize: 6
    });
    console.log("[AITalk] /api/tourismAgent/plan response", res);
    if (res.code !== 200) {
      sseMessages.value.pop();
      ElMessage.error(res.message || "多智能协作规划失败");
      return;
    }
    const result = res.data;
    const lastMessage = sseMessages.value[sseMessages.value.length - 1];
    const workflowText = (result.workflowSummary || [])
      .map((item: string) => `- ${item}`)
      .join("\n");
    const botContent = [
      result.explanationMarkdown || "",
      workflowText ? `\n### 协作流程\n${workflowText}` : ""
    ].join("\n");
    if (lastMessage && lastMessage.role === "bot") {
      lastMessage.content = botContent;
    } else {
      sseMessages.value.push({ role: "bot", content: botContent });
    }
    inputMessage.value = "";
    scrollToBottom();
  } catch (error: any) {
    sseMessages.value.pop();
    ElMessage.error(extractErrorMessage(error, "多智能协作规划失败"));
    console.error("[AITalk] /api/tourismAgent/plan error", error);
  } finally {
    isLoading.value = false;
  }
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight;
    }
  });
};

// 初始化欢迎消息
onMounted(() => {
  sseMessages.value.push({
    role: "bot",
    content: "欢迎使用 ProChat ，我是你的专属旅游推荐官。"
  });
});
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
}

.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
}

.message-content {
  max-width: 70%;
  padding: 10px;
  border-radius: 8px;
  background-color: #e0f7fa;
  margin: 0 10px;
}

.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  background-color: #bbdefb;
}

.bot-message {
  justify-content: flex-start;
}

.avatar {
  flex-shrink: 0;
}

.chat-input {
  display: flex;
  gap: 10px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>

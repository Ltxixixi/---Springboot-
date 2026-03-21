<template>
  <div class="chat-container">
    <div class="chat-main has-plan">
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

      <div class="plan-panel">
        <div v-if="latestPlanResult" class="plan-dashboard">
          <div class="dashboard-header">
            <div>
              <div class="dashboard-kicker">多智能协作结果</div>
              <h3 class="dashboard-title">
                推荐智能体 + 规划智能体 + 讲解智能体
              </h3>
            </div>
            <div class="dashboard-summary">
              <div class="summary-chip">
                {{ latestPlanResult.recommendationSummary }}
              </div>
              <div class="summary-chip">
                {{ latestPlanResult.routeSummary }}
              </div>
            </div>
          </div>

          <div class="overview-grid">
            <div class="overview-card">
              <div class="overview-label">需求解析</div>
              <div class="overview-value">
                {{
                  latestPlanResult.preferenceAnalysis?.analysisSummary || "暂无"
                }}
              </div>
              <div class="tag-group">
                <span
                  v-for="tag in latestPlanResult.preferenceAnalysis
                    ?.parsedTagList || []"
                  :key="tag"
                  class="inline-tag"
                >
                  {{ tag }}
                </span>
              </div>
            </div>
            <div class="overview-card">
              <div class="overview-label">路线概览</div>
              <div class="overview-metrics">
                <div class="metric-item">
                  <span class="metric-number">{{
                    latestPlanResult.routePlan?.totalDays || 0
                  }}</span>
                  <span class="metric-label">天数</span>
                </div>
                <div class="metric-item">
                  <span class="metric-number">{{
                    latestPlanResult.routePlan?.totalSpotCount || 0
                  }}</span>
                  <span class="metric-label">景点</span>
                </div>
                <div class="metric-item">
                  <span class="metric-number">{{
                    latestPlanResult.routePlan?.totalEstimatedCost || 0
                  }}</span>
                  <span class="metric-label">预算(元)</span>
                </div>
              </div>
            </div>
          </div>

          <div class="dashboard-section">
            <div class="section-title">推荐景点</div>
            <div class="spot-grid">
              <div
                v-for="spot in latestPlanResult.recommendationSpotList || []"
                :key="spot.id"
                class="spot-card"
              >
                <img
                  v-if="spot.spotAvatar"
                  :src="spot.spotAvatar"
                  :alt="spot.spotName"
                  class="spot-avatar"
                />
                <div class="spot-content">
                  <div class="spot-name">{{ spot.spotName }}</div>
                  <div class="spot-location">
                    {{ spot.spotLocation || "未知地区" }}
                  </div>
                  <div class="spot-reason">
                    {{ spot.recommendReason || "综合推荐" }}
                  </div>
                  <div class="tag-group">
                    <span
                      v-for="tag in (spot.spotTagList || []).slice(0, 4)"
                      :key="tag"
                      class="inline-tag muted"
                    >
                      {{ tag }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="dashboard-two-column">
            <div class="dashboard-section">
              <div class="section-title">推荐亮点</div>
              <div class="insight-list">
                <div
                  v-for="(
                    item, index
                  ) in latestPlanResult.recommendationHighlightList || []"
                  :key="`recommend-${index}`"
                  class="insight-item"
                >
                  {{ item }}
                </div>
              </div>
            </div>
            <div class="dashboard-section">
              <div class="section-title">路线亮点</div>
              <div class="insight-list">
                <div
                  v-for="(item, index) in latestPlanResult.routeHighlightList ||
                  []"
                  :key="`route-${index}`"
                  class="insight-item"
                >
                  {{ item }}
                </div>
              </div>
            </div>
          </div>

          <div class="dashboard-section">
            <div class="section-title">逐日安排</div>
            <div class="day-grid">
              <div
                v-for="day in latestPlanResult.routePlan?.dayPlanList || []"
                :key="day.dayNumber"
                class="day-card"
              >
                <div class="day-header">
                  <span class="day-title">第 {{ day.dayNumber }} 天</span>
                  <span class="day-cost"
                    >约 {{ day.estimatedCost || 0 }} 元</span
                  >
                </div>
                <div class="day-route">{{ day.routeDescription }}</div>
                <div class="day-distance">
                  预计里程 {{ formatDistance(day.totalDistance) }}
                </div>
                <div class="day-spot-list">
                  <span
                    v-for="spotName in day.spotNameList || []"
                    :key="`${day.dayNumber}-${spotName}`"
                    class="day-spot"
                  >
                    {{ spotName }}
                  </span>
                </div>
              </div>
            </div>
          </div>

          <div class="dashboard-section">
            <div class="section-title">协作流程</div>
            <div class="workflow-list">
              <div
                v-for="(item, index) in latestPlanResult.workflowSummary || []"
                :key="`workflow-${index}`"
                class="workflow-item"
              >
                <span class="workflow-index">{{ index + 1 }}</span>
                <span>{{ item }}</span>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="plan-empty">
          <div class="plan-empty-kicker">多智能协作面板</div>
          <h3>还没有生成路线方案</h3>
          <p>
            请输入出行城市、天数、偏好和预算，然后点击右下角绿色按钮
            <strong>协作规划</strong>。
          </p>
          <div class="empty-tips">
            <span>示例：北京3天，历史文化+美食，预算1500元</span>
            <span>推荐智能体负责景点筛选</span>
            <span>规划智能体负责逐日路线</span>
            <span>讲解智能体负责可读讲解文案</span>
          </div>
        </div>
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
import {
  addUserAiMessageUsingPost,
  listMyUserAiMessageVoByPageUsingPost
} from "@/api/userAiMessageController";
import { generateTourismMultiAgentPlanUsingPost } from "@/api/tourismAgentController";
import { ElMessage } from "element-plus";

// 消息类型
interface Message {
  role: "user" | "bot";
  content: string;
}

interface SpotCard {
  id: number;
  spotName: string;
  spotAvatar?: string;
  spotLocation?: string;
  spotTagList?: string[];
  recommendReason?: string;
}

interface DayPlan {
  dayNumber: number;
  estimatedCost?: number | string;
  totalDistance?: number;
  routeDescription?: string;
  spotNameList?: string[];
}

interface HistoryMessageRecord {
  userInputText?: string;
  aiGenerateText?: string;
}

interface TourismMultiAgentResult {
  preferenceAnalysis?: {
    analysisSummary?: string;
    parsedTagList?: string[];
  };
  recommendationSpotList?: SpotCard[];
  recommendationSummary?: string;
  recommendationHighlightList?: string[];
  routeSummary?: string;
  routeHighlightList?: string[];
  workflowSummary?: string[];
  explanationMarkdown?: string;
  routePlan?: {
    totalDays?: number;
    totalSpotCount?: number;
    totalEstimatedCost?: number | string;
    dayPlanList?: DayPlan[];
  };
}

// 加载中状态
const isLoading = ref(false);
// 用户输入的消息
const inputMessage = ref("");
// 消息容器的引用
const messagesRef = ref<HTMLElement | null>(null);
// SSE消息
const sseMessages = ref<Message[]>([]);
const latestPlanResult = ref<TourismMultiAgentResult | null>(null);
const HISTORY_PAGE_SIZE = 20;

const extractErrorMessage = (error: any, fallback: string) => {
  return error?.response?.data?.message || error?.message || fallback;
};

const formatDistance = (distance?: number) => {
  if (distance === undefined || distance === null || Number.isNaN(distance)) {
    return "0.00 km";
  }
  return `${distance.toFixed(2)} km`;
};

const loadHistoryMessages = async () => {
  try {
    const res = await listMyUserAiMessageVoByPageUsingPost({
      current: 1,
      pageSize: HISTORY_PAGE_SIZE,
      sortField: "createTime",
      sortOrder: "descend"
    });
    if (res.code !== 200) {
      return;
    }
    const records = (res.data?.records || []) as HistoryMessageRecord[];
    if (!records.length) {
      return;
    }
    const historyMessages: Message[] = [];
    records
      .slice()
      .reverse()
      .forEach((record) => {
        const userText = record.userInputText?.trim();
        const aiText = record.aiGenerateText?.trim();
        if (userText) {
          historyMessages.push({ role: "user", content: userText });
        }
        if (aiText) {
          historyMessages.push({ role: "bot", content: aiText });
        }
      });
    if (historyMessages.length) {
      sseMessages.value = historyMessages;
    }
  } catch (error) {
    // 历史消息加载失败不阻塞页面
    ElMessage.warning("历史消息加载失败，可继续正常对话");
  }
};

// 发送消息
const sendMessage = async () => {
  isLoading.value = true;
  ElMessage.success({
    duration: 3000,
    message: "人多时，AI 接口调用缓慢，一般为 15-30 秒，注意等待"
  });
  if (!inputMessage.value.trim()) {
    isLoading.value = false;
    return;
  }

  // 添加用户消息
  sseMessages.value.push({ role: "user", content: inputMessage.value });

  scrollToBottom();

  // 添加机器人消息占位符
  sseMessages.value.push({ role: "bot", content: "" });

  try {
    const res = await addUserAiMessageUsingPost({
      userInputText: inputMessage.value,
      userId: GET_ID()
    });
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
  } finally {
    isLoading.value = false;
  }
};

const sendMultiAgentPlan = async () => {
  if (!inputMessage.value.trim()) {
    return;
  }
  isLoading.value = true;
  sseMessages.value.push({ role: "user", content: inputMessage.value });
  scrollToBottom();
  sseMessages.value.push({ role: "bot", content: "" });
  try {
    const res = await generateTourismMultiAgentPlanUsingPost({
      userInputText: inputMessage.value,
      recommendSize: 6
    });
    if (res.code !== 200) {
      sseMessages.value.pop();
      ElMessage.error(res.message || "多智能协作规划失败");
      return;
    }
    const result = res.data as TourismMultiAgentResult;
    latestPlanResult.value = result;
    const lastMessage = sseMessages.value[sseMessages.value.length - 1];
    const workflowText = (result.workflowSummary || [])
      .map((item: string) => `- ${item}`)
      .join("\n");
    const botContent = [
      result.recommendationSummary
        ? `### 推荐摘要\n${result.recommendationSummary}`
        : "",
      result.routeSummary ? `\n### 路线摘要\n${result.routeSummary}` : "",
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
onMounted(async () => {
  await loadHistoryMessages();
  if (!sseMessages.value.length) {
    sseMessages.value.push({
      role: "bot",
      content: "欢迎使用 ProChat ，我是你的专属旅游推荐官。"
    });
  }
  scrollToBottom();
});
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  padding: 18px;
  box-sizing: border-box;
  background-color: #f5f5f5;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.chat-main {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: 1fr;
  gap: 14px;
  margin-bottom: 14px;
}

.chat-main.has-plan {
  grid-template-columns: minmax(0, 1.15fr) minmax(360px, 1fr);
}

.chat-messages {
  min-height: 0;
  overflow-y: auto;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
}

.plan-panel {
  min-height: 0;
  overflow: hidden;
}

.plan-empty {
  height: 100%;
  min-height: 380px;
  border-radius: 20px;
  padding: 24px;
  background: linear-gradient(
      135deg,
      rgba(227, 243, 255, 0.95),
      rgba(255, 251, 237, 0.95)
    ),
    #fff;
  border: 1px solid rgba(92, 143, 255, 0.2);
  box-shadow: 0 18px 40px rgba(33, 71, 120, 0.08);
  color: #2f4b66;
}

.plan-empty-kicker {
  font-size: 13px;
  color: #5f7ea1;
  letter-spacing: 0.08em;
}

.plan-empty h3 {
  margin: 8px 0 10px;
  font-size: 24px;
  color: #17324d;
}

.plan-empty p {
  margin: 0;
  line-height: 1.8;
}

.empty-tips {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.empty-tips span {
  padding: 10px 12px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid rgba(92, 143, 255, 0.12);
}

.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
}

.message-content {
  width: fit-content;
  max-width: min(86%, 760px);
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
  flex-shrink: 0;
  padding: 12px;
  border-radius: 12px;
  background: #fff;
  border: 1px solid #e6edf5;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.plan-dashboard {
  height: 100%;
  overflow-y: auto;
  margin-bottom: 0;
  padding: 24px;
  border-radius: 20px;
  background: linear-gradient(
      135deg,
      rgba(223, 242, 255, 0.95),
      rgba(255, 249, 234, 0.95)
    ),
    #fff;
  border: 1px solid rgba(92, 143, 255, 0.18);
  box-shadow: 0 18px 40px rgba(33, 71, 120, 0.08);
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 20px;
}

.dashboard-kicker {
  font-size: 13px;
  color: #5f7ea1;
  letter-spacing: 0.08em;
}

.dashboard-title {
  margin: 6px 0 0;
  font-size: 24px;
  color: #17324d;
}

.dashboard-summary {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 420px;
}

.summary-chip {
  padding: 10px 14px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.75);
  color: #35506d;
  font-size: 14px;
  line-height: 1.6;
}

.overview-grid {
  display: grid;
  grid-template-columns: 1.3fr 1fr;
  gap: 16px;
  margin-bottom: 22px;
}

.overview-card,
.dashboard-section,
.spot-card,
.day-card {
  background: rgba(255, 255, 255, 0.86);
  border: 1px solid rgba(92, 143, 255, 0.12);
  box-shadow: 0 14px 30px rgba(45, 83, 135, 0.08);
}

.overview-card {
  padding: 18px;
  border-radius: 18px;
}

.overview-label {
  margin-bottom: 10px;
  font-size: 13px;
  color: #6282a3;
}

.overview-value {
  color: #20354b;
  line-height: 1.8;
}

.overview-metrics {
  display: flex;
  gap: 14px;
}

.metric-item {
  flex: 1;
  padding: 14px 10px;
  border-radius: 16px;
  background: linear-gradient(180deg, #f4f9ff, #ffffff);
  text-align: center;
}

.metric-number {
  display: block;
  margin-bottom: 4px;
  font-size: 26px;
  font-weight: 700;
  color: #1663d6;
}

.metric-label {
  font-size: 12px;
  color: #6b84a2;
}

.dashboard-section {
  margin-bottom: 18px;
  padding: 20px;
  border-radius: 18px;
}

.section-title {
  margin-bottom: 14px;
  font-size: 18px;
  font-weight: 600;
  color: #17324d;
}

.spot-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 14px;
}

.spot-card {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border-radius: 18px;
}

.spot-avatar {
  width: 100%;
  height: 150px;
  object-fit: cover;
  background: #eef4fb;
}

.spot-content {
  padding: 16px;
}

.spot-name {
  font-size: 18px;
  font-weight: 700;
  color: #1d344c;
}

.spot-location {
  margin-top: 6px;
  font-size: 13px;
  color: #6483a2;
}

.spot-reason {
  margin-top: 10px;
  font-size: 14px;
  line-height: 1.7;
  color: #324f6d;
}

.tag-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.inline-tag {
  padding: 4px 10px;
  border-radius: 999px;
  background: #e9f4ff;
  color: #1f67c8;
  font-size: 12px;
}

.inline-tag.muted {
  background: #f4f7fb;
  color: #6d839a;
}

.dashboard-two-column {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.insight-list,
.workflow-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.insight-item,
.workflow-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 12px 14px;
  border-radius: 14px;
  background: #f7fbff;
  color: #314d68;
  line-height: 1.7;
}

.workflow-index {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: #2c7df0;
  color: #fff;
  font-size: 12px;
  flex-shrink: 0;
}

.day-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 14px;
}

.day-card {
  padding: 18px;
  border-radius: 18px;
}

.day-header {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: center;
}

.day-title {
  font-size: 18px;
  font-weight: 700;
  color: #19334b;
}

.day-cost {
  font-size: 13px;
  color: #34794f;
}

.day-route {
  margin-top: 12px;
  color: #35536f;
  line-height: 1.7;
}

.day-distance {
  margin-top: 10px;
  font-size: 13px;
  color: #6a8298;
}

.day-spot-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.day-spot {
  padding: 6px 10px;
  border-radius: 12px;
  background: #eef6ff;
  color: #255fbc;
  font-size: 12px;
}

@media (max-width: 1280px) {
  .chat-main.has-plan {
    grid-template-columns: 1fr;
  }

  .plan-dashboard {
    max-height: 58vh;
  }
}

@media (max-width: 900px) {
  .dashboard-header,
  .overview-grid,
  .dashboard-two-column {
    grid-template-columns: 1fr;
    display: grid;
  }

  .dashboard-header {
    display: flex;
    flex-direction: column;
  }

  .overview-metrics {
    flex-direction: column;
  }

  .chat-input {
    flex-direction: column;
  }

  .action-buttons {
    flex-direction: row;
  }

  .message-content {
    max-width: 92%;
  }
}
</style>

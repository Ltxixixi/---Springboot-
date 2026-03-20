<template>
  <div class="spot-page">
    <div class="section-header">
      <h1>为你推荐</h1>
      <p>结合用户偏好、评分表现与景点热度生成推荐结果</p>
    </div>
    <SpotList :spotList="recommendSpotList" />

    <div class="section-header">
      <h1>景点列表</h1>
      <p>浏览平台当前已收录的景点信息</p>
    </div>
    <SpotList :spotList="spotList" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import {
  getRecommendSpotsUsingGet,
  listSpotVoByPageUsingPost
} from "@/api/spotController";
import SpotList from "@/components/SpotList/index.vue";
import { ElMessage } from "element-plus";

// 景点列表数据
const spotList = ref([]);
const recommendSpotList = ref([]);

const loadRecommendSpotList = async () => {
  try {
    const res = await getRecommendSpotsUsingGet({
      size: 6
    });
    if (res.code === 200) {
      recommendSpotList.value = res.data || [];
    } else {
      ElMessage.error("获取推荐景点失败");
    }
  } catch (error: any) {
    ElMessage.error("获取推荐景点失败", error);
  }
};

// 加载景点列表
const loadSpotList = async () => {
  try {
    const res = await listSpotVoByPageUsingPost({
      current: 1,
      pageSize: 10
    });
    if (res.code === 200) {
      spotList.value = res.data.records;
    } else {
      ElMessage.error("获取景点列表失败");
    }
  } catch (error: any) {
    ElMessage.error("获取景点列表失败", error);
  }
};

// 初始化加载数据
onMounted(() => {
  loadRecommendSpotList();
  loadSpotList();
});
</script>

<style scoped lang="scss">
.spot-page {
  padding: 20px;
}

.section-header {
  margin-bottom: 12px;

  h1 {
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 6px;
  }

  p {
    color: #666;
    margin-bottom: 16px;
  }
}
</style>

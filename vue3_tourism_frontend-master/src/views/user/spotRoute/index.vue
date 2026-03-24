<template>
  <div class="spot-route-page">
    <el-card class="planner-card">
      <div class="section-header">
        <h1>智能路线规划</h1>
        <p>输入天数、预算、地区和偏好标签，系统会自动生成按天旅游路线。</p>
      </div>

      <el-form :model="plannerForm" label-width="88px" class="planner-form">
        <el-row :gutter="16">
          <el-col :xs="24" :md="6">
            <el-form-item label="游玩天数">
              <el-input-number
                v-model="plannerForm.dayCount"
                :min="1"
                :max="7"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="6">
            <el-form-item label="预算(元)">
              <el-input-number
                v-model="plannerForm.budget"
                :min="0"
                :precision="0"
                :controls="false"
                placeholder="可选"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="6">
            <el-form-item label="目标地区">
              <el-input
                v-model="plannerForm.spotLocation"
                placeholder="如 黄山 / 杭州 / 西湖"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="6">
            <el-form-item label="偏好标签">
              <el-input
                v-model="plannerForm.tagText"
                placeholder="如 自然风光, 亲子, 古镇"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="6">
            <el-form-item label="行程节奏">
              <el-select v-model="plannerForm.paceType" style="width: 100%">
                <el-option label="轻松" value="RELAXED" />
                <el-option label="常规" value="NORMAL" />
                <el-option label="紧凑" value="COMPACT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <div class="planner-actions">
          <el-button
            type="primary"
            :loading="planLoading"
            @click="generatePlan"
          >
            生成路线
          </el-button>
          <el-button @click="resetPlannerForm">重置条件</el-button>
        </div>
      </el-form>
    </el-card>

    <el-card v-if="routePlan" class="result-card">
      <div class="section-header">
        <h2>{{ routePlan.routeTitle }}</h2>
        <p>{{ routePlan.routeDescription }}</p>
      </div>

      <div class="summary-grid">
        <div class="summary-item">
          <span class="label">总天数</span>
          <span class="value">{{ routePlan.totalDays }} 天</span>
        </div>
        <div class="summary-item">
          <span class="label">景点数量</span>
          <span class="value">{{ routePlan.totalSpotCount }} 个</span>
        </div>
        <div class="summary-item">
          <span class="label">预估花费</span>
          <span class="value">{{ routePlan.totalEstimatedCost || 0 }} 元</span>
        </div>
        <div class="summary-item">
          <span class="label">匹配标签</span>
          <span class="value">{{
            routePlan.matchedTagList?.length
              ? routePlan.matchedTagList.join("、")
              : "综合推荐"
          }}</span>
        </div>
        <div class="summary-item">
          <span class="label">行程节奏</span>
          <span class="value">{{
            formatPaceLabel(lastPlanRequest?.paceType || "NORMAL")
          }}</span>
        </div>
      </div>

      <div class="adjust-panel">
        <div class="adjust-header">
          <div>
            <div class="adjust-title">路线微调</div>
            <div class="adjust-subtitle">
              基于当前路线继续优化，支持更轻松、更省钱和增加夜游偏好。
            </div>
          </div>
          <div v-if="routePlan.adjustmentSummary" class="adjust-summary">
            {{ routePlan.adjustmentSummary }}
          </div>
        </div>
        <div class="adjust-actions">
          <el-button
            :loading="adjustLoading"
            @click="adjustPlan('RELAXED', '更轻松')"
          >
            更轻松
          </el-button>
          <el-button
            :loading="adjustLoading"
            @click="adjustPlan('SAVE_BUDGET', '更省钱')"
          >
            更省钱
          </el-button>
          <el-button
            :loading="adjustLoading"
            @click="adjustPlan('ADD_NIGHT_VIEW', '加夜游')"
          >
            加夜游
          </el-button>
        </div>
        <div
          v-if="previousRoutePlan && compareOverviewList.length"
          class="compare-card"
        >
          <div class="adjust-analysis-title">改线前后对比</div>
          <div class="compare-overview-grid">
            <div
              v-for="item in compareOverviewList"
              :key="item.label"
              class="compare-overview-item"
            >
              <div class="compare-label">{{ item.label }}</div>
              <div class="compare-values">
                <span class="compare-before">{{ item.before }}</span>
                <span class="compare-arrow">→</span>
                <span class="compare-after">{{ item.after }}</span>
              </div>
            </div>
          </div>
          <div class="compare-day-list">
            <div
              v-for="item in compareDayList"
              :key="`compare-day-${item.dayNumber}`"
              class="compare-day-item"
            >
              <div class="compare-day-title">第 {{ item.dayNumber }} 天</div>
              <div class="compare-day-row">
                <span class="compare-day-label">改线前</span>
                <span class="compare-day-text">{{ item.before }}</span>
              </div>
              <div class="compare-day-row">
                <span class="compare-day-label after">改线后</span>
                <span class="compare-day-text">{{ item.after }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="day-plan-list">
        <el-card
          v-for="dayPlan in routePlan.dayPlanList"
          :key="dayPlan.dayNumber"
          class="day-plan-card"
        >
          <div class="day-card-header">
            <div class="day-title">第 {{ dayPlan.dayNumber }} 天</div>
            <el-tag size="small" effect="dark" round>
              {{ dayPlan.intensityLevel || "适中" }}
            </el-tag>
          </div>
          <div class="day-desc">{{ dayPlan.routeDescription }}</div>
          <div class="day-meta">
            <span>景点：{{ dayPlan.spotNameList.join(" -> ") }}</span>
            <span>预估花费：{{ dayPlan.estimatedCost || 0 }} 元</span>
            <span>预估里程：{{ formatDistance(dayPlan.totalDistance) }}</span>
            <span>
              建议游玩时长：{{
                formatDuration(dayPlan.totalVisitDurationMinutes)
              }}
            </span>
          </div>
          <div
            v-if="dayPlan.timeBlockList && dayPlan.timeBlockList.length"
            class="time-block-grid"
          >
            <div
              v-for="block in dayPlan.timeBlockList"
              :key="`${dayPlan.dayNumber}-${block.blockKey}`"
              class="time-block-card"
            >
              <div class="time-block-label">{{ block.blockLabel }}</div>
              <div class="time-block-summary">{{ block.summary }}</div>
            </div>
          </div>
          <div
            v-if="dayPlan.scheduleLineList && dayPlan.scheduleLineList.length"
            class="schedule-panel"
          >
            <div class="schedule-title">时间轴安排</div>
            <div class="schedule-list">
              <div
                v-for="(line, index) in dayPlan.scheduleLineList"
                :key="`${dayPlan.dayNumber}-${index}`"
                class="schedule-item"
              >
                {{ line }}
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <div class="section-header section-subtitle">
        <h2>路线包含景点</h2>
        <p>下面是本次智能规划选出的景点及纳入路线的原因。</p>
      </div>
      <SpotList :spotList="routePlan.spotList || []" />
    </el-card>

    <el-card class="route-list-card">
      <div class="section-header">
        <h2>已有路线</h2>
        <p>查看系统中已有的路线方案，也可以和智能规划结果做对比。</p>
      </div>

      <div class="route-list">
        <el-row :gutter="16">
          <el-col :span="8" v-for="item in routeList" :key="item.id">
            <el-card style="margin-bottom: 16px">
              <el-link @click="goRouteDetail(item.id)" :underline="false">
                <el-avatar
                  :src="item.spotRouteAvatar"
                  size="large"
                  style="margin-right: 10px"
                />
                <div class="content">
                  <div class="title">{{ item.spotRouteDescription }}</div>
                  <div class="description">
                    <div>创建时间: {{ item.createTime }}</div>
                    <div>管理员ID: {{ item.adminId }}</div>
                  </div>
                </div>
              </el-link>
            </el-card>
          </el-col>
        </el-row>

        <el-pagination
          style="margin-top: 20px; text-align: center"
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
  adjustRoutePlanUsingPost,
  generateRoutePlanUsingPost,
  listSpotRouteVoByPageUsingPost
} from "@/api/spotRouteController";
import SpotList from "@/components/SpotList/index.vue";

const $router = useRouter();

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const routeList = ref([]);
const planLoading = ref(false);
const adjustLoading = ref(false);
const routePlan = ref<any>(null);
const previousRoutePlan = ref<any>(null);
const lastPlanRequest = ref<any>(null);

const plannerForm = ref({
  dayCount: 2,
  budget: undefined as number | undefined,
  spotLocation: "",
  tagText: "",
  paceType: "NORMAL"
});

const parseTagText = (tagText: string) =>
  tagText
    .split(/[,，]/)
    .map((item) => item.trim())
    .filter(Boolean);

const generatePlan = async () => {
  planLoading.value = true;
  try {
    const requestBody = {
      dayCount: plannerForm.value.dayCount,
      budget: plannerForm.value.budget,
      spotLocation: plannerForm.value.spotLocation,
      spotTagList: parseTagText(plannerForm.value.tagText),
      paceType: plannerForm.value.paceType
    };
    const res = await generateRoutePlanUsingPost(requestBody);
    if (res.code === 200) {
      previousRoutePlan.value = null;
      routePlan.value = res.data;
      lastPlanRequest.value = requestBody;
      ElMessage.success("路线规划生成成功");
      return;
    }
    ElMessage.error(res.message || "路线规划生成失败");
  } catch (error: any) {
    ElMessage.error(error?.message || "路线规划生成失败");
  } finally {
    planLoading.value = false;
  }
};

const resetPlannerForm = () => {
  plannerForm.value = {
    dayCount: 2,
    budget: undefined,
    spotLocation: "",
    tagText: "",
    paceType: "NORMAL"
  };
  previousRoutePlan.value = null;
  routePlan.value = null;
  lastPlanRequest.value = null;
};

const fetchRouteList = async () => {
  try {
    const response = await listSpotRouteVoByPageUsingPost({
      current: currentPage.value,
      pageSize: pageSize.value,
      sortField: "id",
      sortOrder: "descend"
    });
    routeList.value = response?.data?.records || [];
    total.value = response?.data?.total || 0;
  } catch (error: any) {
    ElMessage.error(error?.message || "获取路线列表失败");
  }
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchRouteList();
};

const goRouteDetail = (id: number) => {
  $router.push(`/user/spotRoute/detail/${id}`);
};

const formatDistance = (distance?: number) => {
  if (distance === undefined || distance === null) {
    return "0 公里";
  }
  return `${distance.toFixed(2)} 公里`;
};

const formatDuration = (minutes?: number) => {
  if (!minutes) {
    return "0 小时";
  }
  const hour = Math.floor(minutes / 60);
  const minute = minutes % 60;
  if (hour <= 0) {
    return `${minute} 分钟`;
  }
  if (minute === 0) {
    return `${hour} 小时`;
  }
  return `${hour} 小时 ${minute} 分钟`;
};

const formatPaceLabel = (paceType?: string) => {
  if (paceType === "RELAXED") {
    return "轻松";
  }
  if (paceType === "COMPACT") {
    return "紧凑";
  }
  return "常规";
};

const cloneRoutePlan = (plan: any) => {
  if (!plan) {
    return null;
  }
  return JSON.parse(JSON.stringify(plan));
};

const formatCompareValue = (value: any, suffix = "") => {
  if (value === undefined || value === null || value === "") {
    return "无";
  }
  return `${value}${suffix}`;
};

const summarizeDaySpots = (dayPlan: any) => {
  const spotNameList = dayPlan?.spotNameList || [];
  if (!spotNameList.length) {
    return "暂无安排";
  }
  return spotNameList.join(" -> ");
};

const compareOverviewList = computed(() => {
  if (!previousRoutePlan.value || !routePlan.value) {
    return [];
  }
  return [
    {
      label: "景点数量",
      before: formatCompareValue(previousRoutePlan.value.totalSpotCount, " 个"),
      after: formatCompareValue(routePlan.value.totalSpotCount, " 个")
    },
    {
      label: "总花费",
      before: formatCompareValue(
        previousRoutePlan.value.totalEstimatedCost,
        " 元"
      ),
      after: formatCompareValue(routePlan.value.totalEstimatedCost, " 元")
    },
    {
      label: "路线标题",
      before: formatCompareValue(previousRoutePlan.value.routeTitle),
      after: formatCompareValue(routePlan.value.routeTitle)
    }
  ];
});

const compareDayList = computed(() => {
  if (!previousRoutePlan.value || !routePlan.value) {
    return [];
  }
  const beforeCount = previousRoutePlan.value?.dayPlanList?.length || 0;
  const afterCount = routePlan.value?.dayPlanList?.length || 0;
  const maxDayCount = Math.max(beforeCount, afterCount);
  return Array.from({ length: maxDayCount }, (_, index) => {
    const beforeDay = previousRoutePlan.value?.dayPlanList?.[index];
    const afterDay = routePlan.value?.dayPlanList?.[index];
    return {
      dayNumber: index + 1,
      before: summarizeDaySpots(beforeDay),
      after: summarizeDaySpots(afterDay)
    };
  });
});

const applyAdjustmentLocally = (requestBody: any, adjustType: string) => {
  const nextRequest = {
    ...requestBody,
    spotTagList: [...(requestBody?.spotTagList || [])]
  };
  if (adjustType === "RELAXED") {
    nextRequest.paceType = "RELAXED";
  }
  if (adjustType === "SAVE_BUDGET") {
    const budgetBase = Number(
      nextRequest.budget || routePlan.value?.totalEstimatedCost || 0
    );
    nextRequest.budget =
      budgetBase > 0 ? Math.max(Math.floor(budgetBase * 0.8), 200) : 500;
    nextRequest.paceType = "RELAXED";
  }
  if (adjustType === "ADD_NIGHT_VIEW") {
    if (!nextRequest.spotTagList.includes("夜游")) {
      nextRequest.spotTagList.push("夜游");
    }
  }
  return nextRequest;
};

const adjustPlan = async (adjustType: string, successLabel: string) => {
  if (!lastPlanRequest.value || !routePlan.value) {
    return;
  }
  adjustLoading.value = true;
  try {
    const res = await adjustRoutePlanUsingPost({
      originalRequest: lastPlanRequest.value,
      adjustType,
      currentEstimatedCost: routePlan.value?.totalEstimatedCost
    });
    if (res.code === 200) {
      previousRoutePlan.value = cloneRoutePlan(routePlan.value);
      routePlan.value = res.data;
      lastPlanRequest.value = applyAdjustmentLocally(
        lastPlanRequest.value,
        adjustType
      );
      ElMessage.success(`${successLabel}路线生成成功`);
      return;
    }
    ElMessage.error(res.message || `${successLabel}路线失败`);
  } catch (error: any) {
    ElMessage.error(error?.message || `${successLabel}路线失败`);
  } finally {
    adjustLoading.value = false;
  }
};

onMounted(() => {
  fetchRouteList();
});
</script>

<style scoped lang="scss">
.spot-route-page {
  padding: 20px;
}

.planner-card,
.result-card,
.route-list-card {
  margin-bottom: 20px;
}

.section-header {
  margin-bottom: 16px;

  h1,
  h2 {
    margin: 0 0 8px;
    font-size: 24px;
    font-weight: 700;
  }

  p {
    margin: 0;
    color: #666;
  }
}

.section-subtitle {
  margin-top: 24px;
}

.planner-actions {
  display: flex;
  gap: 12px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.adjust-panel {
  margin-bottom: 20px;
  padding: 16px 18px;
  border-radius: 14px;
  background: linear-gradient(135deg, #f8fbff, #eef5ff);
  border: 1px solid #dbe7ff;
}

.adjust-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 14px;
}

.adjust-title {
  font-size: 16px;
  font-weight: 700;
  color: #204974;
}

.adjust-subtitle {
  margin-top: 4px;
  color: #6a7d92;
  font-size: 13px;
}

.adjust-summary {
  max-width: 420px;
  padding: 10px 12px;
  border-radius: 12px;
  background: #fff;
  color: #315b8f;
  font-size: 13px;
  line-height: 1.6;
}

.adjust-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.adjust-analysis-title {
  font-size: 14px;
  font-weight: 700;
  color: #24496f;
}

.compare-card {
  margin-top: 14px;
  padding: 14px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.88);
  border: 1px solid #dbe7ff;
}

.compare-overview-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.compare-overview-item,
.compare-day-item {
  padding: 12px;
  border-radius: 12px;
  background: #f8fbff;
  border: 1px solid #e1ebfb;
}

.compare-label,
.compare-day-title {
  font-size: 13px;
  font-weight: 700;
  color: #2b4e77;
}

.compare-values {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  flex-wrap: wrap;
}

.compare-before {
  color: #6a7b90;
}

.compare-arrow {
  color: #90a3b9;
}

.compare-after {
  color: #1d4ed8;
  font-weight: 700;
}

.compare-day-list {
  display: grid;
  gap: 10px;
  margin-top: 12px;
}

.compare-day-row {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}

.compare-day-label {
  min-width: 48px;
  color: #7a8da4;
  font-size: 12px;
}

.compare-day-label.after {
  color: #1d4ed8;
  font-weight: 700;
}

.compare-day-text {
  color: #40566d;
  font-size: 13px;
  line-height: 1.6;
}

.summary-item {
  padding: 16px;
  border-radius: 12px;
  background: #f6f9ff;
  display: flex;
  flex-direction: column;
  gap: 8px;

  .label {
    color: #666;
    font-size: 14px;
  }

  .value {
    color: #1d4ed8;
    font-size: 20px;
    font-weight: 700;
  }
}

.day-plan-list {
  display: grid;
  gap: 16px;
}

.day-plan-card {
  border-radius: 12px;
}

.day-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 8px;
}

.day-title {
  font-size: 18px;
  font-weight: 700;
}

.day-desc {
  color: #333;
  margin-bottom: 12px;
}

.day-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 20px;
  color: #666;
  font-size: 14px;
}

.time-block-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
  margin-top: 14px;
}

.time-block-card {
  padding: 14px;
  border-radius: 12px;
  background: linear-gradient(180deg, #ffffff, #f4f8ff);
  border: 1px solid #dbe7ff;
}

.time-block-label {
  font-size: 14px;
  font-weight: 700;
  color: #24538e;
}

.time-block-summary {
  margin-top: 8px;
  color: #4d6177;
  font-size: 13px;
  line-height: 1.7;
}

.schedule-panel {
  margin-top: 14px;
  padding: 14px;
  border-radius: 12px;
  background: linear-gradient(180deg, #f9fbff, #f2f7ff);
  border: 1px solid #dbe7ff;
}

.schedule-title {
  margin-bottom: 10px;
  font-size: 14px;
  font-weight: 700;
  color: #315b8f;
}

.schedule-list {
  display: grid;
  gap: 8px;
}

.schedule-item {
  padding: 10px 12px;
  border-radius: 10px;
  background: #fff;
  color: #3b4b5c;
  font-size: 14px;
  border-left: 3px solid #4f8ef7;
}

.route-list .content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
}

.route-list .title {
  font-size: 18px;
  font-weight: bold;
  margin-left: 10px;
}

.route-list .description {
  font-size: 14px;
  margin: 10px 0 0 10px;
  color: #888;

  div {
    margin-bottom: 5px;
  }
}

@media (max-width: 768px) {
  .adjust-header {
    flex-direction: column;
  }

  .adjust-natural-panel {
    grid-template-columns: 1fr;
  }
}
</style>

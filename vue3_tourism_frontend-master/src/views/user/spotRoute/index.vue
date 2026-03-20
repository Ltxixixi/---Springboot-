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
      </div>

      <div class="day-plan-list">
        <el-card
          v-for="dayPlan in routePlan.dayPlanList"
          :key="dayPlan.dayNumber"
          class="day-plan-card"
        >
          <div class="day-title">第 {{ dayPlan.dayNumber }} 天</div>
          <div class="day-desc">{{ dayPlan.routeDescription }}</div>
          <div class="day-meta">
            <span>景点：{{ dayPlan.spotNameList.join(" -> ") }}</span>
            <span>预估花费：{{ dayPlan.estimatedCost || 0 }} 元</span>
            <span>预估里程：{{ formatDistance(dayPlan.totalDistance) }}</span>
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
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
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
const routePlan = ref<any>(null);

const plannerForm = ref({
  dayCount: 2,
  budget: undefined as number | undefined,
  spotLocation: "",
  tagText: ""
});

const parseTagText = (tagText: string) =>
  tagText
    .split(/[,，]/)
    .map((item) => item.trim())
    .filter(Boolean);

const generatePlan = async () => {
  planLoading.value = true;
  try {
    const res = await generateRoutePlanUsingPost({
      dayCount: plannerForm.value.dayCount,
      budget: plannerForm.value.budget,
      spotLocation: plannerForm.value.spotLocation,
      spotTagList: parseTagText(plannerForm.value.tagText)
    });
    if (res.code === 200) {
      routePlan.value = res.data;
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
    tagText: ""
  };
};

const fetchRouteList = async () => {
  try {
    const response = await listSpotRouteVoByPageUsingPost({
      current: currentPage.value,
      pageSize: pageSize.value
    });
    routeList.value = response.data.records;
    total.value = response.data.total;
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

.day-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
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
</style>

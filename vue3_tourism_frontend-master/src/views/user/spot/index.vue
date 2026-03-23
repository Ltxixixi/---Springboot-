<template>
  <div class="spot-page">
    <div class="section-header">
      <h1>为你推荐</h1>
      <p>结合用户偏好、评分表现与景点热度生成推荐结果</p>
    </div>
    <SpotList :spotList="recommendSpotList" />

    <div class="filter-panel">
      <div class="filter-header">
        <div>
          <h2>景点筛选</h2>
          <p>支持按名称、城市和标签组合筛选，帮你更快定位想看的景点。</p>
        </div>
        <div class="filter-summary">
          共 {{ total }} 个景点，当前匹配 {{ filteredSpotList.length }} 个
        </div>
      </div>

      <el-row :gutter="16">
        <el-col :xs="24" :md="8">
          <el-input
            v-model="filterForm.keyword"
            clearable
            placeholder="搜索景点名，如 西湖 / 洪崖洞"
          />
        </el-col>
        <el-col :xs="24" :md="6">
          <el-select
            v-model="filterForm.location"
            clearable
            placeholder="选择城市"
            style="width: 100%"
          >
            <el-option
              v-for="location in locationOptions"
              :key="location"
              :label="location"
              :value="location"
            />
          </el-select>
        </el-col>
        <el-col :xs="24" :md="8">
          <el-select
            v-model="filterForm.tags"
            multiple
            collapse-tags
            collapse-tags-tooltip
            clearable
            placeholder="选择标签"
            style="width: 100%"
          >
            <el-option
              v-for="tag in tagOptions"
              :key="tag"
              :label="tag"
              :value="tag"
            />
          </el-select>
        </el-col>
        <el-col :xs="24" :md="2">
          <el-button class="reset-button" @click="resetFilters">
            重置
          </el-button>
        </el-col>
      </el-row>
    </div>

    <div class="section-header">
      <h1>景点列表</h1>
      <p>浏览平台当前已收录的全部景点信息，最新导入的景点会优先展示。</p>
    </div>
    <div class="spot-count">
      <span>当前共收录 {{ total }} 个景点</span>
      <span v-if="hasActiveFilters">已启用筛选</span>
    </div>
    <SpotList :spotList="filteredSpotList" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import {
  getRecommendSpotsUsingGet,
  listSpotVoByPageUsingPost
} from "@/api/spotController";
import SpotList from "@/components/SpotList/index.vue";
import { ElMessage } from "element-plus";

// 景点列表数据
const allSpotList = ref<any[]>([]);
const recommendSpotList = ref([]);
const total = ref(0);
const filterForm = ref({
  keyword: "",
  location: "",
  tags: [] as string[]
});

const normalizeText = (value?: string) => (value || "").trim().toLowerCase();

const locationOptions = computed(() => {
  const locationSet = new Set(
    allSpotList.value
      .map((item) => item?.spotLocation)
      .filter((item) => typeof item === "string" && item.trim())
  );
  return Array.from(locationSet);
});

const tagOptions = computed(() => {
  const tagSet = new Set<string>();
  allSpotList.value.forEach((item) => {
    (item?.spotTagList || []).forEach((tag: string) => {
      if (tag && tag.trim()) {
        tagSet.add(tag);
      }
    });
  });
  return Array.from(tagSet);
});

const hasActiveFilters = computed(() => {
  return Boolean(
    filterForm.value.keyword ||
      filterForm.value.location ||
      filterForm.value.tags.length
  );
});

const filteredSpotList = computed(() => {
  const keyword = normalizeText(filterForm.value.keyword);
  const location = filterForm.value.location;
  const selectedTags = filterForm.value.tags;

  return allSpotList.value.filter((item) => {
    const matchKeyword = keyword
      ? normalizeText(item?.spotName).includes(keyword)
      : true;
    const matchLocation = location ? item?.spotLocation === location : true;
    const spotTags = item?.spotTagList || [];
    const matchTags = selectedTags.length
      ? selectedTags.every((tag) => spotTags.includes(tag))
      : true;
    return matchKeyword && matchLocation && matchTags;
  });
});

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
      pageSize: 200,
      sortField: "id",
      sortOrder: "descend"
    });
    if (res.code === 200) {
      allSpotList.value = res?.data?.records || [];
      total.value = res?.data?.total || 0;
    } else {
      ElMessage.error("获取景点列表失败");
    }
  } catch (error: any) {
    ElMessage.error("获取景点列表失败", error);
  }
};

const resetFilters = () => {
  filterForm.value = {
    keyword: "",
    location: "",
    tags: []
  };
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

.filter-panel {
  margin: 18px 0 26px;
  padding: 18px;
  border-radius: 16px;
  background: linear-gradient(135deg, #f7fbff, #eef6ff);
  border: 1px solid #d9e8ff;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 16px;

  h2 {
    margin: 0 0 6px;
    font-size: 20px;
    font-weight: 700;
  }

  p {
    margin: 0;
    color: #66809a;
  }
}

.filter-summary {
  padding: 10px 14px;
  border-radius: 999px;
  background: #ffffff;
  color: #2f5b8a;
  font-size: 13px;
  white-space: nowrap;
}

.spot-count {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  color: #3b5b7a;
  font-size: 14px;
}

.reset-button {
  width: 100%;
}

@media (max-width: 900px) {
  .filter-header {
    flex-direction: column;
  }

  .filter-summary {
    white-space: normal;
  }
}
</style>

// @ts-ignore
/* eslint-disable */
import request from "@/utils/request";

/**
 * 获取景点推荐
 * 基于协同过滤与内容特征融合的混合推荐算法
 */
export async function getSpotRecommendations(
  body: {
    city?: string;
    size?: number;
    preferredTags?: string[];
    budget?: number;
    crowdType?: string;
    recommendMode?: string;
    needReason?: boolean;
  },
  options?: { [key: string]: any }
) {
  return request<any>("/api/spot/recommend", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    data: body,
    ...(options || {})
  });
}

/**
 * 获取默认推荐（首页推荐）
 */
export async function getDefaultRecommendations(
  size: number = 10,
  options?: { [key: string]: any }
) {
  return request<any>("/api/spot/recommend/default", {
    method: "GET",
    params: { size },
    ...(options || {})
  });
}

/**
 * 获取个性化推荐
 */
export async function getPersonalizedRecommendations(
  params: {
    city?: string;
    tags?: string;
    budget?: number;
    crowdType?: string;
    size?: number;
  },
  options?: { [key: string]: any }
) {
  return request<any>("/api/spot/recommend/personalized", {
    method: "GET",
    params,
    ...(options || {})
  });
}

/**
 * 刷新推荐模型（管理员接口）
 */
export async function refreshRecommendModel(options?: { [key: string]: any }) {
  return request<any>("/api/spot/recommend/refresh", {
    method: "POST",
    ...(options || {})
  });
}

/**
 * AI服务状态
 */
export async function getAiServiceStatus(options?: { [key: string]: any }) {
  return request<any>("/api/ai/status", {
    method: "GET",
    ...(options || {})
  });
}

/**
 * AI对话（带稳定性保障）
 */
export async function aiChat(
  body: {
    message: string;
    sessionId?: string;
    context?: any;
  },
  options?: { [key: string]: any }
) {
  return request<any>("/api/tourismAgent/chat", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    data: body,
    ...(options || {})
  });
}

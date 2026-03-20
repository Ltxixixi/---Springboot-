/* eslint-disable */
import request from "@/utils/request";

export async function generateTourismMultiAgentPlanUsingPost(
  body: {
    userInputText: string;
    dayCount?: number;
    budget?: number;
    spotLocation?: string;
    preferredTagList?: string[];
    recommendSize?: number;
  },
  options?: { [key: string]: any }
) {
  return request<any>("/api/tourismAgent/plan", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    data: body,
    ...(options || {})
  });
}

import axios from "axios";
import { ElMessage } from "element-plus";
import useUserStore from "@/store/modules/user";
//创建axios实例
const request = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 60000,
  withCredentials: true
});
//请求拦截器
request.interceptors.request.use((config) => {
  // 获取用户相关的小仓库，获取仓库内部的token,登录成功以后携带给服务器
  const userStore = useUserStore();
  if (userStore.token) {
    config.headers.satoken = userStore.token;
  }
  config.headers.Authorization =
    "Bearer " + window.localStorage.getItem("TOKEN");
  return config;
});
//响应拦截器
request.interceptors.response.use(
  (response) => {
    const payload = response.data;
    // 后端拦截器在未登录时可能返回 { status: 401, msg: "..." } 且 HTTP 仍是 200
    // 这里统一转成 reject，避免业务页面直接读取 res.data.records 报错
    if (
      payload &&
      typeof payload === "object" &&
      Number(payload.status) === 401
    ) {
      return Promise.reject({
        response: {
          status: 401,
          data: {
            message: payload.msg || "未登录"
          }
        },
        message: payload.msg || "未登录"
      });
    }
    return payload;
  },
  (error) => {
    let msg = "";
    const code = error?.response?.status;
    const backendMessage = error?.response?.data?.message;
    if (backendMessage) {
      msg = backendMessage;
    }
    switch (code) {
      case 401:
        msg = msg || "token过期";
        break;
      case 403:
        msg = msg || "无权访问";
        break;
      case 404:
        msg = msg || "请求地址错误";
        break;
      case 500:
        msg = msg || "服务器出现问题";
        break;
      default:
        msg = msg || error?.message || "无网络";
    }
    ElMessage({
      type: "error",
      message: msg
    });
    return Promise.reject(error);
  }
);
export default request;

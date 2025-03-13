import axios from "axios";
import Cookies from "js-cookie";

// =============================================
// ✅ Axios 인스턴스 생성
// =============================================
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "YOUR_BACKEND_URL/api",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

// ✅ 요청 인터셉터: 매 요청마다 자동으로 토큰 추가
api.interceptors.request.use(
  (config) => {
    const token = Cookies.get("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// =============================================
// ✅ AI 챗봇 관련 API
// =============================================

export const getResult = async (id) => {
  try {
    const response = await api.get("/smalltalk/ai/review/" + id);
    return response.data;
  } catch (error) {
    console.error("AI 대화 시작 중 오류 발생:", error);
    throw error;
  }
};

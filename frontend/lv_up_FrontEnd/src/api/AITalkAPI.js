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
export const fetchScenarios = () =>
  api.get("/smalltalk/ai/scenario").then((res) => res.data);

export const fetchPersonalities = () =>
  api.get("/smalltalk/ai/personality").then((res) => res.data);

export const startAIConversation = async (payload) => {
  try {
    const response = await api.post(
      "/smalltalk/ai/conversation/start",
      payload
    );
    return response.data;
  } catch (error) {
    console.error("AI 대화 시작 중 오류 발생:", error);
    throw error;
  }
};

export const continueAIConversation = async (payload) => {
  try {
    const response = await api.post("/smalltalk/ai/conversation/text", payload);
    return response.data;
  } catch (error) {
    console.error("AI 대화 중 오류 발생:", error);
    throw error;
  }
};

export const getAIHint = async (payload) => {
  try {
    const response = await api.post("/smalltalk/ai/conversation/hint", payload);
    console.log("힌트 응답:", response.data);
    return response.data;
  } catch (error) {
    console.error("AI 대화 중 오류 발생:", error);
    throw error;
  }
};

export const finishAIChat = async (id) => {
  try {
    await api.get("/smalltalk/ai/conversation/end/" + id);
  } catch (error) {
    console.error("AI 대화 중 오류 발생:", error);
  }
};

export const endAIChatAll = async () => {
  try {
    await api.get("/smalltalk/ai/conversation/endall");
  } catch (error) {
    console.error("AI 대화 중 오류 발생:", error);
  }
};

// =============================================
// ✅ AI 기록 관련 API
// =============================================

export const fetchAIHistory = async (page, date) => {
  try {
    const response = await api.get(`/smalltalk/ai/history`, {
      params: {
        page,
        date,
      },
    });
    return response;
  } catch (error) {
    console.error("AI 대화 기록 조회 중 오류 발생:", error);
    throw error;
  }
};

//통계 호출

export const fetchStatistics = async (date) => {
  try {
    const response = await api.get(
      `/smalltalk/ai/statistics/` + date.year + `/` + date.month
    );
    return response;
  } catch (error) {
    console.error("AI 대화 기록 조회 중 오류 발생:", error);
    throw error;
  }
};

// 월별 기록 조회
export const fetchAIHistoryByMonth = async (newDate) => {
  try {
    const response = await api.get(`/smalltalk/ai/history/monthly?date=${newDate}`);
    return response;
  } catch (error) {
    console.error('매칭 기록 조회 실패:', error);
    throw error;
  }
};

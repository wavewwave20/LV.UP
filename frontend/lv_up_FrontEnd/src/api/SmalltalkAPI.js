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
// ✅ 소소한 이야기(SmallTalk) 관련 API
// =============================================
export const fetchFeedbackOptions = () => api.get("/smalltalk/user/feedback/options");

export const postFeedback = (feedbackData) => api.post("/smalltalk/user/feedback", feedbackData);

export const fetchMatchMode = () => api.get("/smalltalk/user/match/mode");

export const fetchReportOptions = () => api.get("/smalltalk/user/report/options");

export const postReport = (reportData) => api.post("/smalltalk/user/report", reportData);

export const fetchUserFeedback = (page, orderby) => api.get(`/smalltalk/user/feedback?page=${page}&orderby=${orderby}`);

// =============================================
// ✅ SmallTalk - 매칭 관련 API
// =============================================
export const requestSmalltalkMatch = (modeId, target) =>
  api.post("/smalltalk/user/match", null, {
    params: { modeId, target },
  });

export const cancelSmalltalkMatch = () => api.post("/smalltalk/user/match/cancel");

export const extendSmalltalkMatch = () => api.post("/smalltalk/user/match/extend");

export const endSmalltalkMatch = () => api.post("/smalltalk/user/match/end");

/**
 * 매칭 중 주제(토픽) 추천 API
 * @returns {Promise} - axios 요청의 결과(성공/실패)
 */
export const fetchSmalltalkTopic = () => api.get("/smalltalk/user/match/topic");

// =============================================
// ✅ SmallTalk - WebSocket 관련 API
// =============================================
export const connectSmalltalkWebSocket = ({ onOpen, onMessage, onClose, onError, sendPing = true }) => {
  const token = Cookies.get("token");
  if (!token) {
    throw new Error("토큰이 없습니다. 웹소켓 연결 전에 로그인을 확인해주세요.");
  }

  const ws = new WebSocket(`wss://YOUR_WEBSOKET_URL/ws/match?token=${token}`);
  let pingIntervalId = null;

  ws.onopen = (event) => {
    console.log("[WebSocket] 연결 성공");
    onOpen && onOpen(event);

    if (sendPing) {
      pingIntervalId = setInterval(() => {
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(JSON.stringify({ type: "ping" }));
          console.log("[WebSocket] ping 전송");
        } else {
          clearInterval(pingIntervalId);
        }
      }, 30000);
    }
  };

  ws.onmessage = (event) => {
    console.log("[WebSocket] 메시지 수신:", event.data);
    onMessage && onMessage(event);
  };

  ws.onclose = (event) => {
    console.log("[WebSocket] 연결 종료");
    if (pingIntervalId) {
      clearInterval(pingIntervalId);
    }
    onClose && onClose(event);
  };

  ws.onerror = (error) => {
    console.error("[WebSocket] 에러:", error);
    onError && onError(error);
  };

  return ws;
};

// =============================================
// ✅ SmallTalk - 매칭 기록 관련 API
// =============================================
export const fetchMatchHistory = (page = 0, date) =>
  api.get(`/smalltalk/user/match/history`, {
    params: { page, date },
  });

// 유저 매칭 기록 상세 조회
export const fetchUserFeedbackDetail = (matchingId) => api.get(`/smalltalk/user/feedback/${matchingId}`);

// ✅ 매칭 종료 후 신고 요청 API 추가
export const reportMatching = async (matchingId, reportTypeIds, description) => {
  return api.post("/smalltalk/user/report/matching", {
    matchingId,
    reportTypeIds,
    description,
  });
};

// 월별 기록 조회
export const fetchMatchHistoryByMonth = async (newDate) => {
  try {
    const response = await api.get(`/smalltalk/user/match/history/monthly?date=${newDate}`);
    return response;
  } catch (error) {
    console.error('매칭 기록 조회 실패:', error);
    throw error;
  }
};

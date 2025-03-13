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
// ✅ 관리자(Admin) 관련 API 테스트트
// =============================================

// 신고된 목록 조회
export const fetchAdminReports = () => api.get("/admin/reports");

// 단일 신고 상세 조회
export const fetchAdminReportById = (reportId) =>
  api.get(`/admin/report/${reportId}`);

// 벌점(패널티) 부여
export const createAdminPenalty = (userId, reportId, data) =>
  api.post(`/admin/penalty?userId=${userId}&reportId=${reportId}`, data);

// 벌점(패널티) 옵션 생성
export const createAdminPenaltyOptions = (data) =>
  api.post("/admin/penalty/options", data);

export const checkAdmin = () => api.get("/admin");

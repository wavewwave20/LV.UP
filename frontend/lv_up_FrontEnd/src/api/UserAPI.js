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
// ✅ 유저 관련 API
// =============================================

// 회원가입
export const registerUser = (data) => api.post("/users/register", data);

// 닉네임 변경
export const fetchNickname = (nickname) =>
  api.post("/users/nickname", nickname, {
    headers: { "Content-Type": "text/plain" },
  });

// 닉네임 중복확인
export const checkNickname = (nickname) =>
  api.post("/users/checknickname", nickname, {
    headers: { "Content-Type": "text/plain" },
  });

// 관심사 옵션 조회
export const fetchInterests = () => api.get("/users/interest/options");

// 내 관심사 조회
export const getUserInterests = () => api.get("/users/interest");

// 메인 페이지용 유저 데이터 가져오기
export const fetchUserData = () => api.get("/users/main");

// 자기소개글 등록
export const postIntroduction = (introductionText) =>
  api.post("/users/introduction", introductionText, {
    headers: { "Content-Type": "text/plain" },
  });

// 선택된 관심사 저장
export const postSelectedInterests = (selectedInterests) =>
  api.post("/users/interest", selectedInterests);

// 마이페이지 데이터 조회
export const fetchMypageData = () => api.get("/users");

// 회원가입 완료시 선물 지급
export const getInitialTickets = () => api.post("/asset/initial");

// 코인 조회
export const fetchMyCoin = () => api.get("/users/coin");

// 티켓 조회
export const fetchMyTicket = () => api.get("/users/ticket");

// 아바타 조회
export const getSelectedAvatar = () => api.get("/users/avatar/selected");

// 관리자 권한 조회
export const fetchAdmin = () => api.get("/users/role");

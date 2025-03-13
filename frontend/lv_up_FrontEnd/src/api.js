import axios from "axios";
import Cookies from "js-cookie";

// ✅ Axios 인스턴스 생성
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

// 닉네임 중복확인
export const checkNickname = (nickname) =>
  api.post("/users/nickname", nickname, {
    headers: { "Content-Type": "text/plain" }, // 순수 문자열로 전송
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
    headers: { "Content-Type": "text/plain" }, // JSON 대신 순수 텍스트 전송
  });

// 선택된 관심사 저장
export const postSelectedInterests = (selectedInterests) => api.post("/users/interest", selectedInterests);

// 마이페이지 데이터 조회
export const fetchMypageData = () => api.get("/users");

// =============================================
// ✅ 관리자(Admin) 관련 API
// =============================================

// 신고된 목록 조회
export const fetchAdminReports = () => api.get("/admin/reports");

// 단일 신고 상세 조회
export const fetchAdminReportById = (reportId) => api.get(`/admin/report/${reportId}`);

// 벌점(패널티) 부여
export const createAdminPenalty = (userId, reportId, data) => api.post(`/admin/penalty?userId=${userId}&reportId=${reportId}`, data);

// 벌점(패널티) 옵션 생성
export const createAdminPenaltyOptions = (data) => api.post("/admin/penalty/options", data);

// =============================================
// ✅ 미션 관련 API
// =============================================

// 오늘의 미션 목록 조회
export const getUserMissions = async () => {
  try {
    const response = await api.get("/mission/today");
    return response.data.map((mission) => ({
      id: mission.mission_id,
      text: mission.name,
      description: mission.description,
      coin: mission.coin,
      ticket: mission.ticket,
      exp: mission.exp,
      status: mission.is_completed
        ? mission.reward_claimed
          ? "complete" // 완료 & 보상 수령
          : "gift" // 완료 & 보상 미수령
        : "not_complete", // 미션 진행 중
    }));
  } catch (error) {
    throw error;
  }
};

// 미션 보상 받기
export const postReward = (mission_id) => api.post(`/mission/reward/${mission_id}`);

// 출석 체크
export const getAttendance = () => api.get("/users/attendance");

// =============================================
// ✅ 공지사항/이벤트 게시판 관련 API
// =============================================

// 공지사항 목록 조회
export const fetchAnnouncements = (page = 1) => api.get(`/board/announcement?page=${page}`);

// 이벤트 목록 조회
export const fetchEvents = (page = 1) => api.get(`/board/event?page=${page}`);

// 게시글 상세 조회
export const fetchBoardDetail = (articleId) => api.get(`/board/view/${articleId}`);

// 게시글 작성
export const createBoard = (boardData) => {
  const token = Cookies.get("token");
  if (!token) {
    throw new Error("인증 토큰이 없습니다.");
  }

  const requestData = {
    type: boardData.type,
    board_title: boardData.board_title,
    board_content: boardData.board_content,
    visionable: 1,
  };

  const config = {
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  };

  return api.post("/board", requestData, config);
};

// 게시글 수정
export const updateBoard = (articleId, boardData) =>
  api.put(`/board/${articleId}`, {
    ...boardData,
    visionable: 1,
  });

// 게시글 삭제
export const deleteBoard = (articleId) => api.delete(`/board/${articleId}`);

// =============================================
// ✅ 소소한 이야기(SmallTalk) 관련 추가 API
// =============================================

// 1) 피드백 옵션 조회
// GET: /api/smalltalk/user/feddback/options
export const fetchFeedbackOptions = () => api.get("/smalltalk/user/feddback/options");

// 2) 피드백 등록
// POST: /api/smalltalk/user/feedback
// 요청 예시: { "feedbackOptions": [1, 2, 3] }
export const postFeedback = (feedbackData) => api.post("/smalltalk/user/feedback", feedbackData);

// 3) 매칭 모드 조회
// GET: /api/smalltalk/user/match/mode
export const fetchMatchMode = () => api.get("/smalltalk/user/match/mode");

// 4) 신고 옵션 조회
// GET: /api/samlltalk/user/report/options
export const fetchReportOptions = () => api.get("/smalltalk/user/report/options");

// 5) 신고 등록
// POST: /api/smalltalk/user/report
// 요청 예시: { "reportTypeId": [1, 2, 3], "description": "신고 내용" }
export const postReport = (reportData) => api.post("/smalltalk/user/report", reportData);

// 기록페이지 관련 API
export const fetchUserFeedback = (page, orderby) =>  api.get(`/smalltalk/user/feedback?page=${page}&orderby=${orderby}`);

// =============================================
// ✅ AI 챗봇 관련 API
// =============================================

export const fetchScenarios = () => api.get("/smalltalk/ai/scenario").then((res) => res.data);
export const fetchPersonalities = () => api.get("/smalltalk/ai/personality").then((res) => res.data);

// smalltalk 매칭 요청 (modeId, target을 파라미터로 전송)
export const requestSmalltalkMatch = (modeId, target) =>
  api.post("/smalltalk/user/match", null, {
    params: { modeId, target },
  });

// smalltalk 매칭 취소
export const cancelSmalltalkMatch = () =>
  api.post("/smalltalk/user/match/cancel");

import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "./Toastify.css"; // 기본 스타일

import Cookies from "js-cookie";
import ProtectedRoute from "./ProtectedRoute.jsx";
import AdminProtectedRoute from "./AdminProtectedRoute.jsx";

import LoginPage from "./SignUpPage/LoginPage.jsx";
import CustomizeProfile from "./SignUpPage/CustomizeProfile.jsx";
import SignUpComplete from "./SignUpPage/SignUpComplete.jsx";
import LoginSuccess from "./SignUpPage/LoginSuccess.jsx";

import MainPage from "./MainPage/MainPage.jsx";
import AITalkPage from "./AI_Talk/AITalkPage.jsx";
import AITalkResult from "./AI_Talk/AITalkResult.jsx";
import SignOutPage from "./SignOutPage/SignOutPage.jsx";
import MyPage from "./MyPage/MyPage.jsx";
import UserMatchHistory from "./MyPage/UserMatchHistory.jsx";
import Interest from "./MyPage/Interest.jsx";
import AIHistory from "./MyPage/AIHistory.jsx";
import Notice from "./MyPage/Notice.jsx";
import UserFeedback from "./MyPage/UserFeedback.jsx";
import NicknameEditor from "./MyPage/NicknameEditor.jsx";

import TimeExtension from "./SmallTalk/TimeExtension.tsx";
import EvaluateUser from "./SmallTalk/EvaluateUser.jsx";
import SmallTalk from "./SmallTalk/SmallTalk.tsx";
import UserReport from "./SmallTalk/UserReport.jsx";
import UserReport2 from "./RecordPage/UserReport2.jsx";

import LineChart from "./MyPage/LineChart.jsx";
import IntegratedRecord from "./RecordPage/IntegratedRecord.jsx";
<<<<<<< HEAD
import UserReport from "./SmallTalk/UserReport.jsx";

import MatchRequest from "./SmallTalkTest/MatchRequest.jsx";
import VideoSession from "./SmallTalkTest/VideoSession.jsx";
import MatchVideoSession from "./SmallTalkTest/MatchVideoSession.jsx";

=======

import AICharacterGender from "./AI_Talk/AICharacterGender.jsx";
import AICharacterPersonality from "./AI_Talk/AICharacterPersonality.jsx";
import AICharacterScenario from "./AI_Talk/AICharacterScenario.jsx";

>>>>>>> develop
import AdminPage from "./AdminPage/AdminPage.jsx";

import "./App.css";
import "./font/Pretendard-1.3.9/web/variable/pretendardvariable.css";
import "./font/NanumPen/nanumpen.css";

function App() {
  return (
    <Router>
      <ToastContainer
        position="top-center" // 토스트가 표시될 위치 (예: top-center, top-right 등)
        autoClose={3000} // 3000ms (3초) 후 자동 닫힘
        hideProgressBar={false} // 프로그래스바 표시 여부
        closeOnClick
        pauseOnHover
        draggable
        theme="colored" // 테마 (colored, light, dark 등)
      />
      <div className="app-container">
        <Routes>
          {/* 🔹 로그인 관련 페이지 (토큰 검사 X) */}
          <Route path="/" element={<LoginPage />} />
          <Route path="/customize-profile" element={<CustomizeProfile />} />
          <Route path="/sign-up-complete" element={<SignUpComplete />} />
          <Route path="/login-success" element={<LoginSuccess />} />
<<<<<<< HEAD
          <Route path="/smalltalk-test" element={<SmallTalkTest />} />
          <Route path="/user-report" element={<UserReport />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/record-test" element={<Record />}></Route>
          <Route path="/tts-test" element={<TTS />}></Route>
=======

          {/* 🔹 로그인이 필요한 페이지 (ProtectedRoute 적용) */}
          <Route element={<ProtectedRoute />}>
            <Route path="/main" element={<MainPage />} />
            <Route path="/ai-talk" element={<AITalkPage />} />
            <Route path="/ai-talk-result" element={<AITalkResult />} />
            <Route path="/sign-out" element={<SignOutPage />} />
            <Route path="/mypage" element={<MyPage />} />
            <Route path="/record" element={<IntegratedRecord />} />
            <Route path="/user-match-history" element={<UserMatchHistory />} />
            <Route path="/linechart" element={<LineChart />} />
            <Route path="/interest" element={<Interest />} />
            <Route path="/ai-history" element={<AIHistory />} />
            <Route path="/notice" element={<Notice />} />
            <Route
              path="/user-feedback/:matchingId"
              element={<UserFeedback />}
            />
            <Route path="/nickname" element={<NicknameEditor />} />
            <Route path="/time-extension" element={<TimeExtension />} />
            <Route path="/evaluate-user" element={<EvaluateUser />} />
            <Route path="/smalltalk" element={<SmallTalk />} />
            <Route path="/user-report" element={<UserReport />} />
            <Route path="/user-report-endmatch" element={<UserReport2 />} />
            {/* 🔹 관리자 페이지 보호 */}
            <Route element={<AdminProtectedRoute />}>
              <Route path="/admin" element={<AdminPage />} />
            </Route>
            <Route
              path="/ai-character-gender"
              element={<AICharacterGender />}
            />
            <Route
              path="/ai-character-personality"
              element={<AICharacterPersonality />}
            />
            <Route
              path="/ai-character-scenario"
              element={<AICharacterScenario />}
            />
          </Route>
>>>>>>> develop
        </Routes>
      </div>
    </Router>
  );
}

export default App;

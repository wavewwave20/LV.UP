import React, { useState, useEffect } from "react";
<<<<<<< HEAD
=======
import { useNavigate } from "react-router-dom";
>>>>>>> develop
import "./MainPage.css";
import Mission from "./Mission";
import Profile from "./Profile";
import AITalkPopUp from "./AITalkPopUp";
import SmallTalkPopUp from "../SmallTalk/SmallTalkPopUp.tsx";
import NavigationBar from "./NavigationBar";
import NoticeList from "./NoticeList";
<<<<<<< HEAD
import { fetchUserData } from "../api"; // API 함수 가져오기
=======
import { fetchUserData } from "../api/UserAPI";
import LevelUpPopup from "./LevelUpPopup";
import AvatarModal from "../MyPage/AvatarModal";
import axios from "axios";
import Cookies from "js-cookie";
>>>>>>> develop

// 이미지 import
import ticket from "../assets/imageFile/ticket.png";
import coin from "../assets/imageFile/coin.png";
import robot from "../assets/imageFile/robot.png";
import talk from "../assets/imageFile/talk.gif";
import lvup from "../assets/imageFile/lvup.gif"; // 로딩용으로 사용할 gif

const LEVEL_REWARDS = {
  5: { coins: 1, tickets: 5 },
  10: { coins: 5, tickets: 5 },
  15: { coins: 5, tickets: 10 },
  20: { coins: 5, tickets: 15 },
  25: { coins: 10, tickets: 10 },
  30: { coins: 10, tickets: 15 },
  35: { coins: 10, tickets: 20 },
  40: { coins: 10, tickets: 20 },
  45: { coins: 15, tickets: 20 },
  50: { coins: 20, tickets: 20 },
};

function MainPage() {
<<<<<<< HEAD
  const [tickets, setTickets] = useState(0);
  const [coins, setCoins] = useState(0);
  const [isAIPopupOpen, setIsAIPopupOpen] = useState(false);
=======
  const navigate = useNavigate();
  const [tickets, setTickets] = useState(0);
  const [coins, setCoins] = useState(0);
  const [currentLevel, setCurrentLevel] = useState(null);
>>>>>>> develop
  const [isSmallTalkPopupOpen, setIsSmallTalkPopupOpen] = useState(false);
  const [userData, setUserData] = useState(null);
  const [currentAvatar, setCurrentAvatar] = useState(null);
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);
  const [availableAvatars, setAvailableAvatars] = useState([]);

<<<<<<< HEAD
  const openAIPopup = () => setIsAIPopupOpen(true);
  const closeAIPopup = () => setIsAIPopupOpen(false);
=======
  // 레벨업 팝업 관련 상태
  const [isLevelUpPopupOpen, setIsLevelUpPopupOpen] = useState(false);
  const [levelUpReward, setLevelUpReward] = useState(null);
  const [pendingLevelUp, setPendingLevelUp] = useState(null);

  // 추가된 상태: 로딩 중 여부
  const [isLoading, setIsLoading] = useState(true);

  // AI 스몰톡 클릭 핸들러
  const handleAISmallTalkClick = () => {
    navigate("/ai-character-gender");
  };

>>>>>>> develop
  const openSmallTalkPopup = () => setIsSmallTalkPopupOpen(true);
  const closeSmallTalkPopup = () => setIsSmallTalkPopupOpen(false);

  useEffect(() => {
    const fetchAvatarData = async () => {
      try {
        // 사용 가능한 아바타 목록 가져오기
        const avatarsResponse = await axios.get("/api/users/avatar", {
          headers: {
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        });
        setAvailableAvatars(avatarsResponse.data);
      } catch (error) {
        console.error("아바타 정보 로딩 중 오류:", error);
      }
    };

    fetchAvatarData();
  }, []);

  // 아바타 선택 핸들러
  const handleAvatarSelect = async (avatarId) => {
    try {
      setCurrentAvatar(avatarId);
    } catch (error) {
      console.error("❌ 아바타 선택 실패:", error);
    }
  };

  // 아바타 모달 확인 핸들러
  const handleModalConfirm = async () => {
    try {
      // 서버에 저장 요청
      await axios.post("/api/users/avatar", currentAvatar, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      });

      setIsProfileModalOpen(false);
    } catch (error) {
      console.error("❌ 아바타 변경 실패:", error);
      alert("아바타 변경 중 오류가 발생했습니다.");
    }
  };

  // 첫 마운트 시 유저 데이터 가져오기
  useEffect(() => {
    const getUserData = async () => {
      try {
<<<<<<< HEAD
        console.log("🔍 유저 정보 요청 중...");
        const response = await fetchUserData(); // API 호출
=======
        setIsLoading(true); // 로딩 시작
        const response = await fetchUserData();
>>>>>>> develop
        const data = response.data;

        setUserData(data);
        setTickets(data.ticket_quantity);
        setCoins(data.coin_quantity);
        setCurrentAvatar(data.avatarId);
      } catch (error) {
        console.error("❌ 유저 데이터 불러오기 오류:", error);
      } finally {
        setIsLoading(false); // 로딩 종료
      }
    };

    getUserData();
  }, [navigate]);

<<<<<<< HEAD
  // ✅ 보상 수령 후 티켓과 코인 업데이트하는 함수
=======
  // 유저 잔액 갱신 (미션 완료 시 호출 등)
>>>>>>> develop
  const updateBalance = async () => {
    try {
      setIsLoading(true);
      const response = await fetchUserData();
      const newUserData = response.data;

      const prevLevel = currentLevel;
      const newLevel = newUserData.level;

      setUserData(newUserData);
      setTickets(newUserData.ticket_quantity);
      setCoins(newUserData.coin_quantity);
      setCurrentLevel(newLevel);

      // ✅ 레벨업 발생 시, 최종 레벨 보상 저장 (딜레이 적용)
      if (prevLevel !== null && newLevel > prevLevel) {
        let totalCoins = 0;
        let totalTickets = 0;

        for (let level = prevLevel + 1; level <= newLevel; level++) {
          if (LEVEL_REWARDS[level]) {
            totalCoins += LEVEL_REWARDS[level].coins;
            totalTickets += LEVEL_REWARDS[level].tickets;
          }
        }

        const finalReward = {
          level: newLevel,
          coins: totalCoins,
          tickets: totalTickets,
        };

        // **레벨업 팝업을 딜레이 후 실행**
        setPendingLevelUp(finalReward);

        setTimeout(() => {
          setLevelUpReward(finalReward);
          setIsLevelUpPopupOpen(true);
          setPendingLevelUp(null);
        }, 1000); // 1초 딜레이 후 팝업 띄우기
      }
    } catch (error) {
      console.error("❌ 유저 데이터 갱신 실패:", error);
    } finally {
      setIsLoading(false);
    }
  };

  // 로딩 중이면 lvup.gif를 보여주고, 아니면 원래 화면을 렌더링
  if (isLoading) {
    return (
      <div className="loading-container">
        <img src={lvup} alt="Loading..." className="loading-gif" />
      </div>
    );
  }

  return (
    <div className="main-page">
      <div className="gradient-section">
        <div className="your-balance">
          <div className="ticket">
            <img src={ticket} alt="ticket" />
            <p className="balance-section">{tickets}</p>
          </div>
          <div className="coin">
            <img src={coin} alt="coin" />
            <p className="balance-section">{coins}</p>
          </div>
        </div>
        <div className="profile-section">
          <Profile
            nickname={userData?.nickname}
            level={userData?.level}
            remainExp={userData?.remain_exp_to_next_level}
            expForNextLevel={userData?.exp_for_next_level}
            currentAvatar={currentAvatar}
            onProfileClick={() => setIsProfileModalOpen(true)}
          />
        </div>
      </div>

      {/* 공지사항 컴포넌트 */}
      <NoticeList />

      <div className="talking">
        <div className="go-AI-smalltalk" onClick={openAIPopup}>
          <img src={robot} alt="robot" />
          <p>AI스몰톡</p>
        </div>
        <div className="go-smalltalk" onClick={openSmallTalkPopup}>
          <img src={talk} alt="talk" />
          <p>유저 스몰톡</p>
        </div>
      </div>

      <AITalkPopUp
        isOpen={isAIPopupOpen}
        onClose={closeAIPopup}
        tickets={tickets}
      />
      <SmallTalkPopUp
        isOpen={isSmallTalkPopupOpen}
        onClose={closeSmallTalkPopup}
        tickets={tickets}
      />

      <Mission
        userData={userData}
        setUserData={setUserData}
        updateBalance={updateBalance}
      />

      <NavigationBar />

      {isLevelUpPopupOpen && levelUpReward && (
        <LevelUpPopup
          onClose={() => setIsLevelUpPopupOpen(false)}
          rewardData={levelUpReward}
          level={levelUpReward.level}
        />
      )}

      <AvatarModal
        isOpen={isProfileModalOpen}
        onClose={() => {
          handleModalConfirm();
          setIsProfileModalOpen(false);
        }}
        currentAvatar={currentAvatar}
        onSelect={handleAvatarSelect}
      />
    </div>
  );
}

export default MainPage;

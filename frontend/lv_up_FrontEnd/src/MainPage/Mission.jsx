import React, { useState, useEffect } from "react";
import Cookies from "js-cookie";
import MissionPopup from "./MissionPopup";
import complete from "../assets/imageFile/complete.png";
import not_complete from "../assets/imageFile/not_complete.png";
import gift from "../assets/imageFile/gift.png";
import { getUserMissions } from "../api"; // ✅ fetchUserData 제거
import "./Mission.css";

function Mission({ updateBalance }) {
  // ✅ userData, setUserData 삭제
  const [missions, setMissions] = useState([]);
  const [selectedMission, setSelectedMission] = useState(null);
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  useEffect(() => {
    const token = Cookies.get("token"); // 쿠키에서 토큰 가져오기
    if (!token) {
      return; // ✅ 토큰이 없으면 API 요청 안 함
    }

    const fetchMissions = async () => {
      try {
        const missionsData = await getUserMissions();
        setMissions(missionsData);
      } catch (error) {
        console.warn("⚠️ API 요청 실패!");
      }
    };

    fetchMissions();
  }, []);

  // ✅ 미션 보상 수령 후 유저 데이터를 갱신하는 함수
  const updateMissionStatus = async (missionId) => {
    setMissions((prevMissions) =>
      prevMissions.map((mission) =>
        mission.id === missionId ? { ...mission, status: "complete" } : mission
      )
    );

    try {
      await updateBalance(); // ✅ 최신 유저 데이터 갱신
    } catch (error) {
      console.error("❌ 유저 정보 갱신 실패:", error);
    }
  };

  const openPopup = (mission) => {
    setSelectedMission(mission);
    setIsPopupOpen(true);
  };

  const closePopup = () => {
    setIsPopupOpen(false);
  };

  return (
    <div className="mission-container">
      <p className="today-mission">오늘의 미션</p>

      <div className="mission-list">
        {missions.map((mission, index) => (
          <div
            key={mission.id}
            className={`mission-item ${
              index < missions.length - 1 ? "connected" : ""
            }`}
            onClick={() => openPopup(mission)}
          >
            <p className="mission-text">{mission.text}</p>
            <img
              src={
                mission.status === "complete"
                  ? complete
                  : mission.status === "gift"
                  ? gift
                  : not_complete
              }
              alt={mission.text}
              className="mission-image"
            />
          </div>
        ))}
      </div>

      {isPopupOpen && (
        <MissionPopup
          mission={selectedMission}
          onClose={closePopup}
          onRewardClaimed={updateMissionStatus} // ✅ 미션 완료 후 유저 데이터 갱신
        />
      )}
    </div>
  );
}

export default Mission;

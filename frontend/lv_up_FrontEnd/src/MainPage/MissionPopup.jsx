import React, { useState, useEffect } from "react";
import "./MissionPopup.css";
import ticket from "../assets/imageFile/ticket.png";
import coin from "../assets/imageFile/coin.png";
import { postReward } from "../api";

function MissionPopup({ mission, onClose, onRewardClaimed }) {
  const [missionState, setMissionState] = useState("in_progress");

  useEffect(() => {
    if (mission?.status) {
      setMissionState(mission.status);
    }
  }, [mission]);

  const handleClaimReward = async () => {
    try {
      await postReward(mission.id);
      setMissionState("reward_claimed");

      onRewardClaimed(mission.id); // ✅ 미션 완료 후 `updateMissionStatus` 실행
      onClose();
    } catch (error) {
      console.error("❌ 보상 수령 오류:", error);
    }
  };

  const renderMissionState = () => {
    if (missionState === "not_complete") {
      return <p className="mission-state-text">미션 진행중...</p>;
    } else if (missionState === "gift") {
      return (
        <button
          className="mission-button reward-btn"
          onClick={handleClaimReward}
        >
          수령하기
        </button>
      );
    } else if (
      missionState === "complete" ||
      missionState === "reward_claimed"
    ) {
      return <button className="mission-button claimed-btn">수령완료</button>;
    }
  };

  return (
    <div className="popup-overlay" onClick={onClose}>
      <div className="mission-popup" onClick={(e) => e.stopPropagation()}>
        <button className="mission-close-button" onClick={onClose}>
          ×
        </button>
        <div className="mission-title">{mission?.text}</div>
        <div className="mission-description">{mission?.description}</div>

        <div className="mission-reward">
          {mission?.ticket > 0 && (
            <div className="reward-item">
              <img src={ticket} alt="ticket" />
              <p className="reward-quantity">{mission.ticket}</p>
            </div>
          )}
          {mission?.coin > 0 && (
            <div className="reward-item">
              <img src={coin} alt="coin" />
              <p className="reward-quantity">{mission.coin}</p>
            </div>
          )}
        </div>

        <div className="mission-state">{renderMissionState()}</div>
      </div>
    </div>
  );
}

export default MissionPopup;

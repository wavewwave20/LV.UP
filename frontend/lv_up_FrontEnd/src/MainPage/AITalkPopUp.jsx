import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { fetchScenarios, fetchPersonalities } from "../api";
import "./AITalkPopUp.css";
import ticket from "../assets/imageFile/ticket.png";
import AIchatStart from "../assets/imageFile/AIchatStart.png";

function AITalkPopUp({ isOpen, onClose, tickets }) {
  const [scenarios, setScenarios] = useState([]);
  const [selectedScenario, setSelectedScenario] = useState(null);
  const [personalities, setPersonalities] = useState([]);
  const [selectedPersonality, setSelectedPersonality] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    if (isOpen) {
      fetchScenarios()
        .then((data) => setScenarios(data.scenarios))
        .catch(() => {
          setScenarios([
            { id: 1, name: "예시 시나리오 1", description: "이것은 예시 설명입니다." },
            { id: 2, name: "예시 시나리오 2", description: "이것은 예시 설명입니다." },
          ]);
        });

      fetchPersonalities()
        .then((data) => setPersonalities(data.personalities))
        .catch(() => {
          setPersonalities([
            { id: 1, name: "예시 성격 1", description: "이것은 예시 성격 설명입니다." },
            { id: 2, name: "예시 성격 2", description: "이것은 예시 성격 설명입니다." },
          ]);
        });
    }
  }, [isOpen]);

  if (!isOpen) return null;

  return (
    <div className="popup">
      <div className="popup-content">
        <button className="close-button" onClick={onClose} aria-label="닫기">
          ×
        </button>

        <div className="scenario-ticket">
          <img className="ticket-img" src={ticket} alt="ticket" />
          <p className="ticket-section">{tickets}</p>
        </div>

        <h2>시나리오 선택</h2>
        <select className="dropdown scenario-dropdown" onChange={(e) => setSelectedScenario(scenarios.find((s) => s.id === Number(e.target.value)))} defaultValue="default">
          <option value="default" disabled>
            시나리오를 선택하세요
          </option>
          {scenarios.map((scenario) => (
            <option key={scenario.id} value={scenario.id}>
              {scenario.name}
            </option>
          ))}
        </select>
        {selectedScenario && <p className="description scenario-description">{selectedScenario.description}</p>}

        <h2>성격 선택</h2>
        <select className="dropdown personality-dropdown" onChange={(e) => setSelectedPersonality(personalities.find((p) => p.id === Number(e.target.value)))} defaultValue="default">
          <option value="default" disabled>
            성격을 선택하세요
          </option>
          {personalities.map((personality) => (
            <option key={personality.id} value={personality.id}>
              {personality.name}
            </option>
          ))}
        </select>
        {selectedPersonality && <p className="description personality-description">{selectedPersonality.description}</p>}

        <button
          className="start-button"
          onClick={() =>
            navigate("/ai-talk", {
              state: { scenario: selectedScenario, personality: selectedPersonality },
            })
          }
          disabled={!selectedScenario || !selectedPersonality}
          aria-disabled={!selectedScenario || !selectedPersonality}
        >
          <p className="start-chat">시작하기</p>
          <img src={AIchatStart} alt="AIChatStart" />
        </button>
      </div>
    </div>
  );
}

export default AITalkPopUp;

import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import "./AICharacterScenario.css";
import backButton from "../assets/imageFile/backButton.png";
import AIchatStart from "../assets/imageFile/ticket.png";
import { fetchScenarios, endAIChatAll } from "../api/AITalkAPI";

function AICharacterScenario() {
  const navigate = useNavigate();
  const location = useLocation();

  endAIChatAll();

  // 넘어온 state
  const { age, gender, personality } = location.state || {};

  const defaultScenarios = [
    {
      id: 1,
      scenario_name: "학교에서 첫 만남",
      emoji: "🏫",
      description:
        "당신(user_name)은 오늘 이 학교로 전학 온 새 학생입니다. 쉬는 시간에 옆자리에 앉아 있던 기존 학생(opponent_name)이 호기심 반, 친절함 반으로 당신에게 말을 겁니다. 서로 전혀 모르는 사이라 공통점을 찾기 쉽지 않아 약간 어색한 분위기입니다. 간단한 자기소개와 함께 서로의 취향이나 관심사를 물어보며 대화를 이어가려고 해 보세요.",
    },
    {
      id: 2,
      scenario_name: "카페에서의 대화",
      emoji: "☕",
      description:
        "당신(user_name)은 카페에서 공부를 하고 있습니다. 옆 테이블에서 공부하던 사람(opponent_name)이 갑자기 당신에게 도움을 요청합니다. 서로 모르는 사이지만, 같은 학교 학생인 것 같아 보입니다. 어색하지만 친절하게 도와주면서 대화를 시작해보세요.",
    },
    {
      id: 3,
      scenario_name: "동아리 첫 모임",
      emoji: "🎨",
      description:
        "새로운 동아리에 가입한 당신(user_name)은 첫 모임에 참석했습니다. 동아리 선배(opponent_name)가 당신에게 다가와 이야기를 시작합니다. 서로의 관심사와 동아리 활동에 대한 기대를 나누며 대화를 이어가보세요.",
    },
  ];

  const [scenarios, setScenarios] = useState(defaultScenarios);
  const [selectedScenario, setSelectedScenario] = useState(0);

  useEffect(() => {
    const getScenarios = async () => {
      try {
        const data = await fetchScenarios();
        if (data && data.scenarios) {
          setScenarios(data.scenarios);
        } else {
          setScenarios(defaultScenarios);
        }
      } catch (error) {
        console.error("시나리오 불러오기 실패:", error);
        setScenarios(defaultScenarios);
      }
    };
    getScenarios();
  }, []);

  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: true,
    beforeChange: (current, next) => setSelectedScenario(next),
  };

  const handleStartClick = () => {
    const scenarioData = scenarios[selectedScenario];
    navigate("/ai-talk", {
      state: {
        age,
        gender,
        personality,
        scenario: scenarioData,
      },
    });
  };

  return (
    <div className="aicharacter_container">
      <img
        className="aicharacter_back_button"
        src={backButton}
        alt="back"
        onClick={() => navigate(-1)}
      />
      <h1 className="aicharacter_main_title">AI 캐릭터 생성</h1>

      <div className="aicharacter_selected_info">
        {/* 첫 번째 줄 (나이, 성별) */}
        <div className="ai-row-1">
          <div className="aicharacter_tag">
            <p>#{age}</p>
          </div>
          <div className="aicharacter_tag">
            <p>#{gender === "male" ? "남성" : "여성"}</p>
          </div>
        </div>

        {/* 두 번째 줄 (성격) */}
        <div className="ai-row-2">
          <div className="aicharacter_personality">
            <p>#{personality?.name ?? "기본성격"}</p>
          </div>
        </div>
      </div>

      <div className="aicharacter_scenario_box">
        <Slider {...sliderSettings}>
          {scenarios.map((scenario) => (
            <div key={scenario.id} className="aicharacter_scenario_slide">
              <div className="aicharacter_scenario_content">
                <span className="aicharacter_scenario_emoji">
                  {scenario.emoji}
                </span>
                <h3 className="aicharacter_scenario_title">
                  {scenario.scenario_name}
                </h3>
                <p className="aicharacter_scenario_description">
                  {scenario.description}
                </p>
              </div>
            </div>
          ))}
        </Slider>
      </div>

      <button className="aicharacter_start_button" onClick={handleStartClick}>
        <img
          className="aicharacter_AIchatStart"
          src={AIchatStart}
          alt="start"
        />
        시작하기
      </button>
    </div>
  );
}

export default AICharacterScenario;

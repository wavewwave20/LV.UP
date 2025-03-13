import React, { useState, useEffect } from "react";
import Slider from "react-slick";
import { useNavigate, useLocation } from "react-router-dom";
import "./AICharacterPersonality.css";
import backButton from "../assets/imageFile/backButton.png";
import { fetchPersonalities } from "../api/AITalkAPI";

// 기본 personality 리스트 선언
const defaultPersonalities = [
  {
    id: 1,
    name: "다정한",
    level: "쉬움",
    emoji: "😊",
  },
  {
    id: 2,
    name: "다정한",
    level: "쉬움에서 보통",
    emoji: "😊",
  },
  {
    id: 3,
    name: "다정한",
    level: "보통",
    emoji: "😊",
  },
  {
    id: 4,
    name: "다정한",
    level: "보통에서 약간 도전적",
    emoji: "😊",
  },
];

function AICharacterPersonality() {
  const navigate = useNavigate();
  const location = useLocation();
  const { age, gender } = location.state || {};

  // 초기 상태 설정 (이제 선언 순서 문제 없음)
  const [personalities, setPersonalities] = useState(defaultPersonalities);
  const [selectedPersonality, setSelectedPersonality] = useState(
    defaultPersonalities[0]
  );

  const sliderSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: true,
    afterChange: (currentIndex) => {
      // 현재 슬라이드의 personality를 선택
      setSelectedPersonality(personalities[currentIndex]);
    },
  };

  useEffect(() => {
    fetchPersonalities()
      .then((data) => {
        if (data && data.personalities) {
          setPersonalities(data.personalities);
          setSelectedPersonality(data.personalities[0]); // 첫 번째 선택
        }
      })
      .catch((error) => {
        console.error("성격 정보 가져오기 실패:", error);
        setSelectedPersonality(defaultPersonalities[0]);
      });
  }, []);

  const handleNextClick = () => {
    if (!selectedPersonality) {
      alert("성격을 선택해주세요.");
      return;
    }
    navigate("/ai-character-scenario", {
      state: { age, gender, personality: selectedPersonality },
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

      <div className="aicharacter_selected_info_character">
        <div className="aicharacter_tag">
          <p>#{age}</p>
        </div>
        <div className="aicharacter_tag">
          <p>#{gender === "male" ? "남성" : "여성"}</p>
        </div>
      </div>

      <div className="aicharacter_scenario_box">
        <Slider {...sliderSettings}>
          {personalities.map((personality) => (
            <div key={personality.id} className="aicharacter_scenario_slide">
              <div className="aicharacter_scenario_content">
                <span className="aicharacter_scenario_emoji">
                  {personality.emoji}
                </span>
                <h3 className="aicharacter_scenario_title">
                  {personality.name}
                </h3>
                <p className="aicharacter_scenario_description">
                  {personality.level}
                </p>
              </div>
            </div>
          ))}
        </Slider>
      </div>

      <button className="aicharacter_next_button" onClick={handleNextClick}>
        다음
      </button>
    </div>
  );
}

export default AICharacterPersonality;

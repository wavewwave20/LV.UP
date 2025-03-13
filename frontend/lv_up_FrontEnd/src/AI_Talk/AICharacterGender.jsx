import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AICharacterGender.css";
import backButton from "../assets/imageFile/backButton.png";
import maleIcon from "../assets/imageFile/gender_male.png";
import femaleIcon from "../assets/imageFile/gender_female.png";

function AICharacterGender() {
  const navigate = useNavigate();
  const [selectedAge, setSelectedAge] = useState(null);
  const [selectedGender, setSelectedGender] = useState(null);

  const ageGroups = ["10대", "20대", "30대", "40대", "50대"];

  const handleNextClick = () => {
    if (selectedAge && selectedGender) {
      navigate("/ai-character-personality", {
        state: {
          age: selectedAge,
          gender: selectedGender,
        },
      });
    } else {
      alert("나이와 성별을 모두 선택해주세요.");
    }
  };

  return (
    <div className="aicharacter_container">
      <div className="aicharacter_content">
        <img className="aicharacter_back_button" src={backButton} alt="back" onClick={() => navigate("/main")} />
        <h1 className="aicharacter_main_title">AI 캐릭터 생성</h1>

        {/* 나이 선택 섹션 */}
        <div className="aicharacter_selection_box aicharacter_age_section">
          <h2 className="aicharacter_section_title">AI 나이 선택</h2>
          <div className="aicharacter_age_grid">
            {ageGroups.map((age) => (
              <button key={age} className={`aicharacter_age_button ${selectedAge === age ? "aicharacter_selected" : ""}`} onClick={() => setSelectedAge(age)}>
                {age}
              </button>
            ))}
          </div>
        </div>

        {/* 성별 선택 섹션 */}
        <div className="aicharacter_selection_box aicharacter_gender_section">
          <h2 className="aicharacter_section_title">AI 성별 선택</h2>
          <div className="aicharacter_gender_options">
            <div className={`aicharacter_gender_option ${selectedGender === "male" ? "aicharacter_selected" : ""}`} onClick={() => setSelectedGender("male")}>
              <div className="aicharacter_gender_icon_wrapper">
                <img src={maleIcon} alt="male" className="aicharacter_gender_icon" />
              </div>
            </div>
            <div className={`aicharacter_gender_option ${selectedGender === "female" ? "aicharacter_selected" : ""}`} onClick={() => setSelectedGender("female")}>
              <div className="aicharacter_gender_icon_wrapper">
                <img src={femaleIcon} alt="female" className="aicharacter_gender_icon" />
              </div>
            </div>
          </div>
        </div>

        <div className="aicharacter_button_area">
          <button className="aicharacter_next_button" onClick={handleNextClick} disabled={!selectedAge || !selectedGender}>
            다음
          </button>
        </div>
      </div>
    </div>
  );
}

export default AICharacterGender;

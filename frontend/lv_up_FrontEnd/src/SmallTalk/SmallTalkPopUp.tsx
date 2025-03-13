import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./SmallTalkPopUp.css";

// (1) fetchMatchMode 불러오기
import { fetchMatchMode } from "../api";

// 이미지 파일들 (프로젝트 경로에 맞게 수정)
import gender_female from "../assets/imageFile/gender_female.png";
import gender_male from "../assets/imageFile/gender_male.png";
import gender_male_female from "../assets/imageFile/gender_male_female.png";
import gender_female_w from "../assets/imageFile/gender_female_w.png";
import gender_male_w from "../assets/imageFile/gender_male_w.png";
import gender_male_female_w from "../assets/imageFile/gender_male_female_w.png";

type SmallTalkPopUpProps = {
  isOpen: boolean;
  onClose: () => void;
  tickets?: number;
};

// 모드 데이터 구조 (matching_mode_id를 그대로 사용)
interface ModeItem {
  id: number; // matching_mode_id (1, 2, 3 등)
  title: string; // content
  description: string; // description
}

function SmallTalkPopUp({ isOpen, onClose, tickets }: SmallTalkPopUpProps) {
  const navigate = useNavigate();

  // step: 1(모드 선택) / 2(성별 선택)
  const [step, setStep] = useState<number>(1);
  // 현재 선택한 모드ID (숫자) - 1,2,3... (없으면 null)
  const [selectedMode, setSelectedMode] = useState<number | null>(null);

  // (2) 서버 응답을 그대로 numeric id로 저장할 state
  const [modes, setModes] = useState<ModeItem[]>([]);

  // (3) 팝업 열릴 때 서버에서 모드 목록 가져오기
  useEffect(() => {
    if (isOpen) {
      fetchMatchMode()
        .then((res) => {
          // res.data 가 [{matching_mode_id, content, description}, ...] 형태라고 가정
          const apiData = res.data || [];
          // 변환: numeric id와 title, description만 정리
          const transformed: ModeItem[] = apiData.map((modeItem: any) => ({
            id: modeItem.matching_mode_id, // 숫자 그대로
            title: modeItem.content,
            description: modeItem.description,
          }));
          setModes(transformed);
        })
        .catch((err) => {
          console.error("모드 목록 가져오기 실패:", err);
          // 필요 시 기본 모드 목록으로 대체하거나 에러처리
        });
    } else {
      // 팝업이 닫힐 때 초기화
      setStep(1);
      setSelectedMode(null);
      setModes([]);
    }
  }, [isOpen]);

  if (!isOpen) return null;

  // 모드 선택 시
  const handleModeSelect = (modeId: number) => {
    setSelectedMode(modeId);
  };

  // "시작하기" 또는 "다음" 버튼
  const handleNextClick = () => {
    if (selectedMode == null) {
      alert("모드를 선택해주세요!");
      return;
    }

    // ★ 예시: 4 => 초보 모드
    //         5 => 마이크패스 모드
    //         6 => 자유 모드
    // 아래는 selectedMode === 1(= beginner)면 바로 진행,
    // 그 외(2,3)이면 성별 선택(Step2)으로 넘어가는 로직
    if (selectedMode === 4) {
      // 초보 모드인 경우: 바로 gender='A'로 페이지 이동
      navigate("/smalltalk", { state: { mode: selectedMode, gender: "A" } });
      onClose();
    } else {
      // 마이크패스 or 자유 모드 -> 성별 선택
      setStep(2);
    }
  };

  // 성별 화면에서 최종 선택 시
  const handleConfirmGender = (genderValue: string) => {
    // 선택된 모드 ID와 성별을 함께 전달
    navigate("/smalltalk", {
      state: { mode: selectedMode, gender: genderValue },
    });
    onClose(); // 팝업 닫기
  };

  return (
    <div className="smalltalk_overlay">
      <div className="smalltalk_popup">
        {/* 팝업 닫기(X) 버튼 */}
        <button className="close_button" onClick={onClose}>
          ×
        </button>

        {/* STEP 1: 모드 선택 */}
        {step === 1 && (
          <>
            <h2 className="popup_title">어떤 대화를 시도해볼까요?</h2>

            <div className="modes_container">
              {modes.map((mode) => {
                const isDisabled = mode.id === 4 || mode.id === 5; // 4, 5번 모드 비활성화

                return (
                  <button
                    key={mode.id}
                    className={`mode_button ${
                      selectedMode === mode.id ? "active" : ""
                    } ${isDisabled ? "disabled" : ""}`}
                    onClick={() => !isDisabled && handleModeSelect(mode.id)} // 비활성화된 경우 클릭 방지
                    disabled={isDisabled} // 비활성화 적용
                  >
                    <h3 className="mode_title">{mode.title}</h3>
                    <p className="mode_description">{mode.description}</p>
                  </button>
                );
              })}
            </div>

            <button
              className={`start_button ${selectedMode ? "active" : ""}`}
              onClick={handleNextClick}
            >
              {/* 선택된 모드가 4(초보 모드)라면 '시작하기', 그 외엔 '다음' */}
              {selectedMode === 4 ? "시작하기" : "다음"}
            </button>
          </>
        )}

        {/* STEP 2: 성별 선택 */}
        {step === 2 && (
          <GenderSelectPopUp
            onConfirm={handleConfirmGender}
            onClose={onClose}
          />
        )}
      </div>
    </div>
  );
}

export default SmallTalkPopUp;

/** ──────────────────────────────────────────────────────────────────
 *  내부 Sub-Component: GenderSelectPopUp (뒤로가기 없음)
 *   - 이미지를 통한 성별 선택 + 시작하기
 *   - 닫기 버튼(X) 누르면 팝업 전체를 닫고, step 초기화
 * ────────────────────────────────────────────────────────────────── */
type GenderSelectPopUpSubProps = {
  onClose: () => void; // 팝업 닫기
  onConfirm: (gender: string) => void;
};

function GenderSelectPopUp({ onClose, onConfirm }: GenderSelectPopUpSubProps) {
  const [selectedGender, setSelectedGender] = useState<string | null>(null);

  // 아이콘 목록
  const genderOptions = [
    {
      id: "F",
      label: "여성",
      icon: gender_female,
      iconWhite: gender_female_w,
    },
    {
      id: "M",
      label: "남성",
      icon: gender_male,
      iconWhite: gender_male_w,
    },
    {
      id: "A",
      label: "모두",
      icon: gender_male_female,
      iconWhite: gender_male_female_w,
    },
  ];

  const handleGenderSelect = (genderId: string) => {
    setSelectedGender(genderId);
  };

  const handleStart = () => {
    if (!selectedGender) {
      alert("성별을 선택해주세요!");
      return;
    }
    onConfirm(selectedGender);
  };

  return (
    <>
      {/* 성별 선택화면 제목 */}
      <h2 className="popup_title">누구와 대화할까요?</h2>

      {/* 성별 선택 */}
      <div className="gender_options_container">
        {genderOptions.map((option) => {
          const isSelected = selectedGender === option.id;
          return (
            <div
              key={option.id}
              className={`gender_option ${isSelected ? "selected" : ""}`}
              onClick={() => handleGenderSelect(option.id)}
            >
              <div className="gender_icon_background">
                <img
                  src={isSelected ? option.iconWhite : option.icon}
                  alt={option.label}
                  className="gender_icon"
                />
              </div>
              <div className="gender_label">{option.label}</div>
            </div>
          );
        })}
      </div>

      {/* 버튼: 시작하기 */}
      <div style={{ marginTop: "20px" }}>
        <button
          className={`start_button ${selectedGender ? "active" : ""}`}
          onClick={handleStart}
        >
          시작하기
        </button>
      </div>
    </>
  );
}
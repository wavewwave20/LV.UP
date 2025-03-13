import React, { useState, useEffect } from "react";
import "./SelectInterest.css";
import { fetchInterests } from "../api"; // API 함수 가져오기

function SelectInterest({ setSelectedInterests }) {
  const defaultInterests = {
    1: "운동",
    2: "음악",
    3: "게임",
    4: "독서",
    5: "영화",
    6: "음식",
    7: "반려동물",
    8: "헬스",
    9: "맛집",
    10: "캠핑",
    11: "드라마",
    12: "넷플릭스",
    13: "요리",
    14: "사진찍기",
    15: "웹툰",
    16: "그림그리기",
  };

  const [interests, setInterests] = useState(defaultInterests);
  const [selected, setSelected] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // 관심사 데이터를 서버에서 가져오는 함수
  useEffect(() => {
    const getInterests = async () => {
      try {
        const response = await fetchInterests();
        const data = response.data;

        if (data.length > 0) {
          // 배열을 객체 형태로 변환
          const formattedInterests = data.reduce((acc, item) => {
            acc[item.interest_id] = item.name;
            return acc;
          }, {});
          setInterests(formattedInterests);
        } else {
          console.log("⚠ 관심사 값이 없음, 기본 관심사 사용");
        }
      } catch (err) {
        setError("관심사를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setLoading(false);
      }
    };

    getInterests();
  }, []);

  // 관심사 선택 함수 (숫자 ID로 저장)
  const handleSelect = (interestId) => {
    let updatedSelection;
    if (selected.includes(interestId)) {
      updatedSelection = selected.filter((id) => id !== interestId);
    } else if (selected.length < 3) {
      updatedSelection = [...selected, interestId];
    } else {
      return;
    }

    setSelected(updatedSelection);
    setSelectedInterests(updatedSelection);
  };

  return (
    <div className="select_interest_container">
      <div className="interest_title">관심사 선택</div>
      <div className="interest_explain">
        요즘 관심있는 주제가 있나요? <br />
        관심사를 선택해주시면 상대방과 대화할 때 도움이 돼요.
      </div>

      <div className="interest_count">{selected.length}/3 선택됨</div>

      {/* 로딩 상태 표시 */}
      {loading && <p>🔄 관심사를 불러오는 중...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* 관심사 버튼 목록 */}
      {!loading && (
        <div className="interests">
          {Object.entries(interests).map(([id, name]) => (
            <button
              key={id}
              className={`interest_button ${
                selected.includes(Number(id)) ? "selected" : ""
              }`}
              onClick={() => handleSelect(Number(id))}
            >
              {name}
            </button>
          ))}
        </div>
      )}

    </div>
  );
}

export default SelectInterest;

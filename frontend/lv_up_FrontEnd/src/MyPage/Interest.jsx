import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Interest.css";
import back from "../assets/imageFile/backButton.png";
<<<<<<< HEAD
import { fetchInterests, postSelectedInterests } from "../api"; // API 호출 함수 추가
=======
import {
  fetchInterests,
  postSelectedInterests,
  getUserInterests,
} from "../api/UserAPI"; // API 호출 함수 추가
>>>>>>> develop

function Interest() {
  const navigate = useNavigate();
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const defaultCategories = [
    { id: 1, name: "운동" },
    { id: 2, name: "음악" },
    { id: 3, name: "게임" },
    { id: 4, name: "독서" },
    { id: 5, name: "캠핑" },
    { id: 6, name: "드라마" },
    { id: 7, name: "영화" },
    { id: 8, name: "음식" },
    { id: 9, name: "반려동물" },
    { id: 10, name: "넷플릭스" },
    { id: 11, name: "사진" },
    { id: 12, name: "메이크업" },
    { id: 13, name: "자기개발" },
    { id: 14, name: "노래" },
    { id: 15, name: "그림" },
    { id: 16, name: "헬스" },
    { id: 17, name: "맛집" },
    { id: 18, name: "요리" },
    { id: 19, name: "댄스" },
    { id: 20, name: "등산" },
    { id: 21, name: "러닝" },
    { id: 22, name: "축구" },
    { id: 23, name: "쇼핑" },
    { id: 24, name: "술" },
    { id: 25, name: "만화" },
    { id: 26, name: "힙합" },
    { id: 27, name: "건강" },
    { id: 28, name: "동물" },
    { id: 29, name: "언어" },
    { id: 30, name: "강아지" },
    { id: 31, name: "자동차" },
    { id: 32, name: "고양이" },
    { id: 33, name: "디저트" },
    { id: 34, name: "웹툰" },
  ];

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 관심사 목록 가져오기
        const interestsResponse = await fetchInterests();
        const interestsData = interestsResponse.data;

        // formattedInterests를 try 블록 바깥에서 선언하여 참조 가능하게 수정
        let formattedInterests = [];

        if (interestsData.length > 0) {
          formattedInterests = interestsData.map((item) => ({
            id: item.interest_id,
            name: item.name,
          }));
          setCategories(formattedInterests);
        } else {
          formattedInterests = defaultCategories;
          setCategories(defaultCategories);
        }

        // 사용자의 기존 관심사 가져오기
        const userInterestsResponse = await getUserInterests();
        const userInterestsData = userInterestsResponse.data;

        // 기존 관심사와 일치하는 ID 찾기
        const selectedIds = userInterestsData
          .map((interest) => {
            const found = formattedInterests.find(
              (item) => item.name === interest
            );
            return found ? found.id : null;
          })
          .filter((id) => id !== null); // null 값 제거

        setSelectedInterests(selectedIds);
      } catch (err) {
        console.error("❌ 관심사 불러오기 오류:", err);
        setError("관심사를 불러오는 중 오류가 발생했습니다.");
        setCategories(defaultCategories);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  // 관심사 선택 핸들러 (최대 3개)
  const handleInterestClick = (interestId) => {
    if (selectedInterests.includes(interestId)) {
      setSelectedInterests(selectedInterests.filter((id) => id !== interestId));
    } else if (selectedInterests.length < 3) {
      setSelectedInterests([...selectedInterests, interestId]);
    }
  };

  // 선택한 관심사 서버로 전송 (POST 요청)
  const handleConfirm = async () => {
    if (selectedInterests.length === 3) {
      try {
        await postSelectedInterests(selectedInterests);
        navigate("/mypage");
      } catch (error) {
        console.error("❌ 관심사 저장 실패:", error);
      }
    }
  };

  return (
    <div className="interest_container">
      <div className="back_button_interest" onClick={() => navigate('/mypage')}>
        <img src={back} alt="뒤로가기" />
      </div>

      <h1 className="interest_title">관심사 선택</h1>

      <p className="interest_description">
        요즘 관심있는 주제가 있나요? 관심사를 선택해주시면 상대방과 대화할 때
        도움이 될 수 있어요
      </p>

      <div className="interest_count">{selectedInterests.length}/3</div>

      {loading && <p>🔄 관심사를 불러오는 중...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && (
        <div className="interests_grid">
          {categories.map((category) => (
            <button
              key={category.id}
              className={`interest_item ${
                selectedInterests.includes(category.id) ? "selected" : ""
              }`}
              onClick={() => handleInterestClick(category.id)}
            >
              {category.name}
            </button>
          ))}
        </div>
      )}

      <button
        className={`confirm_button_interest ${
          selectedInterests.length === 3 ? "active" : ""
        }`}
        onClick={handleConfirm}
      >
        확인
      </button>
    </div>
  );
}

export default Interest;

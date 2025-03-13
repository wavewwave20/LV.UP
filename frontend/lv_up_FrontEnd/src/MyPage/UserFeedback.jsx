import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "./UserFeedback.css";
import check from "../assets/imageFile/check.png";
import { fetchUserFeedbackDetail } from "../api/SmalltalkAPI";
import back from "../assets/imageFile/backButton.png";

function UserFeedback() {
  const navigate = useNavigate();
  const { matchingId } = useParams();
  const [loading, setLoading] = useState(true);

  const [feedbackData, setFeedbackData] = useState({
    checklist_names: [],
    ratee_nickname: "",
    rating_score: 0,
    rating_content: "",
    start_at: "",
    ratee_avatar_id: 1,
  });

  // 아바타 매핑 정보
  const avatars = {
    1: "/assets/avatar1.jpg",
    2: "/assets/avatar2.jpg",
    3: "/assets/avatar3.jpg",
    4: "/assets/avatar4.jpg",
    5: "/assets/avatar5.jpg",
  };

  // 아바타 이미지 가져오기
  const getAvatarImage = (avatarId) => {
    return avatars[avatarId] || avatars[1];
  };

  useEffect(() => {
    const loadFeedbackDetail = async () => {
      try {
        const response = await fetchUserFeedbackDetail(matchingId);
        let formattedContent = response.data.rating_content;

        try {
          const parsed = JSON.parse(formattedContent);
          formattedContent = parsed.content;
        } catch (e) {
          formattedContent = formattedContent;
        }

        setFeedbackData({
          ...response.data,
          rating_content: formattedContent,
        });
      } catch (error) {
        console.error("피드백 데이터 로딩 실패:", error);
        if (error.response?.data === "체크리스트 정보가 없습니다.") {
          setFeedbackData({
            checklist_names: [],
            ratee_nickname: "-",
            rating_score: 0,
            rating_content: "",
            start_at: "",
            ratee_avatar_id: 1,
          });
        }
      } finally {
        setLoading(false);
      }
    };

    if (matchingId) {
      loadFeedbackDetail();
    }
  }, [matchingId]);

  // 피드백이 없을 때 표시할 컴포넌트
  const EmptyFeedback = () => (
    <div className="empty_feedback">
      <div className="empty_feedback_icon">📭</div>
      <h2>아직 받은 피드백이 없어요</h2>
      <p>상대방이 피드백을 작성하면 이곳에서 확인하실 수 있어요!</p>
    </div>
  );

  const handleConfirm = () => {
    navigate(-1);
  };

  if (loading) {
    return <div className="loading">로딩중...</div>;
  }

  // 피드백 데이터가 없는 경우
  if (!feedbackData.rating_content && !feedbackData.checklist_names.length) {
    return (
      <div className="feedback_container">
        <div className="back_button" onClick={() => navigate("/record")}>
          <img src={back} alt="뒤로가기" />
        </div>
        <EmptyFeedback />
      </div>
    );
  }

  return (
    <div className="feedback_container">
      <div className="back_button" onClick={() => navigate("/record")}>
        <img src={back} alt="뒤로가기" />
      </div>
      <div className="feedback_header">
        <img
          className="profile_image"
          src={getAvatarImage(feedbackData.ratee_avatar_id)}
          alt="프로필"
        />
        <span>
          <span className="profile_name">{feedbackData.ratee_nickname}</span>
          님이 보낸 따뜻한 편지가 도착했어요.
        </span>
      </div>

      <hr className="divider" />

      <div className="check_list">
        {feedbackData.checklist_names &&
        feedbackData.checklist_names.length > 0 ? (
          feedbackData.checklist_names.map((item, index) => (
            <div key={index} className="check_item">
              <img className="check_icon" src={check} alt="체크" />
              <span className="check_text">{item}</span>
            </div>
          ))
        ) : (
          <p className="no-feedback">선택된 체크리스트가 없어요🥲</p>
        )}
      </div>

      <hr className="divider" />

      <div className="feedback_message">
        💌
        <div className="feedback_box">
          <p className="feedback_text">
            {feedbackData.rating_content === "null"
              ? "작성된 피드백이 없습니다."
              : feedbackData.rating_content}
          </p>
        </div>
      </div>

      <button className="confirm_button" onClick={handleConfirm}>
        <span>확인</span>
      </button>
    </div>
  );
}

export default UserFeedback;

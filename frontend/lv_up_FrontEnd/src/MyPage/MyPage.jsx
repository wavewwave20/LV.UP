import React, { useState, useEffect, useCallback } from "react";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
<<<<<<< HEAD
import profileImage from "../assets/imageFile/male.png";
=======
>>>>>>> develop
import next from "../assets/imageFile/next.png";
import "./MyPage.css";
import NavigationBar from "../MainPage/NavigationBar";
import modify from "../assets/imageFile/modify.png";
import IntroductionPopup from "./IntroductionPopup";
<<<<<<< HEAD
import { getUserInterests, fetchMypageData } from "../api";
=======
import { getUserInterests, fetchMypageData } from "../api/UserAPI";
import lvup from "../assets/imageFile/lvup.gif";
//
import AvatarModal from "./AvatarModal";
import axios from "axios";
>>>>>>> develop

function MyPage() {
  const navigate = useNavigate();
  const [nickname, setNickname] = useState("User");
  const [age, setAge] = useState(0);
  const [content, setContent] = useState(
    "나를 보여줄 수 있는 내용을 작성해주세요."
  );

  const [interests, setInterests] = useState([]);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
<<<<<<< HEAD
  const handleLogout = () => {
    setIsLoggedIn(false);
    navigate("/"); // 로그인 페이지로 이동
  };
=======
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);
  const [currentAvatar, setCurrentAvatar] = useState(null);
  const [availableAvatars, setAvailableAvatars] = useState([]);
  const [loading, setLoading] = useState(false);
>>>>>>> develop

  useEffect(() => {
    const getUserData = async () => {
      try {
        setLoading(true); // 로딩 시작
        const response = await fetchMypageData();
        const { nickname, birthyear, introduction, avatarId } = response.data;

        setNickname(nickname || "User");
        setAge(new Date().getFullYear() - birthyear);
        setContent(introduction || "나를 보여줄 수 있는 내용을 작성해주세요.");
        setCurrentAvatar(avatarId || 1);
      } catch (err) {
        console.error("❌ 유저 정보 불러오기 오류:", err);
      } finally {
        setLoading(false); // 로딩 종료
      }
    };

    const getInterests = async () => {
      try {
        const response = await getUserInterests();

        if (Array.isArray(response.data) && response.data.length > 0) {
          setInterests(response.data);
        } else {
          setInterests([]);
        }
      } catch (err) {
        console.error("❌ 관심사 불러오기 오류:", err);
        setInterests([]);
      }
    };

    getUserData();
    getInterests();
  }, []);

<<<<<<< HEAD
=======
  // 아바타 변경 핸들러
  useEffect(() => {
    const fetchAvatarData = async () => {
      try {
        // 사용 가능한 아바타 목록 가져오기
        const avatarsResponse = await axios.get("/api/users/avatar", {
          headers: {
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        });
        setAvailableAvatars(avatarsResponse.data);

        // 현재 선택된 아바타 가져오기
        const selectedAvatarResponse = await axios.get(
          "/api/users/avatar/selected",
          {
            headers: {
              Authorization: `Bearer ${Cookies.get("token")}`,
            },
          }
        );
        setCurrentAvatar(selectedAvatarResponse.data.avatarId);
      } catch (error) {
        console.error("아바타 정보 로딩 중 오류:", error);
      }
    };

    fetchAvatarData();
  }, []);

  const handleAvatarSelect = useCallback(async (avatarId) => {
    try {
      setCurrentAvatar(avatarId);
    } catch (error) {
      console.error("❌ 아바타 선택 실패:", error);
    }
  }, []);

  const handleModalConfirm = async () => {
    try {
      // 서버에 저장 요청
      await axios.post("/api/users/avatar", currentAvatar, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      });

      setIsProfileModalOpen(false);
    } catch (error) {
      console.error("❌ 아바타 변경 실패:", error);
      alert("아바타 변경 중 오류가 발생했습니다.");
    }
  };

  const getAvatarImage = (avatarId) => {
    return `/assets/avatar${avatarId}.jpg`;
  };

  // ✅ 로그아웃 처리 함수 (토큰 삭제 후 이동)
  const handleLogout = () => {
    Cookies.remove("token"); // 🔥 토큰 삭제
    window.location.href = `https://kauth.kakao.com/oauth/logout?client_id=${clientId}&logout_redirect_uri=${redirectUri}`;
    navigate("/"); // 🔥 로그인 페이지로 이동
  };

>>>>>>> develop
  return (
    <div className="my_page_container">
      <div className="gradient_section">
        <div className="profile_section">
          <div className="profile_image_container">
            <img
              // src={profileImage}
              src={
                currentAvatar
                  ? getAvatarImage(currentAvatar)
                  : getAvatarImage(1)
              }
              alt="프로필 이미지"
              className="mypage_profile_image"
              onClick={() => setIsProfileModalOpen(true)}
              style={{ cursor: "pointer" }}
            />
          </div>
          <div className="user_info">
            <span className="user_name">{nickname}</span> /
            <span className="user_age"> {age}</span>
          </div>
        </div>

        <div className="menu_section">
          <div className="menu_category">
            <span className="category_title">
              관심사
              <img
                src={next}
                alt="다음"
                className="next_icon_small"
                onClick={() => navigate("/interest")}
              />
            </span>
            <div className="menu_items_interest">
              <div className="menu_item" onClick={() => navigate("/interest")}>
                <div className="writing_categories">
                  {interests.length > 0 ? (
                    interests.map((interest, index) => (
                      <span key={index}>{interest}</span>
                    ))
                  ) : (
                    <span>관심사를 추가하세요</span> // 관심사 없을 때 표시
                  )}
                </div>
              </div>
            </div>
          </div>

          <div className="menu_category-category">
            <span className="introduction-title">자기소개</span>
            <div className="introduction-container">
              <div className="introduction-content">
                <span className="introduction-text">{content}</span>
              </div>
              <img
                src={modify}
                alt="modify-image"
                className="introduction-modify-image"
                onClick={() => setIsPopupOpen((prev) => !prev)}
                style={{ cursor: "pointer" }}
              />
            </div>

            {/* 아래로 확장되는 입력창 */}
            <IntroductionPopup
              initialValue={content}
              onSave={(newContent) => {
                setContent(newContent);
                setIsPopupOpen(false);
              }}
              isOpen={isPopupOpen}
            />
          </div>

          <div className="menu_category">
            <span className="category_title">고객만족센터</span>
            <div className="menu_items">
              <div className="menu_item" onClick={() => navigate("/notice")}>
                <span>공지사항</span>
                <img src={next} alt="다음" className="next_icon" />
              </div>
            </div>
          </div>
          <div className="menu_category">
            <span className="category_title">개인정보</span>
            <div className="menu_items">
              <div className="menu_item" onClick={() => navigate("/nickname")}>
                <span>닉네임 변경</span>
                <img src={next} alt="다음" className="next_icon" />
              </div>
              <div className="menu_item" onClick={handleLogout}>
                <span>로그아웃</span>
                <img src={next} alt="다음" className="next_icon" />
              </div>
            </div>
          </div>
        </div>

        <div className="version_info">
          <span>앱 버전 1.2</span>
        </div>

        <div className="withdraw" onClick={() => navigate("/sign-out")}>
          탈퇴하기
        </div>
      </div>

      <AvatarModal
        isOpen={isProfileModalOpen}
        onClose={() => {
          handleModalConfirm();
          setIsProfileModalOpen(false);
        }}
        currentAvatar={currentAvatar}
        onSelect={handleAvatarSelect}
      />
      <NavigationBar />

      {loading && (
        <div className="loading-overlay">
          <img src={lvup} alt="레벨업" className="loading-gif" />
        </div>
      )}
    </div>
  );
}

export default MyPage;

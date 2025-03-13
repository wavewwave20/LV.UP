import React, { useState, useEffect } from "react";
import "./NoticeList.css";
<<<<<<< HEAD
import { notices } from "../MyPage/Notice"; // 공지사항 데이터 가져오기
=======
import { fetchAnnouncements } from "../api/BoardAPI";
import PostDetailModal from "../AdminPage/PostDetailModal";
import Cookies from "js-cookie";
>>>>>>> develop

function NoticeList() {
  const [currentNoticeIndex, setCurrentNoticeIndex] = useState(0);

  const [selectedPostId, setSelectedPostId] = useState(null);

  useEffect(() => {
<<<<<<< HEAD
=======
    const token = Cookies.get("token"); // 쿠키에서 토큰 가져오기
    if (!token) {
      return; // ✅ 토큰이 없으면 API 요청 안 함
    }
    const getNotices = async () => {
      try {
        const response = await fetchAnnouncements();

        if (response.data.announcements) {
          // 최대 3개까지만 가져오기 announcement
          const latestNotices = response.data.announcements.slice(0, 3);
          setNotices(latestNotices);
        }
      } catch (error) {
        console.error("❌ 공지사항 가져오기 실패:", error);
      } finally {
        setLoading(false);
      }
    };

    getNotices();
  }, []);

  useEffect(() => {
>>>>>>> develop
    if (notices.length === 0) return;

    const interval = setInterval(() => {
      setCurrentNoticeIndex((prevIndex) => (prevIndex + 1) % notices.length); // 🔥 notices.length를 넘으면 다시 0으로 돌아감
    }, 5000); // 5초마다 변경

    return () => clearInterval(interval);
<<<<<<< HEAD
  }, []);
=======
  }, [notices]);

  const handleNoticeClick = () => {
    if (notices.length > 0) {
      const notice = notices[currentNoticeIndex];
      setSelectedNotice(notice);
      setIsModalOpen(true);
    }
  };

  if (loading) {
    return (
      <div className="announcement">
        <div className="announcement-list">
          <p>공지사항을 불러오는 중...</p>
        </div>
      </div>
    );
  }
>>>>>>> develop

  return (
    <div className="announcement">
      <div className="announcement-list">
<<<<<<< HEAD
        <div className="announcement-item fade-in-up">
          <span className="notice-tag">{notices[currentNoticeIndex].tag}</span>
          <span className="notice-title">
            {notices[currentNoticeIndex].title}
          </span>
          <span className="notice-date">
            {notices[currentNoticeIndex].date}
          </span>
        </div>
=======
        {notices.length > 0 ? (
          <div
            className="announcement-item fade-in-up"
            style={{ cursor: "pointer" }}
            onClick={() => {
              if (notices.length > 0) {
                const notice = notices[currentNoticeIndex];
                setSelectedPostId(notice.board_id);
              }
            }}
          >
            <span className="notice-tag">
              {notices[currentNoticeIndex]?.type === "A"
                ? "[공지사항]"
                : notices[currentNoticeIndex]?.type === "E"
                ? "[이벤트]"
                : "[공지]"}
            </span>
            <span className="notice-title">
              {notices[currentNoticeIndex]?.board_title}
            </span>
            <span className="notice-date">
              {new Date(
                notices[currentNoticeIndex]?.created_at
              ).toLocaleDateString("ko-KR")}
            </span>
          </div>
        ) : (
          <p>공지사항이 없습니다.</p>
        )}
>>>>>>> develop
      </div>
      {selectedPostId && (
        <PostDetailModal
          postId={selectedPostId}
          onClose={() => setSelectedPostId(null)}
        />
      )}
    </div>
  );
}

export default NoticeList;

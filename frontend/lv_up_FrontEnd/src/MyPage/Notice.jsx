export const notices = [
  {
    id: 1,
    tag: "[공지사항]",
    title: "v 1.2 업데이트 안내",
    date: "2025.01.19",
  },
  {
    id: 2,
    tag: "[공지사항]",
    title: "v 1.1 업데이트 안내",
    date: "2025.01.16",
  },
  {
    id: 3,
    tag: "[이벤트]",
    title: "매칭권 두 배 이벤트",
    date: "2025.01.16",
  },
];

import React from "react";
import { useNavigate } from "react-router-dom";
import back from "../assets/imageFile/backButton.png";
import "./Notice.css";

function Notice() {
  const navigate = useNavigate();
<<<<<<< HEAD
=======
  const [notices, setNotices] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [selectedPostId, setSelectedPostId] = useState(null);

  useEffect(() => {
    loadNotices(currentPage);
  }, [currentPage]);

  const loadNotices = async (page) => {
    try {
      const response = await fetchAnnouncements(page);
      setNotices(response.data.announcements);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error("공지사항 불러오기 실패:", error);
    }
  };

  const handlePageChange = (page) => {
    if (page >= 1 && page <= totalPages) {
      setCurrentPage(page);
    }
  };
>>>>>>> develop

  return (
    <div className="notice_container">
      <div className="back_button" onClick={() => navigate("/mypage")}>
        <img src={back} alt="뒤로가기" />
      </div>
      <h1 className="notice">공지사항</h1>
      <hr className="notice_line" />

      <div className="notice_list">
<<<<<<< HEAD
        {notices.map((notice) => (
          <div key={notice.id} className="notice_item" style={{ cursor: 'pointer' }}>
            <div className="notice_box">
              <span className="notice_tag">{notice.tag}</span>
              <span className="notice_title">{notice.title}</span>
              <span className="notice_date">{notice.date}</span>
=======
        {notices?.length > 0 ? (
          notices.map((notice) => (
            <div
              key={notice.board_id}
              className="notice_item"
              style={{ cursor: "pointer" }}
              onClick={() => setSelectedPostId(notice.board_id)}
            >
              <div className="notice_box">
                <span className="notice_tag">
                  {notice.type === "A"
                    ? "[공지사항]"
                    : notice.type === "E"
                    ? "[이벤트]"
                    : notice.type}
                </span>
                <span className="notice_title">{notice.board_title}</span>
                <span className="notice_date">
                  {new Date(notice.created_at).toLocaleDateString()}
                </span>
              </div>
>>>>>>> develop
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Notice;

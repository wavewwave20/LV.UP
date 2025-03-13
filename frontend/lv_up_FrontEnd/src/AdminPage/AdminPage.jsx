import React, { useState } from "react";
import "./AdminPage.css";
import PostsList from "./PostsList";
import ReportsList from "./ReportsList";

export default function AdminPage() {
  const [activeTab, setActiveTab] = useState("posts");

  return (
    <div className="admin_container">
      <AdminHeader
        activeTab={activeTab}
        setActiveTab={setActiveTab}
      />
      <AdminBody activeTab={activeTab} />
    </div>
  );
}

function AdminHeader({ activeTab, setActiveTab }) {
  return (
    <header className="admin_header">
      <div className="admin_logo">귀염뽀짝 관리자</div>
      <nav className="admin_nav">
        <button
          className={activeTab === "posts" ? "tab_button active" : "tab_button"}
          onClick={() => setActiveTab("posts")}
        >
          게시글 (공지/이벤트)
        </button>
        <button
          className={activeTab === "report" ? "tab_button active" : "tab_button"}
          onClick={() => setActiveTab("report")}
        >
          신고 내역
        </button>
      </nav>
    </header>
  );
}

function AdminBody({ activeTab }) {
  return (
    <div className="admin_body">
      {activeTab === "posts" && <PostsList />}
      {activeTab === "report" && <ReportsList />}
    </div>
  );
}
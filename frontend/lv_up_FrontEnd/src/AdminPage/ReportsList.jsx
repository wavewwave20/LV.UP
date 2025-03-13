// ReportsList.jsx
import React, { useState, useEffect } from 'react';
import ReportDetailModal from './ReportDetailModal';
import { fetchAdminReports } from '../api';
import './PostsList.css';

export default function ReportsList() {
  const [reports, setReports] = useState([]);
  const [selectedReport, setSelectedReport] = useState(null);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // 검색 필터 상태
  const [searchFilters, setSearchFilters] = useState({
    date: '',
    reporter: '',
    reported: '',
    status: ''
  });

  useEffect(() => {
    loadReports();
  }, [currentPage]);

  const loadReports = async () => {
    try {
      setLoading(true);
      const response = await fetchAdminReports();
      const data = response.data;
      setReports(data.content);
      setTotalPages(data.total_pages);
    } catch (error) {
      console.error('❌ 신고 내역 로딩 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e) => {
    const { name, value } = e.target;
    setSearchFilters(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // 필터링된 리포트
  const filteredReports = reports.filter(report => {
    const matchDate = !searchFilters.date || report.created_at.includes(searchFilters.date);
    const matchReporter = !searchFilters.reporter || String(report.reporter).includes(searchFilters.reporter);
    const matchReported = !searchFilters.reported || String(report.reportee).includes(searchFilters.reported);
    const matchStatus = !searchFilters.status || report.state === searchFilters.status;

    return matchDate && matchReporter && matchReported && matchStatus;
  });

  return (
    <div>
      <h2>신고 내역</h2>
      <div className="search_container">
        <input
          type="date"
          name="date"
          value={searchFilters.date}
          onChange={handleSearch}
          placeholder="신고일 검색"
        />
        <input
          type="text"
          name="reporter"
          value={searchFilters.reporter}
          onChange={handleSearch}
          placeholder="신고자 검색"
        />
        <input
          type="text"
          name="reported"
          value={searchFilters.reported}
          onChange={handleSearch}
          placeholder="신고받은 자 검색"
        />
        <select
          name="status"
          value={searchFilters.status}
          onChange={handleSearch}
        >
          <option value="">처리 여부</option>
          <option value="대기중">대기중</option>
          <option value="처리됨">처리됨</option>
        </select>
      </div>

      {loading ? (
        <p>로딩중...</p>
      ) : (
        <table className="cute_table">
          <thead>
            <tr>
              <th>번호</th>
              <th>신고일</th>
              <th>신고내용</th>
              <th>신고자</th>
              <th>피신고자</th>
              <th>처리여부</th>
            </tr>
          </thead>
          <tbody>
            {filteredReports.map((report) => (
              <tr
                key={report.report_id}
                onClick={() => setSelectedReport(report)}
                style={{ cursor: 'pointer' }}
              >
                <td>{report.report_id}</td>
                <td>{new Date(report.created_at).toLocaleDateString()}</td>
                <td>{report.description}</td>
                <td>{report.reporter}</td>
                <td>{report.reportee}</td>
                <td>{report.state}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {/* 페이지네이션 */}
      <div className="pagination">
        <button 
          onClick={() => setCurrentPage(prev => prev - 1)}
          disabled={currentPage === 0}
        >
          이전
        </button>
        <span>{currentPage + 1} / {totalPages}</span>
        <button 
          onClick={() => setCurrentPage(prev => prev + 1)}
          disabled={currentPage === totalPages - 1}
        >
          다음
        </button>
      </div>

      {selectedReport && (
        <ReportDetailModal
          reportId={selectedReport.report_id}
          onClose={() => setSelectedReport(null)}
        />
      )}
    </div>
  );
}
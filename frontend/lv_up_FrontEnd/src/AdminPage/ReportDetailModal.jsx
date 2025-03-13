import React, { useState, useEffect } from 'react';
import { fetchAdminReportById, createAdminPenalty } from '../api';
import './ReportDetailModal.css';

export default function ReportDetailModal({ reportId, onClose }) {
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [endDate, setEndDate] = useState('');
  const [penaltyOptions] = useState([1, 2, 3]); // 기본 패널티 옵션

  useEffect(() => {
    loadReportDetail();
  }, [reportId]);

  const loadReportDetail = async () => {
    try {
      const response = await fetchAdminReportById(reportId);
      console.log('✅ 신고 상세 정보:', response.data);
      setReport(response.data);
    } catch (error) {
      console.error('❌ 신고 상세 정보 로딩 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePenalty = async () => {
    if (!endDate) {
      alert('정지 종료일을 선택해주세요.');
      return;
    }

    try {
      const penaltyData = {
        penaltyOptions: penaltyOptions,
        endAt: `${endDate}T23:59:59`
      };

      await createAdminPenalty(report.reportee, report.report_id, penaltyData);
      alert('패널티가 성공적으로 적용되었습니다.');
      onClose();
    } catch (error) {
      console.error('❌ 패널티 적용 실패:', error);
      alert('패널티 적용 중 오류가 발생했습니다.');
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="modal_overlay">
        <div className="modal_content">
          <p>로딩중...</p>
        </div>
      </div>
    );
  }

  if (!report) return null;

  return (
    <div className="modal_overlay" onClick={(e) => {
      if (e.target === e.currentTarget) onClose();
    }}>
      <div className="modal_content">
        <h3 className="modal_title">신고 상세 내역</h3>
        
        <div className="info_row">
          <span className="info_label">신고 번호:</span>
          <span className="info_value">{report.report_id}</span>
        </div>

        <div className="info_row">
          <span className="info_label">신고자 ID:</span>
          <span className="info_value">{report.reporter}</span>
        </div>

        <div className="info_row">
          <span className="info_label">피신고자 ID:</span>
          <span className="info_value">{report.reportee}</span>
        </div>

        <div className="info_row">
          <span className="info_label">신고 내용:</span>
          <span className="info_value">{report.description}</span>
        </div>

        <div className="info_row">
          <span className="info_label">처리 상태:</span>
          <span className="info_value">{report.state}</span>
        </div>

        <div className="info_row">
          <span className="info_label">신고일:</span>
          <span className="info_value">{formatDate(report.created_at)}</span>
        </div>

        {report.report_types && report.report_types.length > 0 && (
          <div className="info_row">
            <span className="info_label">신고 유형:</span>
            <ul className="report_types_list">
              {report.report_types.map((type) => (
                <li key={type.report_type_id} className="report_type_item">
                  {type.description}
                </li>
              ))}
            </ul>
          </div>
        )}
        
        {report.state !== '처리됨' && (
          <div className="penalty_section">
            <h4>패널티 적용</h4>
            <div className="date_input_container">
              <label>정지 종료일: </label>
              <input
                type="date"
                className="date_input"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div className="button_container">
              <button 
                className="ban_button" 
                onClick={handlePenalty}
                disabled={report.state === '처리됨'}
              >
                패널티 적용
              </button>
            </div>
          </div>
        )}

        <div className="button_container">
          <button className="report_close_button" onClick={onClose}>
            닫기
          </button>
        </div>
      </div>
    </div>
  );
}
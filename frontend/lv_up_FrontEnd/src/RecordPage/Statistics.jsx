import React, { useState, useEffect, useRef } from "react";
import Chart from "chart.js/auto";
import "./Statistics.css";
import { fetchStatistics } from "../api/AITalkAPI";

const StatisticsModal = ({ isOpen, onClose, date }) => {
  const [currentDate, setCurrentDate] = useState(date);
  const [statisticsData, setStatisticsData] = useState(null);
  const lineCanvasRef = useRef(null);
  const lineChartRef = useRef(null);
  const radarCanvasRef = useRef(null);
  const radarChartRef = useRef(null);

  // 부모로부터 받은 date prop이 변경될 때마다 currentDate 업데이트
  useEffect(() => {
    setCurrentDate(date);
  }, [date]);

  // 모달이 열리거나 currentDate가 변경되면 API 호출하여 데이터 저장
  useEffect(() => {
    if (isOpen && currentDate) {
      fetchStatistics({
        year: currentDate.getFullYear(),
        month: currentDate.getMonth() + 1,
      }).then((response) => {
        setStatisticsData(response.data);
      });
    }
  }, [isOpen, currentDate]);

  // 해당 월의 총 일 수 계산
  const getDaysInMonth = (v) => {
    const year = v.getFullYear();
    const month = v.getMonth();
    return new Date(year, month + 1, 0).getDate();
  };

  // x축 라벨 생성: 해당 월의 1일부터 마지막 일까지 문자열 배열
  const getLabels = () => {
    const days = getDaysInMonth(currentDate);
    return Array.from({ length: days }, (_, i) => `${i + 1}`);
  };

  // daily_detail에서 각 항목별 평균값(0이 아닌 값들만)의 배열을 생성 (5개 항목)
  const computeRadarData = () => {
    if (!statisticsData || !statisticsData.daily_detail) return [0, 0, 0, 0, 0];
    const details = statisticsData.daily_detail;
    const numCategories = 5;
    const sums = Array(numCategories).fill(0);
    const counts = Array(numCategories).fill(0);

    details.forEach((day) => {
      day.forEach((value, idx) => {
        if (value !== 0) {
          sums[idx] += value;
          counts[idx] += 1;
        }
      });
    });
    return sums.map((sum, idx) => (counts[idx] > 0 ? sum / counts[idx] : 0));
  };

  // 선(Line) 차트 생성/갱신: statisticsData가 있을 때 실행
  useEffect(() => {
    if (!isOpen || !statisticsData) return;
    const ctx = lineCanvasRef.current.getContext("2d");
    const config = {
      type: "line",
      data: {
        labels: getLabels(),
        datasets: [
          {
            label: "일일평균점수",
            data: statisticsData.daily_overall,
            borderColor: "rgb(75, 192, 192)",
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            fill: false,
            tension: 0.3,
            pointRadius: 2,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          y: { min: 0, max: 100 },
        },
        plugins: {
          legend: { position: "top" },
          title: {
            display: false,
            text: `${currentDate.getFullYear()}년 ${
              currentDate.getMonth() + 1
            }월 통계`,
          },
        },
      },
    };

    if (lineChartRef.current) {
      lineChartRef.current.destroy();
    }
    lineChartRef.current = new Chart(ctx, config);
    return () => {
      if (lineChartRef.current) {
        lineChartRef.current.destroy();
        lineChartRef.current = null;
      }
    };
  }, [isOpen, currentDate, statisticsData]);

  // 레이더(Radar) 차트 생성/갱신: statisticsData가 있을 때 실행
  useEffect(() => {
    if (!isOpen || !statisticsData) return;
    const ctx = radarCanvasRef.current.getContext("2d");
    const radarData = computeRadarData();
    const config = {
      type: "radar",
      data: {
        labels: [
          "감정&의도",
          "상호작용",
          "상황&분위기",
          "목적&내용",
          "예의&긍정적",
        ],
        datasets: [
          {
            label: "의사소통능력",
            data: radarData,
            fill: true,
            backgroundColor: "rgba(255, 99, 132, 0.2)",
            borderColor: "rgb(255, 99, 132)",
            pointBackgroundColor: "rgb(255, 99, 132)",
            pointBorderColor: "#fff",
            pointHoverBackgroundColor: "#fff",
            pointHoverBorderColor: "rgb(255, 99, 132)",
          },
        ],
      },
      options: {
        scales: {
          r: { min: 0, max: 100 },
        },
        elements: { line: { borderWidth: 3 } },
        plugins: {
          title: {
            display: false,
            text: `${currentDate.getFullYear()}년 ${
              currentDate.getMonth() + 1
            }월 통계 (Radar Chart)`,
          },
        },
      },
    };

    if (radarChartRef.current) {
      radarChartRef.current.destroy();
    }
    radarChartRef.current = new Chart(ctx, config);
    return () => {
      if (radarChartRef.current) {
        radarChartRef.current.destroy();
        radarChartRef.current = null;
      }
    };
  }, [isOpen, statisticsData, currentDate]);

  const goToPreviousMonth = () => {
    setCurrentDate((prev) => {
      const year = prev.getFullYear();
      const month = prev.getMonth();
      return new Date(year, month - 1, 1);
    });
  };

  const goToNextMonth = () => {
    setCurrentDate((prev) => {
      const year = prev.getFullYear();
      const month = prev.getMonth();
      return new Date(year, month + 1, 1);
    });
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h2 className="title">나의 통계</h2>
        <div className="modal-navigation">
          <button onClick={goToPreviousMonth}>◀</button>
          <span>
            {currentDate.getFullYear()}년 {currentDate.getMonth() + 1}월
          </span>
          <button onClick={goToNextMonth}>▶</button>
        </div>
        <div className="chart-container">
          <canvas ref={lineCanvasRef} />
        </div>
        <div className="chart-container">
          <canvas ref={radarCanvasRef} />
        </div>
        <div className="modal-footer">
          <button className="modal-close-btn" onClick={onClose}>
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default StatisticsModal;

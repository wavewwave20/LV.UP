import React from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

// Chart.js의 필수 요소 등록
ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const LineChart = () => {
  const data = {
    labels: ["1월", "2월", "3월", "4월", "5월", "6월"], // X축 값
    datasets: [
      {
        label: "매출액",
        data: [30, 45, 60, 50, 70, 80], // Y축 값
        borderColor: "rgba(75, 192, 192, 1)", // 선 색상
        backgroundColor: "rgba(75, 192, 192, 0.2)", // 점 색상
        borderWidth: 2, // 선 굵기
        pointRadius: 5, // 데이터 점 크기
        pointBackgroundColor: "rgba(75, 192, 192, 1)", // 데이터 점 색상
        tension: 0.4, // 곡선 정도 (0이면 직선)
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { display: true },
      title: { display: true, text: "월별 매출액" }, // 차트 제목
    },
    scales: {
      x: { grid: { display: false } }, // X축 그리드 숨기기
      y: { beginAtZero: true },
    },
  };

  return <Line data={data} options={options} />;
};

export default LineChart;

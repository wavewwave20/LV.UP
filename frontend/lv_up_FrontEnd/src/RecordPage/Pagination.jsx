import React from 'react';
import './Pagination.css';

export const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  return totalPages > 1 ? (
    <div className="pagination">
      <button
        onClick={() => onPageChange(Math.max(0, currentPage - 1))}
        disabled={currentPage === 0}
      >
        이전
      </button>
      <span>
        {currentPage + 1} / {totalPages}
      </span>
      <button
        onClick={() => onPageChange(Math.min(totalPages - 1, currentPage + 1))}
        disabled={currentPage === totalPages - 1}
      >
        다음
      </button>
    </div>
  ) : null;
};
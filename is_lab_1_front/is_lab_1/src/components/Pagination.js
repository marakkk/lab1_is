import React from 'react';
import './Pagination.css';

function Pagination({ currentPage, totalPages, onPageChange }) {
    const handlePrevious = () => {
        if (currentPage > 1) {
            onPageChange(currentPage - 1);
        }
    };

    const handleNext = () => {
        if (currentPage < totalPages) {
            onPageChange(currentPage + 1);
        }
    };

    return (
        <div className="pagination">
            <button
                className="pagination__button"
                onClick={handlePrevious}
                disabled={currentPage === 1}
            >
                Предыдущая
            </button>

            <span className="pagination__info">
                Страница {currentPage} из {totalPages}
            </span>

            <button
                className="pagination__button"
                onClick={handleNext}
                disabled={currentPage === totalPages}
            >
                Следующая
            </button>
        </div>
    );
}

export default Pagination;

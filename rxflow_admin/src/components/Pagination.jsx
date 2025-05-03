import React, { useState } from 'react'

const Pagination = ({ totalPage, maxPagesToShow, page, setPage }) => {

    const getPageNumbers = () => {
        const pages = []

        if (totalPage < maxPagesToShow) {
            for (let i = 1; i <= totalPage; i++) pages.push(i)
        } else {
            if (page <= 3) {
                pages.push(1, 2, 3, 4, '...', totalPage)
            } else if (page >= totalPage - 2) {
                pages.push(1, '...', totalPage - 3, totalPage - 2, totalPage - 1, totalPage)
            } else {
                pages.push(1, '...', page - 1, page, page + 1, '...', totalPage)
            }
        }

        return pages
    }

    return (
        <div className='flex items-center justify-center mt-4 space-x-2 '>
            <button
                onClick={() => setPage((prev) => Math.max(prev - 1, 1))}
                className='px-3 py-1 rounded-lg border text-sm font-medium hover:bg-gray-100 disabled:opacity-50'
                disabled={page === 1}
            >
                Prev
            </button>
            {
                getPageNumbers().map((pg, index) => {
                    return (
                        <button
                            key={index}
                            onClick={() => typeof pg === 'number' && setPage(pg)}
                            className={`px-3 py-1 rounded-lg border text-sm font-medium 
                                                ${page === pg ? 'bg-blue-500 text-white' : 'hover:bg-gray-100'} 
                                                ${pg === '...' ? 'cursor-default text-gray-400' : ''}`
                            }
                        >
                            {pg}
                        </button>
                    )
                })
            }
            <button
                onClick={() => setPage((prev) => Math.min(prev + 1, totalPage))}
                className='px-3 py-1 rounded-lg border text-sm font-medium hover:bg-gray-100 disabled:opacity-50'
                disabled={page === totalPage}
            >
                Next
            </button>
        </div>
    )
}

export default Pagination
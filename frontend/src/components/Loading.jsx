/* eslint-disable react/prop-types */
import React from 'react'

export const Loading = () => {
    return (
        <div className="fixed inset-0 bg-white/50 backdrop-blur-sm flex items-center justify-center z-50">
            <div className="w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin"></div>
        </div>
        // <div className="flex items-center justify-center w-full h-full">
        //     <div className="animate-spin rounded-full h-8 w-8 border-4 border-blue-500 border-t-transparent"></div>
        // </div>

    )
}

const LoadWrapper = ({ loading, children }) => {
    return loading ? (
        <div className="flex items-center justify-center h-40">
            <Loading />
        </div>
    ) : (
        children
    )
}

export default LoadWrapper
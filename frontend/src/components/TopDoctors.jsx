/* eslint-disable no-unused-vars */
import React, { useContext } from 'react'
import {  useNavigate } from 'react-router-dom'
import { AppContext } from '../context/AppContext'
import DoctorCard from './DoctorCard'

const TopDoctors = () => {

    const navigate = useNavigate()
    const { doctors } = useContext(AppContext)

    return (
        <div className='flex flex-col items-center gap-4 my-16 text-gray-900 md:mx-10'>
            <h1 className='text-3xl font-medium'>Top Doctors to Book</h1>
            <p className='sm:w-1/3 text-center text-sm'>Simply browse through our extensive list of trusted doctors</p>
            <div onClick={() => scrollTo(0,0)} className='w-full grid grid-cols-auto gap-4 pt-5 gap-y-6 px-3 sm:px-0 '>
                {
                    doctors.slice(0, 10).map((doctor, index) => (
                        <DoctorCard key={index} doctor={doctor} />
                    ))
                }
            </div>

            {/* ------------------------ More Button --------------------------- */}
            <button onClick={() => {
                navigate("/doctors");
                scrollTo(0, 0);
            }}
                className='bg-blue-50 text-gray-600 px-12 py-3 rounded-full mt-10 cursor-pointer'
            >
                More
            </button>
        </div>
    )
}

export default TopDoctors
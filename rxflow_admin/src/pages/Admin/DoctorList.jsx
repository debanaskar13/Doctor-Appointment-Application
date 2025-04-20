import React, { useContext, useEffect } from 'react'
import { AdminContext } from '../../context/AdminContext'
import ApiService from '../../api/Axios'
import { toast } from 'react-toastify'

const DoctorList = () => {

    const { token, doctors, getAllDoctors, changeAvailability } = useContext(AdminContext)



    useEffect(() => {

        if (token) {
            getAllDoctors()
        }

    }, [])

    return (
        <div className='m-5 max-h-[90vh] overflow-scroll'>
            <h1 className='text-lg font-medium'>All Doctors</h1>
            <div className='w-full flex flex-wrap gap-4 pt-5 gap-y-6'>
                {
                    doctors.map((item, index) => (
                        <div key={index} className='border border-indigo-200 max-w-56 overflow-hidden cursor-pointer group'>
                            <img className='h-56 w-56 bg-indigo-50 group-hover:bg-[#5F6FFF] transition-all duration-500' src={item.user.image} alt="" />
                            <div className='p-4'>
                                <p className='text-neutral-800 text-lg font-medium'>{item.user.name}</p>
                                <p className='text-zinc-600 text-sm'>{item.speciality}</p>
                                <div className='mt-2 flex items-center gap-1 text-sm'>
                                    <input onChange={() => changeAvailability(item.id)} type="checkbox" checked={item.available} />
                                    <p>Available</p>
                                </div>
                            </div>
                        </div>
                    ))
                }
            </div>


        </div>
    )
}

export default DoctorList
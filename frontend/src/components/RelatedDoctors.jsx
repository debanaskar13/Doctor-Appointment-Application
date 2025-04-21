/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React, { useContext, useEffect, useState } from 'react'
import { AppContext } from '../context/AppContext'
import DoctorCard from './DoctorCard'

const RelatedDoctors = ({ docId, speciality }) => {

    const { doctors } = useContext(AppContext)

    const [relDoc, setRelDocs] = useState([])

    useEffect(() => {

        if (doctors.length > 0 && speciality) {
            const doctorsData = doctors.filter(doc => doc.speciality == speciality && doc.id !== Number(docId))
            setRelDocs(doctorsData)
        }

    }, [doctors, speciality, docId])


    return (
        <div className='flex flex-col items-center gap-4 my-16 text-gray-900 md:mx-10'>
            <h1 className='text-3xl font-medium'>Related Doctors</h1>
            <p className='sm:w-1/3 text-center text-sm'>Simply browse through our extensive list of trusted {speciality} doctors</p>
            <div onClick={() => scrollTo(0, 0)} className='w-full grid grid-cols-auto gap-4 gap-y-6'>
                {
                    relDoc.slice(0, 5).map((doctor, index) => (
                        <DoctorCard key={index} doctor={doctor} />
                    ))
                }
            </div>
        </div>
    )
}

export default RelatedDoctors
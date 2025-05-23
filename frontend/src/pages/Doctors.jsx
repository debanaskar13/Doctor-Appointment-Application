/* eslint-disable react/jsx-key */
/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-unused-vars */
import React, { useContext, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { AppContext } from "../context/AppContext"
import DoctorCard from "../components/DoctorCard"
import Loading from '../components/Loading'

const Doctors = () => {

  const { speciality } = useParams()
  const [filterDoc, setFilterDoc] = useState([])
  const [showFilter, setShowFilter] = useState(false)
  const [loading, setLoading] = useState(false)

  const navigate = useNavigate()

  const { doctors } = useContext(AppContext)

  const [specialityList, setSpecialityList] = useState([])

  const getSpecialityList = () => {
    const set = new Set()
    doctors.forEach(doctor => {
      set.add(doctor.speciality)
    });
    setSpecialityList(Array.from(set))
  }

  const applyFilter = async () => {

    if (speciality) {
      setFilterDoc(doctors.filter((doctor) => doctor.speciality == speciality))
    } else {
      setFilterDoc(doctors)
    }
  }

  useEffect(() => {
    scrollTo(0, 0)
  }, [])

  useEffect(() => {
    getSpecialityList()
    applyFilter()
  }, [speciality, doctors])

  return loading ? <Loading /> : (
    <div>
      <p className='text-gray-600'>Browse through the doctors specialist.</p>
      <div className='flex flex-col sm:flex-row items-start gap-5 mt-5'>
        <button className={`py-1 px-3 border border-gray-300 rounded text-sm transition-all sm:hidden ${showFilter ? 'bg-primary text-white' : ''}`} onClick={() => setShowFilter(prev => !prev)}>Filter</button>
        <div className={`flex-col gap-4 text-sm text-gray-600 ${showFilter ? 'flex' : 'hidden sm:flex'}`}>

          {
            specialityList.map((special, index) => (

              <p key={index} onClick={() => speciality === special ? navigate("/doctors") : navigate(`/doctors/${special}`)} className={`w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer ${speciality === special ? "active-list" : ""}`}>{special}</p>
            ))
          }

          {/* <p onClick={() => speciality === 'Gynecologist' ? navigate("/doctors") : navigate("/doctors/Gynecologist")} className='w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer'>Gynecologist</p>
            <p onClick={() => speciality === 'Dermatologist' ? navigate("/doctors") : navigate("/doctors/Dermatologist")} className='w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer'>Dermatologist</p>
            <p onClick={() => speciality === 'Pediatricians' ? navigate("/doctors") : navigate("/doctors/Pediatricians")} className='w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer'>Pediatricians</p>
            <p onClick={() => speciality === 'Neurologist' ? navigate("/doctors") : navigate("/doctors/Neurologist")} className='w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer'>Neurologist</p>
            <p onClick={() => speciality === 'Gastroenterologist' ? navigate("/doctors") : navigate("/doctors/Gastroenterologist")} className='w-[94vw] sm:w-auto pl-3 py-1.5 pr-16 border border-gray-300 rounded transition-all cursor-pointer'>Gastroenterologist</p> */}
        </div>

        <div className='w-full grid grid-cols-auto gap-4 gap-y-6'>
          {
            filterDoc.map((doctor, index) => (
              <DoctorCard key={index} doctor={doctor} />
            ))
          }
        </div>

      </div>
    </div>
  )
}

export default Doctors
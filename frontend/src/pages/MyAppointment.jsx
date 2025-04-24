/* eslint-disable no-unused-vars */
import React, { useContext } from 'react'
import { AppContext } from '../context/AppContext'
import { useState } from 'react'
import ApiService from '../api/Axios'
import { toast } from 'react-toastify'
import { useEffect } from 'react'
import LoadWrapper from '../components/Loading'
import { useNavigate } from 'react-router-dom'

const MyAppointment = () => {

  const { doctors, profile, token } = useContext(AppContext)
  const [appointments, setAppointments] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const months = ["", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

  const navigate = useNavigate()

  const slotDateFormat = (slotDate) => {
    const arr = slotDate.split('_')
    return arr[0] + " " + months[arr[1]] + " " + arr[2]
  }

  const getMyAppointments = async () => {

    try {
      setIsLoading(true)

      const { data } = await ApiService.fetchMyAppointments()

      if (data.success) {
        console.log(data.appointments)
        setAppointments(data.appointments)
      } else {
        toast.error(data.message)
      }

    } catch (error) {
      console.log(error)
      toast.error(error.response.data.message)
    } finally {
      setIsLoading(false)
    }

  }

  useEffect(() => {

    if (token) {
      getMyAppointments()
    } else {
      navigate('/login')
    }

  }, [token])


  return (
    <LoadWrapper loading={isLoading}>

      <div>
        <p className='pb-3 mt-12 font-medium text-zinc-700 border-b border-gray-300'>My Appointments</p>
        <div>
          {
            appointments.map((item, index) => (
              <div className='grid grid-cols-[1fr_2fr] gap-4 sm:flex sm:gap-6 py-2 border-b border-gray-300' key={index}>
                <div>
                  <img className='w-32 h-32 bg-indigo-50' src={item.user.image} alt="" />
                </div>
                <div className='flex-1 text-sm text-zinc-600'>
                  <p className='text-neutral-800 font-semibold'>{item.user.name}</p>
                  <p>{item.doctor.speciality}</p>
                  <p className='text-zinc-700 font-medium mt-1'>Address : </p>
                  <p className='text-xs'>{item.user.address.line1}</p>
                  <p className='text-xs'>{item.user.address.line1}</p>
                  <p className='text-xs mt-1'><span className='text-sm text-neutral-700 font-medium'>Date & Time : </span> {slotDateFormat(item.slotDate)} | {item.slotTime}</p>
                </div>
                <div></div>
                <div className='flex flex-col gap-2 justify-end'>
                  <button className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border border-gray-300 rounded cursor-pointer hover:bg-[#5F6FFF] hover:text-white transition-all duration-300'>Pay Online</button>
                  <button className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border border-gray-300 rounded cursor-pointer hover:bg-red-600 hover:text-white transition-all duration-300'>Cancel Appointment</button>
                </div>
              </div>
            ))
          }
        </div>
      </div>
    </LoadWrapper>
  )
}

export default MyAppointment
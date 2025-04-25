/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable no-unused-vars */
import React, { useContext, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { AppContext } from '../context/AppContext'
import { assets } from '../assets/assets'
import RelatedDoctors from '../components/RelatedDoctors'
import { toast } from 'react-toastify'
import ApiService from '../api/Axios'
import LoadWrapper from '../components/Loading'

const Appointment = () => {

  const { docId } = useParams()
  const { doctors, currencySymbol, token, getAllDoctors } = useContext(AppContext)
  const daysOfWeek = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT']

  const [docInfo, setDocInfo] = useState(null)
  const [docSlots, setDocSlots] = useState([])
  const [slotIndex, setSlotIndex] = useState(0)
  const [slotTime, setSlotTime] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const navigate = useNavigate()

  const fetchDocInfo = async () => {
    const docInfo = doctors.find(doc => doc.id === Number(docId))
    setDocInfo(docInfo)
  }

  const getAvailableSlots = async () => {
    setDocSlots([])

    // getting current Date
    let today = new Date()

    for (let i = 0; i < 7; i++) {
      // getting date with index
      let currentDate = new Date(today)
      currentDate.setDate(today.getDate() + i)

      // Setting end time of the date with index
      let endTime = new Date()
      endTime.setDate(today.getDate() + i)
      endTime.setHours(21, 0, 0, 0)

      // Setting Hours
      if (today.getDate() === currentDate.getDate()) {
        currentDate.setHours(currentDate.getHours() > 10 ? currentDate.getHours() + 1 : 10)
        currentDate.setMinutes(currentDate.getMinutes() > 30 ? 30 : 0)
      } else {
        currentDate.setHours(10)
        currentDate.setMinutes(0)
      }

      let timeSlots = []

      while (currentDate < endTime) {
        let formattedTime = currentDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })

        let day = currentDate.getDate()
        let month = currentDate.getMonth() + 1
        let year = currentDate.getFullYear()

        const slotDate = day + "_" + month + "_" + year
        const slotTime = formattedTime

        // console.log(docInfo)
        const isSlotAvailable = docInfo && docInfo.slotsBooked[slotDate] && docInfo.slotsBooked[slotDate].includes(slotTime) ? false : true

        if (isSlotAvailable) {
          // add slot to array
          timeSlots.push(
            {
              datetime: new Date(currentDate),
              time: formattedTime
            }
          )
        }


        // Increment time by 30 minutes
        currentDate.setMinutes(currentDate.getMinutes() + 30)

      }

      setDocSlots(prev => ([...prev, timeSlots]))
    }
  }

  const bookAppointment = async () => {

    if (!token) {
      toast.warning('Login to book appointment')
      return navigate('/login')
    }

    try {
      setIsLoading(true)

      const date = docSlots[slotIndex][0].datetime

      let day = date.getDate()
      let month = date.getMonth() + 1
      let year = date.getFullYear()

      const slotDate = day + "_" + month + "_" + year

      const { data } = await ApiService.bookAppointment({
        doctorId: docId,
        slotDate: slotDate,
        slotTime: slotTime
      })

      if (data.success) {
        toast.success(data.message)
        getAllDoctors()
        navigate('/my-appointments')
      } else {
        toast.error(data.message)
      }

    } catch (error) {
      // console.log(error)
      toast.error(error.response.data.message)
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchDocInfo()
  }, [doctors, docId])

  useEffect(() => {
    getAvailableSlots()
  }, [docInfo])

  useEffect(() => {
    // console.log(docSlots)
  }, [docSlots])

  if (!docInfo) {
    return <div>Error : Context is not available</div>
  }

  return (
    <LoadWrapper loading={isLoading}>

      <div>
        {/* ----------------Doctor Details ------------------- */}

        <div className='flex flex-col sm:flex-row gap-4'>


          <div>
            <img className='bg-primary w-full sm:max-w-72 rounded-lg' src={docInfo.user.image} alt="" />
          </div>

          <div className='flex-1 border border-gray-400 rounded-lg p-8 py-7 bg-white mx-2 sm:mx-0 mt-[-80px] sm:mt-0'>
            {/* ------------------------- Doctor Info -------------------- */}

            <p className='flex items-center gap-2 text-2xl font-medium text-gray-900'>{docInfo.user.name} <img className='w-5' src={assets.verified_icon} alt="" /></p>

            <div className='flex items-center gap-2 text-sm text-gray-600 mt-1'>
              <p className=''>{docInfo.degree} - {docInfo.speciality}</p>
              <button className='py-0.5 px-2 border text-xs rounded-full'>{docInfo.experience}</button>
            </div>

            {/* ------------------------------ Doctor About ------------------ */}

            <div>
              <p className='flex items-center gap-1 text-sm font-medium text-gray-900 mt-3'>About <img src={assets.info_icon} alt="" /></p>
              <p className='text-sm text-gray-500 max-w-[700px] mt-1'>{docInfo.about}</p>
            </div>

            <p className='text-gray-500 font-medium mt-4'>
              Appointment Fee : <span className='text-gray-600'>{currencySymbol}{docInfo.fees}</span>
            </p>

          </div>
        </div>

        {/* --------------------------------- Booking Slots ------------------ */}
        <div className='sm:ml-72 sm:pl-4 mt-4 font-medium text-gray-700'>
          <p>Booking Slots</p>
          <div className='flex gap-3 w-full items-center overflow-x-scroll mt-4'>
            {
              docSlots.length && docSlots.map((item, index) => (
                <div onClick={() => setSlotIndex(index)} className={`transition-all text-center py-6 min-w-16 rounded-full cursor-pointer ${slotIndex == index ? 'bg-primary text-white' : 'border border-gray-300'}`} key={index}>
                  <p>{item[0] && daysOfWeek[item[0].datetime.getDay()]}</p>
                  <p>{item[0] && item[0].datetime.getDate()}</p>
                </div>
              ))
            }
          </div>

          <div className='flex items-center gap-3 w-full overflow-x-scroll mt-4'>
            {docSlots.length && docSlots[slotIndex].map((item, index) => (
              <p onClick={() => setSlotTime(item.time)} className={`text-sm font-light shrink-0 px-5 py-2 rounded-full cursor-pointer ${item.time === slotTime ? 'bg-primary text-white' : 'border text-gray-400 border-gray-300'}`} key={index}>
                {item.time.toLowerCase()}
              </p>
            ))}
          </div>

          <button onClick={bookAppointment} className='cursor-pointer bg-primary text-white text-sm font-light px-14 py-3 rounded-full my-6'>Book an Appointment</button>
        </div>

        {/* ---------------Listing Related Doctors ---------------- */}
        <RelatedDoctors docId={docId} speciality={docInfo.speciality} />

      </div>
    </LoadWrapper>
  )
}

export default Appointment
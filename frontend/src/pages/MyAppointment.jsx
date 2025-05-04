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

  const { doctors, profile, token, getAllDoctors } = useContext(AppContext)
  const [appointments, setAppointments] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const months = ["", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]

  const navigate = useNavigate()

  const slotDateFormat = (slotDate) => {
    const arr = slotDate.split('_')
    return arr[0] + " " + months[Number(arr[1])] + " " + arr[2]
  }

  const getMyAppointments = async () => {

    try {
      setIsLoading(true)

      const { data } = await ApiService.fetchMyAppointments()

      if (data.success) {
        // console.log(data.appointments)
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

  const cancelAppointment = async (appointmentId) => {

    console.log(appointmentId)

    try {

      const { data } = await ApiService.cancelAppointment({
        id: appointmentId
      });

      if (data.success) {
        toast.success(data.message)
        getMyAppointments()
        getAllDoctors()
      } else {
        toast.error('Appointment cancellation failed')
      }
    } catch (error) {
      console.log(error)
      toast.error(error.response.data.message)
    }
  }

  const initPay = (order) => {

    const options = {
      key: import.meta.env.VITE_RAZOR_PAY_API_KEY,
      amount: order.amount,
      currency: order.currency,
      name: 'RxFlow Apoointment Payment',
      description: 'Rxflow Apoointment Payment',
      order_id: order.id,
      receipt: order.receipt,
      handler: async (response) => {
        try {

          const { data } = await ApiService.verifyPaymentRazorpay({
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature
          })

          if (data.success) {
            toast.success(data.message)
            getMyAppointments()
          }

        } catch (error) {
          toast.error(error.response ? error.response.data.message : 'something went wrong')
        }
      }
    }

    const rzp = new window.Razorpay(options)
    rzp.open()

  }

  const appointmentRazorPay = async (appointmentId) => {

    try {

      const { data } = await ApiService.paymentRazorPay({
        id: appointmentId
      })

      if (data.success) {

        // console.log(data.order.modelJson.map)
        initPay(data.order.modelJson.map)

      } else {
        toast.error(data.message)
      }

    } catch (error) {
      console.log(error)
      toast.error(error.response ? error.response.data.message : 'something went wrong')
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
                  {
                    !item.cancelled && !item.payment && <button onClick={() => appointmentRazorPay(item.id)} className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border border-gray-300 rounded cursor-pointer hover:bg-[#5F6FFF] hover:text-white transition-all duration-300'>Pay Online</button>
                  }
                  {
                    !item.cancelled && item.payment && <button className='sm:min-w-48 py-2 bg-gray-200 text-gray-700 rounded'>Paid</button>
                  }
                  {
                    !item.cancelled && <button onClick={() => cancelAppointment(item.id)} className='text-sm text-stone-500 text-center sm:min-w-48 py-2 border border-gray-300 rounded cursor-pointer hover:bg-red-600 hover:text-white transition-all duration-300'>Cancel Appointment</button>
                  }
                  {
                    item.cancelled && <button className='sm:min-w-48 py-2 border border-red-500 text-red-500 rounded'>Appointment cancelled</button>
                  }

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
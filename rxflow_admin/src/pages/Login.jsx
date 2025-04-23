import React, { useContext, useState } from 'react'
import { toast } from 'react-toastify'
import ApiService from '../api/Axios'
import { AdminContext } from '../context/AdminContext'
import LoadWrapper from '../components/Loading'

const Login = () => {

  const [state, setState] = useState('Admin')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)


  const { setToken, setRole } = useContext(AdminContext)

  const onSumbitHandler = async (event) => {

    event.preventDefault()

    try {
      setLoading(true)
      const { data } = await ApiService.login({
        username, password
      })

      if (data.success) {
        if ((state === 'Admin' && data.role === 'ROLE_ADMIN') || (state === 'Doctor' && data.role === 'ROLE_DOCTOR')) {

          localStorage.setItem('aToken', data.token)
          setToken(data.token)
          setRole(data.role)
          toast.info("Login Successfull")

        } else if (state === 'Admin' && data.role === 'ROLE_DOCTOR') {
          toast.warn('Please login with admin credentials')
        } else if (state === 'Doctor' && data.role === 'ROLE_ADMIN') {
          toast.warn('Please login with doctor credentials')
        } else {
          toast.error("You don't have access to this page")
        }
      } else {
        toast.error(data.message)
      }



    } catch (e) {
      console.log(e)
      toast.error(e.response.data.message)
    } finally {
      setLoading(false)
    }

  }

  return (
    <LoadWrapper loading={loading}>
      <form onSubmit={onSumbitHandler} className='min-h-[80vh] flex items-center'>
        <div className='flex flex-col gap-3 m-auto items-start p-8 min-h-[340px] sm:min-w-96 border border-gray-300 rounded-xl text-[#5E5E5E] text-sm shadow-lg'>
          <p className='text-2xl font-semibold m-auto'><span className='text-primary'> {state}</span> Login</p>
          <div className='w-full'>
            <p>Username</p>
            <input onChange={(e) => setUsername(e.target.value)} value={username} className='border border-[#DADADA] rounded w-full p-2 mt-1' type="text" required />
          </div>

          <div className='w-full'>
            <p>Password</p>
            <input onChange={(e) => setPassword(e.target.value)} value={password} className='border border-[#DADADA] rounded w-full p-2 mt-1' type="password" required />
          </div>

          <button className='bg-primary text-white w-full py-2 rounded-md text-base'>Login</button>

          {
            state === 'Admin'
              ? <p>Doctor Login? <span className='text-primary underline cursor-pointer' onClick={() => setState('Doctor')}>Click here</span></p>
              : <p>Admin Login? <span className='text-primary underline cursor-pointer' onClick={() => setState('Admin')}>Click here</span></p>
          }

        </div>
      </form>
    </LoadWrapper>
  )
}

export default Login
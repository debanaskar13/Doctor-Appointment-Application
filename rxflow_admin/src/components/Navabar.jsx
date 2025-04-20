import React, { useContext } from 'react'
import { assets } from '../assets/assets'
import { AdminContext } from '../context/AdminContext'
import { useNavigate } from 'react-router-dom'

const Navabr = () => {

    const { token, setToken, role } = useContext(AdminContext)
    const navigate = useNavigate()

    const Logout = () => {

        token && setToken('')
        token && localStorage.removeItem('aToken')
        navigate('/')

    }

    return (
        <div className='flex justify-between items-center px-4 sm:px-10 py-3 border-b border-gray-300 bg-white'>

            <div className='flex items-center gap-2 text-xs '>
                <img className='w-28 sm:w-32 cursor-pointer' src={assets.admin_logo_rxflow} alt="" />
                <p className='border px-2.5 py-0.5 rounded-full border-gray-400 text-gray-600'>{role === 'ROLE_ADMIN' ? 'Admin' : 'Doctor'}</p>
            </div>
            <button onClick={() => Logout()} className='bg-primary text-white text-sm px-10 py-2 rounded-full'>Logout</button>

        </div>
    )
}

export default Navabr
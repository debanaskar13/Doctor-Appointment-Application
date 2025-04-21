/* eslint-disable no-unused-vars */
import React, { useContext, useState } from 'react'
import { AppContext } from '../context/AppContext'
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import Loading from '../components/Loading'
import { assets } from '../assets/assets'
import { toast } from 'react-toastify'
import ApiService from '../api/Axios'

const MyProfile = () => {

  const { profile, setProfile, token, loadUserProfile } = useContext(AppContext)

  const [isEdit, setIsEdit] = useState(false)
  const [image, setImage] = useState(false)
  const [isLoading, setIsLoading] = useState(false)

  const navigate = useNavigate()

  const updateUserProfileData = async () => {

    try {
      setIsLoading(true)

      const formData = new FormData()
      image && formData.append('image', image)
      formData.append('profileData', JSON.stringify({
        name: profile.name,
        phone: profile.phone,
        address: {
          line1: profile.address.line1,
          line2: profile.address.line2
        },
        gender: profile.gender,
        dob: profile.dob
      }))

      const { data } = await ApiService.updateProfileData(formData)

      if (data.success) {
        toast.success(data.message)
        await loadUserProfile()
        setIsEdit(false)
        setImage(false)
      } else {
        toast.error(data.message)
      }

    } catch (error) {
      console.log(error)
      if (error.response) {
        toast.error(error.response.data.message)
      } else {
        toast.error('Server Down')
      }
    }

    setIsLoading(false)

  }

  useEffect(() => {
    if (!token) {
      navigate('/login')
    }
  }, [token])


  return isLoading
    ? <Loading />
    : (profile && (
      <div className='max-w-lg flex flex-col gap-2 text-sm'>

        {
          isEdit
            ? <label htmlFor='image'>
              <div className='inline-block relative cursor-pointer'>
                <img className='w-36 rounded opacity-75 bg-primary' src={image ? URL.createObjectURL(image) : profile.image} alt="" />
                <img className='w-10 absolute bottom-12 right-12 ' src={image ? '' : assets.upload_icon} alt="" />
              </div>
              <input onChange={(e) => setImage(e.target.files[0])} type="file" id="image" hidden />
            </label>
            : <img className='w-36 rounded bg-primary' src={profile.image} alt="" />
        }

        {
          isEdit
            ? <input className='bg-gray-50 text-3xl font-medium max-w-60 mt-4' type="text" value={profile.name} onChange={(e) => setProfile(prev => ({ ...prev, name: e.target.value }))} />
            : <p className='font-medium text-3xl text-neutral-800 mt-4'>{profile.name}</p>
        }

        <hr className='bg-zinc-400 h-[1px] border-none' />
        <div>
          <p className='text-neutral-500 underline mt-3'>CONTACT INFORMATION</p>
          <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700'>
            <p className='font-medium'>Username: </p>
            <p className='text-blue-500'>{profile.username}</p>
            <p className='font-medium'>Phone :</p>
            {
              isEdit
                ? <input className='bg-gray-100 max-w-52' type="text" value={profile.phone} onChange={(e) => setProfile(prev => ({ ...prev, phone: e.target.value }))} />
                : <p className='text-blue-400'>{profile.phone}</p>
            }
            <p className='font-medium'>Address :</p>
            {
              isEdit
                ? <p>
                  <input className='bg-gray-50' value={profile.address.line1} onChange={e => setProfile(prev => ({ ...prev, address: { ...prev.address, line1: e.target.value } }))} type="text" />
                  <br />
                  <input className='bg-gray-50' value={profile.address.line2} onChange={e => setProfile(prev => ({ ...prev, address: { ...prev.address, line2: e.target.value } }))} type="text" />
                </p>
                : <p className='text-gray-500'>
                  {profile.address.line1}
                  <br />
                  {profile.address.line2}
                </p>
            }
          </div>
        </div>

        <div>
          <p className='text-neutral-500 underline mt-3'>BASIC INFORMATION</p>
          <div className='grid grid-cols-[1fr_3fr] gap-y-2.5 mt-3 text-neutral-700'>
            <p className='font-medium'>Gender :</p>
            {
              isEdit
                ? <select className='max-w-20 bg-gray-100' value={profile.gender} onChange={e => setProfile(prev => ({ ...prev, gender: e.target.value }))}>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                </select>
                : <p className='text-gray-400'>{profile.gender}</p>
            }
            <p className='font-medium'>Birthday :</p>
            {
              isEdit
                ? <input className='max-w-28 bg-gray-100' type="date" value={profile.dob} onChange={e => setProfile(prev => ({ ...prev, dob: e.target.value }))} />
                : <p className='text-gray-400'>{profile.dob}</p>
            }
          </div>
        </div>

        <div className='mt-10'>
          {
            isEdit
              ? <button className='border border-primary px-8 py-2 rounded-full cursor-pointer hover:bg-[#5F6FFF] hover:text-white transition-all' onClick={updateUserProfileData}>Save Information</button>
              : <button className='border border-primary px-8 py-2 rounded-full cursor-pointer hover:bg-[#5F6FFF] hover:text-white transition-all' onClick={() => setIsEdit(true)}>Edit</button>
          }
        </div>

      </div>
    ))
}

export default MyProfile
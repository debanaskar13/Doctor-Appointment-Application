import React, { useContext, useState } from 'react'
import { assets } from '../../assets/assets'
import { AdminContext } from '../../context/AdminContext'
import { toast } from 'react-toastify'
import ApiService from '../../api/Axios'
import Loading from '../../components/Loading'

const AddDoctor = () => {

    const [loading, setLoading] = useState(false)

    const [docImage, setDocImage] = useState(false)
    const [name, setName] = useState('')
    const [email, setEmail] = useState('')
    const [username, setUsername] = useState('')
    const [password, setPassword] = useState('')
    const [experience, setExperience] = useState('1 Year')
    const [fees, setFees] = useState('')
    const [about, setAbout] = useState('')
    const [speciality, setSpeciality] = useState('General physician')
    const [degree, setDegree] = useState('')
    const [address1, setAddress1] = useState('')
    const [address2, setAddress2] = useState('')


    const onSubmitHandler = async (event) => {

        event.preventDefault()

        setLoading(true)

        try {

            if (!docImage) {
                return toast.error('Image not selected')
            }

            const formData = new FormData()

            formData.append('image', docImage)
            formData.append('doctorData', JSON.stringify({
                user: {
                    username,
                    password,
                    name,
                    email,
                    address: {
                        line1: address1,
                        line2: address2
                    }
                },
                speciality,
                degree,
                experience,
                about,
                fees: Number(fees)
            }))

            const { data } = await ApiService.addDoctor(formData)

            if (data.success) {
                toast.success(data.message)
                setDocImage(false)
                setName('')
                setPassword('')
                setAddress1('')
                setAddress2('')
                setDegree('')
                setEmail('')
                setAbout('')
                setFees('')
                setUsername('')
            } else {
                toast.error(data.message)
            }

        } catch (e) {
            toast.error(e.response.data.message)
        } finally {
            setLoading(false)
        }

    }


    return (
        <>
            <form onSubmit={onSubmitHandler} className='m-5 w-full'>

                <p className='mb-3 text-lg font-medium'>Add Doctor</p>
                <div className='bg-white px-8 py-8 border border-gray-300 rounded max-w-4xl maxh-[80vh] overflow-y-scroll'>
                    <div className='flex items-center gap-4 mb-8 text-gray-500'>
                        <label htmlFor='doc-img'>
                            <img className='w-16 bg-gray-100 rounded-full cursor-pointer' src={docImage ? URL.createObjectURL(docImage) : assets.upload_area} alt='' />
                        </label>
                        <input onChange={(e) => setDocImage(e.target.files[0])} type="file" id='doc-img' hidden />
                        <p>Upload doctor <br /> picture</p>
                    </div>

                    <div className='flex flex-col lg:flex-row items-start gap-10 text-gray-600'>
                        <div className='w-full lg:flex-1 flex flex-col gap-4'>
                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Doctor Name</p>
                                <input onChange={(e) => setName(e.target.value)} value={name} className='border rounded px-3 py-2 border-gray-300' type="text" placeholder='Name' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Doctor Username</p>
                                <input onChange={(e) => setUsername(e.target.value)} value={username} className='border rounded px-3 py-2 border-gray-300' type="text" placeholder='Name' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Doctor Email</p>
                                <input onChange={(e) => setEmail(e.target.value)} value={email} className='border rounded px-3 py-2 border-gray-300' type="email" placeholder='Email' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Doctor Password</p>
                                <input onChange={(e) => setPassword(e.target.value)} value={password} className='border rounded px-3 py-2 border-gray-300' type="password" placeholder='Password' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Experience</p>
                                <select onChange={(e) => setExperience(e.target.value)} value={experience} className='border rounded px-3 py-2 border-gray-300' name="" >
                                    <option value="1 Year">1 Year</option>
                                    <option value="2 Year">2 Year</option>
                                    <option value="3 Year">3 Year</option>
                                    <option value="4 Year">4 Year</option>
                                    <option value="5 Year">5 Year</option>
                                    <option value="6 Year">6 Year</option>
                                    <option value="7 Year">7 Year</option>
                                    <option value="8 Year">8 Year</option>
                                    <option value="9 Year">9 Year</option>
                                    <option value="10 Year">10 Year</option>
                                    <option value="11 Year">11 Year</option>
                                    <option value="12 Year">12 Year</option>
                                    <option value="13 Year">13 Year</option>
                                    <option value="14 Year">14 Year</option>
                                    <option value="15 Year">15 Year</option>
                                </select>
                            </div>

                        </div>
                        <div className='w-full lg:flex-1 flex flex-col gap-4'>
                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Fees</p>
                                <input onChange={(e) => setFees(e.target.value)} value={fees} className='border rounded px-3 py-2 border-gray-300' type="number" placeholder='Fees' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Speciality</p>
                                <select onChange={(e) => setSpeciality(e.target.value)} value={speciality} className='border rounded px-3 py-2 border-gray-300' name="" >
                                    <option value="General physician">General physician</option>
                                    <option value="Gynecologist">Gynecologist</option>
                                    <option value="Dermatologist">Dermatologist</option>
                                    <option value="Pediatricians">Pediatricians</option>
                                    <option value="Neurologist">Neurologist</option>
                                    <option value="Gastroenterologist">Gastroenterologist</option>
                                </select>
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Education</p>
                                <input onChange={(e) => setDegree(e.target.value)} value={degree} className='border rounded px-3 py-2 border-gray-300' type="text" placeholder='Education' required />
                            </div>

                            <div className='flex-1 flex flex-col gap-1'>
                                <p>Address</p>
                                <input onChange={(e) => setAddress1(e.target.value)} value={address1} className='border rounded px-3 py-2 border-gray-300' type="text" placeholder='Address 1' />
                                <input onChange={(e) => setAddress2(e.target.value)} value={address2} className='border rounded px-3 py-2 border-gray-300' type="text" placeholder='Address 2' />
                            </div>


                        </div>
                    </div>

                    <div >
                        <p className='mt-4 mb-2 text-gray-600'>About Doctor</p>
                        <textarea onChange={(e) => setAbout(e.target.value)} value={about} className='w-full px-4 pt-2 border border-gray-300 rounded ' type="text" placeholder='Write about doctor' rows={5} required />
                    </div>

                    <button type='submit' className='bg-primary rounded-full px-10 py-3 mt-4 text-white'>Add Doctor</button>

                </div>
            </form>

            {loading && (
                <Loading />
            )}
        </>
    )
}

export default AddDoctor
/* eslint-disable no-unused-vars */
import React, { useState } from 'react'

const Login = () => {

  const [state, setState] = useState("Sign Up")

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [name, setName] = useState("")

  const onSubmitHandler = async (event) => {
    event.preventDefault()


  }


  return (
    <form className='min-h-[80vh] flex items-center' onSubmit={onSubmitHandler}>
      <div className='flex flex-col m-auto gap-3 items-center p-8 min-w-[340px] sm:min-w-96 border border-gray-300 rounded-xl text-zinc-600 text-sm shadow-lg'>
        <p className='text-2xl font-semibold'>{state === 'Sign Up' ? 'Create Account' : 'Login'}</p>
        <p>Please {state === 'Sign Up' ? 'sign up' : 'log in'} to book appointment</p>
        {
          state === 'Sign Up'
            ? <div className='w-full'>
              <p>Full Name</p>
              <input className='border border-zinc-300 rounded-xl w-full p-2 mt-1' type="text" value={name} onChange={(e) => setName(e.target.value)} required />
            </div>
            : ''
        }
        <div className='w-full'>
          <p>Email</p>
          <input className='border border-zinc-300 rounded-xl w-full p-2 mt-1' type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </div>
        <div className='w-full'>
          <p>Password</p>
          <input className='border border-zinc-300 rounded-xl w-full p-2 mt-1' type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        </div>

        <button className='bg-primary text-white w-full py-2 rounded-md text-base'>{state === 'Sign Up' ? 'Create Account' : 'Login'}</button>

        {
          state === 'Sign Up'
            ? <p className=''>Already have an account? <span className='text-[#5F6FFF] underline cursor-pointer' onClick={() => setState('Login')}>Login here</span></p>
            : <p>Create a new account? <span className='text-[#5F6FFF] underline cursor-pointer' onClick={() => setState('Sign Up')}>Click here</span></p>
        }

      </div>

    </form>
  )
}

export default Login
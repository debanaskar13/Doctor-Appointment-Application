
/* eslint-disable no-unused-vars */
import React from 'react'
import { assets } from '../assets/assets'

const About = () => {
  return (
    <div>

      <div className='text-center text-2xl pt-10 text-gray-500'>
        <p>ABOUT <span className='text-gray-700 font-medium'>US</span></p>
      </div>

      <div className='my-10 flex flex-col md:flex-row gap-12'>
        <img className='w-full md:max-w-[360px]' src={assets.about_image} alt="" />
        <div className='flex flex-col justify-center gap-6 md:w-2/4 text-sm text-gray-600'>
          <p>Welcome To RxFlow . Your Trusted Partner in Managing your Healthcare Needs Conveniently and Efficiently. At RxFlow , We Understand the Challenges Individual Face When It Comes To Scheduling Doctor Appointment And Managing Their Health Records</p>
          <p>RxFlow is Commited to Execute in Healthcare Technology. We Continuously Strive To Enhance Our Platform . Integrating The Latest Advancements To Improve User Experience and Deliver Superior Service. Whether You&apos;re Booking Your First Appintment Or Managing Ongoing Care. RxFlow is here To Support You Evry Step Of The Way.    </p>
          <b className='text-gray-800'>Our Vision</b>
          <p>Our Vision At RxFlow is To Create a Seamless Healthcare Experience For  Every User. We Aim To Bridge The Gap Between Patients And Helthcare Providers, Making It Easier For You, To Access The Care You Need When You Need It.   </p>
        </div>
      </div>

      <div className='text-xl my-4'>
        <p>WHY <span className='text-gray-700 font-semibold'>CHOOSE US</span></p>
      </div>

      <div className='flex flex-col md:flex-row mb-20'>
        <div className='border border-gray-300 px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-[#5F6FFF] hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>EFFICIENCY</b>
          <p>Streamlined appointment scheduling thats fits into your busy lifestyle</p>
        </div>


        <div className='border border-gray-300 px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-[#5F6FFF] hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>CONVENIENCE</b>
          <p>Access to a network of trusted helthcare professionals in your area.</p>
        </div>


        <div className='border border-gray-300 px-10 md:px-16 py-8 sm:py-16 flex flex-col gap-5 text-[15px] hover:bg-[#5F6FFF] hover:text-white transition-all duration-300 text-gray-600 cursor-pointer'>
          <b>PERSONALIZATION</b>
          <p>Tailored recommendations and reminders to help you stay on top of your health.</p>
        </div>
      </div>

    </div>
  )
}

export default About
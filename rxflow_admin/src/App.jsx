import { useContext } from 'react'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/ReactToastify.css'
import './App.css'
import { AdminContext } from './context/AdminContext'
import Login from './pages/Login'
import Navabar from './components/Navabar'
import Sidebar from './components/Sidebar'
import { Route, Routes } from 'react-router-dom'
import Dashborad from './pages/Admin/Dashborad'
import AllApointment from './pages/Admin/AllApointment'
import AddDoctor from './pages/Admin/AddDoctor'
import DoctorList from './pages/Admin/DoctorList'
import UserList from './pages/Admin/UserList'

function App() {

  const { token, role } = useContext(AdminContext)

  return token ? (
    <>
      <div className='bg-[#F8F9FD]'>
        <ToastContainer />
        <Navabar />
        <div className='flex items-start'>
          <Sidebar />
          <Routes>
            <Route path='' element={<></>} />
            <Route path='/admin-dashboard' element={<Dashborad />} />
            <Route path='/all-appointments' element={<AllApointment />} />
            <Route path='/add-doctor' element={<AddDoctor />} />
            <Route path='/doctor-list' element={<DoctorList />} />
            <Route path='/user-list' element={<UserList />} />
          </Routes>
        </div>
      </div >

    </>
  ) : (
    <>
      <Login />
      <ToastContainer />
    </>
  )
}

export default App

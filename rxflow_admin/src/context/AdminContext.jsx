import { createContext, useEffect, useState } from "react";
import { jwtDecode } from 'jwt-decode';
import { toast } from "react-toastify";
import ApiService from '../api/Axios'


export const AdminContext = createContext()


const AdminContextProvider = (props) => {
    const [token, setToken] = useState(localStorage.getItem('aToken'))
    const [role, setRole] = useState('')
    const [doctors, setDoctors] = useState([])

    const getAllDoctors = async () => {

        try {

            const { data } = await ApiService.fetchAllDoctors()

            if (data.success) {

                setDoctors(data.doctors)
                // console.log(data.doctors)

            } else {
                toast.error(data.message)
            }

        } catch (error) {
            toast.error(error.response.data.message)
        }
    }

    const changeAvailability = async (doctorId) => {
        try {
            const { data } = await ApiService.changeAvailability(doctorId)

            if (data.success) {
                toast.success('Availability changed')
                getAllDoctors()
            } else {
                toast.error(data.message)
            }

        } catch (error) {
            toast.error(error.response.data.message)
        }
    }

    const value = {
        token, setToken, role, setRole, doctors, getAllDoctors, changeAvailability
    }


    useEffect(() => {
        if (token) {
            try {
                const decoded = jwtDecode(token)

                if (decoded.exp * 1000 > Date.now()) {
                    setRole(decoded.role)
                } else {
                    toast.error('Token expired. Please login again')
                    localStorage.removeItem('aToken')

                }
            } catch (error) {
                toast.error('Invalid token')
                localStorage.removeItem('aToken')
            }
        }
    }, [])

    return (
        <AdminContext.Provider value={value}>
            {props.children}
        </AdminContext.Provider>
    )
}

export default AdminContextProvider
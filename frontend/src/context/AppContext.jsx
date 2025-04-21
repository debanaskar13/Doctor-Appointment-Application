/* eslint-disable react-refresh/only-export-components */
/* eslint-disable react/prop-types */
import { useState } from "react";
import { createContext, useEffect } from "react";
import ApiService from "../api/Axios";
// import { doctors } from "../assets/assets";
import { toast } from 'react-toastify'
import { jwtDecode } from 'jwt-decode'


export const AppContext = createContext()

const AppContextProvider = (props) => {
    const currencySymbol = '$'

    const [doctors, setDoctors] = useState([])
    const [token, setToken] = useState(localStorage.getItem('token') ? localStorage.getItem('token') : false)
    const [profile, setProfile] = useState(false)

    const getAllDoctors = async () => {
        try {

            const { data } = await ApiService.getAllDoctors()

            if (data.success) {
                // console.log(data.doctors)
                setDoctors(data.doctors)
            } else {
                toast.error('something went wrong')
            }

        } catch (error) {
            toast.error(error.response.data.message)
        }

    }

    const loadUserProfile = async () => {

        try {
            const { data } = await ApiService.myUserProfile()

            if (data.success) {
                // console.log(data.userProfile)
                setProfile(data.userProfile)
            } else {
                toast.error('something went wrong')
            }

        } catch (error) {
            toast.error(error.response.data.message)
        }
    }

    const value = {
        doctors, currencySymbol, token, setToken, profile, setProfile, loadUserProfile
    }

    useEffect(() => {
        getAllDoctors()
    }, [])

    useEffect(() => {
        if (token != false) {
            try {
                const decoded = jwtDecode(token)
                // console.log(decoded)

                if (decoded.exp * 1000 > Date.now()) {
                    loadUserProfile()
                } else {
                    localStorage.removeItem('token')
                    toast.error('jwt token expired, please login again')
                }

            } catch (error) {
                toast.error(error)
            }
        } else {
            setProfile(false)
        }
    }, [token])

    return (
        <AppContext.Provider value={value}>
            {props.children}
        </AppContext.Provider>
    )
}

export default AppContextProvider
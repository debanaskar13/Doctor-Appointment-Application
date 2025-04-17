/* eslint-disable react/prop-types */
import { createContext, useEffect } from "react";
import { doctors } from "../assets/assets";


export const AppContext = createContext()

const AppContextProvider = (props) => {

    const currencySymbol = '$'

    const value = {
        doctors, currencySymbol
    }

    useEffect(() => {}, [])

    return (
        <AppContext.Provider value={value}>
            {
                props.children
            }
        </AppContext.Provider>
    )
}

export default AppContextProvider
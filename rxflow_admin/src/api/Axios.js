import axios from 'axios'

export const api = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_API_URL
});


class ApiService {

    static setAuthToken(token) {
        if (token) {
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        } else {
            delete api.defaults.headers.common['Authorization']
        }
    }

    static async login(credentials) {
        return await api.post('/login', credentials)
    }

    static async addDoctor(formData) {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.post('/doctors', formData)
    }

    static async fetchAllDoctors() {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.get('/doctors')
    }

    static async changeAvailability(doctorId) {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.post('/doctors/change-availability', {
            doctorId: doctorId
        })
    }


}

export default ApiService
import axios from "axios";


const api = axios.create({
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

    static async register(requestBody) {
        return await api.post('/register', requestBody)
    }

    static async login(requestBody) {
        return await api.post('/login', requestBody)
    }

    static async getAllDoctors() {
        return await api.get('/doctors/list')
    }

    static async myUserProfile() {
        this.setAuthToken(localStorage.getItem('token'))
        return await api.get(`/user/profile`)
    }

    static async getDoctor(doctorId) {
        return await api.get(`/doctors/${doctorId}`)
    }

    static async updateProfileData(formData) {
        this.setAuthToken(localStorage.getItem('token'))
        return await api.put('/user/profile/update', formData)
    }

    static async bookAppointment(data) {
        this.setAuthToken(localStorage.getItem('token'))
        return await api.post('/user/book-appointment', data)
    }

    static async fetchMyAppointments() {
        this.setAuthToken(localStorage.getItem('token'))
        return await api.get('/user/my-appointments')
    }

}

export default ApiService;
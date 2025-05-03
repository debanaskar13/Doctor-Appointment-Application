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
        return await api.post('/admin/doctors', formData)
    }

    static async fetchAllDoctors() {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.get('/admin/doctors')
    }

    static async changeAvailability(doctorId) {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.post('/admin/doctors/change-availability', {
            doctorId: doctorId
        })
    }

    static async fetchAllUsers(page, limit, sortField, sortDirection) {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.get('/admin/users?page=' + page + '&limit=' + limit + '&sortBy=' + sortField + '&direction=' + sortDirection)
    }

    static async updateUser(userId, payload) {
        this.setAuthToken(localStorage.getItem('aToken'))
        return await api.put('/admin/users/' + userId, payload)
    }


}

export default ApiService
import axios from "axios";


const api = axios.create({
    baseURL: import.meta.env.VITE_BACKEND_API_URL
});

class ApiService{

    static async register(requestBody){
        return await api.post('/register', requestBody)
    }

    static async login(requestBody){
        return await api.post('/login', requestBody)
    }

    static setAuthToken(token){
        if(token){
            api.defaults.headers.common['Authorization'] = `Bearer ${token}`
        }else{
            delete api.defaults.headers.common['Authorization']
        }
    }

    static async myUserProfile(userId){
        return await api.get(`/users/${userId}`)
    }

    static async getDoctor(doctorId){
        return await api.get(`/doctors/${doctorId}`)
    }

}

export default ApiService;
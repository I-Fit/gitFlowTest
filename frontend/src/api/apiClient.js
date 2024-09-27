import axios from 'axios';
import VueCookies from 'vue-cookies';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
});

const Cookies = VueCookies;

apiClient.interceptors.request.use(
    (config) => {
        const accessToken = Cookies.get('accessToken');
        if (accessToken) {
            config.headers['Authorization'] = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default apiClient;
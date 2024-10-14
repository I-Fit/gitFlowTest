import axios from 'axios';
import VueCookies from 'vue-cookies';

const apiClient = axios.create({
    baseURL: 'http://ec2-54-175-24-210.compute-1.amazonaws.com:8080/api',
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

apiClient.interceptors.response.use(
    response => response,
    async (error) => {
        const originalRequest = error.config;

        // 401 에러가 발생한 경우
        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;  // 무한 루프 방지?

            const refreshToken = Cookies.get('refreshToken');
            if (refreshToken) {
                try {
                    const response = await axios.post('/api/refresh-token', null, {
                        headers: {
                            'refresh-token': `Bearer ${refreshToken}`,
                        },
                    });

                    const newAccessToken = response.data.accessToken;       // 서버에서 새로 받은 accessToken;
                    Cookies.set('accessToken', newAccessToken, { expires: 1 / 24 });

                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;

                    return apiClient(originalRequest);
                } catch (refreshError) {
                    // refreshToken이 만료되었거나 오류가 발생한 경우 처리
                    console.error('Refresh token error', refreshError);

                    alert.error("인증이 만료되었습니다. 다시 로그인 해주세요.");

                    Cookies.remove('accessToken');
                    Cookies.remove('refreshToken');

                    window.location.href = '/sign-in';

                    return Promise.reject(refreshError);
                }
            }
        }
        return Promise.reject(error);
    }
);

export default apiClient;
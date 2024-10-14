import axios from "axios";

export const registerGroupOnServer = async (formData) => {
    try {
        const response = await axios.post('', formData);    // 서버 api 주소
        return response.data;   // 서버 응답 데이터
    } catch (error) {
        console.error('Failed to register group: ', error);
        throw error;
    }
}
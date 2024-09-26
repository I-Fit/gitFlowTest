import axios from "axios";
import VueCookies from 'vue-cookies';

export default {
    namespaced: true,
    state: {
        Loggedin: false,
        userId: null,   // 사용자 식별 Id
        accessToken: null,    // JWT 토큰(accessToken)
        refreshToken: null,   // JWT 토큰(refreshToken)
    },

    mutations: {
        SET_LOGGEDIN(state, payload) {
            state.Loggedin = payload;
        },
        SET_USERID(state, payload) {
            state.userId = payload;
        },
        SET_ACCESSTOKEN(state, payload) {
            state.accessToken = payload;
        },
        SET_REFRESHTOKEN(state, payload) {
            state.refreshToken = payload;
        },
        LOGOUT(state) {
            state.Loggedin = false;
            state.userId = null;
            state.accessToken = null;
            state.refreshToken = null;
        },
    },

    actions: {
        async signin({ commit }, userSignInData) {
            const Cookies = VueCookies;
            try {
                const response = await axios.post('http://localhost:8080/api/login', {      /* sign-in */
                    id: userSignInData.id,
                    password: userSignInData.password,
                });

                if (response.data.success) {
                    commit('SET_LOGGEDIN', true);
                    commit('SET_USERID', response.data.userId);
                    commit('SET_ACCESSTOKEN', response.data.accessToken);
                    commit('SET_REFRESHTOKEN', response.data.refreshToken);

                    //  쿠키에 토큰 저장 (7일?)
                    Cookies.set('accessToken', response.data.accessToken, { expires : 1/24 });
                    Cookies.set('refreshToken', response.data.refreshToken, { expires : 7 });

                } else {
                    commit('SET_LOGGEDIN', false);
                    commit('SET_USERID', null);
                    commit('SET_ACCESSTOKEN', null);
                    commit('SET_REFRESHTOKEN', null);
                    throw new Error(response.data.message || '로그인 실패');
                }

            } catch (error) {
                console.error('로그인 요청 중 오류 발생', error);
                commit('SET_ERROR', error.message || '로그인 Id, 비밀번호 중 하나 오류');
            }
        },
        async logout({ commit }) {
            const Cookies = VueCookies;
            try {
                await axios.post('/api/logout');

                commit('LOGOUT');
                Cookies.remove('accessToken')  //  쿠키에서 엑세스 토큰 삭제
                Cookies.remove('refreshToken') //  쿠키에서 리프레시 토큰 삭제
            } catch (error) {
                console.error('로그아웃 요청 중 오류 발생', error);
            }
        },
    },

    getters: {
        loggedIn: (state) => state.Loggedin,
        userId: (state) => state.userId,
        accessToken: (state) => state.accessToken,
        refreshToken: (state) => state.refreshToken,
    },
};
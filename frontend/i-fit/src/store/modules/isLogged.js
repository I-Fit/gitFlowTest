import axios from "axios";

export default {
    state: {
        Loggedin: false,
        userId: null,   // 사용자 식별 Id
    },

    mutations: {
        SET_LOGGEDIN(state, payload) {
            state.Loggedin = payload;
        },
        SET_USERID(state, payload) {
            state.userId = payload;
        },
        LOGOUT(state) {
            state.Loggedin = false;
            state.userId = null;
        },
    },

    actions: {
        async login({ commit }, userLoginData) {
            try {
                const response = await axios.post('/api/login', {
                    id: userLoginData.id,
                    password: userLoginData.password,
                });

                if (response.data.success) {
                    commit('SET_LOGGEDIN', true);
                    commit('SET_USERId', response.data.userId);
                } else {
                    commit('SET_LOGGEDIN', false);
                    commit('SET_USERId', null);
                    throw new Error(response.data.message || '로그인 실패');
                }
            } catch (error) {
                console.error('로그인 요청 중 오류 발생', error);
            }
        },
        async logout({ commit }) {
            try {
                await axios.post('/api/logout');

                commit('LOGOUT');
            } catch (error) {
                console.error('로그아웃 요청 중 오류 발생', error);
            }
        },
    },

    getters: {
        LOGGEDIN: (state) => state.Loggedin,
        userId: (state) => state.userId,
    },
};
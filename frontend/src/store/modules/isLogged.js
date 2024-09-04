import axios from "axios";

export default {
    namespaced: true,
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
        async signin({ commit }, userSignInData) {
            try {
                const response = await axios.post('/api/signin', {      /* sign-in */
                    id: userSignInData.id,
                    password: userSignInData.password,
                });

                if (response.data.success) {
                    commit('SET_LOGGEDIN', true);
                    commit('SET_USERID', response.data.userId);
                } else {
                    commit('SET_LOGGEDIN', false);
                    commit('SET_USERID', null);
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
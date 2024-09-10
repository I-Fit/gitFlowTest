import axios from "axios";

export default {
    namespaced: true,
    state: {
        Loggedin: false,
        userId: null,   // 사용자 식별 Id
        token: null,    // JWT 토큰
    },

    mutations: {
        SET_LOGGEDIN(state, payload) {
            state.Loggedin = payload;
        },
        SET_USERID(state, payload) {
            state.userId = payload;
        },
        SET_TOKEN(state, payload) {
            state.token = payload;
        },
        LOGOUT(state) {
            state.Loggedin = false;
            state.userId = null;
            state.token = null;
        },
    },

    actions: {
        async signin({ commit }, userSignInData) {
            try {
                const response = await axios.post('http://localhost:8080/api/login', {      /* sign-in */
                    id: userSignInData.id,
                    password: userSignInData.password,
                });

                if (response.data.success) {
                    commit('SET_LOGGEDIN', true);
                    commit('SET_USERID', response.data.userId);
                    commit('SET_TOKEN', response.data.token);

                    //  쿠키에 토큰 저장 (7일?)
                    this.$cookies.set('token', response.data.token, { expires : 7 });

                } else {
                    commit('SET_LOGGEDIN', false);
                    commit('SET_USERID', null);
                    commit('SET_TOKEN', null);
                    throw new Error(response.data.message || '로그인 실패');
                }

            } catch (error) {
                console.error('로그인 요청 중 오류 발생', error);
                commit('SET_ERROR', error.message || 'Id와 비밀번호 오류');
            }
        },
        async logout({ commit }) {
            try {
                await axios.post('/api/logout');

                commit('LOGOUT');
                this.$cookies.remove('token')  //  쿠키에서 토큰 삭제
            } catch (error) {
                console.error('로그아웃 요청 중 오류 발생', error);
            }
        },
    },

    getters: {
        LOGGEDIN: (state) => state.Loggedin,
        userId: (state) => state.userId,
        token: (state) => state.token,
    },
};
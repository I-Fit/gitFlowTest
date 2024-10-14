import axios from "axios";
import VueCookies from 'vue-cookies';

export default {
    namespaced: true,
    state: {
        loggedIn: !!VueCookies.get('accessToken'),
        userId: null,   // 사용자 식별 Id
        accessToken: VueCookies.get('accessToken') || null,    // JWT 토큰(accessToken)
        refreshToken: VueCookies.get('refreshToken') || null,   // JWT 토큰(refreshToken)
        errorMessage: null,
        userName: null,
        userImage: null,
    },

    mutations: {
        SET_LOGGEDIN(state, payload) {
            state.loggedIn = payload;
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
        SET_USERNAME(state, payload) {
            state.userName = payload;
        },
        SET_USERIMAGE(state, payload) {
            state.userImage = payload;
        },
        LOGOUT(state) {
            state.loggedIn = false;
            state.userId = null;
            state.userName = null,
            state.userImage = null,
            state.accessToken = null;
            state.refreshToken = null;
        },
        SET_ERROR(state, payload) {
            state.errorMessage = payload;
            console.error(payload);
        },
    },

    actions: {
        async signin({ commit }, userSignInData) {
            const Cookies = VueCookies;
            try {
                const response = await axios.post('/api/login', {
                        loginId: userSignInData.id,
                        password: userSignInData.password,
                });

                if (response.data.message === '로그인 성공') {
                    commit('SET_LOGGEDIN', true);
                    commit('SET_ACCESSTOKEN', response.data.accessToken);
                    commit('SET_REFRESHTOKEN', response.data.refreshToken);
                    commit('SET_USERID', response.data.userId);
                    commit('SET_USERNAME', response.data.userName);
                    commit('SET_USERIMAGE', response.data.userProfile);
                    
                    // userImage가 null 일 경우 기본 이미지로 설정
                    const userImage = response.data.userProfile ? `data:image/png;base64,${response.data.userProfile}` : require('@/assets/images/default-profile.png');
                    commit('SET_USERIMAGE', userImage);

                    //  쿠키에 토큰 저장 (7일?)
                    Cookies.set('accessToken', response.data.accessToken, { expires : 1/24 });
                    Cookies.set('refreshToken', response.data.refreshToken, { expires : 7 });

                } else {
                    commit('SET_LOGGEDIN', false);
                    commit('LOGOUT');
                    commit('SET_ERROR', '로그인 실패');
                }

            } catch (error) {
                commit('SET_ERROR', error.message || '로그인 Id, 비밀번호 중 하나 오류');
            }
        },
        async logout({ commit }) {
            const Cookies = VueCookies;
            try {
                const refreshToken = Cookies.get('refreshToken');

                if (refreshToken) {
                    await axios.post('/api/logout', {}, {
                        headers: {
                            'refresh-token': `Bearer ${refreshToken}`,
                        },
                    });
                    
                    commit('LOGOUT');
                    Cookies.remove('accessToken')  //  쿠키에서 엑세스 토큰 삭제
                    Cookies.remove('refreshToken') //  쿠키에서 리프레시 토큰 삭제
                    return true;
                }
            } catch (error) {
                console.error('로그아웃 요청 중 오류 발생', error);
                return false;
            }
        },
        async restoreToken({ commit }) {
            const Cookies = VueCookies;
            const accessToken = Cookies.get("accessToken");
            const refreshToken = Cookies.get("refreshToken");

            if (accessToken) {
                commit('SET_ACCESSTOKEN', accessToken);
                commit('SET_REFRESHTOKEN', refreshToken);
                commit('SET_LOGGEDIN', true);

                try {
                    const response = await axios.get('/api/user-info', {
                        headers: {
                            Authorization: `Bearer ${accessToken}`,
                        },
                    });
                    if (response.data) {
                        commit('SET_USERID', response.data.userId);
                        commit('SET_USERNAME', response.data.userName);
                        commit('SET_USERIMAGE', response.data.userProfile);

                        const userImage = response.data.userProfile ? `data:image/png;base64,${response.data.userProfile}` : require('@/assets/images/default-profile.png');
                        commit('SET_USERIMAGE', userImage);
                    }
                } catch (error) {
                    console.error('사용자 정보 로드 중 오류 발생', error);
                }
            } else {
                commit('LOGOUT');
            }
        },
        // async deleteAccount({ commit }) {
        //     const Cookies = VueCookies;
        //     try {
        //         const refreshToken = Cookies.get('refreshToken');

        //         if (refreshToken) {

        //             await axios.delete("/delete-account", {}, {
        //                 headers: {
        //                     'refresh-token': `Bearer ${refreshToken}`,
        //                 },
        //             });
        //         }

        //         commit('LOGOUT');
        //         Cookies.remove('accessToken');
        //         Cookies.remove('refreshToken');
        //         return true;
        //     } catch (error) {
        //         console.error("회원 탈퇴 요청 중 오류 발생", error);
        //         return false;
        //     }
        // },
    },

    getters: {
        loggedIn: (state) => state.loggedIn,
        userId: (state) => state.userId,
        userName: (state) => state.userName,
        userImage: (state) => state.userImage,
        accessToken: (state) => state.accessToken,
        refreshToken: (state) => state.refreshToken,
        errorMessage: (state) => state.errorMessage,
    },
};
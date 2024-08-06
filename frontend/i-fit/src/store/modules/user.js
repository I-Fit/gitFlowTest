import axios from "axios";

export const state = () => ({
  formData: {
    contact: "",
    id: "",
    password: "",
    passwordCheck: "",
    name: "",
    phone: "",
    email: "",
  },
  idAvailabel: null, // 아이디 사용 가능 여부 저장 상태
});

export const mutations = {
  SET_ID_AVAILABILITY(state, available) {
    state.idAvailabel = available;
  },

  SET_FORM_DATA(state, payload) {
    state.formData = { ...state.formData, ...payload };
  },
};

export const actions = {
  // 서버에 아이디 중복 확인 요청
  async checkId({ commit }, id) {
    try {
      const response = await axios.get("/api/check-id", { params: { id } });
      commit(response.data.available); //서버 응답에 따라 아이디 사용가능 여부 업데이트
    } catch (error) {
      console.error(error);
      commit(false); // 요청 실패
    }
  },

  // 사용자 데이터 서버에 전송
  async submitUserData({ state }) {
    try {
      await axios.post("/api/user", state.formData);
    } catch (error) {
      console.log(error);
    }
  },

  updateFormData({ commit }, formData) {
    commit("SET_FORM_DATA", formData);
  },
};

export const getters = {
  // 비밀번호 일치/불일치
  passwordMatch: (state) => {
    return state.formData.password === state.formData.passwordCheck;
  },
  isIdAvailable: (state) => {
    return state.idAvailabel;
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};

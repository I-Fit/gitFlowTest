import axios from "axios";

const state = () => ({
  formData: {
    title: "",
    topboxContent: "",
    sport: "종목",
    location: "",
    person: "",
    selectedTime: "",
  },
  categories: ['러닝', '웨이트', '라이딩', '요가', '수영', '등산', '테니스', '클라이밍', '필라테스', 'GX'],
  
  additionalData: {
    communityId: "",
    user_img: "",
    username: "",
  },
});

const mutations = {
  SET_TITLE(state, title) {
    state.formData.title = title;
  },

  SET_TOPBOX_CONTENT(state, content) {
    state.formData.topboxContent = content;
  },

  SET_SPORT(state, sport) {
    state.formData.sport = sport;
  },

  SET_LOCATION(state, location) {
    state.formData.location = location;
  },

  SET_PERSON(state, person) {
    state.formData.person = person;
  },

  SET_SELECTED_TIME(state, selectedTime) {
    state.formData.selectedTime = selectedTime;
  },

  ADD_CATEGORY(state, category) {
    if (!state.categories.includes(category)) {
      state.categories.push(category);
    }
  },

  // 서버가 준 데이터 추가
  UPDATE_ADDITIONAL_DATA(state, additionalData) {
    state.additionalData = additionalData;
  },

};

const actions = {
  updateTitle({ commit }, title) {
    commit("SET_TITLE", title);
  },

  updateTopboxContent({ commit }, content) {
    commit("SET_TOPBOX_CONTENT", content);
  },

  updateSport({ commit }, sport) {
    commit("SET_SPORT", sport);
  },

  updateLocation({ commit }, location) {
    commit("SET_LOCATION", location);
  },

  updatePerson({ commit }, person) {
    commit("SET_PERSON", person);
  },

  updateSelectedTime({ commit }, selectedTime) {
    commit("SET_SELECTED_TIME", selectedTime);
  },

  addCategory({ commit }, category) {
    commit("ADD_CATEGORY", category);
  },
  // 서버에서 준 데이터 관련 코드
  updateAdditionalData({ commit }, additionalData) {
    commit("UPDATE_ADDITIONAL_DATA", additionalData);
  },

  async createGroup({ dispatch, state }) {
    try {
      const response = await axios.post('', state.formData);  // 모임 생성 요청
      const { communityId, user_img, username } = response.data;

      // 서버에서 받은 추가 데이터를 vuex 상태에 저장
      await dispatch('updateAdditionalData', { communityId, user_img, username });

      // 그룹 목록에 새 그룹 추가
      await dispatch('groupList/addGroup', {
        communityId,
        ...state.formData,
        user_img,
        username
      }, { root: true });

      return response.data;
    } catch (error) {
      console.error('Error', error);
      throw error;
    }
  }
};

const getters = {
  formData(state) {
    return state.formData;
  },
  categories(state) {
    return state.categories;
  },
  additionalData(state) {
    return state.additionalData;
  },

};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};

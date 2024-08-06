import axios from "axios";

const state = {
    groups: []
};

const mutations = {
    ADD_GROUP(state, group) {
        state.groups.push(group);
    }
};

const actions = {
    async axiosGroups({ commit }) {
        try {
            const response = await axios.get('');   // 서버에서 그룹 목록을 가져온다.
            const groups = response.data;
            groups.forEach(group => commit('ADD_GROUP', group));
        } catch (error) {
            console.error('Error', error);
        }
    },

    addGroup({ commit }, group) {
        commit('ADD_GROUP', group);
    }
};

const getters = {
    groups: state => state.groups
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
};
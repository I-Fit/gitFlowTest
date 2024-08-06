import axios from "axios";

const state = () => ({
    likedGroups: [],
    groupDetails: {},
});

const mutations = {
    addLikedGroup(state, communityId) {
        if (!state.likedGroups.includes(communityId)) {
            state.likedGroups.push(communityId);
        }
    },
    removeLikedGroup(state, communityId) {
        state.likedGroups = state.likedGroups.filter(id => id !== communityId);
    },
    setGroupDetails(state, groupData) {
        state.groupDetails = groupData;
    },
};

const actions = {
    async toggleLikeGroup({ commit, state }, communityId) {
        if (state.likedGroups.includes(communityId)) {
            commit('remove', communityId);
        } else {
            commit('add', communityId);
        }

        try {
            const response = await axios.get(`/api/groups/${communityId}`);
            commit('setGroupDetails', response.data);
        } catch (error) {
            console.error('Error', error);
        }
    }
};

const getters = {
    isGroupLiked: (state) => (communityId) => {
        return state.likedGroups.includes(communityId);
    },
    groupDetails: (state) => state.groupDetails
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}
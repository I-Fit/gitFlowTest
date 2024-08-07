import axios from "axios";

const state = () => ({
    likedGroups: [],    // 찜을 한 모임 Id 등록
    groupDetails: {},   // 모임 Id에 해당하는 상세 데이터
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
        // 이미 찜한 모임인지 확인
        const isLiked = state.likedGroups.includes(communityId);

        // 모임이 찜한 상태이면 제거하고, 아니면 추가
        if (state.likedGroups.includes(communityId)) {
            commit('removeLikedGroup', communityId);
        } else {
            commit('addLikedGroup', communityId);
        }

        // 현재 찜한 상태를 서버에 post 요청으로 보내어 서버의 데이터도 업데이트
        try {
            await axios.post(`/api/groups/${communityId}/like`, { liked: !isLiked});
        } catch (error) {
            console.error("Error", error);
        }

        // communityId에 대한 모임의 상세 정보를 서버에서 가져와서
        // setGroupDetails를 통해 상태를 업데이트
        try {
            const response = await axios.get(`/api/groups/${communityId}`);
            commit('setGroupDetails', response.data);
        } catch (error) {
            console.error('Error', error);
        }
    }
};

// 상태를 기반으로 데이터를 가져오는 함수
const getters = {
    // communityId가 likedGroups에 포함되어 있는지 확인
    isGroupLiked: (state) => (communityId) => {
        return state.likedGroups.includes(communityId);
    },
    // 상태 반환
    groupDetails: (state) => state.groupDetails
};

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}
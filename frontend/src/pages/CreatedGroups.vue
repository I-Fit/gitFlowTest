<template>
  <main>
    <AppNav />
    <div class="party">
      <div class="party-null-block"></div>
      <div class="party-top">
        <h2>모임 관리</h2>
        <p class="line text01">내가 만든 모임</p>
        <p class="line text02" @click="groupJoinlist">모임 참여 내역</p>
        <p class="text03" @click="likeGroup">찜한 모임 내역</p>
      </div>

      <div class="party-middle">
        <div class="middle-filter">

          <div class="middle-filter-search-box">
            <input type="text" name="search" id="search_input" placeholder="검색어를 입력하세요." class="search-box-input"
              v-model="searchTerm" @keyup.enter="searchGroups" />
            <img src="@/assets/images/search.icon.png" alt="search" class="search-box-icon" @click="searchGroups" />
          </div>

          <select title="정렬" class="middle-filter-sort" v-model="selectedOption" @change="selectedGroupList">
            <option value="" selected disabled>정렬</option>
            <option v-for="option in options" :key="option.value" :value="option.value">
              {{ option.text }}
            </option>
          </select>

        </div>

        <!-- 참여 한 모임 내역 -->
        <div class="group">

          <div v-for="group in groups" :key="group.communityId" class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal(group)" />

              <img
                :src="group.saved ? require('@/assets/images/saved-icon3.png') : require('@/assets/images/save-icon.png')"
                class="group-save-icon" @click="likeGroup(group.communityId)" />

              <img class="group-join-icon" src="@/assets/images/plus-icon2.png"
                @click="showConfirmPopup = true; currentGroupId = group.communityId" />

              <div v-if="showConfirmPopup && currentGroupId === group.communityId" class="confirm-popup">
                <div class="popup-content">
                  <p>모임을 삭제 하시겠습니까?</p>
                  <button class="confirm-btn" @click="confirmDeletion(group.communityId)">
                    확인
                  </button>
                  <button class="cancle-btn" @click="cancelDeletion(group)">
                    취소
                  </button>
                </div>
              </div>
            </div>

            <div class="sport-image-box">
              <img src="../assets/images/yoga-image.png" alt="요가 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">{{ group.sport }}</p>
                <div class="user-info">
                  <span class="user-name">{{ group.username }}</span>
                  <img :src="group.user_img || require('@/assets/images/default-profile.png')" alt="사용자 이미지"
                    class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="new-title"> {{ group.title }}</p>
                <p class="date-and-time">{{ group.date }}{{ group.time }} <span class="location">| {{ group.location
                    }}</span>
                </p>
                <p class="person">인원: {{ group.peopleParticipation }} / {{ group.person }}</p>
              </div>
            </div>
          </div>
        </div>
        <!-- <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="fetchGroups" /> -->
      </div>
    </div>
    <!-- 모달 창 -->
    <div class="modal" v-if="isModalOpen">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>모임 상세설명 : {{ selectedItem ? selectedItem.content : "" }}</p>
      </div>
    </div>
  </main>
</template>

<script>
import AppNav from "@/components/layout/AppNav.vue";
import { useRouter } from "vue-router";
// import Pagination from "@/components/common/Pagination.vue";
// import { usePagination } from "@/utils/pagination";
import { ref, onBeforeMount } from "vue";
// import axios from "axios";
import apiClient from "@/api/apiClient";

export default {
  name: "CreatedGroups",
  components: {
    AppNav,
    // Pagination,
  },

  data() {
    return {
      isModalOpen: false,
      selectedItem: null,
    };
  },
  methods: {
    openModal(group) {
      this.selectedItem = {
        content: group.topboxContent,
      };
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
      this.selectedItem = null;
    },
  },

  setup() {
    const router = useRouter();
    // const store = useStore();
    const searchTerm = ref('');   // 검색어를 저장할 변수
    const groups = ref([]);

    const groupJoinlist = () => {
      router.push({ name: "JoinedGroups" });
    };

    // 페이지네이션
    // const { currentPage, totalPages, visibleDatas, fetchdatas, onPageChange } = usePagination(groups, 6);

    // 사용자 식별 ID의 상태를 가져옴
    // const userId = computed(() => store.getters["isLogged/userId"]);

    //  웹 페이지가 로딩 되기 전에 userId를 서버에 보내서 해당되는 모임을 로딩해줌
    const loadgroups = async () => {
      try {
        const response = await apiClient.get('/created');
        groups.value = response.data;

        // fetchdatas(1);
      } catch (error) {
        console.error("Error", error);
      }
    };

    onBeforeMount(async () => {
      await loadgroups();
    });

    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };

    //  검색어
    const searchGroups = async () => {
      if (!searchTerm.value) {
        return;
      }
      try {
        const response = await apiClient.get("/created/search", {
          params: {
            searchTerm: searchTerm.value,
          }
        });
        groups.value = response.data;
      } catch (error) {
        console.error("오류가 발생", error);
      }
    };

    // 정렬
    const selectedOption = ref('');
    const options = ref([
      // { value: '', text: '정렬' },
      { value: '1', text: '인기순' },
      { value: '2', text: '최신순' },
      { value: '3', text: '오래된순' },
    ])

    const selectedGroupList = async () => {
      try {
        const response = await apiClient.get("/created/sort", {
          params: {
            value: selectedOption.value,
          }
        });
        // 서버에서 받은 모임 리스트
        groups.value = response.data;
      } catch (error) {
        console.error("오류가 발생", error);
      }
    };

    // 내가 만든 모임 삭제 후 다시 로드
    const showConfirmPopup = ref(false);
    const currentGroupId = ref(null);

    const confirmDeletion = async (communityId) => {
      const group = groups.value.find(group => group.communityId === communityId);

      if (!group) {
        console.error("유효하지 않는 모임 입니다.", communityId);
        return;
      }

      try {
        await apiClient.delete('/created/delete', {
          params: {
            communityId: communityId,
          },
        });
        alert("모임 삭제 완료")
      } catch (error) {
        console.error("Error", error);
      } finally {
        await loadgroups();
        showConfirmPopup.value = false;
        currentGroupId.value = null;
      }
    };
    //  참석 모달 닫기
    const cancelDeletion = () => {
      showConfirmPopup.value = false;
      currentGroupId.value = null;
    };

    // 찜
    const likeGroup = async (communityId) => {
      const group = groups.value.find(g => g.communityId === communityId);
      if (!group) return;   // 그룹을 찾지 못하면 종료

      try {
        const method = group.saved ? 'DELETE' : 'POST';
        const url = '/created/like';

        const response = await apiClient({
          method: method,
          url: url,
          data: {
            communityId: group.communityId,
            saved: !group.saved,
          },
        });

        if (response.status === 200) {
          group.saved = !group.saved;

        } else {
          console.error("찜 실패", response.data.message);
        }
      } catch (error) {
        console.error("Error", error);
      }
    }

    return {
      loadgroups,
      // currentPage,
      // totalPages,
      // visibleDatas,
      // fetchdatas,
      // onPageChange,
      showConfirmPopup,

      groups,
      groupJoinlist,
      likeGroup,
      toggleTooltip,
      confirmDeletion,
      cancelDeletion,

      options,
      selectedGroupList,
      selectedOption,

      searchTerm,
      searchGroups,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 1200px;
  /* height: 100%; */
  display: grid;
  grid-template-columns: 180px 1fr;
}

.party {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 60px 150px 1fr;
}

.party-null-block {
  width: 100%;
  height: 100%;
}

.party-top {
  position: relative;
  width: 100%;
  height: 100%;
}

.party-top::after {
  content: "";
  display: block;
  width: 100%;
  height: 2px;
  background-color: #ccc;
}

h2 {
  font-size: 50px;
  font-weight: bold;
  color: #5d5a88;
  margin: 0;
  margin-left: 1056px;
}

.line::after {
  content: "|";
  color: #ccc;
  margin: 0 5px 0 5px;
}

.text01,
.text02,
.text03 {
  font-size: 24px;
  font-weight: bolder;
  margin-top: 28px;
  margin-bottom: 20px;
  display: inline-block;
  vertical-align: middle;
  cursor: pointer;
}

.text02,
.text03 {
  font-weight: lighter;
}

.party-middle {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: 95px 1fr;
}

.middle-filter {
  display: flex;
  align-items: center;
  padding: 10px 5px;
}

.middle-filter-search-box {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 5px 0px 5px 5px;
  width: 250px;
  margin-left: 906px;
  position: relative;
}

.search-box-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
  width: 100%;
}

.search-box-icon {
  width: 15px;
  height: 15px;
  position: absolute;
  right: 10px;
  /* margin-left: 45px;
  text-align: end; */
  cursor: pointer;
}

.middle-filter-sort {
  width: 75px;
  height: 37px;
  font-size: 13.3333px;
  margin-left: 30px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff !important;
  text-align: center;
  font-weight: 400;
}

/* 참여 한 모임 내역 css */
.group {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.group-container {
  display: grid;
  grid-template-rows: 40px 250px 1fr;
  width: 280px;
  height: 450px;
  margin-right: 50px;
}

.group>.group-container:nth-of-type(4n) {
  margin-right: 0px;
}

.unicode-box {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 5px 3px 0px 4px;
}

/* 그룹 상세설명 버튼 */
.group-detail-icon {
  width: 25px;
  height: 25px;
  cursor: pointer;
}

.group-detail {
  font-size: 26px;
  cursor: pointer;
  margin-bottom: 3px;
  margin-right: 3px;
  color: grey;
  font-weight: 100;
  margin-top: 2px;
}

.sport-image-box {
  width: 100%;
  height: 250px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.sport-image {
  width: 270px;
  height: 250px;
}

.group-information {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.user-info-and-group-sport {
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sport-text {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
  width: 60px;
  height: 30px;
  background-color: #1a73e8;
  color: white;
  font-weight: bold;
  font-size: 14px;
  margin-left: 5px;

  display: flex;
  justify-content: center;
  align-items: center;
}

.closing-soon {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
  width: 60px;
  height: 30px;
  background-color: black;
  color: white;
  font-weight: bold;
  font-size: 14px;
  margin-right: 70px;

  display: flex;
  justify-content: center;
  align-items: center;
}

.user-info {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 8px;
}

.user-name {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
  font-weight: bold;
  font-size: 12px;
  margin-right: 5px;
}

.user-img {
  width: 30px;
  height: 30px;
  border-radius: 100%;
}

.group-text {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  display: flex;
  flex-direction: column;
  margin-top: 10px;
}

.title {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  font-size: 16px;
  font-weight: bolder;
  margin-top: -10px;
  margin-left: 5px;
  margin-bottom: 5px;
}

.date-and-time {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  font-size: 14px;
  font-weight: bolder;
  margin-left: 6px;
  margin-bottom: 2px;
  color: grey;
}

.person {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  font-size: 14px;
  font-weight: bold;
  margin-left: 5px;
  color: grey;

}

.attend {
  padding: 11px 17px;
  border: none;
  border-radius: 10px;
  background-color: #1a73e8;
  color: white;
  font-size: 12px;
  font-weight: bold;
  width: 58px;
  height: 38px;
  cursor: pointer;
}

.attend:hover {
  background-color: #87cefa;
}

.attend:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

/* 모달 창 스타일 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
  position: relative;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
}

.modal-content p {
  font-size: 16px;
  text-align: start;
}

/* 팝업 스타일링 */
.confirm-popup {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  font-weight: bold;
}

.popup-content button {
  margin: 20px;
  width: 100px;
  height: 35px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  border: none;
  color: white;
  background-color: #1a73e8;
}

.popup-content button:hover {
  background-color: #87cefa;
}

.popup-content button:active {
  background-color: #87cefa;
  transform: scale(0.98);
  /* 클릭 시 버튼 크기 살짝 축소 */
}

/* 모임 찜 버튼 */
.group-save-icon {
  width: 28px;
  height: 28px;
  cursor: pointer;
}

/* 모임 참석 버튼 */
.group-join-icon {
  width: 25px;
  height: 25px;
  margin-left: auto;
  cursor: pointer;
}
</style>
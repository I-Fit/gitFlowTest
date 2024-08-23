<template>
  <main>
    <AppNav />
    <div class="party">
      <div class="party-null-block"></div>
      <div class="party-top">
        <h2>모임 관리</h2>
        <p class="line text01" @click="myCreategroup">내가 만든 모임</p>
        <p class="line text02">모임 참여 내역</p>
        <p class="text03" @click="likeGroup">찜한 모임 내역</p>
      </div>
      <div class="party-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" id="search_input" placeholder="검색어를 입력하세요." class="search-box-input" />
            <img src="@/assets/images/search.icon.png" alt="search" class="search-box-icon" />
          </div>
          <select title="정렬" class="middle-filter-sort">
            <option value="" selected="selected" disabled="disabled">
              정렬
            </option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <!-- 참여 한 모임 내역 -->
        <div class="group">
          <div class="group-container">
            <div class="user-info">
              <img src="@/assets/images/user_img.png" alt="사용자 이미지" class="user-images" />
              <span>김계란</span>
              <img src="@/assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title"> 수영 같이 하실 분 구함 </span>
            </div>
            <p class="date">24.06.14 (금)</p>
            <p class="time">8:00 PM</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart">
                <div :class="{ 'filled-heart': isHeartFilled, 'empty-heart': !isHeartFilled }"></div>
              </div>
              <span class="size">참여인원: 3/10</span>
              <span class="location">강남구</span>
              <button type="button" class="cancel" @click="showConfirmPopup = true">
                취소
              </button>
              <div v-if="showConfirmPopup" class="confirm-popup">
                <div class="popup-content">
                  <p>모임 참여를 취소하시겠습니까?</p>
                  <button class="confirm-btn" @click="confirmDeletion">
                    확인
                  </button>
                  <button class="cancle-btn" @click="cancelDeletion">
                    취소
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div v-for="group in visibleDatas" :key="group.communityId" class="group-container">
            <div class="user-info">
              <img :src="group.user_img || defaultProfileImage" alt="사용자 이미지" class="user-img" />
              <span>{{ group.username }}</span>
              <img src="@/assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title">{{ group.title }}</span>
            </div>
            <p class="date">{{ group.selectedDate }}</p>
            <p class="time">{{ group.selectedTime }}</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart(group.communityId)">
                <div :class="{ 'filled-heart': group.isHeartFilled, 'empty-heart': !group.isHeartFilled }"></div>
              </div>
              <span class="size">참여인원: {{ group.person }}</span>
              <span class="location">{{ group.location }}</span>
              <button type="button" class="cancel" @click="showConfirmPopup = true">
                취소
              </button>
              <div v-if="showConfirmPopup" class="confirm-popup">
                <div class="popup-content">
                  <p>모임 참여를 취소하시겠습니까?</p>
                  <button class="confirm-btn" @click="confirmDeletion(group.communityId)">
                    확인
                  </button>
                  <button class="cancle-btn" @click="cancelDeletion">
                    취소
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="group-container"></div>
          <div class="group-container"></div>
          <div class="group-container"></div>
        </div>
        <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="fetchJoins" />
      </div>
      <div class="joinlist-floor">
      </div>
    </div>
    <div class="modal" v-if="isModalOpen">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>모임 상세설명: {{ selectedItem ? selectedItem.content : "" }}</p>
      </div>
    </div>
  </main>
</template>

<script>
import AppNav from "@/components/layout/AppNav.vue";
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import axios from "axios";
import Pagination from "@/components/common/Pagination.vue";
import { usePagination } from "@/utils/pagination";

export default {
  name: "JoinedGroups",
  components: {
    AppNav,
    Pagination,
  },
  data() {
    return {
      isModalOpen: false, // 모달 창 상태
      selectedItem: null, // 선택된 아이템
    }
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
    const route = useRoute();

    const myCreategroup = () => {
      router.push({ name: "CreatedGroups" });
    };

    const likeGroup = () => {
      router.push({ name: "LikedGroups" });
    };

    const groups = ref([]);

    const { currentPage, totalPages, visibleGroups, fetchdatas, onPageChange } = usePagination(groups, 6);
    const userId = ref(route.query.userId);
    const communityId = ref(route.query.communityId);

    // 홈 페이지에서 모임 식별 키와 사용자 식별 키를 받아와
    // 서버에 보내서 조회
    const fetchGroups = async () => {
      try {
        const response = await axios.get('/api/joined-groups/details', {
          params: {
            communityId: communityId.value,
            userId: userId.value
          }
        });
        groups.value = response.data.groups;
        fetchdatas(1);
      } catch (error) {
        console.error("Error", error);
      }
    };

    onMounted(async () => {
      await fetchGroups();
    });

    // 취소 모달 열기
    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };
    // 모임 취소 버튼 클릭 후 확인 버튼 누르면 삭제되고 다시 로드
    const showConfirmPopup = ref(false);
    const confirmDeletion = async (communityId) => {
      try {
        await axios.delete('/api/', { data: { communityId } });
        showConfirmPopup.value = false;
        await fetchGroups();  // 삭제 후 데이터 다시 로드

      } catch(error) {
        console.error("Error", error);
      }
    };
    const cancelDeletion = () => {
      showConfirmPopup.value = false;
    };

    // 하트 클릭
    const isHeartFilled = ref(false);
    const toggleHeart = async (communityId) => {
      const group = groups.value.find(group => group.communityid === communityId);
      if (group) {
        group.isHeartFilled = !group.isHeartFilled;

        try {
          await axios.post("/api/", {
            communityId: group.communityId,
            isHeartFilled: group.isHeartFilled,
          });
        } catch (error) {
          console.error("Error", error);
  
          // 서버 요청 실패 시 상태 롤백
          group.isHeartFilled = !group.isHeartFilled;
        }
      }
    };

    return {
      currentPage,
      totalPages,
      visibleGroups,
      fetchdatas,
      onPageChange,

      groups,
      toggleTooltip,
      showConfirmPopup,
      confirmDeletion,
      cancelDeletion,
      isHeartFilled,
      toggleHeart,
      myCreategroup,
      likeGroup,
    };
  },
};
</script>

<style scoped>
/* content 부분 */
main {
  width: 100%;
  height: 1200px;
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

.text01,
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

.joinlist-floor {
  height: 50px;
  /* floor 영역 높이 설정 */
  text-align: center;
  padding: 20px;
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
  width: 360px;
  height: 300px;
  border: 1px solid #ccc;
  border-radius: 20px;
  margin-bottom: 50px;
  margin-right: 95px;
}

.group-container:nth-child(3n) {
  margin-right: 0px;
}

.user-info {
  display: flex;
  align-items: center;
  margin: 29px 0 0 20px;
}

.user-images {
  width: 50px;
  height: 40px;
  margin-left: 10px;
}

.detail-icon {
  width: 20px;
  height: 20px;
  margin-left: 173px;
  cursor: pointer;
}

.group-content {
  display: flex;
  align-items: center;
  margin-top: 15px;
  justify-content: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #5d5a88;
}

.date {
  font-size: 18px;
  color: #9795b5;
  margin-left: 218px;
  margin-top: 20px;
  padding: 5px;
}

.time {
  font-size: 18px;
  color: #9795b5;
  margin-left: 252px;
  padding: 5px;
}

.group-info {
  margin-top: 20px;
  display: flex;
}

.like-images {
  width: 35px;
  height: 35px;
  margin-right: 30px;
}

.size {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
}

.location {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
}

.cancel {
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

.cancel:hover {
  background-color: #87cefa;
}

.cancel:active {
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
  background: rgba(0, 0, 0, 0.5);
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

/* 하트 색상 변경 */
.title-heart {
  cursor: pointer;
  display: inline-block;
  width: 35px;
  /* 하트의 크기를 조정합니다 */
  height: 35px;
  /* 하트의 크기를 조정합니다 */
  margin-left: 40px;
  margin-right: 20px;

  display: flex;
  justify-content: center;
  align-items: center;
}

.title-heart div {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.empty-heart::before {
  content: '\2764';
  /* 빈 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: transparent;
  /* 하트의 내부는 투명하게 */
  -webkit-text-stroke: 1px black;
  /* 하트의 테두리 색상 */
}

.filled-heart::before {
  content: '\2764';
  /* 채워진 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: red;
  /* 채워진 하트의 색상 */
  -webkit-text-stroke: none;
  /* 채워진 하트의 테두리 제거 */
}
</style>

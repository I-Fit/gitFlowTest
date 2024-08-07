<template>
  <main>
    <AppNav />
    <div class="party">
      <div class="party-top">
        <h2>모임 관리</h2>
        <p class="line text01">내가 만든 모임</p>
        <p class="line text02" @click="groupjoinlist">모임 참여 내역</p>
        <p class="text03" @click="likegroup">찜한 모임 내역</p>
      </div>
      <div class="party-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" id="search-input" placeholder="검색어를 입력하세요." class="search-box-input" />
            <img src="@/assets/image/search.icon.png" alt="search" class="search-box-icon" />
          </div>
          <select title="정렬" class="middle-filter-sort">
            <option value="" selected="selected" disabled="disabled">
              정렬
            </option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <!-- 찜한 모임 내역 -->
        <div class="group">
          <div class="group-container">
            <div class="user-info">
              <img src="@/assets/image/user_img.png" alt="사용자 이미지" class="user-image" />
              <span>김계란</span>
              <img src="@/assets/image/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal"/>
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
              <span class="cancel">취소</span>
            </div>
          </div>

          <div v-for="group in groupDetails" :key="group.id" class="group-container">
            <div class="user-info">
              <img :src="group.userImage || '@/assets/image/user_img.png'" alt="사용자 이미지" class="user-image" />
              <span>{{ group.username }}</span>
              <img src="@/assets/image/상세설명 아이콘.png" alt="상세설명 아이콘" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title">{{ group.title }}</span>
            </div>
            <p class="date">{{ group.selectedDate }}</p>
            <p class="time">{{ group.selectedTime }}</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart">
                <div :class="{ 'filled-heart': isHeartFilled, 'empty-heart': !isHeartFilled }"></div>
              </div>
              <span class="size">참여인원: {{ group.person }}</span>
              <span class="location">{{ group.location }}</span>
              <span class="cancel" @click="removeGroup">취소</span>
            </div>
          </div>
          <div class="group-container"></div>
          <div class="group-container"></div>
        </div>
      </div>
    </div>
    <div class="modal" v-if="isModalOpen">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>모임 상세설명 : {{ selectedItem ? selectedItem.content : "" }}</p>
      </div>
    </div>
  </main>
</template>

<script>
import AppNav from '@/components/layout/AppNav.vue';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

export default {
  name: "LikeGroup",
  components: {
    AppNav,
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

    const groupjoinlist = () => {
      router.push({ name: "GroupJoinList" });
    };
    const likegroup = () => {
      router.push({ name: "LikeGroup" });
    };

    const isHeartFilled = ref(true);
    const toggleHeart = () => {
      isHeartFilled.value = !isHeartFilled.value;
    }

    return {
      groupjoinlist,
      likegroup,
      isHeartFilled,
      toggleHeart,
    }
  },
};
</script>

<style scoped>
/* content 부분 */
main {
  width: 100%;
  height: 900px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

.party {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 150px 1fr;
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
}

.text01,
.text02 {
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
  padding: 5px;
  width: 250px;
  margin-left: 906px;
}

.search-box-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
}

.search-box-icon {
  width: 15px;
  height: 15px;
  margin-left: 45px;
  cursor: pointer;
}

.middle-filter-sort {
  width: 75px;
  height: 37px;
  font-size: 14px;
  margin-left: 30px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff !important;
  text-align: center;
  font-weight: lighter;
}

/* 참여 한 모임 내역 css */
.group {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.group-container {
  width: 360px;
  height: 300px;
  border: 1px solid #ccc;
  border-radius: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  margin: 29px 0 0 20px;
}

.user-image {
  width: 50px;
  height: 40px;
  margin-left: 10px;
}

.detail-icon {
  width: 20px;
  height: 20px;
  margin-left: 173px;
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
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.like-image {
  width: 35px;
  height: 35px;
  margin-right: 30px;
  cursor: pointer;
}

.size {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
  margin-left: 10px;
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

/* 하트 색상 변경 */
.title-heart {
  cursor: pointer;
  display: inline-block;
  width: 35px;
  /* 하트의 크기를 조정합니다 */
  height: 35px;
  /* 하트의 크기를 조정합니다 */
  position: relative;
  margin-left: 15px;
}

.title-heart div {
  width: 100%;
  height: 100%;
  position: absolute;
  bottom: 9px;
  right: 10px;
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
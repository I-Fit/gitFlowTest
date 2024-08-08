<template>
  <main>
    <!-- 캐러셀 부분 -->
    <carousel class="carousel">
      <carousel-box class="carousel-box" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div class="carousel-item" v-for="item in items" :key="item.id">
          {{ item.content }}
        </div>
      </carousel-box>
      <div class="arrow" id="left" @click="prevSlide">&#9001;</div>
      <div class="arrow" id="right" @click="nextSlide">&#9002;</div>
    </carousel>

    <!-- 종목, 장소, 날짜, 시간 필터 -->
    <div class="main-bottom">
      <div class="filter">
        <div class="filter-list">
          <select class="list-select" title="운동 종목">
            <option value="" selected disabled>운동 종목</option>
          </select>
          <select class="list-select" title="장소">
            <option value="" selected disabled>장소</option>
          </select>
          <select class="list-select" title="날짜">
            <option value="" selected disabled>날짜</option>
          </select>
          <select class="list-select" title="시간">
            <option value="" selected disabled>시간</option>
          </select>
          <form class="search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." />
            <img src="../assets/image/search.icon.png" alt="search" class="search-icon" />
          </form>
        </div>
        <div class="sort">
          <select class="sort-title" title="정렬">
            <option value="" selected disabled>정렬</option>
            <option value="1">인기순</option>
            <option value="2">오름차순</option>
            <option value="3">내림차순</option>
            <option value="4">최신순</option>
          </select>
        </div>

        <!-- 생성된 모임 영역 -->
        <div class="group">
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/image/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/image/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title"> 수영 같이 하실 분 구함 </span>
            </div>
            <p class="date">24.06.14 (금)</p>
            <p class="time">8:00 PM</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart">
                <div :class="{
                  'filled-heart': isHeartFilled,
                  'empty-heart': !isHeartFilled,
                }"></div>
              </div>
              <span class="size">참여인원: 3/10</span>
              <span class="location">강남구</span>
              <button type="button" class="attend" @click="showConfirmPopup = true">
                참석
              </button>
              <div v-if="showConfirmPopup" class="confirm-popup">
                <div class="popup-content">
                  <p>모임에 참여하시겠습니까?</p>
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
          <div v-for="group in groups" :key="group.communityId" class="group-container">
            <div class="user-info">
              <img :src="group.user_img || '/default-profile.png'" alt="사용자 이미지" class="user-img" />
              <span>{{ group.username }}</span>
              <img src="../assets/image/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title">{{ group.title }}</span>
            </div>
            <p class="date">{{ group.selectedDate }}</p>
            <p class="time">{{ group.selectedTime }}</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart">
                <div :class="{
                  'filled-heart': isHeartFilled,
                  'empty-heart': !isHeartFilled,
                }"></div>
              </div>
              <span class="size">참여인원: {{ group.person }}</span>
              <span class="location">{{ group.location }}</span>
              <button type="button" class="attend" @click="showConfirmPopup = true">
                참석
              </button>
              <div v-if="showConfirmPopup" class="confirm-popup">
                <div class="popup-content">
                  <p>모임에 참여하시겠습니까?</p>
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
        </div>
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
import { ref, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

export default {
  name: "Home",

  data() {
    return {
      currentIndex: 0,
      items: [
        {
          id: 1,
          content: "슬라이드 1",
          popularity: 2,
          createdAt: "2024-08-03",
          liked: false,
        },
        {
          id: 2,
          content: "슬라이드 2",
          popularity: 5,
          createdAt: "2024-07-20",
          liked: false,
        },
        {
          id: 3,
          content: "슬라이드 3",
          popularity: 1,
          createdAt: "2024-08-01",
          liked: false,
        },
        // 더 많은 슬라이드를 추가할 수 있습니다.
        // TODO - 캐러셀 아이템 생성
      ],
      sortOption: "",
      isModalOpen: false, // 모달 창 상태
      selectedItem: null, // 선택된 아이템
    };
  },
  watch: {
    sortOption(newValue) {
      this.sortItems(newValue);
    },
  },
  methods: {
    prevSlide() {
      this.currentIndex =
        this.currentIndex > 0 ? this.currentIndex - 1 : this.items.length - 1;
    },
    nextSlide() {
      this.currentIndex =
        this.currentIndex < this.items.length - 1 ? this.currentIndex + 1 : 0;
    },
    sortItems(option) {
      switch (option) {
        case "1":
          // 인기순
          this.items.sort((a, b) => b.popularity - a.popularity);
          break;
        case "2":
          // 최신순 (날짜)
          this.items.sort(
            (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
          );
          break;
        case "3":
          // 오름차순 (날짜)
          this.items.sort(
            (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
          );
          break;
        case "4":
          // 내림차순 (날짜)
          this.items.sort(
            (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
          );
          break;
        default:
          break;
      }
    },
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
    // TODO - 검색 필터링 메서드
  },

  setup() {
    const router = useRouter();
    const route = useRoute();

    // 빈 배열로 초기화
    const groups = ref([]);

    // 단일 객체라 배열에 넣어줘야 동적으로 모임 생성 가능
    const groupData = computed(() => ({
      userId: route.query.userId,
      communityId: route.query.communityId,
      user_img: route.query.user_img,
      username: route.query.username,
      title: route.query.title,
      topboxContent: route.query.topboxContent,
      sport: route.query.sport,
      location: route.query.location,
      selectedDate: route.query.selectedDate,
      selectedTime: route.query.selectedTime,
    }));

    onMounted(() => {
      groups.value = [groupData.value];
    });

    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };
    // 참석 모달 연 후 참석 버튼 누르면 페이지 이동
    const showConfirmPopup = ref(false);
    const confirmDeletion = () => {
      router.push({ name: "GroupJoinList", query: { communityId: route.query.communityId } });
    };
    const cancelDeletion = () => {
      showConfirmPopup.value = false;
    };
    // 모임 찜 이벤트
    const isHeartFilled = ref(false);
    const toggleHeart = () => {
      isHeartFilled.value = !isHeartFilled.value;
    };

    return {
      groups,
      toggleTooltip,
      confirmDeletion,
      cancelDeletion,
      isHeartFilled,
      toggleHeart,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: 1fr 2fr;
}

/* 캐러셀 */
.carousel {
  position: relative;
  width: 90%;
  height: 600px;
  /* 화면 절반 정도 높이 설정 */
  overflow: hidden;
  margin: 0 auto;
}

.carousel-box {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.carousel-item {
  min-width: 100%;
  height: 600px;
  /* carousel 높이에 맞춤 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  /* background-color: #ddd; */
  /* 슬라이드 아이템 배경색 */
}

.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  font-size: 20px;
  cursor: pointer;
  user-select: none;
  z-index: 1;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s, transform 0.3s;
}

.arrow:hover {
  transform: translateY(-50%) scale(1.1);
}

#left {
  left: 10px;
}

#right {
  right: 10px;
}

/* 필터 */
.main-bottom {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.filter {
  width: 1320px;
  height: 900px;
  display: grid;
  grid-template-rows: 100px 40px 4fr;
}

.filter-list {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.list-select {
  width: 200px;
  height: 48px;
  border-radius: 10px;
  text-align: center;
  background-color: #fff;
  border: 1px solid lightgrey;
  font-size: 16px;
  cursor: pointer;
}

/* 검색창 */
.search-box {
  display: flex;
  align-items: center;
  border: 1px solid lightgrey;
  border-radius: 10px;
  width: 250px;
  height: 48px;
}

.search-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
  font-size: 16px;
}

.search-icon {
  width: 15px;
  height: 15px;
  margin-left: 15px;
  cursor: pointer;
}

/* 정렬순 블럭 */
.sort {
  display: flex;
  justify-content: flex-end;
}

.sort-title {
  width: 110px;
  height: 40px;
  text-align: center;
  border-radius: 10px;
  background-color: #fff;
  border: 1px solid lightgrey;
  box-sizing: border-box;
  font-size: 16px;
  cursor: pointer;
}

/* 생성 모임 영역 */
.group {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-top: 25px;
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

.user-img {
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

.like-img {
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
  margin-left: 10px;
}

.location {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
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
  content: "\2764";
  /* 빈 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: transparent;
  /* 하트의 내부는 투명하게 */
  -webkit-text-stroke: 1px black;
  /* 하트의 테두리 색상 */
}

.filled-heart::before {
  content: "\2764";
  /* 채워진 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: red;
  /* 채워진 하트의 색상 */
  -webkit-text-stroke: none;
  /* 채워진 하트의 테두리 제거 */
}
</style>

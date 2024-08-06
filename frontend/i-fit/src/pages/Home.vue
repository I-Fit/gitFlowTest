<template>
  <main>
    <!-- 캐러셀 부분 -->
    <carousel class="carousel">
      <carousel-box
        class="carousel-box"
        :style="{ transform: `translateX(-${currentIndex * 100}%)` }"
      >
        <div class="carousel-item" v-for="item in items" :key="item.id">
          {{ item.content }}
        </div>
      </carousel-box>
      <div class="arrow" id="left" @click="prevSlide">&lang;</div>
      <div class="arrow" id="right" @click="nextSlide">&rang;</div>
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
            <input
              type="text"
              name="search"
              class="search-input"
              placeholder="검색어를 입력하세요."
            />
            <img
              src="../assets/image/search.icon.png"
              alt="search"
              class="search-icon"
            />
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
              <img
                src="../assets/image/user_img.png"
                alt="사용자 이미지"
                class="user-img"
              />
              <span>김계란</span>
              <img
                src="../assets/image/상세설명 아이콘.png"
                alt=""
                class="detail-icon"
                @click="openModal(item)"
              />
            </div>
            <div class="group-content">
              <span class="title"> 수영 같이 하실 분 구함 </span>
            </div>
            <p class="date">24.06.14 (금)</p>
            <p class="time">8:00 PM</p>
            <div class="group-info">
              <img
                class="like-img"
                src="../assets/image/heart.png"
                alt="하트"
              />
              <span class="size">참여인원: 3/10</span>
              <span class="location">강남구</span>
              <button type="button" class="cancel">참석</button>
            </div>
          </div>
          <div class="group-container">
            <div class="user-info">
              <img
                :src="additionalData.userImg"
                alt="사용자 이미지"
                class="user-img"
              />
              <span>{{ additionalData.username }}</span>
              <img
                src="../assets/image/상세설명 아이콘.png"
                alt=""
                class="detail-icon"
                @click="openModal"
              />
            </div>
            <div class="group-content">
              <span class="title">{{ formData.title }}</span>
            </div>
            <p class="date">24.06.14 (금)</p>
            <p class="time">{{ formData.selectedTime }}</p>
            <div class="group-info">
              <img
                class="like-img"
                src="../assets/image/heart.png"
                alt="하트"
              />
              <span class="size">참여인원: {{ formData.person }}</span>
              <span class="location">{{ formData.location }}</span>
              <span class="cancel">참석</span>
            </div>
          </div>
          <div class="group-container"></div>
          <div class="group-container"></div>
        </div>
      </div>
    </div>
    <!-- 모달 창 -->
    <div class="modal" v-if="isModalOpen">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>{{ formData.topboxContent }}</h2>
        <p>{{ selectedItem ? selectedItem.content : "" }}</p>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, computed } from "vue";
import { useStore } from "vuex";

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
    openModal(item) {
      this.selectedItem = item;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
      this.selectedItem = null;
    },
    // TODO - 검색 필터링 메서드
  },

  setup() {
    const store = useStore();

    const formData = computed(() => store.getters["creategroup/formData"]);
    const additionalData = computed(
      () => store.getters["creategroup/additionalData"]
    );

    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };
    return {
      formData,
      additionalData,
      toggleTooltip,
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
  width: 100%;
  height: 50vh;
  /* 화면 절반 정도 높이 설정 */
  overflow: hidden;
}

.carousel-box {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.carousel-item {
  min-width: 100%;
  height: 50vh;
  /* carousel 높이에 맞춤 */
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  background-color: #ddd;
  /* 슬라이드 아이템 배경색 */
}

.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  font-size: 2rem;
  cursor: pointer;
  user-select: none;
  z-index: 1;
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
  border: 1px solid whitesmoke;
  font-size: 16px;
  cursor: pointer;
}

/* 검색창 */
.search-box {
  display: flex;
  align-items: center;
  border: 1px solid whitesmoke;
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
  border: 1px solid whitesmoke;
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
</style>

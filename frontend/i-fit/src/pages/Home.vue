<template>
  <main>
    <!-- 캐러셀 부분 -->
    <carousel class="carousel">
      <carousel-box class="carousel-box" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div class="carousel-item" v-for="item in items" :key="item.id">
          <img :src="item.images" :alt="item.content" class="slide-images" />
          <div class="slide-content">
            {{ item.content }}
          </div>
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
          <select class="list-select" title="장소" @click="fetchLocationData">
            <option value="" selected disabled>장소</option>
            <option v-for="location in locations" :key="location.id" :value="location.id">
              {{ location.name }}
            </option>
          </select>

          <!-- <select class="list-select" title="날짜"> -->
          <VueDatePicker class="date-picker" locale="ko" v-model="date" :enable-time-picker="false" />
          <!-- <option value="" selected disabled>날짜</option> -->
          <!-- </select> -->

          <select class="list-select" title="시간">
            <option value="" selected disabled>시간</option>
          </select>
          <form class="search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." />
            <img src="../assets/images/search.icon.png" alt="search" class="search-icon" />
          </form>
        </div>
        <div class="sort">
          <select class="sort-title" title="정렬">
            <option value="" selected disabled>정렬</option>
            <option value="1">인기순</option>
            <option value="2">최신순</option>
            <option value="3">오래된순</option>
          </select>
        </div>

        <!-- 생성된 모임 영역 -->
        <div class="group">
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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
          <div class="group-container">
            <div class="user-info">
              <img src="../assets/images/user_img.png" alt="사용자 이미지" class="user-img" />
              <span>김계란</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
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

          <div v-for="group in visibleDatas" :key="group.communityId" class="group-container">
            <div class="user-info">
              <img :src="group.user_img || '/default-profile.png'" alt="사용자 이미지" class="user-img" />
              <span>{{ group.username }}</span>
              <img src="../assets/images/상세설명 아이콘.png" alt="" class="detail-icon" @click="openModal" />
            </div>
            <div class="group-content">
              <span class="title">{{ group.title }}</span>
            </div>
            <p class="date">{{ group.date }}</p>
            <p class="time">{{ group.date }}</p>
            <div class="group-info">
              <div class="title-heart" @click="toggleHeart(group.communityId)">
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
              <div v-if="showConfirmPopup === group.communityId" class="confirm-popup">
                <div class="popup-content">
                  <p>모임에 참여하시겠습니까?</p>
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
        </div>
        <PagiNation :currentPage="currentPage" :totalPages="totalPages" @page-changed="onPageChange" />
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
import { ref, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useStore } from "vuex";
import PagiNation from "@/components/common/HomePagiNation.vue";
import { usePagination } from "@/utils/pagination";

export default {
  name: "Home",
  components: {
    PagiNation,
  },

  data() {
    return {
      currentIndex: 0,
      items: [
        {
          id: 1,
          // content: "slide1",
          popularity: 2,
          createdAt: "2024-08-03",
          liked: false,
          images: require('@/assets/images/slide1.png')
        },
        {
          id: 2,
          // content: "slide2",
          popularity: 5,
          createdAt: "2024-07-20",
          liked: false,
          images: require('@/assets/images/slide2.png')
        },
        {
          id: 3,
          // content: "slide3",
          popularity: 1,
          createdAt: "2024-08-01",
          liked: false,
          images: require('@/assets/images/slide3.png')
        },
        // 더 많은 슬라이드를 추가할 수 있습니다.
        // TODO - 캐러셀 아이템 생성
      ],
      slideInterval: null,
      sortOption: "",
      isModalOpen: false, // 모달 창 상태
      selectedItem: null, // 선택된 아이템
      locations: [],  // 장소 데이터 저장 배열
    };
  },
  mounted() {
    // 컴포넌트가 마운트된 후 자동 슬라이드 전환 시작
    this.startAutoSlide();
  },
  beforeUnmount() {
    // 컴포넌트가 언마운트되기 전에 타이머 클리어
    this.stopAutoSlide();
  },
  watch: {
    sortOption(newValue) {
      this.sortItems(newValue);
    },
  },
  methods: {
    startAutoSlide() {
      // 3초마다 슬라이드 전환
      this.slideInterval = setInterval(() => {
        this.nextSlide();
      }, 4000);
    },
    stopAutoSlide() {
      // 타이머 클리어 -> 자동 슬라이드 전환 중지
      if (this.slideInterval) {
        clearInterval(this.slideInterval);
        this.slideInterval = null;
      }
    },
    prevSlide() {
      this.currentIndex =
        this.currentIndex > 0 ? this.currentIndex - 1 : this.items.length - 1;
    },
    nextSlide() {
      this.currentIndex =
        this.currentIndex < this.items.length - 1 ? this.currentIndex + 1 : 0;
    },
    fetchLocationData() {
      fetch("https://api.example.com/locations", {    // todo - 실제 api 주소의 엔드포인트로 수정 필요
        method: "GET",
        headers: {
          "Content-Type": "application/json",   // todo - api 응답 데이터 구조 확인 후 수정 필요
        },
      })
      .then(response => response.json())
      .then(data => {
        this.locations = data;
      })
      .catch(error => console.error("Error fetching location data:", error));
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
          // 오래된순 (날짜)
          this.items.sort(
            (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
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
    const store = useStore();
    // 여러 모임 데이터 배열에 저장
    const groups = ref([]);
    const PerPage = 6;
    const userId = computed(() => store.getters['isLogged/userId']);

    const { currentPage, totalPages, visibleDatas, fetchdatas, onPageChange } = usePagination(groups, PerPage);
    
    // creategroup에서 생성한 모임에 사용자 식별 ID값을 같이 보내줘서
    // 서버에서 username, user_img를 받아옴
    onMounted(async () => {
      try {
        const response = await axios.get('/api/group-details');

        // 서버로부터 받은 데이터를 groups배열에 저장
        groups.value = response.data.groups;
        fetchdatas(1);  // 데이터 로딩 후 초기 페이지 설정

      } catch (error) {
        console.error("Error", error);
      }
    });

    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };

    // 참석 모달 연 후 참석 버튼 누르면 페이지 이동
    // 사용자 식별 키와 모임 식별 키를 query로 보내줌
    const showConfirmPopup = ref(null);

    const confirmDeletion = async (communityId) => {
      try {
        await axios.post('/api/join-group', {
          communityId: communityId,
          userId: userId.value
        });
        router.push({ name: "GroupJoinList", query: { communityId, userId: userId.value } });
      } catch (error) {
        console.error("Error", error);
      }
    };

    const cancelDeletion = () => {
      showConfirmPopup.value = null;
    };

    // 모임 찜 이벤트
    const isHeartFilled = ref(false);

    // 하트 색상 변하면 서버에 보내서 찜한 모임 저장
    const toggleHeart = async (communityId) => {
      isHeartFilled.value = !isHeartFilled.value;
      try {
        await axios.post('/api/likegroup', {
          userId: userId.value,
          communityId: communityId,
        });
      } catch (error) {
        console.error("Error", error);
      }
    };

    return {
      groups,
      userId,

      currentPage,
      totalPages,
      visibleDatas,
      onPageChange,
      fetchdatas,

      showConfirmPopup,
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
  max-width: 1248px;
  /* height: auto; */
  /* max-height: 700px; */
  overflow: hidden;
  margin: 0 auto;
}

.carousel-box {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.carousel-item {
  min-width: 100%;
  /* height: auto; */
  /* carousel 높이에 맞춤 */
  display: flex;
  align-items: center;
  justify-content: center;
  /* font-size: 24px; */
  /* background-color: #ddd; */
  /* 슬라이드 아이템 배경색 */
}

.slide-images {
  width: 100%;
  height: auto;
  object-fit: contain;
  /* 이미지 비율을 유지하면서 부모 요소에 맞춤 */
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

.date-picker {
  width: 200px;
  height: 48px;
  border-radius: 10px;
  text-align: center;
  background-color: #fff;
  border: 1px solid lightgrey;
  font-size: 16px;
  cursor: pointer;
}

::v-deep .dp__input {
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
  padding-right: 10px; /* 텍스트와 화살표 사이의 간격 확보 */
  -webkit-appearance: none; /* 기본 화살표 제거 */
  -moz-appearance: none;
  appearance: none;
  background-image: url('@/assets/images/custom-arrow.png'); /* 커스텀 화살표 이미지 경로 */
  background-repeat: no-repeat;
  background-position: right 15px center; /* 아이콘 위치 조정 */
  background-size: 12px; /* 아이콘 크기 조정 */
}

/* 생성 모임 영역 */
.group {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  margin-top: 25px;
}

/* 생성된 모임의 위치를 justify-content로 하지 않고 margin값을 준 다음에
3번째 item에만 margin값을 주지 않음 */
.group-container {
  width: 360px;
  height: 300px;
  border: 1px solid #ccc;
  border-radius: 20px;
  margin-right: 120px;
}

.group-container:nth-child(3n) {
  margin-right: 0px;
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
  margin-top: 20px;
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
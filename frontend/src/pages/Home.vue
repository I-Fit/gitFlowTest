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
    
    <div class="main-bottom">
    <!-- 종목, 지역, 날짜, 시간 필터-->
      <div class="filter">
        <div class="filter-list">
          <select class="list-select" title="운동 종목">
            <option value="" selected disabled>운동</option>
          </select>
          <select class="list-select" title="지역" @click="fetchLocationData">
            <option value="" selected disabled>지역</option>
            <option v-for="location in locations" :key="location.id" :value="location.id">
              {{ location.name }}
            </option>
          </select>

          <div>
            <VueDatePicker class="date-picker" placeholder="날짜" locale="ko" v-model="date" :enable-time-picker="false"
              year-first="true" select-text="확인" cancel-text="취소" />
          </div>

          <div>
            <VueDatePicker class="time-picker" placeholder="시간" locale="ko" v-model="time" time-picker select-text="확인"
              cancel-text="취소">
              <template #input-icon>
                <img class="input-slot-image" src="@/assets/images/clock-icon.png" />
              </template>
            </VueDatePicker>
          </div>

          <form class="search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." />
            <img src="../assets/images/search.icon.png" alt="search" class="search-icon" />
          </form>
        </div>
        <div class="sort">
          <select class="sort-title" title="정렬" v-model="selectedOption">
            <option value="" selected disabled>정렬</option>
            <option v-for="option in options" :key="option.value" :value="option.value">
              {{ option.text }}
            </option>
          </select>
        </div>

        <!-- 생성된 모임 영역 -->
        <div class="group">
          <div class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal" />
              <img :src="isSaved ? require('@/assets/images/saved-icon3.png') : require('@/assets/images/save-icon.png')" class="group-save-icon" @click="likeGroup" />
              <img class="group-join-icon" src="@/assets/images/plus-icon2.png" @click="showConfirmPopup = true" />

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
            <div class="sport-image-box">
              <img src="../assets/images/yoga-image.png" alt="요가 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">요가</p>
                <div class="user-info">
                  <span class="user-name">yoga123</span>
                  <img src="../assets/images/user-img-one.png" alt="사용자 이미지" class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="title"> 월요일 아침 같이 요가 해요 ~!</p>
                <p class="date-and-time">24.08.26 (월) 6:00 <span class="location">| 강남구</span></p>
                <p class="person">인원 : 2 / 5</p>
              </div>
            </div>
          </div>

          <div class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal" />
              <img :src="isSaved ? require('@/assets/images/saved-icon.png') : require('@/assets/images/save-icon.png')" class="group-save-icon" @click="likeGroup" />
              <img class="group-join-icon" src="@/assets/images/plus-icon2.png" @click="showConfirmPopup = true"/>

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
            <div class="sport-image-box">
              <img src="../assets/images/running-image.png" alt="러닝 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">러닝</p>
                <div class="user-info">
                  <span class="user-name">러닝걸</span>
                  <img src="../assets/images/user-img-two.png" alt="사용자 이미지" class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="title">이번 주말 한강 러닝 고?!</p>
                <p class="date-and-time">24.08.31 (토) 8:00 <span class="location">| 강남구</span></p>
                <p class="person">인원 : 0 / 6</p>
              </div>
            </div>
          </div>

          <div class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal" />
              <img :src="isSaved ? require('@/assets/images/saved-icon.png') : require('@/assets/images/save-icon.png')" class="group-save-icon" @click="likeGroup" />
              <img class="group-join-icon" src="@/assets/images/plus-icon2.png" @click="showConfirmPopup = true"/>
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
            <div class="sport-image-box">
              <img src="../assets/images/gym-image.png" alt="웨이트 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">웨이트</p>
                <div class="user-info">
                  <span class="user-name">안유진</span>
                  <img src="../assets/images/user-img-three.png" alt="사용자 이미지" class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="title"> 퇴근 후 50분 웨이트 인원 모집!!! 선착순 3명</p>
                <p class="date-and-time">24.08.26 (월) 20:00 <span class="location">| 용산구</span></p>
                <p class="person">인원 : 1 / 3</p>
              </div>
            </div>
          </div>
          
          <div class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal" />
              <img :src="isSaved ? require('@/assets/images/saved-icon.png') : require('@/assets/images/save-icon.png')" class="group-save-icon" @click="likeGroup" />
              <img class="group-join-icon" src="@/assets/images/plus-icon2.png" @click="showConfirmPopup = true"/>
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
            <div class="sport-image-box">
              <img src="../assets/images/running-image.png" alt="요가 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">러닝</p>
                <p class="closing-soon">마감임박</p>
                <div class="user-info">
                  <span class="user-name">런닝맨</span>
                  <img src="../assets/images/user-img-four.png" alt="사용자 이미지" class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="title">오늘 밤 가볍게 러닝하실 분</p>
                <p class="date-and-time">24.08.26 (월) 21:30 <span class="location">| 마포구</span></p>
                <p class="person">인원 : 5 / 6</p>
              </div>
            </div>
          </div>

          <div v-for="group in visibleDatas" :key="group.communityId" class="group-container">
            <div class="unicode-box">
              <img class="group-detail-icon" src="@/assets/images/info-icon.png" @click="openModal" />
              <img class="group-save-icon" src="@/assets/images/save-icon.png" @click="toggleHeart"/>
              <img class="group-join-icon" src="@/assets/images/plus-icon2.png" @click="showConfirmPopup = true"/>
              <div v-if="showConfirmPopup" class="confirm-popup">
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
            <div class="sport-image-box">
              <img src="../assets/images/yoga-image.png" alt="요가 이미지" class="sport-image">
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">{{ group.sport }}</p>
                <div class="user-info">
                  <span class="user-name">{{ group.username }}</span>
                  <img :src="group.user_img || '/default-profile.png'" alt="사용자 이미지" class="user-img">
                </div>
              </div>
              <div class="group-text">
                <p class="new-title"> {{ group.title }}</p>
                <p class="date-and-time">{{ group.date }}{{ group.time }} <span class="location">| {{ group.location }}</span></p>
                <p class="person">인원: {{ group.peopleParticipaion }} / {{ group.person }}</p>
              </div>
            </div>
          </div>
        </div>
        <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="onPageChange" />
      </div>
    </div>

    <!-- 모임 상세설명 모달 창 -->
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
import Pagination from "@/components/common/HomePagination";
import { usePagination } from "@/utils/pagination";

export default {
  name: "Home",
  components: {
    Pagination,
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
      isSaved: false,
      isModalOpen: false, // 모달 창 상태
      selectedItem: null, // 선택된 아이템
      locations: [],  // 지역 데이터 저장 배열
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
    likeGroup() {
      this.isSaved = !this.isSaved;
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
    //TODO - 검색 필터링 메서드
  },

  setup() {
    const time = ref(null);

    const selectedOption = ref('');
    const options = ref([
      // { value: '', text: '정렬' },
      { value: '1', text: '인기순' },
      { value: '2', text: '최신순' },
      { value: '3', text: '오래된순' },
    ])

    const router = useRouter();
    const store = useStore();
    // 여러 모임 데이터 배열에 저장
    const groups = ref([]);
    const PerPage = 8;
    const userId = computed(() => store.getters['isLogged/userId']);

    const { currentPage, totalPages, visibleDatas, fetchdatas, onPageChange } = usePagination(groups, PerPage);

    // creategroup에서 생성한 모임에 사용자 식별 ID값을 같이 보내줘서
    // 서버에서 username, user_img를 받아옴
    onMounted(async () => {
      try {
        const response = await axios.get('/api/group-list');

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
    const showConfirmPopup = ref(false);

    const confirmDeletion = async (communityId) => {
      try {
        await axios.post('/api/join-group', {
          communityId: communityId,
          userId: userId.value
        });
        router.push({ name: "JoinedGroups", query: { communityId, userId: userId.value } });
      } catch (error) {
        console.error("Error", error);
      }

      showConfirmPopup.value = false;
    };

    const cancelDeletion = () => {
      showConfirmPopup.value = false;
    };

    // 모임 찜 이벤트
    // const isHeartFilled = ref(false);

    // 하트 색상 변하면 서버에 보내서 찜한 모임 저장
    // const toggleHeart = async (communityId) => {
    //   isHeartFilled.value = !isHeartFilled.value;
    //   try {
    //     await axios.post('/api/like-group', {
    //       userId: userId.value,
    //       communityId: communityId,
    //     });
    //   } catch (error) {
    //     console.error("Error", error);
    //   }
    // };

    return {
      selectedOption,
      options,
      time,
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
  max-width: 1320px;
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
  border-radius: 3px;
  text-align: center;
  color: black;
  background-color: #fff;
  border: 0.1px solid grey;
  font-size: 14px;
  cursor: pointer;
  /* box-sizing: border-box; */
  padding-right: 10px;
  /* 텍스트와 화살표 사이의 간격 확보 */
  /* -webkit-appearance: none; */
  /* 기본 화살표 제거 */
  /* -moz-appearance: none; */
  appearance: none;
  /* background-image: url('@/assets/images/custom-arrow.png'); */
  /* 커스텀 화살표 이미지 경로 */
  /* background-repeat: no-repeat; */
  /* background-position: right 15px center; */
  /* 아이콘 위치 조정 */
  /* background-size: 12px; */
  /* 아이콘 크기 조정 */
}

.date-picker {
  width: 200px;
  height: 48px;
  text-align: center;
  background-color: #fff;
  font-size: 16px;
  cursor: pointer;
}

::v-deep .dp__input::placeholder {
  font-size: 14px;
  color: black;
}

::v-deep .dp__input {
  width: 200px;
  height: 48px;
  border-radius: 3px;
  text-align: center;
  background-color: #fff;
  border: 0.1px solid grey;
  font-size: 16px;
  cursor: pointer;
}

.time-picker {
  width: 200px;
  height: 48px;
  text-align: center;
  background-color: #fff;
  font-size: 16px;
  cursor: pointer;
}

::v-deep .vue__time-picker input.vue__time-picker-input {
  width: 200px;
  height: 48px;
  border-radius: 3px;
  text-align: center;
  background-color: #fff;
  border: 0.1px solid grey;
  font-size: 16px;
  cursor: pointer;
  color: black;
}

::v-deep .vue__time-picker input.vue__time-picker-input::placeholder {
  font-size: 18px;
  color: black;
}

.input-slot-image {
  height: 20px;
  width: auto;
  margin-left: 11px;
  margin-top: 5px;
}

/* 검색창 */
.search-box {
  display: flex;
  align-items: center;
  border: 1px solid grey;
  border-radius: 3px;
  width: 250px;
  height: 50px;
}

.search-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
  font-size: 14px;
}

.search-icon {
  width: 15px;
  height: 15px;
  margin-left: 220px;
  cursor: pointer;
  position: absolute;
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
  border-radius: 3px;
  /* color: grey; */
  border: 1px solid grey;
  box-sizing: border-box;
  font-size: 15px;
  cursor: pointer;
  padding-right: 10px;
  /* 텍스트와 화살표 사이의 간격 확보 */
  -webkit-appearance: none;
  /* 기본 화살표 제거 */
  -moz-appearance: none;
  appearance: none;
  background-image: url('@/assets/images/custom-arrow.png');
  /* 커스텀 화살표 이미지 경로 */
  background-repeat: no-repeat;
  background-position: right 15px center;
  /* 아이콘 위치 조정 */
  background-size: 12px;
  /* 아이콘 크기 조정 */
}

.sort-title option {
  background-color: #ffffff;
  /* 옵션 배경색 */
  color: #333;
  /* 옵션 글자 색 */
  padding: 10px;
  /* 옵션 내부 여백 */
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
/* 새로운 디자인 */
.group-container {
  display: grid;
  grid-template-rows: 40px 250px 1fr;
  width: 280px;
  height: 450px;
  margin-right: 66px;
}

.group > .group-container:nth-of-type(4n) {
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
<template>
  <main>
    <!-- 캐러셀 부분 -->
    <div class="carousel">
      <div class="carousel-box" :style="{ transform: `translateX(-${currentIndex * 100}%)` }">
        <div class="carousel-item" v-for="item in items" :key="item.id">
          <img :src="item.images" :alt="item.content" class="slide-images" @click="carouselGroups(item.id)" />
          <div class="slide-content">
            {{ item.content }}
          </div>
        </div>
      </div>
      <div class="arrow" id="left" @click="prevSlide">&#9001;</div>
      <div class="arrow" id="right" @click="nextSlide">&#9002;</div>
    </div>

    <div class="main-bottom">
      <!-- 종목, 지역, 날짜, 시간 필터-->
      <div class="filter">
        <div class="filter-list">
          <select class="list-select" title="운동 종목" @change="SportGroupList">
            <option value="" selected disabled>운동</option>
            <option v-for="sport in sports" :key="sport.key" :value="sport.sport">{{ sport.sport }}</option>
          </select>

          <div class="list-select" @click="openDaumApi">
            <input class="input-event" v-model="locationInput" type="text" placeholder="지역" />
          </div>

          <div>
            <VueDatePicker class="date-picker" placeholder="날짜" locale="ko" v-model="date" :enable-time-picker="false"
              select-text="확인" cancel-text="취소" @date-update="dateClicked" format="yyyy-MM-dd" />
          </div>

          <div>
            <input type="time" class="time-picker" v-model="timeString" @change="timeSelected" placeholder="시간">
          </div>

          <form class="search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." v-model="searchTerm"
              @keyup.enter="searchGroups" />
            <img src="../assets/images/search.icon.png" alt="search" class="search-icon" @click="searchGroups" />
          </form>
        </div>
        <div class="sort">
          <select class="sort-title" title="정렬" v-model="selectedOption" @change="selectedGroupList">
            <option value="" selected disabled>정렬</option>
            <option v-for="option in options" :key="option.value" :value="option.value">
              {{ option.text }}
            </option>
          </select>
        </div>

        <!-- 생성된 모임 영역 -->
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
                  <p>모임에 참여하시겠습니까?</p>
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
              <img :src="getSportImage(group.sport)" alt="스포츠 이미지" class="sport-image">
              <!-- <img src="../assets/images/yoga-image.png" alt="요가 이미지" class="sport-image"> -->
            </div>
            <div class="group-information">
              <div class="user-info-and-group-sport">
                <p class="sport-text">{{ group.sport }}</p>
                <div class="user-info">
                  <span class="user-name">{{ group.username }}</span>
                  <img
                    :src="group.profileUrl ? `data:image/png;base64,${group.profileUrl}` : require('@/assets/images/default-profile.png')"
                    alt="사용자 이미지" class="user-img">
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
      </div>
    </div>

    <!-- 모임 상세설명 모달 창 -->
    <div class="modal" v-if="isModalOpen">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p class="modal-description"><strong>모임 상세설명 : </strong>{{ selectedItem ? selectedItem.content : "" }}</p>
        <p><strong>모임 상세주소 : </strong>{{ selectedItem ? selectedItem.location : "" }}</p>
      </div>
    </div>

  </main>
</template>

<script>
import { ref, computed, onBeforeMount } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import { useStore } from "vuex";
import apiClient from '@/api/apiClient';
// import { timePicker } from "vue3-timepicker";
// import { usePagination } from "@/utils/pagination";
// import InfiniteLoading from 'vue3-infinite-loading';


export default {
  name: "Home",
  components: {
    // timePicker,
    // Pagination,
    // InfiniteLoading,
  },

  data() {
    return {
      currentIndex: 0,
      items: [
        {
          //  참석자 별로 안남은 모임순
          id: 1,
          // content: "slide1",
          popularity: 2,
          createdAt: "2024-08-03",
          liked: false,
          images: require('@/assets/images/slide1.png')
        },
        {
          //  토, 일 모임만 표시
          id: 2,
          // content: "slide2",
          popularity: 5,
          createdAt: "2024-07-20",
          liked: false,
          images: require('@/assets/images/slide2.png')
        },
        {
          //  오전 6시 ~ 10시 모임만 표시
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
        location: group.fullLocation
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
    const displayedGroups = ref([]);
    const page = ref(1);
    const pageSize = 12;
    const loading = ref(false);
    const isEnd = ref(false);

    const sports = ref([]);   // 운동 종목을 저장할 객체
    const searchTerm = ref('');   // 검색어를 저장할 객체
    const userId = computed(() => store.getters['isLogged/userId']);

    const getSportImage = (sport) => {
      const images = {
        요가 : require('@/assets/images/yoga-image.png'),
        러닝 : require('@/assets/images/running-image.png'),
        웨이트 : require('@/assets/images/gym-image.png'),
      };
      return images[sport] || require('@/assets/images/gym-image.png');
    }

    // const { currentPage, totalPages, visibleDatas, fetchdatas, onPageChange } = usePagination(groups, PerPage);

    const carouselGroups = async (id) => {
      try {
        const response = await axios.get('/api/carousel-groups', {
          params: {
            imageId: id,
          }
        });

        const nonLoggedGroups = response.data;
        const isLoggedIn = computed(() => store.getters['isLogged/loggedIn']);
        if (!isLoggedIn.value) {
          groups.value = nonLoggedGroups.map(group => ({
            ...group,
            saved: 0
          }));
        } else {
          groups.value = nonLoggedGroups;
        }
      } catch (error) {
        console.log("Error", error);
      }
    }

    //  홈 페이지에 전체 모임 데이터
    const groupList = async () => {
      loading.value = true;
      try {
        const response = await axios.get('/api/group-list');

        const nonLoggedGroups = response.data;

        // 로그인 상태를 가져와 로그인이 안된 상태에는 찜 상태를 모두 false로 지정
        const isLoggedIn = computed(() => store.getters['isLogged/loggedIn']);
        if (!isLoggedIn.value) {
          groups.value = nonLoggedGroups.map(group => ({
            ...group,
            saved: 0
          }));
        } else {
          // 로그인이 된 경우
          groups.value = nonLoggedGroups;
        }
        // 서버로부터 받은 데이터를 groups배열에 저장
        // groups.value = response.data;

        // page.value = 1;
        // updateDisplayedGroups();
        //  처음 몇개만 표시
      } catch (error) {
        console.error("Error", error);
      } finally {
        loading.value = false;
      }
    };

    // 현재 페이지에 보여줄 그룹 업데이트
    const updateDisplayedGroups = () => {
      const start = (page.value - 1) * pageSize;
      const end = start + pageSize;
      displayedGroups.value = groups.value.slice(start, end);

      // 데이터가 더 이상 없으면 isEnd를 true로 설정
      if (displayedGroups.value.length >= groups.value.length) {
        isEnd.value = true;
      }
    };

    const loadMore = () => {
      if (!loading.value && !isEnd.value) {
        loading.value = true;
        page.value++;
        updateDisplayedGroups();
        loading.value = false;
      }
    };

    onBeforeMount(async () => {
      await sportList();
      await groupList();
    });

    // onBeforeMount(() => {
    //   window.removeEventListener('scroll', handleScroll);
    // });

    // onMounted(() => {
    //   window.addEventListener('scroll', handleScroll);
    // });

    // const load = async ($state) => {
    //   if (loading.value) return;
    //   loading.value = true;
    //   try {
    //     const response = await axios.get('/api/group-list', {
    //       params: { page: page.value, perPage: perPage }
    //     });
    //     const json = response.data;

    //     if (json.length < 1) {
    //       $state.complete();    // 더 이상 데이터가 없음을 알림
    //     } else {
    //       groups.value.push(...json);
    //       $state.loaded();      // 로딩 완료 상태로 변경
    //     }
    //     page.value++;
    //   } catch (error) {
    //     console.error('Error', error);
    //     $state.complete();        // 에러 발생 시 로딩 완료 상태로 변경
    //   } finally {
    //     loading.value = false;
    //   }
    // };

    //  운동 종목 요청
    const sportList = async () => {
      try {
        const response = await axios.get("/api/filter/exercises");
        const data = response.data;

        sports.value = data.map(item => ({
          key: item.key,
          sport: item.sport
        }));

      } catch (error) {
        console.error("서버와의 통신 오류", error);
      }
    };

    // 선택된 운동 종목에 따라 모임 리스트를 가져오는 함수
    const SportGroupList = async (event) => {
      const selectedSportValue = event.target.value;
      console.log(`sport${selectedSportValue}`);

      if (selectedSportValue) {
        try {
          const response = await axios.get("/api/filter/exercises/sport", {
            params: {
              sport: selectedSportValue,
            }
          });
          groups.value = response.data;
        } catch (error) {
          console.error("모임 리스트를 가져오는 데 오류가 발생했습니다.", error);
        }
      }
    };

    //  지역 다음 api
    const locationInput = ref("");

    const openDaumApi = () => {
      new window.daum.Postcode({
        oncomplete: (data) => {
          console.log("받은 주소 : ", data)
          locationInput.value = data.sigungu;

          fetchLocationData();
        }
      }).open();
    };

    // 지역 필터 저장 후 서버에 요청
    const fetchLocationData = async () => {
      try {
        console.log(locationInput.value);
        const response = await axios.get("/api/filter/location", {
          params: {
            location: locationInput.value,
          }
        });
        groups.value = response.data;
      } catch (error) {
        console.error("오류가 발생", error)
      }
    };

    //  날짜 필터
    const date = ref(null); //  선택된 날짜

    const dateClicked = async (date) => {
      try {
        // console.log(`Selected ${date}`);
        const isoDate = date.toISOString();

        const response = await axios.get("/api/filter/date", {
          params: {
            date: isoDate,
          }
        });
        groups.value = response.data;
      } catch (error) {
        console.error('모임 리스트를 가져오는 중 오류 발생', error);
        alert("모임 리스트 가져오기 실패");
      }
    };

    // 시간 필터
    const timeString = ref('');
    const time = ref({
      hours: null,
      minutes: null,
    });

    const timeSelected = async () => {
      if (timeString.value) {
        try {
          const response = await axios.get("/api/filter/time", {
            params: {
              time: timeString.value,
            }
          });
          groups.value = response.data;
        } catch (error) {
          console.error("API 요청 오류: ", error);
        }

      } else {
        alert("시간을 다시 선택해 주세요.");
      }
    }

    //  검색어
    const searchGroups = async () => {
      if (!searchTerm.value) {
        return;
      }
      try {
        const response = await axios.get("/api/search", {
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
    const selectedGroupList = async () => {
      try {
        const response = await axios.get("/api/sort", {
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

    const isTooltipVisible = ref(true);

    const toggleTooltip = () => {
      isTooltipVisible.value = !isTooltipVisible.value;
    };

    // 참석 모달 연 후 참석 버튼 누르면 페이지 이동
    // 헤더에 토큰(사용자 식별 키)과 모임 식별 키를 query로 보내줌
    const showConfirmPopup = ref(false);
    const currentGroupId = ref(null);

    const confirmDeletion = async (communityId) => {
      const group = groups.value.find(group => group.communityId === communityId);

      if (!group) {
        console.error("유효하지 않는 모임 입니다.", communityId);
        return;
      }

      try {
        await apiClient.post('/join-group', {
          communityId: communityId,
        });
        router.push({ name: "JoinedGroups" });
        alert("모임 참석 완료")
      } catch (error) {
        if (error.response) {
          if (error.response.status === 400) {
            alert(error.response.data); // 400 에러 처리
          } else if (error.response.status === 401) {
            alert("로그인 후 이용 가능합니다."); // catch 블록에서 401 에러 처리
          } else {
            console.error("Error", error);
          }
        } else {
          console.error("Error", error);
        }
      } finally {
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
        const url = '/home/like-group';

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
        if (error.response) {
          if (error.response.status === 400) {
            alert(error.response.data); // 400 에러 처리
          } else if (error.response.status === 401) {
            alert("로그인 후 이용 가능합니다."); // catch 블록에서 401 에러 처리
          } else {
            console.error("Error", error);
          }
        } else {
          console.error("Error", error);
        }
      }
    }

    return {
      getSportImage,
      carouselGroups,
      loadMore,
      fetchLocationData,
      openDaumApi,
      locationInput,
      likeGroup,
      selectedOption,
      options,
      date,
      dateClicked,

      time,
      timeString,
      timeSelected,

      groups,
      displayedGroups,
      loading,
      // load,
      userId,
      page,
      pageSize,

      showConfirmPopup,
      toggleTooltip,
      confirmDeletion,
      cancelDeletion,
      searchTerm,

      searchGroups,
      selectedGroupList,
      sports,

      SportGroupList,
      sportList,
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
  flex-grow: 1;
}

.scroll-container {
  overflow-y: auto;
  max-height: 80vh;
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
  /* border: 0.1px solid grey; */
  font-size: 14px;
  cursor: pointer;
  padding-right: 10px;
  appearance: none;

}

.input-event {
  width: 204px;
  height: 50px;
  border-radius: 3px;
  text-align: center;
  border: 0.1px solid grey;
}

.input-event::placeholder {
  text-align: center;
  font-size: 14px;
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
  border: 0.1px solid grey;
  border-radius: 3px;
  font-size: 16px;
  cursor: pointer;
}

/* ::v-deep .vue__time-picker input.vue__time-picker-input {
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
} */

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

.modal-description {
  margin-bottom: 50px;
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
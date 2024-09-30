tap<template>
  <main>
    <div class="main-container">
      <div class="contain-topwrap">
        <div class="topwrap-title">
          <textarea v-model="title" placeholder="제목을 입력하세요."></textarea>
        </div>
        <div class="topwrap-box" role="textbox">
          <div class="topbox-content" contenteditable="true" data-placeholder="생성할 모임의 상세설명을 작성해주세요."
            ref="topboxContent" @input="updateTopboxContent">
          </div>
        </div>
        <div class="topwrap-tag">
          <p class="tag-item">{{ formData.sport }}</p>
          <p class="item" id="location">{{ formData.location }}</p>
        </div>
      </div>
      <div class="contain-category">
        <p class="category-sidetext">Choose Category</p>
        <div class="category-list">
          <button class="category-btn" type="button" v-for="category in categories" :key="category"
            :class="{ selected: selectedCategory === category }" @click="selectCategory(category)">
            {{ category }}
          </button>
        </div>

        <div class="category-input">
          <input class="input-event" v-model="sportInput" @keydown.enter="handleEnterKey" @click="setSport" type="text"
            placeholder="운동 종목을 입력하세요." />
        </div>

        <p class="category-text">Choose Location</p>
        <div class="category-input">
          <input class="input-event" v-model="locationInput" @keydown.enter="handleEnterKey" @click="openDaumApi"
            type="text" placeholder="위치를 검색하세요." />
        </div>

        <p class="category-text">Choose Date and Time</p>
        <div class="category-date">
          <VueDatePicker locale="ko" time-picker-inline v-model="date" @change="updateFormData"
            class="input-datepicker">
          </VueDatePicker>
        </div>

        <p class="category-text">Choose Group Size</p>
        <div class="category-input">
          <input class="input-event" v-model="personInput" @keydown.enter="handleEnterKey" @click="setPerson"
            type="text" placeholder="인원을 입력하세요." />
        </div>

        <button class="category-register" @click="registerGroup">등록</button>
      </div>
    </div>
  </main>
</template>

<script>
import { computed, reactive, ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { toRaw } from 'vue';
import { useStore } from 'vuex';

export default {
  name: "AddNewGroup",

  setup() {
    const router = useRouter();
    const store = useStore();

    const userId = computed(() => store.getters.userId);

    const title = ref('');
    const sportInput = ref('');
    const locationInput = ref('');
    const personInput = ref('');
    // 날짜
    const date = ref(null);

    // 운동 종목 카테고리 중 고르기
    const selectedCategory = ref(null);

    // enter가 먹히도록 이벤트 핸들러 추가
    const handleEnterKey = () => {
      setSport();
      setLocation();
      setPerson();
    };

    const formData = reactive({
      title: "",
      topboxContent: "",
      sport: "종목",
      location: "",
      person: "",
      date: "",
      userId: "",
    });
    // formData에 데이터가 잘 들어가는지 콘솔에서 확인
    console.log(toRaw(formData));

    const categories = [
      "러닝",
      "웨이트",
      "라이딩",
      "요가",
      "수영",
      "등산",
      "테니스",
      "클라이밍",
      "필라테스",
      "GX",
    ];

    watch(date, (newDate) => {
      if (newDate) {
        const dateObject = new Date(newDate);
        formData.date = dateObject.toISOString();
      } else {
        formData.date = "";
      }
    });

    // 메서드
    const updateFormData = () => {
      formData.date = date.value ? new Date(date.value).toISOString() : "";
    };

    const updateTopboxContent = () => {
      formData.topboxContent = (document.querySelector('.topbox-content') || '').innerText;
    };

    const selectCategory = (category) => {
      formData.sport = category;
      selectedCategory.value = category;
    };

    const setSport = () => {
      formData.sport = sportInput.value;
    };
    const openDaumApi = () => {
      new window.daum.Postcode({
        oncomplete: (data) => {
          console.log("받은 주소 : ", data)
          this.formData.location = data.sigungu;
        }
      }).open();
    };

    const setLocation = () => {
      formData.location = locationInput.value;
    };

    const handleDateChange = (event) => {
      formData.selectedDate = event.target.value;
    };

    const setPerson = () => {
      formData.person = personInput.value;
    };



    // 사용자 식별 Id값도 formData에 추가해서 서버에 보내줘야 한다.
    // 모임 식별 Id값은 서버에서 만들어서 다시 응답해주는 형식?
    const registerGroup = async () => {
      formData.title = title.value;
      title.value = '';
      formData.userId = userId.value ? userId.value : '';
      try {
        await axios.post('/api/create-group', formData);
        alert("모임이 등록되었습니다.");
        router.push({ name: "Home" });
      } catch (error) {
        console.error("Error", error);
        alert("모임 등록에 실패했습니다.");
      }
    };
    return {
      title,
      sportInput,
      locationInput,
      personInput,
      date,
      openDaumApi,

      formData,
      updateFormData,
      categories,
      updateTopboxContent,
      selectCategory,
      selectedCategory,
      handleEnterKey,
      setSport,
      setLocation,
      handleDateChange,
      setPerson,
      registerGroup,
    };
  }
}
</script>

<style scoped>
/* content 부분 */
main {
  width: 100%;
  height: 700px;
  display: flex;
  justify-content: center;
}

.main-container {
  width: 1200px;
  height: 100%;
  display: grid;
  grid-template-columns: 1fr 360px;
}

.contain-topwrap {
  display: flex;
  /* align-items: center; */
  flex-direction: column;
  margin-top: 20px;
}

.topwrap-title {
  display: flex;
  flex-direction: column;
  width: 700px;
  height: 60px;
  border-radius: 10px;
  margin: 10px 0;
}

.topwrap-title::placeholder {
  font-size: smaller;
}

.topwrap-title textarea {
  border: none;
  font-size: 20px;
  width: 100%;
  height: 40px;
  color: #202020;
  border: none;
  outline: none;
  resize: none;
  overflow: hidden;
}

.topwrap-title::after {
  content: "";
  display: block;
  width: 100%;
  height: 1px;
  align-items: center;
  background-color: #eee5e5;
  margin-top: 5px;
  position: relative;
  bottom: -5px;
  left: 50%;
  transform: translateX(-50%);
}

.topwrap-box {
  display: block;
  width: 715px;
  overflow: hidden;
  cursor: text;
}

.topbox-content {
  width: 100%;
  height: 500px;
  cursor: text;
  border: none;
  overflow-y: scroll;
  position: relative;
}

.topbox-content::before {
  content: attr(data-placeholder);
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
  color: gray;
  padding: 0 5px;
  font-size: 18px;
  /* Placeholder 텍스트의 폰트 크기를 조정합니다 */
  transition: opacity 0.2s ease;
}

.topbox-content:not(:empty)::before {
  opacity: 0;
}

.topbox-content::-webkit-scrollbar {
  display: none;
}

[contenteditable] {
  outline: 0px solid transparent;
  font-size: large;
}

.topwrap-tag {
  display: flex;
  flex-direction: row;
}

.item {
  margin: 5px 10px 5px 10px;
  font-weight: lighter;
  font-size: 13px;
}

.tag-item {
  width: 58px;
  height: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 13px;
  border-radius: 10px;
  background-color: #f2f2f2;
  text-align: center;
}

.contain-category {
  display: flex;
  flex-direction: column;
  position: relative;
}

.contain-category::before {
  content: "";
  position: absolute;
  width: 2px;
  height: 90%;
  top: 0px;
  left: -30px;
  background-color: whitesmoke;
}

.category-sidetext {
  text-align: start;
  margin-top: 30px;
  margin-bottom: 10px;
}

.category-list {
  width: 340px;
  height: 130px;
}

.category-btn {
  width: 70px;
  height: 30px;
  border: 1px solid whitesmoke;
  border-radius: 10px;
  margin: 4px;
  padding: 0;
  cursor: pointer;
}

.category-btn.selected {
  border: 2px solid black;
}

.category-input {
  width: 404px;
  height: 35px;
  display: flex;
  margin: 5px 0 15px 0;
  align-items: center;
}

.category-text {
  margin-bottom: 10px;
}

.input-event {
  width: 303px;
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 0.1875rem;
  margin: 5px 5px 5px 4px;
  padding: 5px 0px 5px 10px;
}

.input-datepicker {
  margin: 0px;
  padding: 0px;
  width: 303px;
  height: 35px;
  border-radius: 10px !important;
  border: none;
}

input::placeholder {
  font-size: small;
}

.input-button {
  width: 60px;
  height: 35px;
  margin-left: 5px;
  padding: 8px 16px;
  border: none;
  background-color: #1a73e8;
  color: white;
  border-radius: 10px;
  font-size: 13.3333px;
  font-weight: bold;
  cursor: pointer;
}

.category-date {
  display: flex;
  margin: 5px 0 15px 5px;
}

.date-btn {
  width: 160px;
  height: 35px;
  font-size: 13.3333px !important;
  border: 1px solid #ccc;
  background-color: #fff;
  border-radius: 10px;
  font-weight: lighter !important;
}

.date-calendar {
  margin-right: 7px;
}

.date-time {
  width: 80px;
  height: 35px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-left: 10px;
  font-size: 13.3333px;
}

.category-register {
  margin-top: 115px;
  width: 307px;
  height: 40px;
  border: none;
  border-radius: 0.1875rem;
  background-color: #1a73e8;
  color: white;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}
</style>

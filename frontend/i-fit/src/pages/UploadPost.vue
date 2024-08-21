<template>
  <main>
    <div class="main-container">
      <div class="main-content">
        <div class="content-changeimages">
          <div class="changeimages-icon" @click="triggerFileInput"></div>
          <span>이미지 변경</span>
          <input type="file" ref="fileInput" @change="onFileChange" accept="images/*" multiple style="display: none;" />
        </div>
        <div class="content-title">
          <textarea v-model="title" placeholder="제목을 입력하세요."></textarea>
        </div>
        <div class="content-box" ref="contentBox" contenteditable="true" @input="onContentInput">
        </div>
        <div class="content-topic">
          <p class="item topic-item">{{ formData.sport }}</p>
          <p class="item" id="location">{{ formData.location }}</p>
        </div>
      </div>
      <div class="main-feature">
        <p class="topic-text">Choose Category</p>
        <div class="feature-topic">
          <div class="topic-category">
            <button class="category-tag" type="button" v-for="category in categories" :key="category"
            :class="{ selected: formData.sport === category }" @click="selectCategory(category)">
            {{ category }}
          </button>
          </div>
        </div>
        <div class="feature-input">
          <input
            class="input-framebox" 
            v-model="sportInput" 
            @keydown.enter="handleEnterKey" 
            type="text" 
            placeholder="운동 종목을 입력하세요." />
          <button class="frame-btn" @click="setSport">확인</button>
        </div>
        <p class="category-text">Choose location</p>
        <div class="feature-input">
          <input 
            class="input-framebox" 
            v-model="locationInput" 
            @keydown.enter="handleEnterKey" 
            type="text" 
            placeholder="위치를 검색하세요." />
          <button class="frame-btn" @click="setLocation">확인</button>
        </div>
        <p class="category-text">Choose group size</p>
        <div class="feature-input">
          <input 
            class="input-framebox" 
            v-model="personInput" 
            @keydown.enter="handleEnterKey" 
            type="text" 
            placeholder="인원을 입력하세요." />
          <button class="frame-btn" @click="setPerson">확인</button>
        </div>
        <button class="feature-modify" @click="confirmSubmit">작성 완료</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from 'axios';
import { toRaw, ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';

export default {
  name: 'UploadPost',
  setup() {
    const router = useRouter();
    const store = useStore();

    // State
    const title = ref('');
    const sportInput = ref('');
    const locationInput = ref('');
    const personInput = ref('');
    const selectedCategory = ref(null);

    const formData = reactive({
      title: '',
      sport: '종목',
      location: '',
      person: '',
      date: '',
      userId: store.getters.userId || '', // Vuex에서 userId 가져오기
    });

    console.log(toRaw(formData));

    const categories = [
      '러닝', '웨이트', 
      '라이딩', '요가', 
      '수영', '등산', 
      '테니스', '클라이밍', 
      '필라테스', 'GX'
    ];

    // Methods
    const triggerFileInput = () => {
      document.querySelector('input[type="file"]').click();
    };

    const onFileChange = (event) => {
      const files = event.target.files;
      const contentBox = document.querySelector('.content-box');
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();
        reader.onload = (e) => {
          const img = document.createElement('img');
          img.src = e.target.result;
          img.style.maxWidth = '100%';
          img.style.display = 'block';
          img.style.margin = '10px 0';
          contentBox.appendChild(img);
          contentBox.appendChild(document.createElement('br'));
        };
        reader.readAsDataURL(file);
      }
    };

    const onContentInput = (event) => {
      formData.content = event.target.innerHTML;
    };

    const selectCategory = (category) => {
      formData.sport = category;
      selectedCategory.value = category;
    };

    const handleEnterKey = () => {
      setSport();
      setLocation();
      setPerson();
    };

    const setSport = () => {
      formData.sport = sportInput.value;
    };

    const setLocation = () => {
      formData.location = locationInput.value;
    };

    const setPerson = () => {
      formData.person = personInput.value;
    };

    const confirmSubmit = () => {
      if (confirm('글 작성을 완료하시겠습니까?')) {
        submitData();
      }
    };

    const submitData = () => {
      const userId = store.getters['isLogged/userId'];

      const data = {
        userId: userId,
        title: formData.title,
        content: formData.content,
        imagess: [], // 이미지를 다룰 로직 추가해야함
        category: formData.sport,
        location: formData.location,
        groupSize: formData.person,
      };

      axios.post('/api/posts/modify', data)
        .then((response) => {
          console.log('Data submitted successfully:', response.data);
          router.push({ name: 'Post', query: { content: encodeURIComponent(formData.content)} });
        })
        .catch((error) => {
          console.error('Error submitting data:', error);
        });
    };

    return {
      title,
      sportInput,
      locationInput,
      personInput,
      selectedCategory,
      formData,
      categories,
      triggerFileInput,
      onFileChange,
      onContentInput,
      selectCategory,
      handleEnterKey,
      setSport,
      setLocation,
      setPerson,
      confirmSubmit,
    };
  }
};
</script>

<style scoped>
main {
  width: 100%;
  height: 700px;
  display: flex;
  justify-content: center;
}

input {
  outline: none;
}

.main-container {
  width: 1200px;
  height: 100%;
  display: grid;
  grid-template-columns: 1fr 360px;
}

.main-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content-changeimages {
  display: flex;
  width: 83%;
  align-items: center;
}

.changeimages-icon {
  width: 35px;
  height: 34px;
  background-color: #f2f2f2;
  background-image: url("@/assets/images/camera.png");
  background-repeat: no-repeat;
  background-size: 80% 80%;
  background-position: center;
  border-radius: 100%;
  margin: 10px 0;
  cursor: pointer;
}

.content-changeimages span {
  color: #00000099;
}

.content-title {
  display: flex;
  flex-direction: column;
  width: 700px;
  height: 60px;
  border-radius: 10px;
  margin: 10px 0;
}

.content-title textarea {
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

.content-title::after {
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

.content-box {
  width: 715px;
  height: 500px;
  overflow: auto;
  cursor: text;
  border: 1px solid #eee;
  border-radius: 10px;
  padding: 10px;
}

.content-topic {
  display: flex;
  flex-direction: row;
  margin-right: 67%;
  margin-top: 50px;
}

.topic-item {
  width: 58px;
  height: 24px;
  font-size: 13px;
  cursor: pointer;
  background: #f2f2f2;
  z-index: 96;
  border-radius: 10px;

  display: flex;
  justify-content: center;
  align-items: center;
}

.item {
  margin: 5px 10px;
  font-weight: lighter;
  font-size: 13px;

  display: flex;
  justify-content: center;
  align-items: center;
}

/* feature */
.main-feature {
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-feature::before {
  content: "";
  position: absolute;
  width: 2px;
  height: 90%;
  top: 0px;
  left: -30px;
  background-color: whitesmoke;
}

.feature-topic {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
}

.topic-text {
  text-align: start;
  margin-top: 85px;
  margin-bottom: 10px;
}

.topic-category {
  width: 340px;
  height: 130px;
}

.category-tag {
  width: 72px;
  height: 30px;
  border: 1px solid whitesmoke;
  border-radius: 10px;
  margin: 4px;
  padding: 0;
}

.category-tag.selected {
  border: 2px solid black;
}

.feature-topic p {
  justify-content: left;
  margin: 10px 15px;
}

.feature-input {
  width: 404px;
  height: 35px;
  display: flex;
  margin: 5px 0px 15px 0;
  align-items: center;
}

.input-framebox {
  width: 245px;
  height: 35px;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin: 5px 5px 5px 0px;
  padding: 5px 0px 5px 10px;
}

.frame-btn {
  width: 60px;
  height: 35px;
  background-color: #1a73e8;
  border-radius: 10px;
  border: none;
  color: white;
  font-size: 13.3333px;
  font-weight: bold;
  padding: 8px 16px;
  margin-left: 5px;
  cursor: pointer;
}

.category-text {
  margin-bottom: 10px;
  text-align: start;
}

.feature-modify {
  margin-top: 115px;
  width: 323px;
  height: 40px;
  border: none;
  border-radius: 10px;
  background-color: #1a73e8;
  color: white;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}
</style>
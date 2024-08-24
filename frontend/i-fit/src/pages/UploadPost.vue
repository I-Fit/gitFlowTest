<template>
  <main>
    <div class="main-container">
      <div class="main-content">
        <div class="content-changeimages">
          <div class="changeimages-icon" @click="triggerFileInput"></div>
          <span>이미지 변경</span>
          <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" multiple style="display: none;" />
        </div>
        <div class="content-title">
          <textarea v-model="formData.title" placeholder="제목을 입력하세요."></textarea>
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
            @click="setSport" 
            type="text" 
            placeholder="운동 종목을 입력하세요." />
        </div>
        <p class="category-text">Choose location</p>
        <div class="feature-input">
          <input 
            class="input-framebox" 
            v-model="locationInput" 
            @keydown.enter="handleEnterKey"
            @click="setLocation"
            type="text" 
            placeholder="위치를 검색하세요." />
        </div>
        <p class="category-text">Choose group size</p>
        <div class="feature-input">
          <input 
            class="input-framebox" 
            v-model="personInput"
            @click="setPerson" 
            @keydown.enter="handleEnterKey" 
            type="text" 
            placeholder="인원을 입력하세요." />
        </div>
        <button class="feature-modify" @click="confirmSubmit">작성 완료</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from 'axios';
import { ref, reactive } from 'vue';
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
    const files = ref([]);

    const formData = reactive({
      title: '',
      sport: '종목',
      location: '',
      person: '',
      content: '',
      userId: store.getters.userId || '', // Vuex에서 userId 가져오기
    });

    console.log(formData);

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
      files.value = event.target.files;
      const contentBox = document.querySelector('.content-box');
      contentBox.innerHTML = ''; // 기존 이미지 제거

      for (let i = 0; i < files.value.length; i++) {
        const file = files.value[i];
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

    const submitData = async () => {
      const userId = store.getters['isLogged/userId'];

      // FormData 객체 생성
      const data = new FormData();
      data.append('userId', userId);
      data.append('title', formData.title);
      data.append('content', formData.content);
      data.append('category', formData.sport);
      data.append('location', formData.location);
      data.append('groupSize', formData.person);

      // 이미지 파일들을 FormData에 추가
      for (let i = 0; i < files.value.length; i++) {
        data.append('files', files.value[i]);
      }

      try {
        // 서버로 FormData 전송
        const response = await axios.post('/api/posts/modify', data, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('Data submitted successfully:', response.data);
        router.push({ name: 'Post', query: { content: encodeURIComponent(formData.content)} });
      } catch (error) {
        console.error('Error submitting data:', error);
      }
    };

    return {
      title,
      sportInput,
      locationInput,
      personInput,
      selectedCategory,
      files,
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
      submitData,
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
  /* white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: break-word; */
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
  cursor: pointer;
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
  height: 40px;
  display: flex;
  margin: 5px 0px 15px 0;
  align-items: center;
}

.input-framebox {
  width: 303px;
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 0.1875rem;
  margin: 5px 5px 5px 4px;
  padding: 5px 0px 5px 10px;
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
  border-radius: 0.1875rem;
  background-color: #1a73e8;
  color: white;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
}
</style>
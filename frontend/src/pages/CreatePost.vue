<template>
  <main>
    <div class="main-container">
      <div class="main-content">
        <div class="content-changeimages">
          <div class="changeimages-icon" @click="triggerFileInput"></div>
          <span>이미지 추가</span>
          <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" style="display: none;" />
        </div>
        <div class="content-title">
          <textarea v-model="formData.title" placeholder="제목을 입력하세요." required></textarea>
        </div>
        <div class="content-box" ref="contentBox" contenteditable="true" @input="onContentInput" placeholder="내용을 입력하세요"></div>
        <div class="content-topic">
          <p class="item topic-item">{{ formData.exercise }}</p>
          <p class="item" id="location">{{ formData.location }}</p>
        </div>
      </div>
      <div class="main-feature">
        <p class="topic-text">Choose Category</p>
        <div class="feature-topic">
          <div class="topic-category">
            <button class="category-tag" type="button" v-for="category in categories" :key="category"
            :class="{ selected: formData.exercise === category }" @click="selectCategory(category)">
            {{ category }}
            </button>
          </div>
        </div>
        <div class="feature-input">
          <input
            class="input-box" 
            v-model="exerciseInput" 
            @keydown.enter="handleEnterKey"
            @click="setExercise" 
            type="text" 
            placeholder="운동 종목을 입력하세요." />
        </div>
        <p class="category-text">Choose location</p>
        <div class="feature-input">
          <input 
            class="input-box" 
            v-model="formData.location" 
            @click="openDaumApi"
            type="text" 
            placeholder="위치를 검색하세요."
            /> 
             <!-- :disabled="!isScriptLoaded" -->
            <!-- <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script> -->
        </div>
        <!-- <p class="category-text">Choose group size</p>
        <div class="feature-input">
          <input 
            class="input-box" 
            v-model="personInput"
            @click="setPerson" 
            @keydown.enter="handleEnterKey" 
            type="text" 
            placeholder="인원을 입력하세요." />
        </div> -->
        <button class="confirm-btn" @click="confirmSubmit">작성 완료</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from 'axios';

export default {
  name: 'CreatePost',
  data() {
    return {
      formData: {
        title: '',
        content: '',
        imageUrl: '',
        exercise: '',
        location: '',
        isScriptLoaded: false,
        userId: null,
      },
      responseMessage: '',
    };
  },

  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    onFileChange(event) {
      if (event.target.files.length > 0) {
        this.formData.image = event.target.files[0];
      }
    },
    onContentInput(event) {
      this.formData.content = event.target.innerText;
    },
    openDaumApi() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          console.log("받은 주소 : ", data)
          this.formData.location = data.sigungu;
        }
      }).open();
    },
    async confirmSubmit() {
      // FormData 객체 생성
      const formData = new FormData();
      formData.append('title', this.formData.title);
      formData.append('content', this.formData.content);
      formData.append('exercise', this.formData.exercise);
      formData.append('location', this.formData.location);
      formData.append('image', this.formData.image);

      try {
        const response = await axios.post('http://localhost:8081/board/new', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        console.log(response.data);
        if (response.data.status === "success") {
          alert('게시글이 성공적으로 등록되었습니다!')
          this.$router.push('/board');  // 게시판 메인으로 이동
        }
        // this.responseMessage = response.data.message; // 성공 메세지
      } catch(error) {
        console.error('게시글 작성 실패: ', error);
        alert('게시글 작성에 실패했습니다.')
        // this.responseMessage = error.response.data.message || 'Failed to create post';  // 실패 메세지
      }
    },
    
  },

  // setup() {
  //   const exerciseInput = ref('');
  //   const selectedCategory = ref(null);

  //   const categories = [
  //     '러닝', '웨이트', 
  //     '라이딩', '요가', 
  //     '수영', '등산', 
  //     '테니스', '클라이밍', 
  //     '필라테스', 'GX'
  //   ];

  //   const selectCategory = (category) => {
  //     formData.sport = category;
  //     selectedCategory.value = category;
  //   };

  //   const setExercise = () => {
  //     formData.exercise = exerciseInput.value;
  //   };

  //   return {
  //     exerciseInput,
  //     selectCategory,
  //     categories,
  //     selectCategory,
  //     setExercise,
  //   }
  // }
  
}
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

.input-box {
  width: 315px;
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

.confirm-btn {
  margin-top: 203px;
  width: 315px;
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
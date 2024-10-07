<template>
  <main>
    <div class="main-container">
      <div class="main-content">
        <div class="content-changeimages">
          <div class="changeimages-icon" @click="triggerFileInput"></div>
          <span>이미지 변경</span>
          <!-- 파일 업로드 input 추가 -->
          <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" multiple style="display: none;" />
        </div>
        <div class="content-title">
          <textarea v-model="formData.title" placeholder="제목을 입력하세요." required></textarea>
        </div>
        <div class="content-box" ref="contentBox" contenteditable="true" @input="onContentInput" placeholder="내용을 입력하세요">
          <div v-html="formData.content"></div>
        </div>
        <div class="content-topic">
          <p class="item topic-item" v-if="formData.exercise">{{ formData.exercise }}</p>
          <p class="item topic-item" id="location" v-if="formData.location">{{ formData.location }}</p>
          <!-- <p class="item">인원</p> -->
        </div>
      </div>
      <div class="main-feature">
        <div class="feature-topic">
          <p class="topic-text">Choose Category</p>
          <div class="topic-category">
            <button class="category-tag" type="button" v-for="category in categories" :key="category"
              :class="{ selected: formData.sport === category }" @click="selectCategory(category)">
              {{ category }}
            </button>
          </div>
        </div>
        <div class="feature-input">
          <input class="input-box" type="text" v-model="exerciseInput" @keydown.enter="setExercise" placeholder="운동명을 입력하세요." />
          <button class="confirm-btn" @click="setExercise">확인</button>
        </div>
        <p class="category-text">Choose Location</p>
        <div class="feature-input">
          <input class="input-box" type="text" v-model="formData.location" @click="openDaumApi" placeholder="위치를 검색하세요." />
          <button class="confirm-btn" @click="setLocation">확인</button>
        </div>
        <!-- <p class="category-text">Choose Group Size</p>
        <div class="feature-input">
          <input type="text" v-model="groupSize" class="input-box" placeholder="인원을 입력하세요." />
          <button class="confirm-btn" @click="confirmGroupSize">확인</button>
        </div> -->
        <button class="feature-modify" @click="confirmSubmit">수정 완료</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from "axios";
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: 'EditPost',
  // data() {
  //   return {
  //     formData: {
  //       title: '',
  //       content: '',
  //       imageStr: null,
  //       exercise: '',
  //       location: '',
  //     },
  //     responseMessage: '',
  //     exerciseInput: '',
  //     categories: ['운동1', '운동2', '운동3'],  // todo
  //   };
  // },
  // methods: {
  //   triggerFileInput() {
  //     this.$refs.fileInput.click();
  //   },

  //   onFileChange(event) {
  //     const file = event.target.files[0];
  //     if (file) {
  //       this.formData.imageStr = file;
        
  //       const reader = new FileReader();
  //       reader.onload = (e) => {
  //         //content-box에 이미지 추가
  //         const img = document.createElement('img');
  //         img.src = e.target.result;
  //         img.style.width = '100%';
  //         img.style.height = 'auto';
  //         img.style.marginBottom = '10px';

  //         const contentBox = this.$refs.contentBox;
  //         contentBox.innerHTML += img.outerHTML;
  //         contentBox.focus();
  //       };
  //       reader.readAsDataURL(file);
  //     }
  //   },

  //   onContentInput(event) {
  //     this.formData.content = event.target.innerText;
  //   },

  //   setExercise() {
  //     this.formData.exercise = this.exerciseInput;
  //     this.exerciseInput = '';
  //   },

  //   openDaumApi() {
  //     new window.daum.Postcode({
  //       oncomplete: (data) => {
  //         console.log("받은 주소: ", data);
  //         this.formData.location = data.sigungu;
  //       }
  //     }).open();
  //   },

  //   async confirmSubmit() {
  //     const formData = new FormData();
  //     formData.append('title', this.formData.title);
  //     formData.append('content', this.formData.content);
  //     formData.append('exercise', this.formData.exercise);
  //     formData.append('location', this.formData.location);

  //     if (this.formData.imageStr) {
  //       formData.append('imageStr', this.formData.imageStr);
  //     }

  //     try {
  //       const response = await axios.put(`http://localhost:8080/api/board/update/${this.postId}`, formData, {
  //         headers: {
  //           'Content-Type': 'multipart/form-data',
  //         },
  //       });

  //       if (response.data.status === "success") {
  //         alert('게시글이 성공적으로 수정되었습니다!');
  //         this.$router.push(`/post/${this.postId}`);  // 수정 후 상세 페이지로 리다이렉션
  //       }
  //     } catch (error) {
  //       console.error('게시글 수정 실패: ', error);
  //       alert('게시글 수정이 실패했습니다.');
  //     }

  //   }
  // },
  
  setup() {
    const route = useRoute();
    const postId = route.params.id;
    const formData = ref({
      title: '',
      content: '',
      imageStr: null,
      exercise: '',
      location: '',
    });

    const fetchPost = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/board/post/${postId}`);

        const post = response.data;
        formData.value.title = post.title;
        formData.value.content = post.content;
        formData.value.exercise = post.exercise;
        formData.value.location = post.location;
        // formData.value.imageStr = post.imageStr;
        
        if (post.imageStr) {
          formData.value.imageStr = post.imageStr;
          const img = `<img src="data:image/png;base64,${formData.value.imageStr}" style="width: 100%; height: auto; margin-bottom: 10px" alt="게시글 이미지" />`;
          formData.value.content = img + post.content;
        } else {
          formData.value.imageStr = null;
          formData.value.content = post.content;
        }
      } catch (error) {
        console.error('게시글 로드 실패: ', error);
      }
    };

    const triggerFileInput = () => {
      document.querySelector('input[type="file"]').click();
    };

    const onFileChange = (event) => {
      const file= event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          formData.value.imageStr = e.target.result;

          const img = `<img src="${e.target.result}" style="width: 100%; height: auto; margin-bottom: 10px;" alt="게시글 이미지" />`;
          formData.value.content = img + formData.value.content;
          // const contentBox = document.querySelector('.content-box');
          // contentBox.innerHTML = '';
          // contentBox.innerHTML += `<img src="${e.target.result}" style="width: 100%; height: auto; margin-bottom: 10px;" />`;
        };
        reader.readAsDataURL(file);
      }
    };

    const onContentInput = (event) => {
      const content = event.target.innerHTML;

      if(formData.value.imageStr) {
        const img = `<img src="${formData.value.imageStr}" alt="게시글 이미지" style="width: 100%; height: auto; margin-bottom: 10px;" />`
        formData.value.content = img + content;
      } else {
        formData.value.content = content;
      }
    };

    const confirmSubmit = async () => {
      const formDataToSend = new FormData();
      formDataToSend.append('title', formData.value.title);
      formDataToSend.append('content', formData.value.content);
      formDataToSend.append('exercise', formData.value.exercise);
      formDataToSend.append('location', formData.value.location);

      if (formData.value.imageStr) {
        formDataToSend.append('imageStr', formData.value.imageStr);
      }

      try {
        const response = await axios.put(`http://localhost:8080/api/board/update/${postId}`, formDataToSend, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });

        if (response.data.status === "success") {
          alert('게시글이 성공적으로 수정되었습니다!');
          this.$router.push('/board');
        }
      } catch (error) {
        console.error('게시글 수정 실패: ', error);
        alert('게시글 수정에 실패했습니다.');
      }
    };

    onMounted(fetchPost);

    return {
      formData,
      triggerFileInput,
      onFileChange,
      onContentInput,
      confirmSubmit,
    };
  }
};
</script>

<style scoped>
main {
  width: 100%;
  height: 800px;
  display: flex;
  justify-content: center;
}

input,
textarea {
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
  align-items: center;
  width: 83%;
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

.contentbox-images img {
  width: 100%;
  height: auto;
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
  margin-top: 30px;
  margin-bottom: 10px;
}

.topic-category {
  width: 340px;
  height: 130px;
}

.category-text {
  margin-bottom: 10px;
  margin-top: 30px;
  text-align: start;
}

.category-tag {
  width: 70px;
  height: 30px;
  border: none;
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
  height: 35px;
  display: flex;
  margin: 5px 0 15px 0;
  align-items: center;
}

.input-box {
  width: 245px;
  height: 35px;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin: 5px;
  padding: 5px 0px 5px 10px;
  opacity: 0.5;
}

.confirm-btn {
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

.feature-modify {
  width: 100%;
  height: 40px;
  background-color: #1a73e8;
  border-radius: 10px;
  border: none;
  margin-top: 20%;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s ease;
  /* 배경색 변화에 대한 트랜지션 추가 */
}

.feature-modify:hover {
  background-color: #005ea1;
  /* 호버 시 배경색 변경 */
}
</style>

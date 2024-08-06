<template>
  <main>
    <div class="main-container">
      <div class="main-content">
        <div class="content-changeimage">
          <div class="changeimage-icon" @click="triggerFileInput"></div>
          <span>이미지 변경</span>
          <!-- 파일 업로드 input 추가 -->
          <input type="file" ref="fileInput" @change="onFileChange" accept="image/*" multiple style="display: none;" />
        </div>
        <div class="content-title">
          <textarea v-model="title" type="text" placeholder="제목을 입력하세요."></textarea>
        </div>
        <div class="content-box" contenteditable="true" @input="onContentInput" ref="contentBox">
          <div class="contentbox-image"></div>
          <img src="@/assets/image/image.png" alt="" />
          <p>공휴일에 멤버들이랑 kosta 짐에서 웨이트 했습니다~</p>
        </div>
        <div class="content-topic">
          <p class="item topic-item">종목</p>
          <p class="item">장소</p>
          <p class="item">인원</p>
        </div>
      </div>
      <div class="main-feature">
        <div class="feature-topic">
          <p class="topic-text">Choose Category</p>
          <div class="topic-category">
            <button class="category-tag" type="button" @click="selectCategory('러닝')">러닝</button>
            <button class="category-tag" type="button" @click="selectCategory('웨이트')">웨이트</button>
            <button class="category-tag" type="button" @click="selectCategory('라이딩')">라이딩</button>
            <button class="category-tag" type="button" @click="selectCategory('요가')">요가</button>
            <button class="category-tag" type="button" @click="selectCategory('수영')">수영</button>
            <button class="category-tag" type="button" @click="selectCategory('등산')">등산</button>
            <button class="category-tag" type="button" @click="selectCategory('테니스')">테니스</button>
            <button class="category-tag" type="button" @click="selectCategory('클라이밍')">클라이밍</button>
            <button class="category-tag" type="button" @click="selectCategory('필라테스')">필라테스</button>
            <button class="category-tag" type="button" @click="selectCategory('GX')">GX</button>
          </div>
        </div>
        <div class="feature-input">
          <input type="text" v-model="activity" class="input-framebox" placeholder="운동명을 입력하세요." />
          <button class="frame-btn" @click="confirmActivity">확인</button>
        </div>
        <p class="category-text">Choose Location</p>
        <div class="feature-input">
          <input type="text" v-model="location" class="input-framebox" placeholder="위치를 검색하세요." />
          <button class="frame-btn" @click="confirmLocation">확인</button>
        </div>
        <p class="category-text">Choose Group Size</p>
        <div class="feature-input">
          <input type="text" v-model="groupSize" class="input-framebox" placeholder="인원을 입력하세요." />
          <button class="frame-btn" @click="confirmGroupSize">확인</button>
        </div>
        <button class="feature-modify" @click="confirmSubmit">수정</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from "axios";

export default {
  name: "PostModify",
  data() {
    return {
      title: "",
      content: "",
      images: [],
      category: "",
      activity: "",
      location: "",
      groupSize: "",
    };
  },
  methods: {
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    onFileChange(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();
        reader.onload = (e) => {
          const img = document.createElement("img");
          img.src = e.target.result;
          img.style.maxWidth = "100%";
          img.style.display = "block";
          img.style.margin = "10px 0";
          const br = document.createElement("div");
          br.innerHTML = "<br>";
          this.$refs.contentBox.appendChild(img);
          this.$refs.contentBox.appendChild(br);
          this.images.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    },
    
    onContentInput(event) {
      this.content = event.target.innerHTML;
    },
    
    selectCategory(category) {
      this.category = category;
    },
    confirmActivity() {
      console.log("Activity confirmed:", this.activity);
    },
    confirmLocation() {
      console.log("Location confirmed:", this.location);
    },
    confirmGroupSize() {
      console.log("Group size confirmed:", this.groupSize);
    },
    confirmSubmit() {
      if (confirm("글 수정을 완료하시겠습니까?")) {
        this.submitData();
      }
    },
    submitData() {
      const data = {
        title: this.title,
        content: this.content,
        images: this.images,
        category: this.category,
        activity: this.activity,
        location: this.location,
        groupSize: this.groupSize,
      };

      axios
        .post("/api/posts/modify", data)
        .then((response) => {
          console.log("Data submitted successfully:", response.data);
          // 리디렉션 처리
          this.$router.push({ name: 'BoardDetail' });
        })
        .catch((error) => {
          console.error("Error submitting data:", error);
          // Handle error
        });
    },
  },
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

.content-changeimage {
  display: flex;
  align-items: center;
  width: 83%;
}

.changeimage-icon {
  width: 35px;
  height: 34px;
  background-color: #f2f2f2;
  background-image: url("@/assets/image/camera.png");
  background-repeat: no-repeat;
  background-size: 80% 80%;
  background-position: center;
  border-radius: 100%;
  margin: 10px 0;
  cursor: pointer;
}

.content-changeimage span {
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

.contentbox-image img {
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
    margin-top : 30px;
    text-align: start;
}

.category-tag {
width: 70px;
height: 30px;
border: none;
border-radius: 10px;
margin: 4px;
padding: 0;
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

.input-framebox {
width: 245px;
height: 35px;
border: 1px solid #ccc;
border-radius: 10px;
margin: 5px;
padding: 5px 0px 5px 10px;
opacity: 0.5;
}

.frame-btn {
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
    transition: background-color 0.3s ease; /* 배경색 변화에 대한 트랜지션 추가 */
}

.feature-modify:hover {
  background-color: #005ea1; /* 호버 시 배경색 변경 */
}
</style> 

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
        <div class="content-box" ref="contentBox" contenteditable="true" @input="onContentInput">
        </div>
        <div class="content-topic">
          <div class="item topic-item"><span>런닝</span></div>
          <div class="item"><span>용산구</span></div>
          <div class="item"><span>소규모</span></div>
        </div>
      </div>
      <div class="main-feature">
        <p class="topic-text">Choose Category</p>
        <div class="feature-topic">
          <div class="topic-category">
            <button class="category-tag" type="submit" name="category">
              러닝
            </button>
            <button class="category-tag" type="submit" name="category">
              웨이트
            </button>
            <button class="category-tag" type="submit" name="category">
              라이딩
            </button>
            <button class="category-tag" type="submit" name="category">
              요가
            </button>
            <button class="category-tag" type="submit" name="category">
              수영
            </button>
            <button class="category-tag" type="submit" name="category">
              등산
            </button>
            <button class="category-tag" type="submit" name="category">
              테니스
            </button>
            <button class="category-tag" type="submit" name="category">
              클라이밍
            </button>
            <button class="category-tag" type="submit" name="category">
              필라테스
            </button>
            <button class="category-tag" type="submit" name="category">
              GX
            </button>
          </div>
        </div>
        <div class="feature-input">
          <input
            type="text"
            class="input-framebox"
            placeholder="운동명을 입력하세요."
          />
          <button class="frame-btn">확인</button>
        </div>
        <p class="category-text">Choose location</p>
        <div class="feature-input">
          <input
            type="text"
            class="input-framebox"
            placeholder="위치를 검색하세요."
          />
          <button class="frame-btn">확인</button>
        </div>
        <p class="category-text">Choose group size</p>
        <div class="feature-input">
          <input
            type="text"
            class="input-framebox"
            placeholder="인원을 입력하세요."
          />
          <button class="frame-btn">확인</button>
        </div>
        <button class="feature-modify" @click="confirmSubmit">작성 완료</button>
      </div>
    </div>
  </main>
</template>

<script>
import axios from "axios";

export default {
  name: "PostUpload",
  data() {
    return {
      title: "",
      content: "",
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
      if (confirm("글 작성을 완료하시겠습니까?")) {
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

.content-changeimage {
  display: flex;
  width: 83%;
  align-items: center;
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

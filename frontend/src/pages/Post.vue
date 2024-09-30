<template>
  <main>
    <div>
      <!-- 사용자 정보 및 옵션 -->
      <div class="user">
        <div class="user-post">
          <div class="post-profile"></div>
          <span class="user-name">{{ userId }}</span>
          <span class="creation-date">4 days ago</span>
        </div>
        <div class="user-option" @click="toggleActions">
          <div class="dot-icon"></div>
          <PostActions
            :visible="showActions"
            @navigate="handleNavigation"
            class="post-actions"
          />
        </div>
      </div>

      <!-- 제목 및 좋아요 버튼 -->
      <div v-if="post" class="content-title">
        <h1>{{ post.title }}</h1>
        <div class="title-heart" @click="toggleHeart">
          <div
            :class="{
              'filled-heart': isHeartFilled,
              'empty-heart': !isHeartFilled,
            }"
          ></div>
        </div>
      </div>

      <!-- 이미지 -->
      <div v-if="post" class="content-images">
        <img :src="`data:image/png;base64,${post.imageStr}`" alt="게시글 이미지" />
      </div>

      <!-- 줄 바꿈이나 다른 html 태그가 그대로 렌더링된다 -->
      <p v-if="post" class="content-box">{{ post.content }}</p>
      <!-- <div class="content-box" v-html="formattedContent">{{ post.con }}</div> -->

      <!-- 태그 -->
      <div v-if="post" class="content-topic">
        <p class="topic-item">라이딩</p>
        <p class="item">부산</p>
        <p class="item">소규모</p>
      </div>

      <div v-else>
        <p>게시글 로드중...</p>
      </div>
    </div>
  </main>
</template>

<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: 'Post',
  setup() {
    const route = useRoute();
    const post = ref(null); // 게시글 데이터 저장할 변수
    
    //게시글 id로 데이터 가져오기
    const fetchPost = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/board/post/${route.params.id}`);
        post.value = response.data;
      } catch (error) {
        console.error('게시글 로드 실패: ', error);
      }
    };

    onMounted(() => {
      fetchPost();
    });

    return {
      post
    };
  },
  // props: ['id'],
  // data() {
  //   return {
  //     post: null
  //   };
  // },
  // created() {
  //   this.fetchPost();
  // },
  // methods: {
  //   async fetchPost() {
  //     try {
  //       const response = await axios.get(`http://localhost:8080/api/board/post/${this.id}`);
  //       this.post = response.data;
  //     } catch (error) {
  //       console.error('게시글 로드 실패: ', error);
  //     }
  //   }
  // },
};
</script>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  align-items: center;
}

input {
  outline: none;
  /* 이 밑으로 추가 수정한 것 */
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border-radius: 4px;
  border: 1px solid #ccc;
  box-sizing: border-box;
}

.main-content {
  width: 600px;
  height: 1000px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  padding: 15px;
  margin-bottom: 200px;
  margin-top: 50px;
}

.user {
  display: flex;
  margin: 10px;
  font-size: 20px;
  justify-content: space-between;
}

.user-name {
  font-size: 20px;
  font-weight: bold;
  margin: auto 10px;
}

.creation-date {
  font-size: 14px;
  color: gray;
  margin: auto 10px;
}

.user-post {
  display: flex;
}

.post-profile {
  width: 50px;
  height: 40px;
  background-image: url("@/assets/images/user_img.png");
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: center;
}

.user-option {
  position: relative; /* Make it a positioned element */
  width: 37px;
  height: 10px;
  margin-right: 4px;
  margin-top: 16px;
  cursor: pointer;
}

.dot-icon {
  background-image: url("@/assets/images/dot.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  width: 100%;
  height: 100%;
  cursor: pointer;
}

.post-actions {
  position: absolute;
  top: -5px; /* Adjust this value as needed */
  right: -50px;
  z-index: 10;
  /* You may need to further style or adjust based on your layout */
}

.content-title {
  display: flex;
  justify-content: space-between;
  margin: 10px;
}

.h1 {
  flex: 1;
}

.content-box {
  white-space: pre-wrap; /*줄 바꿈과 공백 유지*/
}

.title-heart {
  cursor: pointer;
  display: inline-block;
  width: 35px;
  /* 하트의 크기를 조정합니다 */
  height: 35px;
  /* 하트의 크기를 조정합니다 */
  position: relative;
}

.title-heart div {
  width: 100%;
  height: 100%;
  position: absolute;
  top: -4px;
  right: 4px;
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

.content-images {
  width: 570px;
  height: 425px;
  border: 1px solid whitesmoke;
  margin: 10px;
  overflow: hidden;
  position: relative;
}

.content-images img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: center;
  display: block;
}

.content-topic {
  display: flex;
  flex-direction: row;
  margin: 10px;
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
  margin-top: 1px;
}

.item {
  margin: 5px 10px;
  font-weight: lighter;
  font-size: 13px;
}

.content-comment {
  width: 100%;
  margin: 10px;
}

.comment-table {
  width: 100%;
  display: flex;
  flex-direction: column;
  float: left;
}

.table-item {
  display: flex;
  width: 100%;
  margin: 10px auto;
}

.item-user {
  width: 150px;
  font-size: 15px;
  font-weight: bold;
}

.item-text {
  width: 400px;
  font-size: 15px;
  color: #757575;
}

.content-add {
  display: flex;
  width: 580px;
  height: 45px;
  border: 1px solid #ccc;
  border-radius: 5px;
  justify-content: space-between;
  align-items: center;
  position: relative;
  /* 추가했는데 별로면 수정. */
}

.content-add input {
  border: none;
  padding: 5px 0px 5px 10px;
  font-size: 15px;
  opacity: 0.5;
  height: 35px;
  width: 100%;
}

.comment-upload {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  font-size: 24px;
  color: #007bff;
}

.comment-upload:hover {
  background-color: #0056b3;
}

.comment-table .table-item {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  position: relative;
}

.author-comment {
  background-color: #e0f7fa;
  font-weight: bold;
  border-left: 4px solid #00796b;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.reply-button {
  cursor: pointer;
  color: #00796b;
  font-size: 14px;
}

.reply-input {
  margin-top: 10px;
  padding: 10px;
  border-top: 1px solid #ddd;
  display: flex;
  align-items: center;
}

.reply-upload {
  cursor: pointer;
  color: #00796b;
  margin-right: 10px;
}

.reply-input input {
  flex: 1;
}

.reply-item {
  margin-left: 20px;
  margin-top: 5px;
}

.comment-container {
  border-bottom: 1px solid #ddd;
  padding: 10px;
  position: relative;
}

.author-comment {
  background-color: #e0f7fa;
  font-weight: bold;
  border-left: 4px solid #00796b;
}

.reply-item {
  border-left: 3px solid #00796b;
  padding-left: 10px;
  margin-top: 5px;
}

.highlighted {
  border: 2px solid red; /* 강조 스타일 추가 */
  background-color: #fdd; /* 강조 스타일 추가 */
}
</style>

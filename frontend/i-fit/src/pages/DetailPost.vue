<template>
  <main>
    <div>
      <div class="user">
        <div class="user-post">
          <div class="post-profile"></div>
          <span class="user-name">김계란</span>
          <span class="creation-date">4 days ago</span>
        </div>
        <div class="user-option" @click="toggleActions">
          <div class="dot-icon"></div>
          <PostActions :visible="showActions" @navigate="handleNavigation" class="post-actions" />
        </div>
      </div>
      <div class="content-title">
        <h1>멤버들이랑 현충일 번개 운동</h1>
        <div class="title-heart" @click="toggleHeart">
          <div :class="{ 'filled-heart': isHeartFilled, 'empty-heart': !isHeartFilled }"></div>
        </div>
      </div>
      <div class="content-image">
        <img src="@/assets/image/riding-1.png" alt="" />
      </div>
      <div class="content-topic">
        <p class="topic-item">런닝</p>
        <p class="item">용산구</p>
        <p class="item">소규모</p>
      </div>
      <div class="content-comment">
        <div class="comment-table">
          <div class="table-item">
            <div class="item-user">밤편지</div>
            <div class="item-text">대단하십니다.</div>
          </div>
          <div class="table-item">
            <div class="item-user">wimper</div>
            <div class="item-text">며칠 걸리셨나요??</div>
          </div>
          <div class="table-item">
            <div class="item-user">건넛</div>
            <div class="item-text">담에 또 하실건가요? +_+</div>
          </div>
          <div class="table-item">
            <div class="item-user">qwer</div>
            <div class="item-text">담 모임은 어디서하시나요?</div>
          </div>
          <div class="table-item">
            <div class="item-user">한강교차로인간</div>
            <div class="item-text">3일 컷은 해야지~</div>
          </div>
          <div class="table-item">
            <div class="item-user">좋으면짖는개</div>
            <div class="item-text">왈왈 크르르릉 왈왈!!</div>
          </div>
        </div>
      </div>
    </div>
      <div class="content-add">
        <input v-model="comment" placeholder="댓글을 입력해주세요." @keydown="checkEnter" @input="autoResize" ref="input" />
        <span class="comment-upload" @click="submit">&#10550;</span>
      </div>
  </main>
</template>

<script>
import PostActions from "@/components/common/PostActions.vue";

export default {
  name: "DetailPost",
  components: {
    PostActions,
  },
  data() {
    return {
      comment: "",
      isHeartFilled: false,
      showActions: false,
    };
  },
  methods: {
    toggleActions() {
      this.showActions = !this.showActions;
    },
    handleNavigation(action) {
      if (action === "EditPost") {
        this.$router.push("/edit-post"); // 수정 페이지로 이동
      } else if (action === "delete") {
        this.$router.push("/board"); // 메인 게시판으로 이동
      }
    },
    toggleHeart() {
      this.isHeartFilled = !this.isHeartFilled; // 하트 상태 토글
    },
    checkEnter(event) {
      if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault(); // 기본 Enter 동작(줄 바꿈)을 막음
        this.submitComment();   // 댓글 제출
      }
    },
    submitComment() {
      if (this.comment.trim() === "") {
        alert("댓글을 입력해주세요.");
        return;
      }
      // 댓글을 서버로 전송하거나, 다른 로직 처리
      this.comment = ""; // 입력 필드 초기화
    },
  },
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
  background-image: url("@/assets/image/user_img.png");
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
  background-image: url("@/assets/image/dot.png");
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
  content: '\2764';
  /* 빈 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: transparent;
  /* 하트의 내부는 투명하게 */
  -webkit-text-stroke: 1px black;
  /* 하트의 테두리 색상 */
}

.filled-heart::before {
  content: '\2764';
  /* 채워진 하트 문자 */
  font-size: 35px;
  /* 하트의 크기 */
  color: red;
  /* 채워진 하트의 색상 */
  -webkit-text-stroke: none;
  /* 채워진 하트의 테두리 제거 */
}

.content-image {
  width: 570px;
  height: 425px;
  border: 1px solid whitesmoke;
  margin: 10px;
  overflow: hidden;
  position: relative;
}

.content-image img {
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
  margin-top: 1px
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
</style>
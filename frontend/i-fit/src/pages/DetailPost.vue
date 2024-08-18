<template>
  <main>
    <div>
      <!-- 사용자 정보 및 옵션 -->
      <div class="user">
        <div class="user-post">
          <div class="post-profile"></div>
          <span class="user-name">{{ postAuthor }}</span>
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
      <div class="content-title">
        <h1>종주할 때 사진 모아봤습니다.</h1>
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
      <div class="content-image">
        <img src="@/assets/image/riding-1.png" alt="" />
      </div>

      <!-- 주제 -->
      <div class="content-topic">
        <p class="topic-item">라이딩</p>
        <p class="item">부산</p>
        <p class="item">소규모</p>
      </div>

      <!-- 댓글 -->
      <div class="content-comment">
        <div class="comment-table">
          <div
            class="table-item comment-container"
            v-for="(comment, index) in comments"
            :key="index"
          >
            <div :class="{ 'author-comment': comment.user === postAuthor }">
              <div class="comment-header">
                <div class="item-user">{{ comment.user }}</div>
                <div class="reply-button" @click="toggleReplyInput(index)">
                  댓글 달기
                </div>
              </div>
              <div class="item-text">{{ comment.text }}</div>

              <!-- 답글 입력 폼 -->
              <div v-if="replyInputs[index]" class="reply-input">
                <span class="reply-upload" @click="submitReply(index)"
                  >&#10551;</span
                >
                <input
                  v-model="comment.replyText"
                  placeholder="답글을 입력해주세요."
                  @keydown="checkReplyEnter($event, index)"
                />
              </div>

              <!-- 답글 목록 -->
              <div
                v-for="(reply, replyIndex) in comment.replies"
                :key="replyIndex"
                class="reply-item"
              >
                <div class="item-user">{{ reply.user }}</div>
                <div class="item-text">{{ reply.text }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 댓글 추가 -->
    <div class="content-add">
      <input
        v-model="comment"
        placeholder="댓글을 입력해주세요."
        @keydown="checkEnter"
      />
      <span class="comment-upload" @click="submitComment">&#10550;</span>
    </div>
  </main>
</template>

<script>
import { ref } from "vue";
import PostActions from "@/components/common/PostActions.vue";

export default {
  name: "DetailPost",
  components: {
    PostActions,
  },
  setup() {
    const postAuthor = "김계란";
    const comment = ref("");
    const isHeartFilled = ref(false);
    const showActions = ref(false);
    const replyInputs = ref({});
    const comments = ref([
      {
        user: "밤편지",
        text: "대단하십니다.",
        replies: [
          { user: "김계란", text: "감사합니다! 열심히 했어요!!"}
        ],
        replyText: "",
      },
      
      {
        user: "wimper",
        text: "며칠 걸리셨나요??",
        replies: [
        { user: "김계란", text: "5일 걸렸습니다."}
        ],
        replyText: "",
      },
      
      {
        user: "건넛",
        text: "담에 또 하실건가요? +_+",
        replies: [
        { user: "김계란", text: "시원할 때, 다시 해보고 싶네요."}
        ],
        replyText: "",
      },

      {
        user: "qwer",
        text: "저도! 한번 도전해보고 싶어요!! +_+",
        replies: [
        { user: "김계란", text: "일정 넉넉하게 잡으시면 누구든 가능합니다! 도전 ㄱㄱ"}
        ],
        replyText: "",
      },

      {
        user: "김계란",
        text: "댓글 남겨주셔서 감사합니다. 이따가 댓글 달아볼게요.",
        replies: [],
        replyText: "",
      },

      {
        user: "한강교차로인간",
        text: "3일 컷은 했었야지~",
        replies: [
        { user: "김계란", text: "제 능력으로 3일 컷은 무리입니다 ㅋㅋ"}
        ],
        replyText: "",
      },

      {
        user: "좋으면짖는개",
        text: "왈왈 크르르릉 왈왈!!",
        replies: [],
        replyText: "",
      },

      {
        user: "박일두",
        text: "거리 어떻게 배분하셨나요??",
        replies: [
        { user: "김계란", text: "마지막날 200km 달렸고요.. 다른 날들은 100km 정도로.."}
        ],
        replyText: "",
      },

      {
        user: "냉카",
        text: "종주 인증 메달이랑 인증서 신청하셨나요?",
        replies: [
        { user: "김계란", text: "예전에 신청해서 받았어요!"}
        ],
        replyText: "",
      },

      {
        user: "vivamondo",
        text: "나도 하고 싶다.",
        replies: [],
        replyText: "",
      },

      {
        user: "ilcapitano",
        text: "준비했던 물품들 알려주세요~ ^^",
        replies: [],
        replyText: "",
      },

      {
        user: "외데가르드",
        text: "공 차러 나와~",
        replies: [
        { user: "김계란", text: "조만간 같이 공 찹시다 ㅋㅋ"}
        ],
        replyText: "",
      },
    ]);

    const toggleActions = () => {
      showActions.value = !showActions.value;
    };

    const handleNavigation = (action) => {
      if (action === "EditPost") {
        this.$router.push("/edit-post");
      } else if (action === "delete") {
        this.$router.push("/board");
      }
    };

    const toggleHeart = () => {
      isHeartFilled.value = !isHeartFilled.value;
    };

    const checkEnter = (event) => {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        submitComment();
      }
    };

    const submitComment = () => {
      if (comment.value.trim() === "") {
        alert("댓글을 입력해주세요.");
        return;
      }
      comments.value.push({
        user: postAuthor,
        text: comment.value,
        replies: [],
        replyText: "",
      });
      comment.value = "";
    };

    const toggleReplyInput = (index) => {
      // 기존 입력폼 숨기기
      Object.keys(replyInputs.value).forEach((key) => {
        if (parseInt(key) !== index) {
          replyInputs.value[key] = false;
        }
      });
      replyInputs.value[index] = !replyInputs.value[index];
    };

    const checkReplyEnter = (event, index) => {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        submitReply(index);
      }
    };

    const submitReply = (index) => {
      const comment = comments.value[index];
      if (comment.replyText.trim() === "") {
        alert("답글을 입력해주세요.");
        return;
      }
      comment.replies.push({ user: postAuthor, text: comment.replyText });
      comment.replyText = "";
      toggleReplyInput(index);
    };

    return {
      postAuthor,
      comment,
      isHeartFilled,
      showActions,
      replyInputs,
      comments,
      toggleActions,
      handleNavigation,
      toggleHeart,
      checkEnter,
      submitComment,
      toggleReplyInput,
      checkReplyEnter,
      submitReply,
    };
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
</style>

<template>
  <main>
    <div>
      <!-- 사용자 정보 및 옵션 -->
      <div class="user">
        <div class="user-post">
          <!-- <div class="post-profile"> -->
          <img
            :src="profileUrl ? `data:image/png;base64,${profileUrl}` : require('@/assets/images/default-profile.png')"
            alt="작성자 이미지" class="post-profile">
          <!-- </div> -->
          <span class="user-name">{{ username }}</span>
          <span class="creation-date">{{ formattedCreatedAt }}</span>
        </div>
        <div class="user-option" @click="toggleActions">
          <div class="dot-icon"></div>
          <PostActions :visible="showActions" @navigate="handleNavigation" />
        </div>
      </div>

      <!-- 제목 및 좋아요 버튼 -->
      <div v-if="post" class="content-title">
        <h1>{{ post.title }}</h1>
        <!-- <p>isHeartFilled: {{ isHeartFilled }}</p> -->
        <div class="title-heart" @click="toggleHeart(post.postId)">
          <div :class="isHeartFilled ? 'filled-heart' : 'empty-heart'"></div>
        </div>
      </div>

      <!-- 이미지 -->
      <div v-if="post" class="content-images">
        <img :src="`data:image/png;base64,${post.imageStr}`" alt="게시글 이미지" />
      </div>

      <!-- 줄 바꿈이나 다른 html 태그가 그대로 렌더링된다 -->
      <p v-if="post" class="content-box" v-html="post.content"></p>
      <!-- <p v-if="post" class="content-box">{{ post.content }}</p> -->

      <!-- 태그 -->
      <div v-if="post" class="content-topic">
        <p class="topic-item">{{ post.exercise }}</p>
        <p class="item">{{ post.location }}</p>
        <!-- <p class="item">소규모</p> -->
      </div>

      <div v-else>
        <p>게시글 로드중...</p>
      </div>

      <NewComment :postId="Number(postId)" :userId="Number(userId)" @commentAdded="fetchComments" />

      <div class="comments-list">
        <div v-for="comment in comments" :key="comment.commentId" class="comment">
          <div class="comment-content">
            <p class="comment-user">{{ comment.username }}</p>
            <p class="comment-text">{{ comment.content }}</p>
          </div>
          <button class="delete-btn" @click="deleteComment(comment.commentId)">삭제</button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import PostActions from '@/components/common/PostActions.vue';
import axios from 'axios';
import { ref, computed, onBeforeMount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';
import NewComment from './NewComment.vue';
import apiClient from '@/api/apiClient';

export default {
  name: 'Post',
  components: {
    PostActions,
    NewComment,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const post = ref(null); // 게시글 데이터 저장할 변수
    const comments = ref([]);
    const profileUrl = ref('');
    const username = ref('');
    const commentUsername = ref('');
    const userId = ref('')
    const createdAt = ref('');
    const postId = route.params.id;

    const isHeartFilled = ref(false);
    const likesCnt = ref(0);
    const showActions = ref(false);

    // 게시글 데이터 가져오기
    const fetchPost = async () => {
      console.log(`Fetching post with Id: ${postId}`);
      try {
        const response = await apiClient.get(`/board/post/${postId}`);
        console.log('fetched post data: ', response.data);

        post.value = response.data;
        userId.value = response.data.userId;
        createdAt.value = response.data.createdAt;
        isHeartFilled.value = response.data.heartFilled;
        username.value = response.data.username;
        profileUrl.value = response.data.profileUrl;

        post.value.content = cleanContent(post.value.content);
      } catch (error) {
        console.error('게시글 로드 실패: ', error);
      }
    };

    const cleanContent = (content) => {
      return content.replace(/<img[^>]*>/g, '');
    };

    const fetchComments = async () => {
      console.log('Fetching comments with postId: ', postId);
      try {
        const response = await axios.get(`http://localhost:8080/api/comments/post/${postId}`);
        comments.value = response.data;
        console.log('fetched comments: ', comments.value);
      } catch (error) {
        console.error('댓글 로드 실패: ', error);
      }
    };

    const deleteComment = async (commentId) => {
      console.log('deleting comment with id: ', commentId);
      if (!commentId || typeof commentId !== 'number') {
        alert('유효하지 않은 댓글 id');
        return;
      }

      if (confirm('정말 댓글을 삭제하시겠습니까?')) {
        try {
          await apiClient.delete(`/comments/delete/${commentId}`);
          alert('댓글 삭제 완료!');
          await fetchComments();
        } catch (error) {
          console.error('댓글 삭제 실패: ', error);
          alert('댓글 삭제 실패!' + (error.response.data.message || ''));
        }
      }
    };

    const formattedCreatedAt = computed(() => {
      if (createdAt.value) {
        const date = new Date(createdAt.value);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      return '';
    });

    const toggleHeart = async (postId) => {
      console.log("postId: ", postId);
      console.log("초기 좋아요 수: ", likesCnt.value);
      console.log("초기 하트 상태: ", isHeartFilled.value);

      try {
        const method = isHeartFilled.value ? 'DELETE' : 'POST';
        const url = '/board/like';

        const response = await apiClient({
          method: method,
          url: url,
          data: {
            postId,
            // isHeartFilled: !isHeartFilled.value,
          },
        });

        if (response.status === 200) {
          isHeartFilled.value = !isHeartFilled.value;
          console.log()
          // isHeartFilled.value = response.data.isHeartFilled;
          await fetchPost();
        }

        console.log(response.data);
        console.log(response.data.heartFilled);
      } catch (error) {
        console.error('좋아요 토글 실패: ', error);
      }
    };

    const toggleActions = () => {
      showActions.value = !showActions.value;
    };

    const handleNavigation = async (action) => {
      const loggedUserId = computed(() => store.getters['isLogged/userId']);
      const postUserId = userId.value;

      if (action === 'EditPost') {
        if (loggedUserId.value !== postUserId) {
          alert("수정 권한이 없습니다.");
          return;
        }
        showActions.value = false;
        router.push({ path: `/edit-post/${postId}` });
      } else if (action === 'delete') {
        if (loggedUserId.value !== postUserId) {
          alert("삭제 권한이 없습니다.");
          return;
        }
        if (confirm('정말 삭제하시겠습니까?')) {
          await deletePost();
        }
      }
    };

    const deletePost = async () => {
      try {
        await apiClient.delete(`/board/delete`, { params: { postId } });
        alert('게시글 삭제 완료!');
        router.push(`/board`);
      } catch (error) {
        console.error('게시물 삭제 실패: ', error);
      }
    };

    onBeforeMount(async () => {
      await fetchPost();
      await fetchComments();
    });

    return {
      profileUrl,
      postId,
      userId,
      comments,
      formattedCreatedAt,
      isHeartFilled,
      likesCnt,
      post,
      username,
      commentUsername,
      toggleHeart,
      showActions,
      toggleActions,
      handleNavigation,
      fetchComments,
      deleteComment,
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
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: center;
}

.user-option {
  position: relative;
  /* Make it a positioned element */
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
  top: -10%;
  left: 123%;
  transform: translateX(-100%);
  z-index: 1000;
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
  white-space: pre-wrap;
  /*줄 바꿈과 공백 유지*/
  margin-bottom: 30px;
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
  margin-top: 10px;
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

/* 댓글 */
.comments-list {
  display: flex;
  flex-direction: column;
}

.comment {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  margin-top: 10px;
}

.comment-text {
  font-size: 13px;
  margin-bottom: 5px;
  margin-top: 5px;
}

.comment-user {
  font-size: 15px;
  font-weight: bold;
  margin-right: 10px;
  display: inline;
}

.delete-btn {
  width: 55px;
  height: 30px;
  margin-left: 10px;
  /* 댓글 내용과 버튼 사이에 약간의 간격 추가 */
  /* background-color: #1a73e8; */
  /* 버튼 색상 (예시) */
  /* color: white; */
  /* 글자 색상 */
  border: none;
  /* 기본 테두리 제거 */
  border-radius: 5px;
  /* 모서리 둥글게 */
  cursor: pointer;
  /* 커서 변경 */
  padding: 5px 10px;
  /* 버튼 안쪽 여백 */
}

.delete-btn:hover {
  background-color: #d32f2f;
  /* 호버 시 색상 변경 */
}
</style>

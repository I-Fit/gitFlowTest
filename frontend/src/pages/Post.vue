<template>
  <main>
    <div>
      <!-- 사용자 정보 및 옵션 -->
      <div class="user">
        <div class="user-post">
          <div class="post-profile"></div>
          <span class="user-name">{{ userId }}</span>
          <span class="creation-date">{{ formattedCreatedAt }}</span>
        </div>
        <div class="user-option" @click="toggleActions">
          <div class="dot-icon"></div>
          <PostActions 
          :visible="showActions"
          @navigate="handleNavigation"
        />
        </div>
      </div>

      <!-- 제목 및 좋아요 버튼 -->
      <div v-if="post" class="content-title">
        <h1>{{ post.title }}</h1>
        <div class="title-heart" @click="toggleHeart(post.id)">
          <!-- <div :class="isHeartFilled ? 'filled-heart' : 'empty-heart'"></div> -->
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
            <p>{{ comment.content }}</p>
          </div>
          <button @click="deleteComment(comment.commentId)">삭제</button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import PostActions from '@/components/common/PostActions.vue';
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import NewComment from './NewComment.vue';

export default {
  name: 'Post',
  components: {
    PostActions,
    NewComment,
  },
  
  setup() {
    const route = useRoute();
    const router = useRouter();

    const post = ref(null); // 게시글 데이터 저장할 변수
    const comments = ref([]);

    const userId = ref('')
    const createdAt = ref('');
    const postId = route.params.id;
    // const postId = Number(route.params.id);

    const isHeartFilled = ref(false);
    const showActions = ref(false);
    
    // 게시글 데이터 가져오기
    const fetchPost = async () => {
      console.log(`Fetching post with Id: ${postId}`);
      try {
        const response = await axios.get(`http://localhost:8080/api/board/post/${postId}`);
        post.value = response.data;
        userId.value = response.data.userId;
        createdAt.value = response.data.createdAt;
        isHeartFilled.value = response.data.isHeartFilled;

        post.value.content = cleanContent(post.value.content);
      } catch (error) {
        console.error('게시글 로드 실패: ', error);
      }
    };

    const cleanContent = (content) => {
      return content.replace(/<img[^>]*>/g, '');
    }

    const fetchComments = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/comments/on/${postId}`);
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
          await axios.delete(`http://localhost:8080/api/comments/delete/${commentId}`);
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

    const toggleHeart = async () => {
      try {
        const response = await axios.post(`http://localhost:8080/api/board/${postId}/like`);

        if (response.status === 200) {
          isHeartFilled.value = !isHeartFilled.value;
          // isHeartFilled.value = response.data.isHeartFilled;
          post.value = response.data;
        }
      } catch (error) {
        console.error('좋아요 토글 실패: ', error);
      }
    };

    const toggleActions = () => {
      showActions.value = !showActions.value;
    };

    const handleNavigation = async (action) => {
      if (action === 'EditPost') {
        showActions.value = false;
        router.push({ path: `/edit-post/${postId}` });
      } else if (action === 'delete') {
        if (confirm('정말 삭제하시겠습니까?')) {
          await deletePost();
        }
      }
    };

    const deletePost = async () => {
      try {
        await axios.delete(`http://localhost:8080/api/board/post/${postId}`);
        alert('게시글 삭제 완료!');
        router.push(`/board`);
      } catch(error) {
        console.error('게시물 삭제 실패: ', error);
      }
    };

    onMounted(async () => {
      await fetchPost();
      await fetchComments();
    });

    return {
      postId,
      post,
      userId,
      comments,
      formattedCreatedAt,
      isHeartFilled,
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
</style>

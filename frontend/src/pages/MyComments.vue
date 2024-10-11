<template>
  <main>
    <AppNav />
    <div class="comment">
      <div class="comment-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="myPosts">내가 쓴 게시글</p>
        <p class="line text02" @click="myComments">내가 쓴 댓글</p>
        <p class="text03" @click="likedPosts">좋아요 한 게시물</p>
      </div>
      <div class="comment-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." v-model="searchKeyword" @keydown.enter="performSearch" />
            <div class="search-icon" @click="performSearch"></div>
          </div>
          <select title="정렬" class="middle-filter-sort">
            <option value="" selected="selected" disabled="disabled">
              정렬
            </option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>

        <div class="comments-list">
          <div v-if="comments.length === 0" class="no-comments">
            <p>작성한 댓글이 없습니다.</p>
          </div>
          <ul>
            <li v-for="comment in comments" :key="comment.commentId" class="comment-item">
              <div class="comment-content">
                <p>{{ comment.postId }}</p>
                <p>{{ comment.content }}</p>
                <p class="comment-date">{{ formattedCreatedAt }}</p>
              </div>
              <!-- 댓글 수정 삭제 메뉴 추가-->
              <button @click="deleteComment(comment.commentId)">삭제</button>
            </li>
          </ul>
        </div>


        <!-- <div class="comment-bottom-table">
          // 댓글 반복 렌더링
          <div
            class="bottom-table"
            v-for="(comment, index) in paginatedComments"
            :key="index"
            @click="navigateToPost(comment.postId, comment.id)"
          >
            <div class="user-info">
              <img
                class="user-info-img"
                src="@/assets/images/user_img.png"
                alt="User Image"
              />
              <p class="user-info-name">{{ comment.userName }}</p>
            </div>
            <div class="user-comment">
              <p class="post-title">게시글 : {{ comment.postTitle }}</p>
              <p class="comment-content">{{ comment.content }}</p>
              <p class="creation-date">{{ comment.creationDate }}</p>
            </div>
            <div class="relative-container">
              <img
                class="modify-icon"
                src="@/assets/images/dot.png"
                alt="dot"
                @click.stop="toggleActions(index)"
              />
              <PostActions
                :visible="comment.showActions"
                @navigate="handleNavigation"
                class="post-actions"
              />
            </div>
          </div>
        </div> -->


      </div>
    </div>
  </main>
</template>


<script>
import { ref, onBeforeMount, computed } from 'vue';
import AppNav from "@/components/layout/AppNav.vue";
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import apiClient from '@/api/apiClient';
// import PostActions from "@/components/common/PostActions.vue";

export default {
  components: {
    AppNav,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const postId = route.params.postId;
    const userId = route.params.userId;
    const commentId = route.params.commentId;
    const visibleDatas = ref([]);
    const comments = ref([]);
    const searchKeyword = ref('');
    const createdAt = ref('');

    const myPosts = () => {
      router.push({ name: "MyPosts" });
    };

    const myComments = () => {
      router.push({ name: "MyComments" });
    };

    const likedPosts = () => {
      router.push({ name: "LikedPosts" });
    };

    // const fetchComments = async () => {
    //   try {
    //     // const response = await fetch(`/comments/user/${userId}`); // 실제 API 경로로 수정
    //     const response = await apiClient.get('/comments/by');
    //     if (!response.ok) {
    //       throw new Error('Network response was not ok');
    //     }
    //     comments.value = await response.json();
    //     console.log('fetching comments: ', comments.value);
    //   } catch (error) {
    //     console.error('댓글 불러오기 실패: ', error);
    //   }
    // };

    const fetchComments = async () => {
      try {
        const response = await apiClient.get('/comments/by');
        if (response.status === 200) {
          comments.value = response.data;
        } else {
          console.error('댓글 로드 실패: ', response.data);
        }
      } catch (error) {
        if (error.response) {
          console.error('내가 쓴 댓글 로드 실패: ', error.response.status);
        } else {
          console.error('내가 쓴 댓글 로드 실패: ', error.message);
        }
      }
    };

    // viewPost -> viewComment로 변경?
    const viewPost = (post) => {
      console.log('Navigating to post: ', post);
      router.push({ path: `/post/${post.postId}` });
    }

    const performSearch = async () => {
      if (!searchKeyword.value) {
        // alert('검색어를 입력하세요!');
        await fetchComments();
        return;
      }

      try {
        const response = await axios.get(`/api/board/search`, {
          params: { keyword: searchKeyword.value },
        });

        console.log('검색 결과: ', response.data);

        if (response.data.length > 0) {
          visibleDatas.value = response.data;
        } else {
          alert('검색 결과가 없습니다.');
          visibleDatas.value = [];
        }
      } catch (error) {
        console.log('검색 실패: ', error);
        alert('검색 실패!');
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
          await axios.delete(`/api/comments/delete/${commentId}`);
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

    onBeforeMount(() => {
      fetchComments();
    });

    return {
      postId,
      commentId,
      userId,
      fetchComments,
      viewPost,
      performSearch,
      deleteComment,
      myPosts,
      myComments,
      likedPosts,
      formattedCreatedAt,
      comments,
      searchKeyword,
    }
  },
}

</script>

<style scoped>
main {
  width: 100%;
  height: 1200px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

.comment {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 60px 150px 1fr;
  margin-top: 60px;
}

.comment-null-block {
  width: 100%;
  height: 100%;
}

.comment-top {
  position: relative;
  width: 100%;
  height: 100%;
}

.comment-top::after {
  content: "";
  display: block;
  width: 100%;
  height: 2px;
  background-color: #ccc;
}

h2 {
  font-size: 50px;
  font-weight: bold;
  color: #5d5a88;
  margin: 0;
  margin-left: 1007px;
}

.line::after {
  content: "|";
  color: #ccc;
  margin: 0 5px 0 5px;
  font-weight: lighter;
}

.text01,
.text02,
.text03 {
  font-size: 24px;
  font-weight: bolder;
  margin-top: 28px;
  margin-bottom: 20px;
  display: inline-block;
  vertical-align: middle;
  cursor: pointer;
}

.text01,
.text03 {
  font-weight: lighter;
}

.comment-middle {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: 95px 1fr;
}

.middle-filter {
  display: flex;
  align-items: center;
  padding: 10px 5px;
}

.middle-filter-search-box {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 5px;
  width: 250px;
  margin-left: 906px;
  margin-top: 180px;
}

.search-box-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
  width: 100%;
}

.search-box-icon {
  width: 15px;
  height: 15px;
  position: absolute;
  margin-left: 218px;
  cursor: pointer;
}

.middle-filter-sort {
  width: 75px;
  height: 37px;
  font-size: 14px;
  margin-left: 30px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff !important;
  text-align: center;
  font-weight: lighter;
  margin-top: 180px;
}

.comments-list {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.comments-list h3 {
  margin-bottom: 10px;
  font-size: 20px;
}

.no-comments {
  text-align: center;
  color: #888;
}

.comment-item {
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}

.comment-item:last-child {
  border-bottom: none; /* 마지막 댓글의 경계선 제거 */
}

.comment-content {
  margin-bottom: 5px;
}

.comment-content p {
  margin: 5px 0;
}

.comment-date {
  font-size: 12px;
  color: #888;
}

/* 삭제 버튼 스타일 */
.comment-item button {
  background-color: #ff4d4d;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
}

.comment-item button:hover {
  background-color: #e60000;
}

/* .comment-bottom-table {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
  margin-top: 90px;
}

.bottom-table {
  display: grid;
  grid-template-columns: 60px 1fr 40px;
  align-items: center;
  padding: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-info-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-bottom: 5px;
}

.user-info-name {
  font-size: 16px;
  font-weight: bold;
}

.user-comment {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #333;
  line-height: 1.5;
  gap: 10px;
}

.post-title {
  font-weight: bold;
  color: #555;
  margin-right: 100px;
  margin-left: 50px;
  white-space: nowrap;
}

.comment-content {
  flex: 1;
  /* margin-right: px;
  /* text-align: center;
  color: #333;
}

.creation-date {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  margin-right: 50px;
}

.relative-container {
  position: relative;
  display: inline-block;
}

.modify-icon {
  width: 20px;
  height: 20px;
  cursor: pointer;
}

.post-actions {
  position: absolute;
  top: 5px;
  left: 20px;
  z-index: 10;
} */
</style>
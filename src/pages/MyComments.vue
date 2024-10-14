<template>
  <main>
    <AppNav />
    <div class="comment-manage">
      <div class="comment-null-block"></div>
      <div class="comment-top">
        <h2>게시글 관리</h2>
        <div class="sub-menu">
          <p class="line text01" @click="myPosts">내가 쓴 게시글</p>
          <p class="line text02" @click="myComments">내가 쓴 댓글</p>
          <p class="text03" @click="likedPosts">좋아요 한 게시글</p>
        </div>
      </div>

      <div class="comment-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" id="search_input" placeholder="검색어를 입력하세요." class="search-box-input"
              v-model="searchKeyword" @keydown.enter="performSearch" />
            <img src="@/assets/images/search.icon.png" alt="search" class="search-box-icon" @click="performSearch" />
          </div>

          <select title="정렬" class="middle-filter-sort" v-model="selectedSort" @change="sortPosts">
            <option value="" selected disabled>정렬</option>
            <option v-for="option in options" :key="option.value" :value="option.value">
              {{ option.text }}
            </option>
          </select>
        </div>
        <div class="comment">
          <div class="comments-list">
            <div v-if="comments.length === 0" class="no-comments">
              <p>작성한 댓글이 없습니다.</p>
            </div>
            <div class="comment-container">
              <ul>
                <li v-for="(comment, index) in comments" :key="comment.commentId" class="comment-item" @click="viewPost(comment.postId)">
                  <div class="comment-content">
                    <p>{{ index + 1 }} &nbsp; {{ comment.postTitle }}</p>
                    <p>{{ comment.content }}</p>
                    <p class="comment-date">{{ formatDate(comment.createdAt) }}</p>
                  </div>
                  <button @click.stop="deleteComment(comment.commentId)">삭제</button>
                </li>
              </ul>
            </div>
            
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, onBeforeMount, computed } from 'vue';
import AppNav from "@/components/layout/AppNav.vue";
import { useRoute, useRouter } from 'vue-router';
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
    const comments = ref([]);
    const searchKeyword = ref('');
    const createdAt = ref('');
    const selectedSort = ref('');

    const options = ref([
      // { value: '', text: '정렬' },
      { value: 'popularity', text: '인기순' },
      { value: 'latest', text: '최신순' },
      { value: 'oldest', text: '오래된순' },
    ])

    const myPosts = () => {
      router.push({ name: "MyPosts" });
    };

    const myComments = () => {
      router.push({ name: "MyComments" });
    };

    const likedPosts = () => {
      router.push({ name: "LikedPosts" });
    };

    const fetchComments = async () => {
      try {
        const response = await apiClient.get('/comments/by');
        if (response.status === 200) {
          comments.value = response.data;
          console.log(comments);
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

    // const viewPost = (post) => {
    //   console.log('Navigating to post: ', post);
    //   router.push({ path: `/post/${post.postId}` });
    // }

    const viewPost = (postId) => {
      if (postId) {
        router.push({ path: `/post/${postId}`});
      }
    };

    const performSearch = async () => {
      if (!searchKeyword.value) {
        // alert('검색어를 입력하세요!');
        await fetchComments();
        return;
      }

      try {
        const response = await apiClient.get(`/comments/search`, {
          params: { keyword: searchKeyword.value },
        });

        console.log('검색 결과: ', response.data);

        if (response.data.length > 0) {
          comments.value = response.data;
        } else {
          alert('검색 결과가 없습니다.');
          comments.value = [];
        }
      } catch (error) {
        console.error('검색 실패: ', error);
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
          await apiClient.delete(`/comments/delete/${commentId}`);
          alert('댓글 삭제 완료!');

          await fetchComments();
        } catch (error) {
          console.error('댓글 삭제 실패: ', error);
          alert('댓글 삭제 실패!' + (error.response.data.message || ''));
        }
      }
    };

    const formatDate = (dateString) => {
      if (!dateString) {
        return '';
      }

      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');

      return `${year}-${month}-${day} ${hours}:${minutes}`;
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
      selectedSort,
      options,
      formatDate,
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

.comment-manage {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 60px 150px 1fr;
  margin-left: 100px;
}

.comment {
  width: 100%;
  height: 100%;
  margin-left: 50px;
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
  width: 90%;
  height: 2px;
  background-color: #ccc;
  margin-left: 50px;
}

h2 {
  font-size: 50px;
  font-weight: bold;
  color: #5d5a88;
  margin: 0;
  margin-left: 920px;
}

.sub-menu {
  margin-left: 50px;
}

.line::after {
  content: "|";
  color: #ccc;
  margin: 0 5px 0 5px;
  /* font-weight: lighter; */
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
  margin-left: -70px;
}

.middle-filter-search-box {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 5px;
  width: 250px;
  margin-left: 906px;
  position: relative;
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
  right: 10px;
  /* margin-left: 45px;
  text-align: end; */
  cursor: pointer;
}

.middle-filter-sort {
  width: 75px;
  height: 37px;
  font-size: 13.3333px;
  margin-left: 30px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #fff !important;
  text-align: center;
  font-weight: 400;
}


/* 댓글 목록 */
.comments-list {
  width: 90%;
  background-color: #fff;
  border-radius: 8px;
  padding-left: 10px;
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  cursor: pointer;
}

.comment-item:last-child {
  border-bottom: none; /* 마지막 댓글의 경계선 제거 */
}

.comment-content {
  margin-bottom: 5px;
  /* margin-left: -15px; */
}

.comment-content p {
  margin: 10px 0;
}

.comment-content p:first-child {
  font-weight: bold;
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
  background-color: #ff9999;
}
</style>
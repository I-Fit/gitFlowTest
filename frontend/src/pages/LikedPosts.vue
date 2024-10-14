<template>
  <main>
    <AppNav />
    <div class="post-manage">
      <div class="post-null-block"></div>
      <div class="post-top">
        <h2>게시글 관리</h2>
        <div class="sub-menu">
          <p class="line text01" @click="myPosts">내가 쓴 게시글</p>
          <p class="line text02" @click="myComments">내가 쓴 댓글</p>
          <p class="text03" @click="likedPosts">좋아요 한 게시글</p>
        </div>
      </div>

      <div class="post-middle">
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

        <!-- 게시글 목록 -->
        <div class="post">
          <div v-for="post in visibleDatas" :key="post.postId" class="post-container">
            <div class="unicode-box">
              <img class="post-edit-icon" src="@/assets/images/edit-icon.png" @click="editPost" />
              <img class="post-delete-icon" src="@/assets/images/delete-icon.png" @click="deletePost" />
            </div>

            <div class="post-image-box">
              <img class="post-image" :src="post.imageStr ? `data:image/png;base64,${post.imageStr}` : ''" alt="게시글 이미지" @click="viewPost(post)" />
            </div>

            <div class="post-data">
              <div class="user-info-and-exercise">
                <p class="exercise-text">{{ post.exercise }}</p>
                <div class="table-group-btn">
                <div class="likes">
                  <div class="title-heart" @click="toggleHeart(post.id)">
                    <div :class="{
                      'filled-heart': !post.isHeartFilled,
                      'empty-heart': post.isHeartFilled,
                    }"></div>
                  </div>
                  <span id="heart-count">{{ post.likesCnt }}</span>
                </div>
                <div class="comment">
                  <img class="btn-icon" src="@/assets/images/comment-icon5.png" alt="댓글 아이콘" />
                  <span id="comment-count">{{ post.commentsCnt }}</span>
                </div>
              </div>
            </div>
            <div class="post-text" @click="viewPost(post)">
              <p class="post-title">{{ post.title }}</p>
              <p class="post-content">{{ post.contentWithoutImage }}</p>
            </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, onBeforeMount, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import AppNav from "@/components/layout/AppNav.vue";
import apiClient from '@/api/apiClient';

export default {
  name: "LikedPosts",
  components: {
    AppNav,
  },

  setup() {
    const route = useRoute();
    const router = useRouter();

    const postId = route.params.postId;
    const userId = route.params.userId;

    const visibleDatas = ref([]);
    const selectedSort = ref('');
    const sortOrder = ref('');
    const searchKeyword = ref('');


    const showActions = ref(false);
    const isHeartFilled = ref(false);
    const likesCnt = ref(0);

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

    // 상세
    const viewPost = (post) => {
      console.log('Navigating to post: ', post);
      router.push({ path: `/post/${post.postId}` });
    }

    // 게시글 불러오기
    const fetchPosts = async () => {
      try {
        const response = await apiClient.get(`/board/posts/liked`);

        // if (!response.ok) {
        //   throw new Error('Network response was not ok');
        // }
        const data = response.data;
        visibleDatas.value = data;
        console.log('fetching posts: ', data);
        sortPosts();
      } catch (error) {
        console.error('게시글 불러오기 실패: ', error);
      }
    };

    const formatDate = (dateString) => {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    };

    const formattedPosts = computed(() => {
      return visibleDatas.value.map(post => ({
        ...post,
        formattedCreatedAt: formatDate(post.createdAt),
        contentWithoutImage: post.content.replace(/<img[^>]*>/g, '').replace(/<[^>]+>/g, ''),
        username: post.username,
        isHeartFilled: post.heartFilled,
      }));
    });

    // 검색
    const performSearch = async () => {
      if (!searchKeyword.value) {
        // alert('검색어를 입력하세요!');
        await fetchPosts();
        return;
      }

      try {
        const response = await apiClient.get(`http://localhost:8080/api/board/posts/liked/search`, {
          params: { keyword: searchKeyword.value },
        });

        console.log('검색 요청: ', searchKeyword.value);
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

    // 정렬
    const sortPosts = () => {
      if (!visibleDatas.value.length) return; // 데이터가 없으면 정렬하지 않음

      let sorted = [...visibleDatas.value]; // 원본 배열을 복사하여 정렬 작업을 수행

      if (selectedSort.value === 'popularity') {
        sorted.sort((a, b) => b.likesCnt - a.likesCnt);
      } else if (selectedSort.value === 'latest') {
        sorted.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      } else if (selectedSort.value === 'oldest') {
        sorted.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
      }

      visibleDatas.value = sorted; // 정렬된 데이터를 다시 저장
    };

    // 좋아요
    const toggleHeart = async (postId) => {
      console.log("postId: ", postId);

      try {
        const response = await axios.post(`/api/board/${postId}/like`);
        
        if (response.status === 200) {
          const { likesCnt: newLikesCnt, heartFilled:newIsHeartFilled } = response.data;
          
          // console.log("좋아요수 : ", newLikesCnt);
          // console.log("isheartfilled (서버 응답): ", newIsHeartFilled);

          likesCnt.value = newLikesCnt;
          isHeartFilled.value = newIsHeartFilled;
        }
        // console.log("server response", response.data);
      } catch (error) {
        console.error('좋아요 토글 실패: ', error);
      }
    };

    // 수정/삭제 메뉴
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

    const editPost = (post) => {
      router.push({ path: `/edit-post/${post.postId}` });
    }

    const deletePost = async () => {
      try {
        await apiClient.delete(`/board/delete/${postId}`);
        alert('게시글 삭제 완료!');
        fetchPosts();
      } catch(error) {
        console.error('게시물 삭제 실패: ', error);
      }
    };

    onBeforeMount(() => {
      fetchPosts();
    });

    return {
      myPosts,
      isHeartFilled,
      toggleHeart,
      visibleDatas,
      formattedPosts,
      sortOrder,
      selectedSort,
      searchKeyword,
      showActions,
      toggleActions,
      handleNavigation,
      myComments,
      sortPosts,
      options,
      performSearch,
      viewPost,
      userId,
      editPost,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 1200px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

.post-manage {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 60px 150px 1fr;
  margin-left: 100px;
}

.post {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
  margin-left: 50px;
}

.post-container {
  display: grid;
  grid-template-rows: 40px 250px 1fr;
  width: 240px;
  height: 450px;
  margin-right: 50px;
}

.post>.post-container:nth-of-type(4n) {
  margin-right: 0px;
}

.unicode-box {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin: 5px 3px 0px 4px;
}

.post-edit-icon,
.post-delete-icon {
  width: 20px;
  height: 20px;
  cursor: pointer;
  margin-left: 10px;
  margin-right: 1px;
}

.post-null-block {
  width: 100%;
  height: 100%;
}

.post-top {
  position: relative;
  width: 100%;
  height: 100%;
}

.post-top::after {
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
.text02 {
  font-weight: lighter;
}

.post-middle {
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

/* 게시글 목록 */
.user-info-and-exercise {
  width: 100%;
  height: 50px;
  margin-bottom: 10px;
  display: inline;
}

.exercise-text {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
  width: 60px;
  height: 30px;
  background-color: #1a73e8;
  color: white;
  font-weight: bold;
  font-size: 14px;
  margin-top: 10px;

  display: flex;
  justify-content: center;
  align-items: center;
}
/* 
.user-info {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 8px;
}

.user-name {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;
  font-weight: bold;
  font-size: 12px;
  margin-right: 5px;
}

.user-img {
  width: 30px;
  height: 30px;
  border-radius: 100%;
} */

.post-text {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  display: flex;
  flex-direction: column;
  /* margin-top: 30px; */
}

.post-title {
  font-family: 'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif;

  font-size: 16px;
  font-weight: bolder;
  margin-left: 5px;
  margin-bottom: 5px;
}

.post-content {
  margin-left: 5px;
}

.post-image-box {
  width: 100%;
  height: 250px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-image {
  width: 270px;
  height: 250px;
}

/* .post-image {
  width: 263px;
  height: 222px;
  margin-top: 5px;
  cursor: pointer;
} */

.post-data {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.post-bottom-table {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.bottom-table-group {
  width: 344px;
  height: 456px;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-right: 119px;
}

.bottom-table-group:nth-child(3n) {
  margin-right: 0px;
}

.table-group-del {
  position: relative;
  display: flex;
  align-items: center;
}

#modify_icon {
  width: 40px;
  height: 40px;
  margin-left: 270px;
  margin-top: 15px;
  cursor: pointer;
  z-index: 1;
}

.table-group-postimg {
  display: flex;
  justify-content: center;
}

.table-group-btn {
  display: flex;
  margin-top: -30px;
  margin-left: 185px;
}


/* .table-group-btn span {
  font-size: 15px;
  color: #757575;
  cursor: pointer;
} */

.likes {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 1px;
}

.btn-icon {
  width: 18px;
  height: 18px;
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  margin-top: 2px;
  margin-left: 5px;
  margin-bottom: 3px;
  cursor: pointer;
}

.comment {
  display: flex;
  justify-content: center;
  align-items: center;
  /* margin: 0 10px; */
}

#heart-count,
#comment-count {
  margin: 5px;
}

.table-group-content {
  float: left;
  margin-left: 40px;
  margin-top: 15px;
  cursor: pointer;
}

.table-group-title {
  font-size: 18px;
  font-weight: bold;
  color: #5d5a88;
  margin-bottom: 2%;
  overflow: hidden;
  text-overflow: ellipsis;
}

.table-group-text {
  font-size: 17px;
  color: #9795b5;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 하트 색상 변경 */
.title-heart {
  cursor: pointer;
  display: inline-block;
  width: 25px;
  /* 하트의 크기를 조정합니다 */
  height: 35px;
  /* 하트의 크기를 조정합니다 */

  display: flex;
  justify-content: center;
  align-items: center;
}

.title-heart div {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.empty-heart::before {
  content: "\2764";
  /* 빈 하트 문자 */
  font-size: 20px;
  /* 하트의 크기 */
  color: transparent;
  /* 하트의 내부는 투명하게 */
  -webkit-text-stroke: 1px black;
  /* 하트의 테두리 색상 */
}

.filled-heart::before {
  content: "\2764";
  /* 채워진 하트 문자 */
  font-size: 20px;
  /* 하트의 크기 */
  color: red;
  /* 채워진 하트의 색상 */
  -webkit-text-stroke: none;
  /* 채워진 하트의 테두리 제거 */
}
</style>
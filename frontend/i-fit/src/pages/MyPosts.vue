<template>
  <main>
    <AppNav />
    <div class="post">
      <div class="post-null-block"></div>
      <div class="post-top">
        <h2>게시글 관리</h2>
        <p class="line text01">내가 쓴 게시물</p>
        <p class="line text02" @click="writtenComments">내가 쓴 댓글</p>
        <p class="text03" @click="likedPosts">좋아요 한 게시물</p>
      </div>
      <div class="post-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" class="search-box-input" placeholder="검색어를 입력하세요." v-model="searchQuery" />
            <img src="@/assets/images/search.icon.png" alt="search" class="search-box-icon" @click="onSearch" />
          </div>
          <select title="정렬" class="middle-filter-sort" v-model="sortOrder">
            <option value="" disabled>정렬</option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <div class="post-bottom-table">
          <div class="bottom-table-group" v-for="post in visibleDatas" :key="post.id">
            <div class="table-group-del">
              <img id="modify_icon" :src="require('@/assets/images/dot.png')" alt="dot"
                @click="toggleActions(post.id)" />
              <PostActions :visible="showActionsForPostId(post.id)" @navigate="handleNavigation" />
            </div>
            <div class="table-group-postimg">
              <img class="table-group-post-img" :src="post.image" alt="게시글 이미지" @click="detailPost2" />
            </div>
            <div class="table-group-btn">
              <div class="likes">
                <div class="title-heart" @click="toggleHeart(post.id)">
                  <div :class="{
                    'filled-heart': post.isHeartFilled,
                    'empty-heart': !post.isHeartFilled,
                  }"></div>
                </div>
                <span id="heart-count">{{ post.likes }}</span>
              </div>
              <div class="comment">
                <img class="btn-icon" src="@/assets/images/comment.png" alt="댓글 아이콘" />
                <span id="comment-count">{{ post.comments }}</span>
              </div>
            </div>
            <div class="table-group-content" @click="detailPost(post.id)">
              <div class="group-content-post">
                <p class="table-group-title">{{ post.title || "제목없음" }}</p>
              </div>
              <div class="group-content-ptext">
                <p class="table-group-text">{{ post.content || "내용없음" }}</p>
              </div>
            </div>
          </div>
        </div>
        <Pagination :currentPage="currentPage" :totalPages="totalPages" @page-changed="onPageChange" />
      </div>
      <div class="post-floor"></div>
    </div>
  </main>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import PostActions from "@/components/common/PostActions.vue";
import Pagination from "@/components/common/Pagination.vue";
import AppNav from "@/components/layout/AppNav.vue";
import { usePagination } from "@/utils/pagination";

export default {
  components: {
    PostActions,
    Pagination,
    AppNav,
  },

  setup() {

    const router = useRouter();

    const posts = ref([
      {
        id: 1,
        image: require("@/assets/images/riding-1.png"),
        likes: 23,
        comments: 7,
        title: "종주할 때 사진 모아봤습니다.",
        content: "힘들었지만, 보람된 시간이였습니다.",
      },

      {
        id: 2,
        image: require("@/assets/images/riding-3.png"),
        likes: 72,
        comments: 17,
        title: "부산 하구둑 인증합니다. :)",
        content: "국토종주 마지막 날이였습니다...",
      },

      {
        id: 3,
        image: require("@/assets/images/riding-2.jpg"),
        likes: 87,
        comments: 10,
        title: "아라뱃길 정서진까지 라이딩",
        content: "목동 한강 합수부에서 만나서...",
      },

      {
        id: 4,
        image: require("@/assets/images/doginvade.jpg"),
        likes: 123,
        comments: 34,
        title: "골든리트리버 난입한 썰",
        content: "공 차는데 큰 개 한마리가 들어왔어요!!",
      },

      {
        id: 5,
        image: require("@/assets/images/rainfootball.jpg"),
        likes: 33,
        comments: 19,
        title: "비오는 날, 모임한 날",
        content: "날씨가 좋지 않음에도 참석해주신 분들이 많았습니다.",
      },

      {
        id: 6,
        image: require("@/assets/images/dmfootball.jpg"),
        likes: 54,
        comments: 19,
        title: "광복절에 용산에서 축구",
        content: "날씨가 무척 더워서 힘들었습니다.",
      },

      {
        id: 7,
        image: require("@/assets/images/medal.jpg"),
        likes: 144,
        comments: 45,
        title: "국토종주 메달왔습니다 인증!!.jpg.",
        content: "메달이 왔네요ㅎㅎ",
      },

      {
        id: 8,
        image: require("@/assets/images/certinfo.jpg"),
        likes: 144,
        comments: 45,
        title: "국토종주 인증서도 왔어요!!",
        content: "드디어 인증서 도착이요 ^^",
      },

      {
        id: 9,
        image: require("@/assets/images/winterfootball.jpg"),
        likes: 144,
        comments: 45,
        title: "추운 날씨에...",
        content: "다친 분 없이 모임이 끝나서 다행입니다.",
      },
    ]); // 초기 게시물 데이터는 빈 배열로 설정했었음.(더미 데이터 넣음)

    const searchQuery = ref("");
    const sortOrder = ref("");
    const showActions = ref(false);
    const selectedPostId = ref(null);

    // usePagination 훅 호출
    const { currentPage, totalPages, visibleDatas, onPageChange, fetchdatas } =
      usePagination(posts, 3);

    // 게시물 데이터 로드
    const fetchPosts = async () => {
      try {
        const response = await axios.get("https://api.example.com/posts");
        posts.value = response.data;
        fetchdatas(); // 초기 페이지 로딩
      } catch (error) {
        console.error("데이터 로드 실패:", error);
      }
    };

    const showActionsForPostId = (postId) => {
      return showActions.value && selectedPostId.value === postId;
    };

    const toggleActions = (postId) => {
      if (selectedPostId.value === postId) {
        showActions.value = !showActions.value;
      } else {
        selectedPostId.value = postId;
        showActions.value = true;
      }
    };

    const handleNavigation = (action) => {
      if (action === "EditPost") {
        router.push("edit-post");
      } else if (action === "delete") {
        deletePost(selectedPostId.value);
      }
    };

    const deletePost = async (postId) => {
      try {
        await axios.delete(`https://api.example.com/posts/${postId}`);
        alert("게시물이 삭제되었습니다.");
        fetchPosts(); // 데이터 재로드
      } catch (error) {
        console.error("삭제 실패:", error);
        alert("게시물 삭제 중 오류가 발생했습니다.");
      }
    };

    const isHeartFilled = ref(false);
    const toggleHeart = (postId) => {
      const post = posts.value.find(post => post.id === postId);
      if (post) {
        post.isHeartFilled = !post.isHeartFilled;
      }
    };

    const onSearch = () => {
      console.log("Searching for:", searchQuery.value);
      // 검색 로직 추가 필요
    };

    const detailPost = (postId) => {
      router.push(`/Post/${postId}`);
    };

    const writtenComments = () => {
      router.push({ name: "MyComments" });
    };

    const likedPosts = () => {
      router.push({ name: "LikedPosts" });
    };

    const detailPost2 = () => {
      router.push({ name: "Post" });
    };

    // 컴포넌트가 마운트될 때 데이터 로딩
    onMounted(() => {
      fetchPosts();
    });

    return {
      posts,
      searchQuery,
      sortOrder,
      showActions,
      selectedPostId,
      currentPage,
      totalPages,
      visibleDatas,
      onPageChange,
      showActionsForPostId,
      toggleActions,
      handleNavigation,
      deletePost,
      isHeartFilled,
      toggleHeart,
      onSearch,
      detailPost,
      writtenComments,
      likedPosts,
      detailPost2,
    }
  },
}

// Vue Router와 Composition API 사용

</script>

<style scoped>
main {
  width: 100%;
  height: 1200px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

.post {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 60px 150px 1fr;
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

.text02,
.text03 {
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

.post-floor {
  height: 50px;
  text-align: center;
  padding: 20px;
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

.table-group-post-img {
  width: 263px;
  height: 222px;
  margin-top: 5px;
  cursor: pointer;
}

.table-group-btn {
  display: flex;
  margin-top: 10px;
  margin-left: 185px;
}

.table-group-btn span {
  font-size: 15px;
  color: #757575;
  cursor: pointer;
}

.likes {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 10px;
}

.btn-icon {
  width: 20px;
  height: 20px;
  background-image: url("@/assets/images/comment.png");
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
  /* margin-right: 7px; */
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

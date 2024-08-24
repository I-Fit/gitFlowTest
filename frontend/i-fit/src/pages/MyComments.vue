<template>
  <main>
    <AppNav />
    <div class="comment">
      <div class="comment-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="navigateTo('MyPosts')">내가 쓴 게시물</p>
        <p class="line text02" @click="navigateTo('CommentPosts')">
          내가 쓴 댓글
        </p>
        <p class="text03" @click="navigateTo('LikedPosts')">좋아요 한 게시물</p>
      </div>
      <div class="comment-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input
              type="text"
              name="search"
              class="search-box-input"
              placeholder="검색어를 입력하세요."
              v-model="searchQuery"
              @input="onInput"
            />
            <img
              src="@/assets/images/search.icon.png"
              alt="search"
              class="search-box-icon"
              @click="onSearch"
            />
          </div>
          <select title="정렬" class="middle-filter-sort">
            <option value="" selected="selected" disabled="disabled">
              정렬
            </option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <div class="comment-bottom-table">
          <!-- 댓글 반복 렌더링 -->
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
        </div>
        <div class="pagination-container">
          <Pagination
            :currentPage="currentPage"
            :totalPages="totalPages"
            @page-changed="fetchComments"
          />
        </div>
      </div>
    </div>
  </main>
</template>


<script>
import { defineComponent } from "vue";
import PostActions from "@/components/common/PostActions.vue";
import AppNav from "@/components/layout/AppNav.vue";
import Pagination from "@/components/common/Pagination.vue";

export default defineComponent({
  components: {
    PostActions,
    AppNav,
    Pagination,
  },

  data() {
    return {
      searchQuery: "",
      comments: [
        {
          id: 1,
          userName: "김계란",
          content: "감사합니다! 열심히 했어요!!",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 2,
          userName: "김계란",
          content: "5일 걸렸습니다.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 3,
          userName: "김계란",
          content: "시원할 때, 다시 해보고 싶네요.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 4,
          userName: "김계란",
          content: "일정 넉넉하게 잡으시면 누구든 가능합니다! 도전 ㄱㄱ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 5,
          userName: "김계란",
          content: "댓글 남겨주셔서 감사합니다. 이따가 댓글 달아볼게요.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 6,
          userName: "김계란",
          content: "제 능력으로 3일 컷은 무리입니다 ㅋㅋ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 7,
          userName: "김계란",
          content: "기분이 좋으시군요 ㅋㅋㅋ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 8,
          userName: "김계란",
          content: "마지막날 200km 달렸고요.. 다른 날들은 100km 정도로..",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 9,
          userName: "김계란",
          content: "예전에 신청해서 받았어요!",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          id: 10,
          userName: "김계란",
          content: "조만간 같이 공 찹시다 ㅋㅋ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },
      ],
      currentPage: 1,
      commentsPerPage: 4,
    };
  },

  computed: {
    totalPages() {
      return Math.ceil(this.comments.length / this.commentsPerPage);
    },
    paginatedComments() {
      const startIndex = (this.currentPage - 1) * this.commentsPerPage;
      return this.comments.slice(startIndex, startIndex + this.commentsPerPage);
    },
  },

  methods: {
    toggleActions(index) {
      this.comments.forEach((comment, idx) => {
        comment.showActions = idx === index ? !comment.showActions : false;
      });
    },
    handleNavigation(action) {
      if (action === "EditPost") {
        this.$router.push("/edit-post");
      } else if (action === "delete") {
        this.$router.push("/my-comments");
      }
    },
    onInput(event) {
      this.searchQuery = event.target.value;
    },
    onSearch() {
      console.log("Searching for:", this.searchQuery);
    },
    navigateTo(routeName) {
      this.$router.push({ name: routeName });
    },

    navigateToPost(postId, commentId) {
      this.$router.push({ name: 'Post', query: { commentId }});
    },

    fetchComments(page) {
      this.currentPage = page;
      console.log("Fetching comments for page:", page);
    },
  },
});
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

.comment-bottom-table {
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
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: center; /* 가운데 정렬 */
}

.user-info-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-bottom: 5px; /* 이미지와 이름 사이에 간격 추가 */
}

.user-info-name {
  font-size: 16px;
  font-weight: bold;
}

.user-comment {
  display: flex;
  justify-content: space-between; /* 요소들 사이에 공간을 균등하게 배치 */
  align-items: center;
  font-size: 14px;
  color: #333;
  line-height: 1.5;
  gap: 10px; /* 요소들 간의 간격을 좁히기 위해 추가 */
}

.post-title {
  font-weight: bold;
  color: #555;
  margin-right: 100px; /* 제목과 콘텐츠 사이 간격을 좁게 설정 */
  margin-left: 50px;
  white-space: nowrap; /* 제목이 길어지면 줄바꿈 없이 표시 */
}

.comment-content {
  flex: 1; /* comment content가 가로 공간을 차지하도록 설정 */
  /* margin-right: px; content와 date 사이 간격을 좁게 설정 */
  /* text-align: center; content를 중앙 정렬 */
  color: #333;
}

.creation-date {
  font-size: 12px;
  color: #999;
  white-space: nowrap; /* 날짜가 줄바꿈 없이 표시 */
  margin-right: 50px; /* date와 content 사이 간격을 좁게 설정 */
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
  /* 클릭 우선순위 조정 */
}

.pagination-container {
  margin-top: 153px;
}
</style>
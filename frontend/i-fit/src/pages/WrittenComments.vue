<template>
  <main>
    <AppNav />
    <div class="comment">
      <div class="comment-null-block"></div>
      <div class="comment-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="navigateTo('HistoryPosts')">
          내가 쓴 게시물
        </p>
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
              src="@/assets/image/search.icon.png"
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
          >
            <div class="user-info">
              <img
                class="user-info-img"
                src="@/assets/image/user_img.png"
                alt="User Image"
              />
              <p class="user-info-name">{{ comment.userName }}</p>
              <img
                class="modify-icon"
                src="@/assets/image/dot.png"
                alt="dot"
                @click="toggleActions(index)"
              />
              <PostActions
                :visible="comment.showActions"
                @navigate="handleNavigation"
              />
            </div>
            <div class="user-comment" @click="navigateTo('DetailPost')">
              {{ comment.content }}
            </div>
            <div class="post-comment">
              <p class="post-title">
                게시글 : {{ comment.postTitle }}
                <span class="creation-date">{{ comment.creationDate }}</span>
              </p>
            </div>
          </div>
        </div>
        <PagiNation
          :currentPage="currentPage"
          :totalPages="totalPages"
          @page-changed="fetchComments"
        />
      </div>
    </div>
  </main>
</template>

<script>
import { defineComponent } from "vue";
import PostActions from "@/components/common/PostActions.vue";
import AppNav from "@/components/layout/AppNav.vue";
import PagiNation from "@/components/common/PagiNation.vue";

export default defineComponent({
  components: {
    PostActions,
    AppNav,
    PagiNation,
  },

  data() {
    return {
      searchQuery: "",
      comments: [
        {
          userName: "김계란",
          content: "감사합니다! 열심히 했어요!!",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "5일 걸렸습니다.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "시원할 때, 다시 해보고 싶네요.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "일정 넉넉하게 잡으시면 누구든 가능합니다! 도전 ㄱㄱ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "댓글 남겨주셔서 감사합니다. 이따가 댓글 달아볼게요.",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "제 능력으로 3일 컷은 무리입니다 ㅋㅋ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "마지막날 200km 달렸고요.. 다른 날들은 100km 정도로..",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "예전에 신청해서 받았어요!",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "조만간 같이 공 찹시다 ㅋㅋ",
          postTitle: "종주할 때 사진 모아봤습니다.",
          creationDate: "6일 전",
          showActions: false,
        },

        {
          userName: "김계란",
          content: "기분이 좋으시군요 ㅋㅋㅋ",
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
        this.$router.push("/written-comments");
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
}

.comment-bottom-table {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.bottom-table {
  position: relative;
  width: 615px;
  height: 225px;
  border: 2px solid #ccc;
  border-radius: 10px;
  margin-right: 41px;
  margin-bottom: 50px;
}

.bottom-table:nth-child(2n) {
  margin-right: 0px;
}

.comment-floor {
  height: 50px; /* floor 영역 높이 설정 */
  text-align: center;
  padding: 20px;
}

.post-actions {
  position: absolute;
  top: 40px; /* 상단에서 20px 위치 */
  right: -30px; /* 오른쪽에서 20px 위치 */
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
  display: flex; /* 버튼이 여러 개일 경우, flexbox로 배치할 수 있습니다 */
  flex-direction: column; /* 버튼을 수직으로 배치 */
}

.user-info {
  display: flex;
  align-items: center;
  margin-top: 25px;
  margin-left: 10px;
}

.user-info-img {
  width: 50px;
  height: 40px;
  margin-left: 10px;
}

.user-info-name {
  font-size: 20px;
  margin-left: 10px;
  font-weight: bold;
}

.modify-icon {
  width: 40px;
  height: 40px;
  margin-left: 415px;
  cursor: pointer;
}

.user-comment {
  font-size: 16px;
  margin-left: 25px;
  margin-right: 25px;
  margin-top: 20px;
  cursor: pointer;
}

.post-comment {
  margin: 20px 0 10px 25px;
}

.post-title {
  font-weight: lighter;
  color: gray;
  font-size: 14px;
}

.creation-date {
  margin-left: 305px;
}
</style>

<template>
  <main>
    <AppNav />
    <div class="comment">
      <div class="comment-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="postHistory">나의 게시물 내역</p>
        <p class="line text02">내가 쓴 댓글</p>
        <p class="text03" @click="myLikepost">좋아요 한 게시물</p>
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
              s="s"
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
          <div class="bottom-table">
            <div class="user-info">
              <img
                class="user-info-img"
                src="@/assets/image/user_img.png"
                alt=""
              />
              <p class="user-info-name">김계란</p>
              <img
                class="modify-icon"
                src="@/assets/image/dot.png"
                alt="dot"
                @click="toggleActions"
              />
              <PostActions
                :visible="showActions"
                @navigate="handleNavigation"
              />
            </div>
            <div class="user-comment" @click="boardDetail">
              ‘카페 메이플스토리’는 핑크 컬러 테마의 카페 식음 공간과 함께
              몬스터 형태의 오브제로 세련된 분위기를 연출한 테라스, 포토 스팟이
              마련된 굿즈존 등으로 구성됐다.
            </div>
            <div class="post-comment">
              <p class="post-title">
                게시글 : 멤버들과 현충일 번개운동
                <span class="creation-date">6일 전</span>
              </p>
            </div>
          </div>
          <div class="bottom-table">
            <div class="user-info">
              <img
                class="user-info-img"
                src="@/assets/image/user_img.png"
                alt=""
              />
              <p class="user-info-name">김계란</p>
              <img 
              class="modify-icon" 
              src="@/assets/image/dot.png" 
              alt="" 
              @click="toggleActions"/>
              <PostActions
                :visible="showActions"
                @navigate="handleNavigation"
              />
            </div>
            <div class="user-comment" @click="boardDetail">
              ‘카페 메이플스토리’는 핑크 컬러 테마의 카페 식음 공간과 함께
              몬스터 형태의 오브제로 세련된 분위기를 연출한 테라스, 포토 스팟이
              마련된 굿즈존 등으로 구성됐다.
            </div>
            <div class="post-comment">
              <p class="post-title">
                게시글 : 멤버들과 현충일 번개운동
                <span class="creation-date">6일 전</span>
              </p>
            </div>
          </div>
          <PagiNation
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
import PostActions from "@/pages/PostActions.vue";
import AppNav from "@/components/layout/AppNav.vue";
import PagiNation from "@/pages/PagiNation.vue";
import { useRouter } from "vue-router";


export default {
  name: "MyComments",
  components: {
    PostActions,
    AppNav,
    PagiNation,
  },

  data() {
    return {
      comments: [],
      currentPage: 1,
      totalPages: 5, // 예를 들면, 총 페이지 수
      showActions: false,
      searchQuery: "",
    };
  },

  methods: {
    toggleActions() {
      console.log("toggleActions called");
      this.showActions = !this.showActions;
    },
    handleNavigation(action) {
      if (action === "PostModify") {
        this.$router.push("/PostModify"); // 수정 페이지로 이동
      } else if (action === "delete") {
        this.$router.push("/MainBoard"); // 메인 게시판으로 이동
      }
    },

    onInput(event) {
      this.searchQuery = event.target.value;
    },
    onSearch() {
      console.log("Searching for:", this.searchQuery);
    },
  },

  setup() {
    const router = useRouter();

    const postHistory = () => {
      router.push({ name: "PostHistory" });
    };

    const myLikepost = () => {
      router.push({ name: "MyLikepost" });
    };

    const boardDetail = () => {
      router.push({ name: "BoardDetail" });
    };

    return {
      postHistory,
      myLikepost,
      boardDetail,
    };
  },
};
</script>

<style scoped>
main {
  width: 100%;
  height: 900px;
  display: grid;
  grid-template-columns: 180px 1fr;
}

.comment {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 150px 1fr;
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
  justify-content: space-between;
}

.bottom-table {
  width: 615px;
  height: 225px;
  border: 2px solid #ccc;
  border-radius: 10px;
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

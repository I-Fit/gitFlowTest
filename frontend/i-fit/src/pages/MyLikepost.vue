<template>
  <main>
    <AppNav />
    <div class="post">
      <div class="post-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="postHistory">나의 게시물 내역</p>
        <p class="line text02" @click="myComments">내가 쓴 댓글</p>
        <p class="text03">좋아요 한 게시물</p>
      </div>
      <div class="post-middle">
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
          <select post="정렬" class="middle-filter-sort">
            <option value="" selected="selected" disabled="disabled">정렬</option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <div class="post-bottom-table">
          <div class="bottom-table-group">
            <div class="table-group-del">
              <img 
              id="modify_icon" 
              src="@/assets/image/dot.png" 
              alt="dot"
              @click="toggleActions"
              />
              <PostActions
                :visible="showActions"
                @navigate="handleNavigation"
              />
            
            </div>
            <div class="table-group-postimg">
              <img
                class="table-group-post-image"
                src="@/assets/image/post_img.png"
                alt="게시글 이미지"
              />
            </div>
            <div class="table-group-btn">
              <img
                class="btn-icon"
                src="@/assets/image/heart_icon.png"
                alt="좋아요 아이콘"
              />
              <span id="heart-count">101</span>
              <img
                class="btn-icon"
                src="@/assets/image/comment.png"
                alt="댓글 아이콘"
              />
              <span id="comment-count">5</span>
            </div>
            <div class="table-group-content" @click="boardDetail">
              <div class="group-content-post">
                <p class="table-group-title">멤버들이랑 현충일 번개운동</p>
              </div>
              <div class="group-content-ptext">
                <p class="table-group-text">
                  공휴일에 멤버들이랑 kosta짐에서<br />웨이트 했습니다 ~
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  </template>
  
  <script>
  import PostActions from "./PostActions.vue";
  import { useRouter } from "vue-router";
  import AppNav from "@/components/layout/AppNav.vue";
  
  export default {
    name: "MyLikepost",
    components: {
      PostActions,
      AppNav,
    },
  
    data() {
    return {
      showActions: false,
      searchQuery: "",
    };
  },

  methods: {
    toggleActions() {
      this.showActions = !this.showActions;
    },
    handleNavigation(action) {
      if (action === "PostModify") {
        this.$router.push("/PostModify"); // 수정 페이지로 이동
      } else if (action === "delete") {
        this.$router.push("/MainBoard"); // 삭제 페이지로 이동
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
  
      const myComments = () => {
        router.push({ name: "MyComments" });
      };
  
      const boardDetail = () => {
        router.push({ name: "BoardDetail" });
      };

      return {
        postHistory,
        myComments,
        boardDetail,
      };
    },
  };
  </script>
  
  <style scoped>
  /* content 부분 */
  main {
    width: 100%;
    height: 900px;
    display: grid;
    grid-template-columns: 180px 1fr;
  }
  
  nav {
    display: flex;
    flex-direction: column;
  }
  
  .nav-list {
    margin-top: 165px;
  }
  
  .selector {
    width: 110px;
    height: 30px;
    position: relative;
  }
  
  .selector::after {
    content: "";
    display: block;
    width: 87%;
    height: 2px;
    background-color: #ccc;
    margin: 5px 0 0 10px;
  }
  
  .selector-post {
    font-size: 20px;
    font-weight: bold;
    margin: 0;
  }
  
  .selector-text {
    font-size: 14px;
    margin: 5px;
    padding: 5px;
  }
  
  .post {
    width: 1270px;
    height: 100%;
    display: grid;
    grid-template-rows: 150px 1fr;
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
  
  .bottom-table-group {
    width: 344px;
    height: 456px;
    border: 1px solid #ccc;
    border-radius: 10px;
    margin-left: 20px;
  }
  
  .table-group-del {
    display: flex;
  }
  
  #modify_icon {
    width: 40px;
    height: 40px;
    margin-left: 270px;
    margin-top: 15px;
    cursor: pointer;
  }
  
  .post-feature-table-group-del img {
    width: 50px;
    height: 45px;
    margin-right: 20px;
    cursor: pointer;
  }
  
  .table-group-postimg {
    display: flex;
    justify-content: center;
  }
  
  .table-group-post-image {
    width: 263px;
    height: 222px;
    margin-top: 5px;
  }
  
  .table-group-btn {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 10px;
    margin-left: 150px;
  }
  
  .btn-icon {
    width: 20px;
    height: 20px;
    margin-left: 10px;
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
  </style>
  
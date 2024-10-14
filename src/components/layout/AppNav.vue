<template>
    <nav>
      <div class="nav-list">
        <div class="selector">
          <p class="selector-title">모임 관리</p>
        </div>
        <p class="selector-text" @click="mycreategroup">내가 만든 모임</p>
        <p class="selector-text" @click="groupjoinlist">모임 참여 내역</p>
        <p class="selector-text" @click="likegroup">찜한 모임 내역</p>
        <div class="selector">
          <p class="selector-title">멤버십</p>
        </div>
        <p class="selector-text" @click="membership">멤버십 혜택</p>
        <div class="selector">
          <p class="selector-title">게시글 관리</p>
        </div>
        <p class="selector-text" @click="postHistory">내가 쓴 게시글</p>
        <p class="selector-text" @click="myComments">내가 쓴 댓글</p>
        <p class="selector-text" @click="myLikepost">좋아요 한 게시글</p>
      </div>
    </nav>
  </template>
  
  
  <script>
  import { useRouter } from 'vue-router'; // useRouter 임포트
  
  export default {
    name: "AppNav",
    components: {},
  
    setup() {
      const router = useRouter();

      const mycreategroup = () => {
        router.push({ name: "CreatedGroups" });
      };

      const groupjoinlist = () => {
        router.push({ name: "JoinedGroups" });
      };

      const likegroup = () => {
        router.push({ name: "LikedGroups" });
      };

      const membership = () => {
        router.push({ name: "Membership" });
      };
  
      const postHistory = () => {
        router.push({ name: "MyPosts" });
      };
  
      const myComments = () => {
        router.push({ name: "MyComments" });
      };
  
      const myLikepost = () => {
        router.push({ name: "LikedPosts" });
      };
  
      return {
        mycreategroup,
        groupjoinlist,
        likegroup,
        membership,
        postHistory,
        myComments,
        myLikepost,
      };
    }
  };
  </script>
  
  
  
  <style scoped>
  /* main {
    width: 100%;
    height: 900px;
    display: grid;
    grid-template-columns: 180px 1fr;
  } */
  
  nav {
    display: flex;
    flex-direction: row-reverse;
    /* margin-left: 50px; */
    margin-right: -50px;
  }
  
  .nav-list {
    /* margin-top: 165px; */
    margin-top: 220px;
    /* margin-left: 100px; */
  }
  
  .selector {
    width: 140px;
    height: 30px;
    position: relative;
  }
  
  .selector::after {
    content: "";
    display: block;
    width: 100%;
    height: 2px;
    background-color: #ccc;
    margin: 5px 0 0 10px;
  }
  
  .selector-title {
    font-size: 20px;
    font-weight: bolder;
    margin-left: 5px;
    margin-top: 20px;
    margin-bottom: 5px;
  }
  
  .selector-text {
    font-size: 14px;
    margin: 10px;
    padding: 5px;
    cursor: pointer;
  }
  </style>
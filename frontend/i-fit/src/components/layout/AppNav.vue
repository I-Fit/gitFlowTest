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
        <p class="selector-text" @click="postHistory">나의 게시물 내역</p>
        <p class="selector-text" @click="myComments">내가 쓴 댓글 내역</p>
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
        router.push({ name: "MyCreateGroup" });
      };

      const groupjoinlist = () => {
        router.push({ name: "GroupJoinList" });
      };

      const likegroup = () => {
        router.push({ name: "LikeGroup" });
      };

      const membership = () => {
        router.push({ name: "Membership" });
      };
  
      const postHistory = () => {
        router.push({ name: "PostHistory" });
      };
  
      const myComments = () => {
        router.push({ name: "MyComments" });
      };
  
      const myLikepost = () => {
        router.push({ name: "MyLikepost" });
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
  
  .selector-title {
    font-size: 20px;
    font-weight: bold;
    margin: 0;
  }
  
  .selector-text {
    font-size: 14px;
    margin: 5px;
    padding: 5px;
    cursor: pointer;
  }
  </style>
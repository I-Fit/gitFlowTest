<template>
  <main>
    <div class="post-container">
      <div class="search-and-list">
        <div class="feature">
          <div class="feature-add">
            <div class="add-btn" @click="postUpload">+</div>
            <span>운동을 인증해보세요!</span>
          </div>
          <button type="button" class="feature-sort">
            <select class="sort" title="정렬">
              <option value="" selected disabled>정렬</option>
              <option value="1">인기순</option>
              <option value="4">최신순</option>
              <option value="2">예전순</option>
            </select>
          </button>
        </div>
        <div class="list">
          <div class="post-box">
            <div class="post-items">
              <div class="post-info">
                <div class="writer-profile-image"></div>
                <span class="writer-name">라이언카카오123</span>
                <span class="created-at">6 days ago</span>
              </div>
              <div class="title-and-content">
                <h2 class="title" @click="boardDetailA">첫 마라톤 완주</h2>
                <span class="text"
                  >회사 동기들이랑 마라톤 참여! 첫 마라톤 첫 완주 ㅎㅎ 뿌듯</span
                >
              </div>
              <div class="post-tags">
                <div class="tag-items">
                  <div class="topic"><span>런닝</span></div>
                  <div class="location"><span>용산구</span></div>
                  <div class="scale"><span>소규모</span></div>
                </div>
                <div class="likes-and-comments">
                  <div class="likes">
                    <div class="like-icon"></div>
                    <span>74</span>
                  </div>
                  <div class="comment">
                    <div class="comment-icon"></div>
                    <span>22</span>
                  </div>
                  <div class="option-icon"></div>
                </div>
              </div>
            </div>
            <img
              class="post-image"
              src="@/assets/image/post_img.png"
              alt="게시글 이미지"
            />
          </div>
          <div class="post-box">
            <div class="post-items">
              <div class="post-info">
                <div class="writer-profile-image"></div>
                <span class="writer-name">라이언카카오123</span>
                <span class="created-at">6 days ago</span>
              </div>
              <div class="title-and-content">
                <h2 class="title" @click="boardDetailB">첫 마라톤 완주</h2>
                <span class="text"
                  >회사 동기들이랑 마라톤 참여! 첫 마라톤 첫 완주 ㅎㅎ 뿌듯</span
                >
              </div>
              <div class="post-tags">
                <div class="tag-items">
                  <div class="topic"><span>런닝</span></div>
                  <div class="location"><span>용산구</span></div>
                  <div class="scale"><span>소규모</span></div>
                </div>
                <div class="likes-and-comments">
                  <div class="likes">
                    <div class="like-icon"></div>
                    <span>74</span>
                  </div>
                  <div class="comment">
                    <div class="comment-icon"></div>
                    <span>22</span>
                  </div>
                  <div class="option-icon"></div>
                </div>
              </div>
            </div>
            <img
              class="post-image"
              src="@/assets/image/post_img.png"
              alt="게시글 이미지"
            />
          </div>
          <div class="post-box">
            <div class="post-items">
              <div class="post-info">
                <div class="writer-profile-image"></div>
                <span class="writer-name">라이언카카오123</span>
                <span class="created-at">6 days ago</span>
              </div>
              <div class="title-and-content">
                <h2 class="title" @click="boardDetailC">첫 마라톤 완주</h2>
                <span class="text"
                  >회사 동기들이랑 마라톤 참여! 첫 마라톤 첫 완주 ㅎㅎ 뿌듯</span
                >
              </div>
              <div class="post-tags">
                <div class="tag-items">
                  <div class="topic"><span>런닝</span></div>
                  <div class="location"><span>용산구</span></div>
                  <div class="scale"><span>소규모</span></div>
                </div>
                <div class="likes-and-comments">
                  <div class="likes">
                    <div class="like-icon"></div>
                    <span>74</span>
                  </div>
                  <div class="comment">
                    <div class="comment-icon"></div>
                    <span>22</span>
                  </div>
                  <div class="option-icon"></div>
                </div>
              </div>
            </div>
            <img
              class="post-image"
              src="@/assets/image/post_img.png"
              alt="게시글 이미지"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="side-bar">
      <div class="side-bar-items">
        <div class="search-container">
          <div class="search-box">
            <input
              type="text"
              class="search-input"
              placeholder="검색어를 입력하세요."
            />
            <div class="search-icon"></div>
          </div>
        </div>
        <div class="search-list">
          <p class="list-title">Recommend category</p>
          <button class="list-item" type="submit">러닝</button>
          <button class="list-item" type="submit">웨이트</button>
          <button class="list-item" type="submit">라이딩</button>
          <button class="list-item" type="submit">요가</button>
          <button class="list-item" type="submit">수영</button>
          <button class="list-item" type="submit">등산</button>
          <button class="list-item" type="submit">테니스</button>
          <button class="list-item" type="submit">클라이밍</button>
          <button class="list-item" type="submit">필라테스</button>
          <button class="list-item" type="submit">GX</button>
        </div>
      </div>
    </div>
  </main>
  </template>
  
  <script>
  import { useRouter } from "vue-router";
  
  export default {
    name: "Board",
    components: {},
  
    setup() {
      const router = useRouter();
  
      const postUpload = () => {
        router.push({ name: "UploadPost" });
      };

      const boardDetailA = () => {
        router.push({ name: "DetailPost" });
      };

      const boardDetailB = () => {
        router.push({ name: "DetailPost" });
      };

      const boardDetailC = () => {
        router.push({ name: "DetailPost" });
      };

      return {
        postUpload,
        boardDetailA,
        boardDetailB,
        boardDetailC,
      };
    },
  };
  </script>
  
  <style scoped>
  main {
    display: grid;
    grid-template-columns: 3fr 2fr;
    width: 100%;
    margin-top: 50px;
  }
  
  input {
    outline: none;
  }
  
  .post-container {
    width: 100%;
    height: 800px;
    justify-content: center;
    align-items: center;
  }
  
  .search-and-list {
    display: flex;
    flex-direction: column;
    height: 100%;
    margin-left: 70px;
  }
  
  .conversion {
    width: 46px;
    height: 54px;
    background-color: #fff;
    position: absolute;
    top: 300px;
    left: 950px;
  }
  
  .conversion span {
    font-size: 15px;
    line-height: 22.5px;
    color: #6b7280;
  }
  
  .feature {
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: relative;
  }
  
  .feature::after {
    content: "";
    display: block;
    width: 100%;
    height: 1px;
    background-color: #ccc;
    margin: 5px 0 0 10px;
    position: absolute;
    bottom: -10px; /* Adjust to position the line properly */
    left: 50%;
    transform: translateX(-50%);
  }
  
  .feature-add {
    display: flex;
    align-items: center;
    margin-left: 25px;
  }
  
  .add-btn {
    width: 35px;
    height: 35px;
    background-color: #00000014;
    border-radius: 100%;
    font-size: 40px;
    text-align: center;
    line-height: 35px;
    cursor: pointer;
  }
  
  .feature-add span {
    margin-left: 10px;
    font-size: 20px;
  }
  
  .feature-sort {
    border: 1px solid whitesmoke !important;
    border-radius: 10px;
  }
  
  .sort {
    width: 75px;
    height: 35px;
    text-align: center;
    border: 1px solid #fff !important;
    background-color: white !important;
    box-sizing: border-box;
  }
  
  .list {
    display: flex;
    flex-direction: column;
    height: 100%;
    margin-top: 20px;
  }
  
  .post-box {
    display: flex;
    width: 100%;
    height: 170px;
    margin: 10px 0;
    justify-content: center;
    position: relative;
    margin-top: 20px;
    /* border: 1px solid whitesmoke; */
    border-radius: 10px;
  }
  
  .post-box::after {
    content: "";
    display: block;
    width: 100%;
    height: 1px;
    background-color: #ccc;
    margin: 5px 0 0 10px;
    position: absolute;
    bottom: -10px; /* Adjust to position the line properly */
    left: 50%;
    transform: translateX(-50%);
  }
  
  .post-items {
    display: flex;
    flex-direction: column;
    width: 80%;
    margin-left: 10px;
    justify-content: space-around;
  }
  
  .post-info {
    display: flex;
  }
  
  .writer-profile-image {
    width: 50px;
    height: 40px;
    background-image: url("@/assets/image/user_img.png");
    background-size: 80% 80%;
    background-repeat: no-repeat;
    background-position: center;
  }
  
  .writer-name,
  .created-at {
    margin: auto 10px;
  }
  
  .created-at {
    font-size: 14px;
    color: #ccc;
  }
  
  .title {
    margin: 0px 0px 10px 10px;
    cursor: pointer;
  }
  
  .text {
    margin-left: 10px;
  }
  
  .post-tags {
    display: flex;
    justify-content: space-between;
  }
  
  /* 게시글에 올라간 사진 이미지 */
  .post-image {
    width: 180px;
    height: 150px;
    border-radius: 10px;
    margin-top: 10px;
    margin-right: 5px;
  }
  
  .tag-items {
    display: flex;
    margin-left: 9px;
  }
  
  .tag-items div {
    width: 52px;
    height: 24px;
    text-align: center;
    font-size: 12px;
  }
  
  .topic {
    width: 65px;
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
  
    cursor: pointer;
    background: #f2f2f2;
    border: none;
    border-radius: 10px;
    color: #757575;
  }
  
  .location {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #757575;
  }
  
  .scale {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #757575;
  }
  
  .likes-and-comments {
    display: flex;
  }
  
  .likes {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 10px;
  }
  
  .likes-and-comments span {
    font-size: 15px;
    color: #757575;
  }
  
  .like-icon {
    width: 20px;
    height: 20px;
    background-image: url("@/assets/image/heart.png");
    background-repeat: no-repeat;
    background-size: contain;
    background-position: center;
    margin-right: 5px;
  }
  
  .comment {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 10px;
  }
  
  .comment-icon {
    width: 20px;
    height: 20px;
    background-image: url("@/assets/image/comment.png");
    background-repeat: no-repeat;
    background-size: contain;
    background-position: center;
    margin-right: 5px;
  }
  
  .option-icon {
    width: 24px;
    height: 24px;
    margin: 0 10px;
    background-image: url("@/assets/image/dot.png");
    background-repeat: no-repeat;
    background-size: contain;
    background-position: center;
    cursor: pointer;
  }
  
  /* features */
  /* .side-bar {
    width: 40%;
    border: 1px solid #222;
  } */
  
  .side-bar-items {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  
  .search-box {
    width: 331px;
    height: 31px;
    border: 1px solid #53525280;
    border-radius: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 20px;
    margin-top: 40px;
  }
  
  .search-input {
    border: none;
    width: 80%;
    height: 100%;
    border-radius: 10px;
    margin-left: 10px;
  }
  
  .search-icon {
    width: 15px;
    height: 15px;
    background-image: url("@/assets/image/search.icon.png");
    background-size: contain;
    cursor: pointer;
    margin: 0 10px;
  }
  
  .search-list {
    width: 340px;
    height: 180px;
  }
  
  .list-title {
    margin-bottom: 20px;
  }
  
  .list-item {
    width: 70px;
    height: 30px;
    border: none;
    border-radius: 10px;
    margin: 5px;
    padding: 0;
  }
  </style>
  
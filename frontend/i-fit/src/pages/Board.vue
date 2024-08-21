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
            <select class="sort" v-model="selectedSort" @change="sortPosts" title="정렬">
              <option value="" disabled>정렬</option>
              <option value="popularity">인기순</option>
              <option value="latest">최신순</option>
              <option value="oldest">오래된순</option>
            </select>
          </button>
        </div>
        <div class="list">
          <div v-for="post in sortedPosts" :key="post.id" class="post-box">
            <div class="post-items">
              <div class="post-info">
                <div class="writer-profile-images"></div>
                <span class="writer-name">{{ post.writerName }}</span>
                <span class="created-at">{{ post.createdAt }}</span>
              </div>
              <div class="title-and-content">
                <h2 class="title" @click="viewPost(post.id)">{{ post.title }}</h2>
                <span class="text">{{ post.content }}</span>
              </div>
              <div class="post-tags">
                <div class="tag-items">
                  <div class="topic"><span>{{ post.topic }}</span></div>
                  <div class="location"><span>{{ post.location }}</span></div>
                  <div class="scale"><span>{{ post.scale }}</span></div>
                </div>
                <div class="likes-and-comments">
                  <div class="likes">
                    <div class="like-icon"></div>
                    <span>{{ post.likes }}</span>
                  </div>
                  <div class="comment">
                    <div class="comment-icon"></div>
                    <span>{{ post.comments }}</span>
                  </div>
                  <div class="option-icon"></div>
                </div>
              </div>
            </div>
            <img class="post-images" :src="post.imagesUrl" alt="게시글 이미지" />
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
              v-model="searchQuery"
            />
            <div class="search-icon"></div>
          </div>
        </div>
        <div class="search-list">
          <p class="list-title">Recommend category</p>
          <button v-for="category in categories" :key="category" class="list-item" type="submit">
            {{ category }}
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Board',
  setup() {
    const router = useRouter();

    const posts = ref([
      { 
        id: 1, 
        writerName: '김계란', 
        createdAt: '6 days ago', 
        title: '종주할 때 사진 모아봤습니다.', 
        content: '힘들었지만, 보람된 시간이였습니다.', 
        topic: '라이딩', location: '부산', scale: '소규모', 
        likes: 54, comments: 20, 
        imagesUrl: require('@/assets/images/riding-1.png')
      },

      { 
        id: 2, 
        writerName: '김계란', 
        createdAt: '6 days ago', 
        title: '부산 하구둑 인증합니다.^^', 
        content: '국토종주 마지막 날이였습니다...', 
        topic: '라이딩', location: '부산', scale: '소규모', 
        likes: 72, comments: 17, 
        imagesUrl: require('@/assets/images/riding-3.png')
      },

      { id: 3, 
        writerName: '김계란', 
        createdAt: '6 days ago', 
        title: '아라뱃길 정서진까지 라이딩', 
        content: '목동 한강 합수부에서 만나서...', 
        topic: '라이딩', location: '목동', scale: '소규모', 
        likes: 87, comments: 10, 
        imagesUrl: require('@/assets/images/riding-2.jpg')
      },
    ]);

    const categories = ref(['러닝', '웨이트', '라이딩', '요가', '수영', '등산', '테니스', '클라이밍', '필라테스', 'GX']);

    const selectedSort = ref('');
    const searchQuery = ref('');

    const sortedPosts = computed(() => {
      let sorted = [...posts.value];

      if (selectedSort.value === 'popularity') {
        sorted.sort((a, b) => b.likes - a.likes);
      } else if (selectedSort.value === 'latest') {
        sorted.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      } else if (selectedSort.value === 'oldest') {
        sorted.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
      }

      if (searchQuery.value) {
        sorted = sorted.filter(post => 
          post.title.includes(searchQuery.value) || 
          post.content.includes(searchQuery.value)
        );
      }

      return sorted;
    });

    const postUpload = () => {
      router.push({ name: 'UploadPost' });
    };

    const viewPost = (postId) => {
      router.push({ name: 'Post', params: { id: postId } });
    };

    const sortPosts = () => {
      // Trigger sorting by reassigning sortedPosts
      sortedPosts.value;
    };

    return {
      posts,
      categories,
      selectedSort,
      searchQuery,
      sortedPosts,
      postUpload,
      viewPost,
      sortPosts
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
  
  .writer-profile-images {
    width: 50px;
    height: 40px;
    background-images: url("@/assets/images/user_img.png");
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
  .post-images {
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
    background-images: url("@/assets/images/heart.png");
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
    background-images: url("@/assets/images/comment.png");
    background-repeat: no-repeat;
    background-size: contain;
    background-position: center;
    margin-right: 5px;
  }
  
  .option-icon {
    width: 24px;
    height: 24px;
    margin: 0 10px;
    background-images: url("@/assets/images/dot.png");
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
    background-images: url("@/assets/images/search.icon.png");
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
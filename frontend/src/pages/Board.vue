<template>
  <main>
    <div class="post-container">
      <div class="search-and-list">
        <div class="feature">
          <div class="feature-add">
              <div class="add-btn" @click="goToCreatePost">+</div>
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
          <div v-for="post in visibleDatas" :key="post.postId" class="post-box">
            <div class="post-items">
              <div class="post-info">
                <div class="writer-profile-image"></div>
                <span class="writer-name">{{ post.writerName }}</span>
                <span class="created-at">{{ post.formattedCreatedAt }}</span>
              </div>
              <div class="title-and-content">
                <h2 class="title" @click="viewPost(post)">{{ post.title }}</h2>
                <span class="text" @click="viewPost(post)" v-html="post.contentWithoutImage"></span>
              </div>
              <div class="post-tags">
                <div class="tag-items">
                  <div class="topic"><span>{{ post.exercise }}</span></div>
                  <div class="location"><span>{{ post.location }}</span></div>
                  <!-- <div class="scale"><span>{{ post.scale }}</span></div> -->
                </div>
                <div class="likes-and-comments">
                  <div class="likes">
                    <div class="title-heart" @click="toggleHeart(post.id)">
                      <div :class="{
                        'filled-heart': post.isHeartFilled,
                        'empty-heart': !post.isHeartFilled,
                      }"></div>
                    </div>
                    <span>{{ post.likesCnt }}</span>
                  </div>
                  <div class="comment" @click="commentPost(post.id)">
                    <div class="comment-icon"></div>
                    <span>{{ post.commentsCnt }}</span>
                  </div>
                  <div class="option-icon"></div>
                </div>
              </div>
            </div>
            <img class="post-image" :src="post.imageStr ? `data:image/png;base64,${post.imageStr}` : ''" alt="게시글 이미지" @click="viewPost(post)" />
          </div>
        </div>
      </div>
    </div>
    <div class="side-bar">
      <div class="side-bar-items">
        <div class="search-container">
          <div class="search-box">
            <input type="text" class="search-input" placeholder="검색어를 입력하세요." v-model="searchKeyword" @keydown.enter="performSearch" />
            <div class="search-icon" @click="performSearch"></div>
          </div>
        </div>
        <div class="search-list">
          <p class="list-title">Recommend category</p>
          <!-- <button 
            v-for="category in categories" 
            :key="category" 
            class="list-item" 
            type="button"
            :class="{ selected: selectedCategory === category }"
            @click="selectCategory(category)"
          >
            {{ category }}
          </button> -->
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { useRouter } from "vue-router";
import axios from 'axios';
import { ref, onMounted, computed } from 'vue';

export default {
  name: 'Board',

  setup() {
    const router = useRouter();
    const searchKeyword = ref('');
    const visibleDatas = ref([]);
    const selectedSort = ref('');

    const goToCreatePost = () => {
      router.push("/create-post");
    };

    const fetchPosts = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/board/list');
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log('fetching posts: ', data);
        visibleDatas.value = data;
      } catch (error) {
        console.error('게시글 불러오기 실패: ', error);
      }
    };

    const processedPosts = computed(() => {
      return visibleDatas.value.map(post => ({
        ...post,
        formattedCreatedAt: formatDate(post.createdAt),
        contentWithoutImage: post.content.replace(/<img[^>]*>/g, ''),
      }))
    })

    const sortPosts = () => {
      let sort = '';
      let direction = 'DESC';

      switch (selectedSort.value) {
        case 'popularity':
          sort = 'morePopular';
          direction = 'DESC';
          break;
        case 'latest':
          sort = 'createdAt';
          direction = 'DESC';
          break;
        case 'oldest':
          sort = 'createdAt';
          direction = 'ASC';
          break;
        default:
          sort = 'createdAt';
          direction = 'DESC';
      }

      fetch(`http://localhost:8080/api/board/sort?sort=${sort}&direction=${direction}`)
        .then(response => {
          if (!response.ok) {
            throw new Error('Network response was not ok');
          }
          return response.json();
        })
        .then(data => {
          visibleDatas.value = data;
        })
        .catch(error => {
          console.error('Error fetching sorted posts: ', error);
        });
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
      }));
    });

    const viewPost = (post) => {
      console.log('Navigating to post: ', post);
      router.push({ path: `/post/${post.postId}` });
    }

    const performSearch = async () => {
      if (!searchKeyword.value) {
        // alert('검색어를 입력하세요!');
        await fetchPosts();
        return;
      }

      try {
        const response = await axios.get(`http://localhost:8080/api/board/search`, {
          params: { keyword: searchKeyword.value },
        });

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

    onMounted(() => {
      fetchPosts();
    })

    return {
      goToCreatePost,
      selectedSort,
      sortPosts,
      viewPost,
      searchKeyword,
      performSearch,
      visibleDatas,
      fetchPosts,
      formattedPosts,
      formatDate,
      processedPosts
    }
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
    margin-right: 0px;
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
    bottom: -10px;
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
    bottom: -10px;
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
    background-image: url("@/assets/images/user_img.png");
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
    cursor: pointer;
  }
  
  .post-tags {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  /* 게시글 이미지 */
  .post-image {
    width: 180px;
    height: 150px;
    border-radius: 10px;
    margin-top: 10px;
    margin-right: 5px;
    cursor: pointer;
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
    margin-right: 20px;
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
    cursor: pointer;
  }
  
  .comment {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 10px;
  }
  
  .comment-icon {
    width: 25px;
    height: 25px;
    background-image: url("@/assets/images/comment.png");
    background-repeat: no-repeat;
    background-size: contain;
    background-position: center;
    margin-right: 7px;
    cursor: pointer;
  }
  
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
    margin-top: 5px;
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
    background-image: url("@/assets/images/search.icon.png");
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
    border: 1px solid whitesmoke;
    border-radius: 10px;
    margin: 5px;
    padding: 0;
    cursor: pointer;
  }

  .list-item.selected {
  border: 2px solid black;
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
  /* margin-left: 40px; */
  margin-bottom: 5px;
  margin-right: 7px;
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
  font-size: 25px;
  color: transparent;
  -webkit-text-stroke: 1px black;
}

.filled-heart::before {
  content: "\2764";
  font-size: 25px;
  color: red;
  -webkit-text-stroke: none;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 650px;
}
</style>
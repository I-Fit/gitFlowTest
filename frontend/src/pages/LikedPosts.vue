<template>
  <main>
    <AppNav />
    <div class="post">
      <div class="post-null-block"></div>
      <div class="post-top">
        <h2>게시글 관리</h2>
        <p class="line text01" @click="myPosts">내가 쓴 게시글</p>
        <p class="line text02" @click="myComments">내가 쓴 댓글</p>
        <p class="text03" @click="likedPosts">좋아요 한 게시물</p>
      </div>
      <div class="post-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input type="text" name="search" class="search-input" placeholder="검색어를 입력하세요." v-model="searchKeyword" @keydown.enter="performSearch" />
            <div class="search-icon" @click="performSearch"></div>
          </div>
          <select title="정렬" class="middle-filter-sort" v-model="sortOrder">
            <option value="" disabled>정렬</option>
            <option value="popular">인기순</option>
            <option value="latest">최신순</option>
          </select>
        </div>
        <div class="post-bottom-table">
          <!-- 서버에서 가져온 게시물들 -->
          <div class="bottom-table-group" v-for="post in visibleDatas" :key="post.id">
            <div class="table-group-del">
              <img
                id="modify_icon"
                src="@/assets/images/dot.png"
                alt="dot"
                @click="toggleActions(post.id)"
              />
              <!-- <PostActions
                class="post-actions"
                :visible="showActionsForPostId(post.id)"
                @navigate="handleNavigation"
              /> -->
            </div>
            <div class="table-group-postimg">
              <img class="post-image" :src="post.imageStr ? `data:image/png;base64,${post.imageStr}` : ''" alt="게시글 이미지" @click="viewPost(post)" />
            </div>
            <div class="table-group-btn">
              <div class="likes">
                <div class="title-heart" @click="toggleHeart(post.id)">
                  <div :class="{
                    'filled-heart': !post.isHeartFilled,
                    'empty-heart': post.isHeartFilled,
                  }"></div>
                </div>
                <span id="heart-count">{{ post.likes }}</span>
              </div>
              <div class="comment">
                <img class="btn-icon" src="@/assets/images/comment.png" alt="댓글 아이콘" />
                <span id="comment-count">{{ post.comments }}</span>
              </div>
            </div>
            <div class="table-group-content" @click="boardDetail(post.id)">
              <div class="group-content-post">
                <p class="table-group-title">{{ post.title || "제목없음" }}</p>
              </div>
              <div class="group-content-ptext">
                <p class="table-group-text">{{ post.content || "내용없음" }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="like-floor"></div>
    </div>
  </main>
</template>

<script>
import { ref, onBeforeMount } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import AppNav from "@/components/layout/AppNav.vue";
import apiClient from '@/api/apiClient';

export default {
  name: "LikedPosts",
  components: {
    AppNav,
  },

  setup() {
    const route = useRoute();
    const router = useRouter();

    const postId = route.params.postId;
    const userId = route.params.userId;
    const visibleDatas = ref([]);
    const searchKeyword = ref('');
    const selectedSort = ref('');
    const sortOrder = ref('');
    const showActions = ref(false);
    const isHeartFilled = ref(false);
    const likesCnt = ref(0);
    
    const myPosts = () => {
      router.push({ name: "MyPosts" });
    };

    const myComments = () => {
      router.push({ name: "MyComments" });
    };

    // 상세
    const viewPost = (post) => {
      console.log('Navigating to post: ', post);
      router.push({ path: `/post/${post.postId}` });
    }

    // 게시글 불러오기
    const fetchPosts = async () => {
      try {
        const response = await apiClient.get(`/board/posts/liked`);

        // if (!response.ok) {
        //   throw new Error('Network response was not ok');
        // }
        const data = response.data;
        visibleDatas.value = data;
        console.log('fetching posts: ', data);
        // sortPosts();
      } catch (error) {
        console.error('게시글 불러오기 실패: ', error);
      }
    };

    // 검색
    const performSearch = async () => {
      if (!searchKeyword.value) {
        // alert('검색어를 입력하세요!');
        await fetchPosts();
        return;
      }

      try {
        const response = await axios.get(`/api/board/search`, {
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

    // 정렬
    const sortPosts = () => {
      let sort = '';
      let direction = 'DESC';

      switch (selectedSort.value) {
        case 'popularity':
          sort = 'popularity';
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
        // default:
        //   sort = 'createdAt';
        //   direction = 'DESC';
      }

      console.log('sort: ', sort);
      console.log('direction: ', direction);

      fetch(`/api/board/sort?sort=${sort}&direction=${direction}`)
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


    // 좋아요
    const toggleHeart = async (postId) => {
      console.log("postId: ", postId);

      try {
        const response = await axios.post(`/api/board/${postId}/like`);
        
        if (response.status === 200) {
          const { likesCnt: newLikesCnt, heartFilled:newIsHeartFilled } = response.data;
          
          // console.log("좋아요수 : ", newLikesCnt);
          // console.log("isheartfilled (서버 응답): ", newIsHeartFilled);

          likesCnt.value = newLikesCnt;
          isHeartFilled.value = newIsHeartFilled;
        }
        // console.log("server response", response.data);
      } catch (error) {
        console.error('좋아요 토글 실패: ', error);
      }
    };

    // 수정/삭제 메뉴
    const toggleActions = () => {
      showActions.value = !showActions.value;
    };

    const handleNavigation = async (action) => {
      if (action === 'EditPost') {
        showActions.value = false;
        router.push({ path: `/edit-post/${postId}` });
      } else if (action === 'delete') {
        if (confirm('정말 삭제하시겠습니까?')) {
          await deletePost();
        }
      }
    };

    const deletePost = async () => {
      try {
        await apiClient.delete(`/board/delete/${postId}`);
        alert('게시글 삭제 완료!');
        router.push(`/board`);
      } catch(error) {
        console.error('게시물 삭제 실패: ', error);
      }
    };

    onBeforeMount(() => {
      fetchPosts();
    });

    return {
      myPosts,
      isHeartFilled,
      toggleHeart,
      visibleDatas,
      sortOrder,
      showActions,
      toggleActions,
      handleNavigation,
      myComments,
      sortPosts,
      performSearch,
      viewPost,
      userId,
    };
  },
};
</script>


<style scoped>
/* content 부분 */
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

.like-floor {
  height: 50px; /* floor 영역 높이 설정 */
  text-align: center;
  padding: 20px;
}

.search-input {
  border: none;
  outline: none;
  padding: 5px 0px 5px 10px;
  width: 100%;
}

.search-icon {
    width: 15px;
    height: 15px;
    background-image: url("@/assets/images/search.icon.png");
    background-size: contain;
    cursor: pointer;
    margin: 0 10px;
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
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.bottom-table-group {
  position: relative;
  width: 344px;
  height: 456px;
  border: 1px solid #ccc;
  border-radius: 10px;
  margin-right: 119px;
}

.bottom-table-group:nth-child(3n) {
  margin-right: 0px;
}

.post-actions {
  position: absolute;
  top: 30px; /* 상단에서 20px 위치 */
  right: -15px; /* 오른쪽에서 20px 위치 */
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
  display: flex; /* 버튼이 여러 개일 경우, flexbox로 배치할 수 있습니다 */
  flex-direction: column; /* 버튼을 수직으로 배치 */
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

.post-image {
  width: 263px;
  height: 222px;
  margin-top: 5px;
  cursor: pointer;
}
/* 
.table-group-post-images {
  width: 263px;
  height: 222px;
  margin-top: 5px;
} */

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

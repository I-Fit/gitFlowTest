<template>
  <main>
    <AppNav />
    <div class="party">
      <div class="party-top">
        <h2>모임 관리</h2>
        <p class="line text01">내가 만든 모임</p>
        <p class="line text02" @click="groupjoinlist">모임 참여 내역</p>
        <p class="text03" @click="likegroup">찜한 모임 내역</p>
      </div>
      <div class="party-middle">
        <div class="middle-filter">
          <div class="middle-filter-search-box">
            <input
              type="text"
              name="search"
              id="search-input"
              placeholder="검색어를 입력하세요."
              class="search-box-input"
            />
            <img
              src="@/assets/image/search.icon.png"
              alt="search"
              class="search-box-icon"
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
        <!-- 찜한 모임 내역 -->
        <div class="group">
          <div class="group-container">
            <div class="user-info">
              <img
                src="@/assets/image/user_img.png"
                alt="사용자 이미지"
                class="user-image"
              />
              <span>김계란</span>
              <img
                src="@/assets/image/상세설명 아이콘.png"
                alt=""
                class="detail-icon"
              />
            </div>
            <div class="group-content">
              <span class="title"> 수영 같이 하실 분 구함 </span>
            </div>
            <p class="date">24.06.14 (금)</p>
            <p class="time">8:00 PM</p>
            <div class="group-info">
              <img
                class="like-image"
                src="@/assets/image/heart_icon.png"
                alt="하트"
                @click="removeGroup"
              />
              <span class="size">참여인원: 3/10</span>
              <span class="location">강남구</span>
              <span class="cancel" @click="removeGroup">취소</span>
            </div>
          </div>
          <div class="group-container"></div>
          <div class="group-container"></div>
          <div class="group-container"></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import AppNav from '@/components/layout/AppNav.vue';
import { useRouter } from 'vue-router';

export default {
  name: "LikeGroup",
  components: {
      AppNav,
    },
  
    setup() {
      const router = useRouter();

      const groupjoinlist = () => {
        router.push({ name: "GroupJoinList" });
      };
      const likegroup = () => {
        router.push({ name: "LikeGroup" });
      };

      return {
        groupjoinlist,
        likegroup,
      }
    },

  methods: {
    removeGroup() {
      this.$emit("remove-group");
    },
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

.party {
  width: 1270px;
  height: 100%;
  display: grid;
  grid-template-rows: 150px 1fr;
}

.party-top {
  position: relative;
  width: 100%;
  height: 100%;
}

.party-top::after {
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
  margin-left: 1056px;
}

.line::after {
  content: "|";
  color: #ccc;
  margin: 0 5px 0 5px;
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
}

.text01,
.text02 {
  font-weight: lighter;
}

.party-middle {
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
}

.search-box-icon {
  width: 15px;
  height: 15px;
  margin-left: 45px;
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

/* 참여 한 모임 내역 css */
.group {
  width: 100%;
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.group-container {
  width: 360px;
  height: 300px;
  border: 1px solid #ccc;
  border-radius: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  margin: 29px 0 0 20px;
}

.user-image {
  width: 50px;
  height: 40px;
  margin-left: 10px;
}

.detail-icon {
  width: 20px;
  height: 20px;
  margin-left: 173px;
}

.group-content {
  display: flex;
  align-items: center;
  margin-top: 15px;
  justify-content: center;
}

.title {
  font-size: 28px;
  font-weight: bold;
  color: #5d5a88;
}

.date {
  font-size: 18px;
  color: #9795b5;
  margin-left: 218px;
  margin-top: 20px;
  padding: 5px;
}

.time {
  font-size: 18px;
  color: #9795b5;
  margin-left: 252px;
  padding: 5px;
}

.group-info {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.like-image {
  width: 35px;
  height: 35px;
  margin-right: 30px;
  cursor: pointer;
}

.size {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
}

.location {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 12px;
  margin-right: 5px;
}

.cancel {
  padding: 11px 17px;
  border: none;
  border-radius: 10px;
  background-color: #1a73e8;
  color: white;
  font-size: 12px;
  font-weight: bold;
  width: 58px;
  height: 38px;
}
</style>
<template>
  <footer>
    <div class="company-name">
      <p class="name">I-Fit</p>
      <p class="desc">Copyright @ 2024 KOSTA I-FIT | All Rights Reserved</p>
    </div>
    <div class="side-map">
      <div class="group1">
        <span class="home">홈</span>
        <span class="home-list">회원가입</span>
        <span class="home-list">로그인</span>
      </div>
      <div class="group2">
        <span class="activity">활동</span>
        <span class="activity-list">모임생성</span>
        <span class="activity-list">원데이 클래스</span>
        <span class="activity-list">오운완 챌린지</span>
      </div>
      <div class="group3">
        <span class="mypage">마이페이지</span>
        <span class="mypage-list">프로필</span>
        <span class="mypage-list">멤버십</span>
        <span class="mypage-list">참여내역</span>
        <span class="mypage-list" @click="mypost">나의 게시물</span>
      </div>
    </div>
  </footer>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: "AppFooter",

  setup() {
    const router = useRouter();

    const mypost = () => {
      router.push({ name: "PostHistory" });
    }

    return {
      mypost,
    }
  }
};
</script>

<style scoped>
footer {
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
  align-items: center;
}

.company-name {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  margin-right: 239px;
}

.name {
  font-size: 42px;
  font-weight: bold;
  color: #5d5a88;
  margin: 42px 0px 0px 0px;
}

.desc {
  margin: 0;
  font-size: 15px;
  color: #5d5a88;
  margin-bottom: 43px;
}

.side-map {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  margin-left: 197px;
  margin-top: 10px;
}

.side-map .group1,
.side-map .group2 {
  display: inline-block;
}

.group1 {
  margin-right: 20px;
}

.group3 {
  margin-top: 5px;
}

.side-map .group1 .home,
.side-map .group2 .activity,
.side-map .group3 .mypage {
  font-size: 18px;
  color: #5d5a88;
  font-weight: bold;
  margin-right: 10px;
}

.side-map .group1 .home-list,
.side-map .group2 .activity-list,
.side-map .group3 .mypage-list {
  font-size: 15px;
  font-weight: lighter;
  color: #9795b5;
}

.side-map .group1 span:nth-child(3)::before,
.side-map .group2 span:nth-child(n + 3)::before,
.side-map .group3 span:nth-child(n + 3)::before {
  content: " | ";
  color: #888;
}
</style>

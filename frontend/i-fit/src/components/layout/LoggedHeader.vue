<template>
  <header>
    <div class="icon-table">
      <div id="icon-img">
        <img src="../../public/image/icon.PNG" alt="I-FIT-icon" />
      </div>
      <div class="icon-text">I-Fit</div>
    </div>
    <div class="text-btn">
      <a href="/main.html">홈</a>
      <a href="#">모임 생성하기</a>
      <a href="#">오운완 챌린지</a>
      <a href="#">멤버십</a>
      <a href="#">마이페이지</a>
      <div class="login-btn">
        <img :src="userImage" alt="사용자 이미지" />
        <p class="login-user-name">{{ userName }}</p>
        <span>님, 환영합니다!</span>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: "Logged",
};
</script>

<style scoped>
header {
  display: grid;
  grid-template-columns: 384px 1fr;
}

.icon-table {
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon-text {
  font-size: 48px;
  font-weight: bold;
  color: #5d5a88;
  margin-left: 20px;
}

.text-btn {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  font-weight: bold;
}

a {
  text-decoration: none;
  color: #5d5a88;
}

a:nth-child(1) {
  margin-left: 150px;
}

.login-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.login-btn img {
  width: 50px;
  height: 40px;
}

.login-user-name {
  font-weight: bold;
}
</style>

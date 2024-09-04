<template>
  <main class="main">
    <h2>탈퇴 완료</h2>
    <p>탈퇴가 성공적으로 완료되었습니다.<br />감사합니다.</p>
  </main>
</template>

<script>
export default {
  name: "AccountDeleted",
};
</script>

<style scoped>
.container {
  height: 900px;
}

/* content 부분 */
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

h2 {
  font-size: 32px;
  font-weight: bold;
}

p {
  text-align: center;
  margin-top: 20px;
}
</style>
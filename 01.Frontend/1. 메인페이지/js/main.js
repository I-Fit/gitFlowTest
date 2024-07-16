// 메인 -> 로그인페이지
$(document).ready(function() {
  $(".login").click(function() {
    window.location.href = "http://127.0.0.1:5501/login.html";
  });
});

// 메인 -> 회원가입 페이지
$(document).ready(function() {
  $(".join").click(function() {
    window.location.href = "http://127.0.0.1:5501/create_account.html";
  });
});

// $(document).ready(function() {
//   ${""}.click(function() {
//     window.location.href = "";
//   });
// });
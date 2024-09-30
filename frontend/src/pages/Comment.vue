<template>
    <!-- 댓글 -->
    <div class="content-comment">
        <div class="comment-table">
          <div
            class="table-item comment-container"
            v-for="(comment, index) in comments"
            :key="index"
            :id="'comment-' + index"
            :class="{ highlighted: index === highlightedCommentIndex }"
          >
            <div @click="navigateToPost(postId, index)">
              <div :class="{ 'author-comment': comment.user === postAuthor }">
                <div class="comment-header">
                  <div class="item-user">{{ comment.user }}</div>
                  <div class="reply-button" @click="toggleReplyInput(index)">
                    댓글 달기
                  </div>
                </div>
              </div>
              <div class="item-text">{{ comment.text }}</div>

              <!-- 답글 입력 폼 -->
              <div v-if="replyInputs[index]" class="reply-input">
                <span class="reply-upload" @click="submitReply(index)"
                  >&#10551;</span
                >
                <input
                  v-model="comment.replyText"
                  placeholder="답글을 입력해주세요."
                  @keydown="checkReplyEnter($event, index)"
                />
              </div>

              <!-- 답글 목록 -->
              <div
                v-for="(reply, replyIndex) in comment.replies"
                :key="replyIndex"
                class="reply-item">
                <div class="item-user">{{ reply.user }}</div>
                <div class="item-text">{{ reply.text }}</div>
              </div>
              
            </div>
          </div>
        </div>
      </div>
      <!-- 댓글 추가 -->
    <div class="content-add">
      <input
        v-model="comment"
        placeholder="댓글을 입력해주세요."
        @keydown="checkEnter"
      />
      <span class="comment-upload" @click="submitComment">&#10550;</span>
    </div>
</template>

<script>
export default {
  name: "Comment",
  components: {
    PostActions,
  },
  props: ['id'],
  data() {
    return {
      post: null
    };
  },
  created() {
    this.fetchPost();
  },
  methods: {
    async fetchPost() {
      try {
        const response = await axios.get(`http://localhost:8080/api/board/post/${this.id}`);
        this.post = response.data;
      } catch (error) {
        console.error('게시글 로드 실패: ', error);
      }
    }
  },
  
  setup() {
    const router = useRouter();
    const route = useRoute();
    // const highlightedCommentIndex = ref(null);
    // const postAuthor = "김계란";
    // const comment = ref("");
    const isHeartFilled = ref(false);
    const showActions = ref(false);
    // const replyInputs = ref({});
    // const rawContent = ref("힘들었지만, 보람된 시간이였습니다.");

    const formattedContent = computed(() => {
      return rawContent.value;
    });

    const comments = ref([
      {
        user: "밤편지",
        text: "대단하십니다.",
        replies: [{ user: "김계란", text: "감사합니다! 열심히 했어요!!" }],
        replyText: "",
      },
      {
        user: "wimper",
        text: "며칠 걸리셨나요??",
        replies: [{ user: "김계란", text: "5일 걸렸습니다." }],
        replyText: "",
      },
      {
        user: "건넛",
        text: "담에 또 하실건가요? +_+",
        replies: [{ user: "김계란", text: "시원할 때, 다시 해보고 싶네요." }],
        replyText: "",
      },
      {
        user: "qwer",
        text: "저도! 한번 도전해보고 싶어요!! +_+",
        replies: [
          {
            user: "김계란",
            text: "일정 넉넉하게 잡으시면 누구든 가능합니다! 도전 ㄱㄱ",
          },
        ],
        replyText: "",
      },
      {
        user: "김계란",
        text: "댓글 남겨주셔서 감사합니다. 이따가 댓글 달아볼게요.",
        replies: [],
        replyText: "",
      },
      {
        user: "한강교차로인간",
        text: "3일 컷은 했었야지~",
        replies: [
          { user: "김계란", text: "제 능력으로 3일 컷은 무리입니다 ㅋㅋ" },
        ],
        replyText: "",
      },
      {
        user: "좋으면짖는개",
        text: "왈왈 크르르릉 왈왈!!",
        replies: [{ user: "김계란", text: "기분이 좋으시군요 ㅋㅋㅋ" }],
        replyText: "",
      },
      {
        user: "박일두",
        text: "거리 어떻게 배분하셨나요??",
        replies: [
          {
            user: "김계란",
            text: "마지막날 200km 달렸고요.. 다른 날들은 100km 정도로..",
          },
        ],
        replyText: "",
      },
      {
        user: "냉카",
        text: "종주 인증 메달이랑 인증서 신청하셨나요?",
        replies: [{ user: "김계란", text: "예전에 신청해서 받았어요!" }],
        replyText: "",
      },
      {
        user: "vivamondo",
        text: "나도 하고 싶다.",
        replies: [],
        replyText: "",
      },
      {
        user: "ilcapitano",
        text: "준비했던 물품들 알려주세요~ ^^",
        replies: [],
        replyText: "",
      },
      {
        user: "외데가르드",
        text: "공 차러 나와~",
        replies: [{ user: "김계란", text: "조만간 같이 공 찹시다 ㅋㅋ" }],
        replyText: "",
      },
    ]);

    const toggleActions = () => {
      showActions.value = !showActions.value;
    };

    const handleNavigation = (action) => {
      if (action === "EditPost") {
        router.push("/edit-post");
      } else if (action === "delete") {
        router.push("/board");
      }
    };

    const toggleHeart = () => {
      isHeartFilled.value = !isHeartFilled.value;
    };

    const checkEnter = (event) => {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        submitComment();
      }
    };

    const submitComment = () => {
      if (comment.value.trim() === "") {
        alert("댓글을 입력해주세요.");
        return;
      }
      comments.value.push({
        user: postAuthor,
        text: comment.value,
        replies: [],
        replyText: "",
      });
      comment.value = "";
    };

    const toggleReplyInput = (index) => {
      Object.keys(replyInputs.value).forEach((key) => {
        if (parseInt(key) !== index) {
          replyInputs.value[key] = false;
        }
      });
      replyInputs.value[index] = !replyInputs.value[index];
    };

    const checkReplyEnter = (event, index) => {
      if (event.key === "Enter" && !event.shiftKey) {
        event.preventDefault();
        submitReply(index);
      }
    };

    const submitReply = (index) => {
      const comment = comments.value[index];
      if (comment.replyText.trim() === "") {
        alert("답글을 입력해주세요.");
        return;
      }
      comment.replies.push({ user: postAuthor, text: comment.replyText });
      comment.replyText = "";
      toggleReplyInput(index);
    };

    const scrollToComment = (commentId) => {
      const element = document.getElementById(commentId);
      if (element) {
        element.scrollIntoView({ behavior: "smooth", block: "start" });
        highlightedCommentIndex.value = parseInt(commentId.split("-")[1]);
        setTimeout(() => {
          highlightedCommentIndex.value = null;
        }, 2000); // 강조 효과 2초간 유지
      } else {
        console.warn(`Element with id ${commentId} not found`);
      }
    };

    const navigateToPost = (postId, index) => {
      scrollToComment("comment-" + index);
    };

    onMounted(() => {
      const commentId = route.query.commentId;
      if (commentId) {
        scrollToComment("comment-" + commentId);
      }
    });

    return {
      formattedContent,
      // postAuthor,
      comment,
      isHeartFilled,
      showActions,
      replyInputs,
      comments,
      toggleActions,
      handleNavigation,
      toggleHeart,
      checkEnter,
      submitComment,
      toggleReplyInput,
      checkReplyEnter,
      submitReply,
      highlightedCommentIndex,
      scrollToComment,
      navigateToPost, // 이 부분을 반환에 추가해야 합니다.
    };
  },
};

</script>

<style>
</style>
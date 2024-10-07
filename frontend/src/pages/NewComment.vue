<template>
    <div class="new-comment">
        <input type="text" v-model="commentText" placeholder="댓글을 입력해주세요" @keydown.enter="submitComment" />
        <button class="submit-btn" @click="submitComment">작성</button>
    </div>
</template>

<script>
import axios from 'axios';
import { ref } from 'vue';

export default {
    name: 'NewComment',
    props: {
        postId: {
            type: Number,
            required: true,
        },
        userId: {
            type: Number,
            required: true,
        }
    },
    setup(props, { emit }) {
        const commentText = ref('');

        const submitComment = async () => {
            if (!commentText.value.trim()) {
                alert('댓글 내용을 입력해주세요!');
                return;
            }

            try {
                await axios.post(`http://localhost:8080/api/comments/new`, {
                    postId: props.postId,
                    content: commentText.value,
                    userId: props.userId,
                });
                commentText.value = '';
                emit('commentAdded'); // 부모 컴포넌트에 이벤트 발생
            } catch (error) {
                console.error('댓글 작성 실패: ', error);
                alert('댓글 작성 실패!');
            }
        };

        return {
            commentText,
            submitComment,
        };
    },
};
</script>

<style scoped>
.new-comment {
    display: flex;
    align-items: center;
    height: 50px;
}

.new-comment input {
    flex: 1;
    margin-right: 8px;
    padding-left: 8px;
    height: 35px;
}

.submit-btn {
    width: 55px;
    height: 35px;
    color: white;
    background-color: #1a73e8;
    border: none;
    border-radius: 5px;
}
</style>
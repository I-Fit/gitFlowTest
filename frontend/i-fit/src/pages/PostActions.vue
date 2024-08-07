<template>
  <div v-if="visible" class="post-actions">
    <button 
      class="action-btn" 
      @click="navigate('PostModify')" 
      style="background-color: #28a745; color: white;"
    >
      수정
    </button>
    <button 
      class="action-btn" 
      @click="confirmDelete" 
      style="background-color: #dc3545; color: white;"
    >
      삭제
    </button>

    <!-- 모달 창 -->
    <div v-if="showDeleteModal" class="modal">
      <div class="modal-content">
        <p>정말로 삭제하시겠습니까?</p>
        <div class="modal-actions">
          <button class="modal-btn" @click="deletePost">확인</button>
          <button class="modal-btn" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  props: {
    visible: {
      type: Boolean,
      required: true,
    },
  },
  data() {
    return {
      showDeleteModal: false,
    };
  },
  methods: {
    navigate(action) {
      this.$emit("navigate", action);
    },
    confirmDelete() {
      this.showDeleteModal = true;
    },
    closeModal() {
      this.showDeleteModal = false;
    },
    deletePost() {
      this.$emit("navigate", "delete");
      this.closeModal();
    },
  },
};
</script>

<style scoped>
.post-actions {
  position: absolute;
  top: 52px;
  right: 27px;
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.action-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 30px;
  text-align: center;
  
  border: none;
  border-radius: 4px;
  padding: 10px 5px;
  margin: 2px 0;
  cursor: pointer;
  font-size: 14px;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.4);
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 15px;
  border-radius: 8px;
  text-align: center;
  max-width: 300px; /* 모달의 최대 너비 */
  width: 90%; /* 모달의 너비 */
}

.modal-actions {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

.modal-btn {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 5px 10px;
  cursor: pointer;
  font-size: 14px;
}

.modal-btn:hover {
  background-color: #0056b3;
}

.modal-btn:nth-child(2) {
  background-color: #6c757d;
}

.modal-btn:nth-child(2):hover {
  background-color: #5a6268;
}
</style>

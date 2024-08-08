<template>
  <div class="pagination-container">
    <!-- 다른 컨텐츠가 여기 올 수 있습니다 -->
  </div>
  <div class="pagination">
    <button 
      :disabled="currentPage === 1" 
      @click="changePage(currentPage - 1)"
      class="pagination-btn"
    >
      &laquo; <!-- << 삽입 -->
    </button>
    
    <span 
      v-for="page in totalPages" 
      :key="page"
      @click="changePage(page)"
      :class="['pagination-number', { active: page === currentPage }]"
    >
      {{ page }}
    </span>
    
    <button 
      :disabled="currentPage === totalPages" 
      @click="changePage(currentPage + 1)"
      class="pagination-btn"
    >
      &raquo; <!-- >> 삽입 -->
    </button>
  </div>
</template>


<script>
export default {
  props: {
    currentPage: {
      type: Number,
      required: true
    },
    totalPages: {
      type: Number,
      required: true
    }
  },
  methods: {
    changePage(page) {
      this.$emit('page-changed', page);
    }
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh; /* 전체 화면 높이 */
  justify-content: flex-end; /* 하단으로 정렬 */
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 970px; /* 페이지네이션의 여백 설정 */
}

.pagination-btn {
  background-color: white;
  border: none;
  padding: 5px 10px;
  margin: 0 5px;
  cursor: pointer;
  border-radius: 5px;
}

.pagination-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.pagination-number {
  margin: 0 5px;
  padding: 5px 10px;
  cursor: pointer;
  border: 1px solid transparent;
  border-radius: 5px;
}

.pagination-number.active {
  background-color: #007bff;
  color: #fff;
  border: 1px solid #007bff;
}

.pagination-number:hover:not(.active) {
  background-color: #e9ecef;
}
</style>
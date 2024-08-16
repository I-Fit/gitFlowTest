import { ref, computed } from 'vue';

export function usePagination(datas, PerPage) {
    // 현재 페이지 나타내는 반응형 변수
    const currentPage = ref(1);

    // 총 페이지 수를 계산하는 반응형 계산 속성
    const totalPages = computed(() => {
        return Math.ceil(datas.value.length / PerPage);
    });

    // 현재 페이지에 보여질 항목을 계산
    const visibleDatas = computed(() => {
        let startIndex = (currentPage.value - 1) * PerPage;
        let endIndex = startIndex + PerPage;
        return datas.value.slice(startIndex, endIndex);
    });

    // 페이지를 설정하는 함수
    const fetchdatas = (page = currentPage.value) => {
        currentPage.value = page;
    };

    // 페이지가 변경될 때 호출되는 함수
    const onPageChange = (page) => {
        if (page < 1 || page > totalPages.value) return;
        fetchdatas(page);
    };

    return {
        currentPage,    // 현재페이지
        totalPages,     // 전체 페이지 수
        visibleDatas,  // 현재 페이지에 보여질 그룹
        onPageChange,   // 페이지를 설정
        fetchdatas,    // 페이지 변경 시 호출되는 함수
    };
}
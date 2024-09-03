document.addEventListener("DOMContentLoaded", function() {
    // 저장 버튼 클릭 이벤트 처리
    document.getElementById("communityInsertBtn").addEventListener("click", function() {
        document.getElementById("communityInsertForm").submit();
    });

    // 취소 버튼 클릭 이벤트 처리
    document.getElementById("communityCancelBtn").addEventListener("click", function() {
        // 취소 시 동작 (예: 폼 초기화 또는 이전 페이지로 이동)
        document.getElementById("communityInsertForm").reset();
    });

    // 목록 버튼 클릭 이벤트 처리
    document.getElementById("communityListBtn").addEventListener("click", function() {
        window.location.href = "/community/communityPostList";
    });
});

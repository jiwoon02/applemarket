document.addEventListener("DOMContentLoaded", function() {
    // 수정 버튼 클릭 이벤트 처리
    document.getElementById("communityUpdateBtn").addEventListener("click", function() {
        document.getElementById("updateForm").submit();
    });

    // 취소 버튼 클릭 이벤트 처리
    document.getElementById("communityCancelBtn").addEventListener("click", function() {
        // 취소 시 동작 (예: 이전 페이지로 이동)
        window.history.back();
    });

    // 목록 버튼 클릭 이벤트 처리
    document.getElementById("communityListBtn").addEventListener("click", function() {
        window.location.href = "/community/communityPostList";
    });
});

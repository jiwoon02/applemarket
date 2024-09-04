document.addEventListener("DOMContentLoaded", function() {
    // 글수정 버튼 클릭 이벤트 처리
    document.getElementById("updateFormBtn").addEventListener("click", function() {
        var postId = document.querySelector("input[name='communityPostID']").value;
        window.location.href = "/community/communityUpdateForm/" + postId;
    });

    // 글삭제 버튼 클릭 이벤트 처리
    document.getElementById("boardDeleteBtn").addEventListener("click", function() {
        var postId = document.querySelector("input[name='communityPostID']").value;
        if (confirm("정말 삭제하시겠습니까?")) {
            window.location.href = "/community/posts/" + postId + "/delete";
        }
    });

    // 신고 버튼 클릭 이벤트 처리
    document.getElementById("reportBtn").addEventListener("click", function() {
        console.log("신고가 완료되었습니다");
    });
});

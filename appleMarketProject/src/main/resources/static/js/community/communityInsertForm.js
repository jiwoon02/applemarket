document.addEventListener("DOMContentLoaded", function() {
    // 저장 버튼 클릭 이벤트 처리
    document.getElementById("communityInsertBtn").addEventListener("click", function(event) {
        const title = document.querySelector("[name='communityTitle']").value;
        const content = document.querySelector("[name='communityContent']").value;

        if (!title && !content) {
            console.log("제목과 내용을 입력해주세요.");
            event.preventDefault();
        } else if (!title) {
            console.log("제목을 입력해주세요.");
            event.preventDefault();
        } else if (!content) {
            console.log("내용을 입력해주세요.");
            event.preventDefault();
        } else {
            document.getElementById("communityInsertForm").submit();
        }
    });

    // 목록 버튼 클릭 이벤트 처리
    document.getElementById("communityListBtn").addEventListener("click", function() {
        window.location.href = "/community/communityPostList";
    });
});

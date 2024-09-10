document.addEventListener("DOMContentLoaded", function() {
    // 수정 버튼 클릭 이벤트 처리
    const updateBtn = document.getElementById("communityUpdateBtn");

    if (updateBtn) { // 버튼이 있을 경우만 이벤트 처리
        updateBtn.addEventListener("click", function(event) {
            event.preventDefault(); // 기본 폼 제출 방지

            // 폼 데이터 가져오기
            const form = document.getElementById("updateForm");
            const postId = document.querySelector("input[name='communityPostID']").value; // postId 가져오기

            const formData = new FormData(form); // FormData 객체 생성

            // FormData 디버깅용 콘솔 로그
            for (let [key, value] of formData.entries()) {
                console.log(`${key}: ${value}`);
            }

            // POST 요청 보내기
            fetch(`/community/posts/${postId}/update`, {
                method: 'POST',
                body: formData,
            })
            .then(response => {
                if (response.ok) {
                    console.log("POST 요청 성공");
                    window.location.href = `/community/communityPostList`; // 수정 후 목록으로 이동
                } else {
                    return response.text().then(text => { 
                        console.error('POST 요청 실패:', text);
                        alert(`POST 요청 실패: ${text}`);
                    });
                }
            })
            .catch(error => {
                console.error('POST 요청 중 오류 발생:', error);
                alert('POST 요청 중 오류 발생: ' + error.message);
            });
        });
    }

    // 취소 버튼 클릭 이벤트 처리
    const cancelBtn = document.getElementById("communityCancelBtn");
    if (cancelBtn) {
        cancelBtn.addEventListener("click", function() {
            window.history.back(); // 이전 페이지로 이동
        });
    }

    // 목록 버튼 클릭 이벤트 처리
    const listBtn = document.getElementById("communityListBtn");
    if (listBtn) {
        listBtn.addEventListener("click", function() {
            window.location.href = "/community/communityPostList";
        });
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // 글 수정 버튼 클릭 이벤트 처리
    document.getElementById("updateFormBtn")?.addEventListener("click", function() {
        var postId = document.querySelector("input[name='communityPostID']").value;
        window.location.href = "/community/communityUpdateForm/" + postId;
    });

    // 글 삭제 버튼 클릭 이벤트 처리
    const deleteBtn = document.getElementById('boardDeleteBtn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', function() {
            const postId = document.querySelector("input[name='communityPostID']").value;
            console.log(postId);  // postId 값을 확인
            if (confirm('정말로 삭제하시겠습니까?')) {
                // GET 방식으로 요청 보내기
                fetch(`/community/posts/${postId}/delete`, {
                    method: 'GET'  // GET 방식으로 변경
                })
                .then(response => {
                    if (response.ok) {
                        alert('게시글이 삭제되었습니다.');
                        window.location.href = '/community/communityPostList';
                    } else {
                        alert('게시글 삭제에 실패했습니다.');
                    }
                })
                .catch(error => {
                    console.error('게시글 삭제 중 오류 발생:', error);
                    alert('게시글 삭제 중 오류가 발생했습니다.');
                });
            }
        });
    }

    // 이미지를 로드하는 함수
    function loadImage(postId) {
        fetch(`/community/image/base64/${postId}`)
            .then(response => response.text())
            .then(imageDataUrl => {
                if (imageDataUrl) {
                    document.getElementById('communityImage').src = imageDataUrl;
                } else {
                    document.getElementById('communityImage').alt = "이미지가 없습니다.";
                }
            })
            .catch(error => {
                console.error("이미지를 가져오는 중 오류 발생:", error);
            });
    }

    // 페이지 로드 시 communityPostID로 이미지를 로드
    const postId = document.querySelector("input[name='communityPostID']").value;
    loadImage(postId);

    // 신고 버튼 클릭 이벤트 처리
    const reportBtn = document.getElementById("reportBtn");
    if (reportBtn) {
        reportBtn.addEventListener("click", function() {
            // JWT 쿠키를 확인
            var token = getCookie("JWT");

            // 로그인되지 않은 경우 처리
            if (!token) {
                alert("로그인이 필요합니다.");
                return; // 신고 요청을 보내지 않음
            }

            var reason = prompt("신고 사유를 입력해주세요:");
            if (reason) {
                var postId = document.querySelector("input[name='communityPostID']").value;
                fetch("/community/posts/" + postId + "/report", {
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: new URLSearchParams({
                        'reason': reason
                    })
                })
                .then(response => response.text())
                .then(result => {
                    alert(result); // 서버로부터의 응답 메시지
                })
                .catch(error => {
                    console.error('신고 중 오류 발생:', error);
                    alert("신고 중 오류가 발생했습니다.");
                });
            }
        });
    }

    // 쿠키 가져오기 함수
    function getCookie(name) {
        let value = "; " + document.cookie;
        let parts = value.split("; " + name + "=");
        if (parts.length === 2) return parts.pop().split(";").shift();
    }
});

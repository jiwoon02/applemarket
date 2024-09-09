document.addEventListener("DOMContentLoaded", function() {
    // 글수정 버튼 클릭 이벤트 처리
    document.getElementById("updateFormBtn")?.addEventListener("click", function() {
        var postId = document.querySelector("input[name='communityPostID']").value;
        window.location.href = "/community/communityUpdateForm/" + postId;
    });

    // 글삭제 버튼 클릭 이벤트 처리
    document.getElementById("boardDeleteBtn")?.addEventListener("click", function() {
        var postId = document.querySelector("input[name='communityPostID']").value;
        if (confirm("정말 삭제하시겠습니까?")) {
            window.location.href = "/community/posts/" + postId + "/delete";
        }
    });

    // 신고 버튼 클릭 이벤트 처리
    document.getElementById("reportBtn")?.addEventListener("click", function() {
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
                console.error('Error reporting the post:', error);
                alert("신고 중 오류가 발생했습니다.");
            });
        }
    });

    // 쿠키 가져오기 함수
    function getCookie(name) {
        let value = "; " + document.cookie;
        let parts = value.split("; " + name + "=");
        if (parts.length === 2) return parts.pop().split(";").shift();
    }
});

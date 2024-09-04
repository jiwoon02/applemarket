document.addEventListener("DOMContentLoaded", function() {
    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 10; // 페이지당 게시글 수
    let isLoading = false; // 데이터 로딩 상태 플래그
    const postList = document.querySelector('.list'); // 게시글 리스트 영역

    // 글쓰기 버튼 클릭 이벤트 처리
    document.getElementById("createPostBtn").addEventListener("click", function() {
        window.location.href = "/community/communityInsertForm";
    });

    // 게시글 제목 클릭 이벤트 처리
    document.querySelectorAll(".goDetail").forEach(function(element) {
        element.addEventListener("click", function() {
            var postId = this.getAttribute("data-post-id");
            window.location.href = "/community/communityPostDetail/" + postId;
        });
    });

    // IntersectionObserver 설정
    const io = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (!entry.isIntersecting) return;
            if (isLoading) return;

            loadPosts(currentPage, pageSize); // 게시글 로드 함수 호출
            currentPage++; // 페이지 번호 증가
        });
    });

    // 감시 시작
    io.observe(document.getElementById('sentinel'));

    // 게시글 리스트 로드 함수
    function loadPosts(page, size) {
        isLoading = true;
        
        fetch(`/community/api/communityPostList?page=${page}&size=${size}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const posts = data.content;
                if (posts.length === 0) {
                    io.unobserve(document.getElementById('sentinel')); // 더 이상 로드할 데이터가 없으면 감시 중지
                    return;
                }

                posts.forEach(post => {
                    const tr = document.createElement('tr');
                    tr.classList.add('text-center');
                    tr.innerHTML = `
                        <td>${post.communityPostID}</td>
                        <td>
                            <span class="goDetail" data-post-id="${post.communityPostID}">${post.communityTitle}</span>
                        </td>
                        <td>${post.userNo}</td>
                        <td>${new Date(post.communityRegDate).toLocaleDateString()}</td>
                        <td>${post.communityCount}</td>
                    `;
                    postList.appendChild(tr);

                    // 새로 추가된 게시글에 클릭 이벤트 추가
                    tr.querySelector(".goDetail").addEventListener("click", function() {
                        var postId = this.getAttribute("data-post-id");
                        window.location.href = "/community/communityPostDetail/" + postId;
                    });
                });

                isLoading = false;
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                isLoading = false;
            });
    }
});

document.addEventListener("DOMContentLoaded", function() {
    let currentPage = 0; // 현재 페이지 번호
    const pageSize = 10; // 페이지당 게시글 수
    let isLoading = false; // 데이터 로딩 상태 플래그
    const postList = document.querySelector('.list'); // 게시글 리스트 영역
    let currentQuery = ""; // 현재 검색 쿼리

    // 글쓰기 버튼 클릭 이벤트 처리
    const createPostBtn = document.querySelector(".btn-primary");
    if (createPostBtn) {
        createPostBtn.addEventListener("click", function() {
            window.location.href = "/community/communityInsertForm";
        });
    }

    // 검색 버튼 클릭 이벤트 처리
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    searchBtn.addEventListener('click', function() {
        searchPosts(searchInput.value);
    });

    // 엔터 키 이벤트 처리
    searchInput.addEventListener('keydown', function(event) {
        if (event.key === 'Enter') {
            searchPosts(searchInput.value);
        }
    });

    // 검색 기능
    function searchPosts(query) {
        currentPage = 0; // 검색 시 첫 페이지로 설정
        currentQuery = query; // 현재 검색어 업데이트
        postList.innerHTML = ''; // 기존 게시글 목록 비우기
        loadPosts(currentPage, pageSize, currentQuery); // 검색된 게시글 로드
    }

    // 게시글 리스트 로드 함수
    function loadPosts(page, size, query = '') {
        if (isLoading) return; // 이미 로딩 중이면 중복 로드를 방지
        isLoading = true;

        // 서버로 검색 쿼리 포함하여 요청
        fetch(`/community/api/communityPostList?offset=${page}&limit=${size}&query=${encodeURIComponent(query)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const posts = data.content;
                if (posts.length === 0 && page === 0) {
                    postList.innerHTML = '<p>검색 결과가 없습니다.</p>'; // 검색 결과가 없는 경우 처리
                    isLoading = false;
                    return;
                }

                posts.forEach(post => {
                    const row = document.createElement('div');
                    row.classList.add('flex', 'items-center', 'border', 'p-4', 'rounded-lg', 'mb-2');
                    row.innerHTML = `
                        <div class="flex-shrink-0">
                            <div class="w-12 h-12 bg-gray-300 rounded-full flex items-center justify-center text-gray-700 font-bold">
                                ${post.userName}
                            </div>
                        </div>
                        <div class="ml-4 flex-grow">
                            <div class="text-lg font-semibold goDetail" data-post-id="${post.communityPostID}">
                                ${post.communityTitle}
                            </div>
                            <div class="text-gray-500">
                                ${new Date(post.communityRegDate).toLocaleDateString()} / 조회수: ${post.communityCount}
                            </div>
                        </div>
                    `;
                    postList.appendChild(row);

                    // 게시글 제목 클릭 이벤트 처리
                    row.querySelector(".goDetail").addEventListener("click", function() {
                        const postId = this.getAttribute("data-post-id");
                        if (postId && !isNaN(postId)) {
                            window.location.href = "/community/communityPostDetail/" + postId;
                        } else {
                            console.error("Invalid postId:", postId);
                            alert("유효하지 않은 게시글 ID입니다.");
                        }
                    });
                });

                isLoading = false;
            })
            .catch(error => {
                console.error('Error loading posts:', error);
                isLoading = false;
            });
    }

    // IntersectionObserver 설정
    const io = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (!entry.isIntersecting || isLoading) return;
            loadPosts(currentPage, pageSize, currentQuery); // 게시글 로드 함수 호출
            currentPage++; // 페이지 번호 증가
        });
    });

    // 감시 시작
    const sentinel = document.getElementById('sentinel');
    if (sentinel) {
        io.observe(sentinel);
    }
});

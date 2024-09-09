document.addEventListener("DOMContentLoaded", function() {
    // 댓글 등록 버튼 클릭 이벤트
    document.getElementById("commentInsertBtn").addEventListener("click", function(event) {
        event.preventDefault(); // 기본 제출 방지

        // 폼 데이터 가져오기
        const commentContent = document.getElementById("commentContent").value;
        const communityPostID = document.querySelector("input[name='communityPostID']").value;

        // 유효성 검사
        if (!commentContent) {
            alert('댓글 내용을 입력해 주세요.');
            return;
        }

        // 서버로 전송할 데이터 객체 생성
        const commentData = {
            commentContent: commentContent,
            communityPost: { communityPostID: communityPostID }
        };

        // AJAX 요청으로 댓글 등록
        fetch('/comments/insert', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(commentData)
        })
        .then(response => response.text())
        .then(result => {
            if (result === 'SUCCESS') {
                alert('댓글이 성공적으로 등록되었습니다.');
                loadComments(communityPostID);  // 댓글 등록 후 즉시 갱신
            } else {
                alert(result);
            }
        })
        .catch(error => console.error('Error:', error));
    });

    // 댓글 리스트 로드 함수
	function loadComments(communityPostID) {
	    fetch(`/comments/all/${communityPostID}`)
	    .then(response => response.json())
	    .then(comments => {
	        const commentList = document.getElementById("commentList");
	        commentList.innerHTML = ''; // 기존 댓글 목록 초기화

	        comments.forEach(comment => {
	            const commentTemplate = document.createElement('div');
	            commentTemplate.classList.add('comment-item', 'flex', 'items-start', 'space-x-4');
	            commentTemplate.innerHTML = `
	                <div class="profile-icon flex items-center justify-center">
	                    <div class="user-circle">
	                        <span class="user-name">${comment.userName}</span>
	                    </div>
	                </div>
	                <div class="comment-content-wrapper flex-1">
	                    <div class="comment-body bg-gray-100 p-4 rounded-lg relative">
	                        <div class="flex items-center justify-between">
	                            <div class="comment-date">
	                                <span>${new Date(comment.commentRegDate).toLocaleString()}</span>
	                            </div>
	                            ${comment.commentOwner ? `
	                                <div class="comment-buttons" style="position: absolute; bottom: 10px; right: 10px;">
	                                    <button type="button" data-btn="upBtn" class="update-comment-btn" data-id="${comment.commentID}" style="margin-right: 5px;">수정</button>
	                                    <button type="button" data-btn="delBtn" class="delete-comment-btn" data-id="${comment.commentID}">삭제</button>
	                                </div>
	                            ` : ''}
	                        </div>
	                        <p class="comment-content">${comment.commentContent}</p>
	                        <textarea class="comment-edit-input hidden" data-id="${comment.commentID}" style="width: 100%; display: none;">${comment.commentContent}</textarea>
	                    </div>
	                </div>
	            `;

	            commentList.appendChild(commentTemplate);

	            // 삭제 및 수정 버튼에 대한 이벤트 핸들러 추가
	            if (comment.commentOwner) {
	                const deleteBtn = commentTemplate.querySelector('[data-btn="delBtn"]');
	                deleteBtn?.addEventListener('click', () => {
	                    deleteComment(comment.commentID, communityPostID);
	                });

	                const updateBtn = commentTemplate.querySelector('[data-btn="upBtn"]');
	                updateBtn?.addEventListener('click', () => {
	                    const commentContent = commentTemplate.querySelector('.comment-content');
	                    const editInput = commentTemplate.querySelector('.comment-edit-input');

	                    // 댓글 내용을 숨기고 수정할 수 있는 입력창 표시
	                    commentContent.style.display = 'none';
	                    editInput.style.display = 'block';

	                    // 엔터키로 수정 완료
	                    editInput.addEventListener('keypress', function(event) {
	                        if (event.key === 'Enter') {
	                            updateComment(comment.commentID, communityPostID, editInput.value);
	                        }
	                    });
	                });
	            }
	        });
	    })
	    .catch(error => console.error('Error loading comments:', error));
	}


	// 댓글 삭제 함수
	function deleteComment(commentId, communityPostID) {
	    fetch(`/comments/delete/${commentId}`, {
	        method: 'DELETE',
	        headers: {
	            'Content-Type': 'application/json'
	        }
	    })
	    .then(() => {
	        alert('댓글이 삭제되었습니다.');
	        loadComments(communityPostID);  // 댓글 삭제 후 즉시 갱신
	    })
	    .catch(error => {
	        console.error('에러 발생:', error);
	        alert('댓글 삭제 중 에러가 발생했습니다.');
	    });
	}

	// 댓글 수정 함수
	function updateComment(commentID, communityPostID, newContent) {
	    const updateData = { commentContent: newContent };

	    fetch(`/comments/update/${commentID}`, {
	        method: 'PUT',
	        headers: {
	            'Content-Type': 'application/json'
	        },
	        body: JSON.stringify(updateData)
	    })
	    .then(() => {
	        alert('댓글 수정이 완료되었습니다.');
	        loadComments(communityPostID);  // 댓글 수정 후 즉시 갱신
	    })
	    .catch(error => {
	        console.error('댓글 수정 중 에러 발생:', error);
	        alert('댓글 수정 중 에러가 발생했습니다.');
	    });
	}

    // 페이지 로드 시 댓글 리스트 불러오기
    const communityPostID = document.querySelector("input[name='communityPostID']").value;
    loadComments(communityPostID);
});

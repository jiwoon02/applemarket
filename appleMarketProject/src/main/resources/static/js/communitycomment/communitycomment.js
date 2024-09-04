document.addEventListener("DOMContentLoaded", function() {
    // 댓글 등록 버튼 클릭 이벤트
    document.getElementById("commentInsertBtn").addEventListener("click", function() {
        // 폼 데이터 가져오기
        const userID = document.getElementById("userID").value;
        const commentContent = document.getElementById("commentContent").value;
        const communityPostID = document.querySelector("input[name='communityPostID']").value; // 게시글 ID 가져오기

        // 서버로 전송할 데이터 객체 생성
        const commentData = {
            userID: userID,
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
                // 댓글 리스트 갱신 또는 페이지 새로고침
                loadComments(communityPostID);
            } else {
                alert('댓글 등록에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
    });

    // 댓글 리스트를 로드하는 함수
    function loadComments(communityPostID) {
        fetch(`/comments/all/${communityPostID}`)
        .then(response => response.json())
        .then(comments => {
            const commentList = document.getElementById("commentList");
            commentList.innerHTML = ''; // 기존 댓글 목록 초기화

            comments.forEach(comment => {
                const commentTemplate = document.createElement('div');
                commentTemplate.classList.add('card', 'mb-2');
                commentTemplate.innerHTML = `
                    <div class="card-header">
                        <span class="name">${comment.userID}</span>
                        <span class="date">${new Date(comment.commentRegDate).toLocaleString()}</span>
                        <button type="button" data-btn="upBtn" class="btn btn-secondary btn-sm">수정하기</button>
                        <button type="button" data-btn="delBtn" class="btn btn-secondary btn-sm">삭제하기</button>
                    </div>
                    <div class="card-body">
                        <p class="card-text">${comment.commentContent}</p>
                    </div>
                `;

                commentList.appendChild(commentTemplate);

                // 댓글 수정/삭제 버튼에 대한 이벤트 처리 추가 가능
                commentTemplate.querySelector('[data-btn="delBtn"]').addEventListener('click', () => {
                    deleteComment(comment.commentID, communityPostID);
                });

                commentTemplate.querySelector('[data-btn="upBtn"]').addEventListener('click', () => {
                    updateComment(comment.commentID, communityPostID);
                });
            });
        })
        .catch(error => console.error('Error loading comments:', error));
    }

	// 댓글 삭제 버튼 이벤트 리스너 설정
   	document.querySelectorAll('.delete-comment').forEach(button => {
       button.addEventListener('click', (event) => {
           event.preventDefault(); // 기본 이벤트 취소
           const commentId = button.getAttribute('data-id');
           
           // AJAX로 댓글 삭제 요청 (서버로 삭제 요청)
           fetch(`/comments/${commentId}/delete`, {
               method: 'DELETE',
               headers: {
                   'Content-Type': 'application/json'
               }
           })
           .then(response => {
               if (response.ok) {
                   button.closest('.flex').remove(); // 성공 시 부모 댓글 요소 삭제
               } else {
                   console.error('댓글 삭제 실패');
               }
           })
           .catch(error => {
               console.error('에러 발생:', error);
           });
       });
   });

    // 댓글 수정 함수
    function updateComment(commentID, communityPostID) {
        const newContent = prompt("수정할 내용을 입력하세요:");
        if (newContent) {
            const updateData = {
                commentContent: newContent
            };

            fetch(`/comments/update/${commentID}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updateData)
            })
            .then(response => response.text())
            .then(result => {
                if (result === 'SUCCESS') {
                    alert('댓글이 수정되었습니다.');
                    loadComments(communityPostID);
                } else {
                    alert('댓글 수정에 실패했습니다.');
                }
            })
            .catch(error => console.error('Error updating comment:', error));
        }
    }

    // 페이지 로드 시 댓글 리스트 불러오기
    const communityPostID = document.querySelector("input[name='communityPostID']").value;
    loadComments(communityPostID);
});

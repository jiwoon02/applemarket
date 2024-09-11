var communityID = $('#communityID').val();
document.addEventListener("DOMContentLoaded", function() {

	// 이미지를 로드하는 함수
	function loadImage(postId) {
	    fetch(`/admin/success/image/base64/${postId}`)
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
	
})

	$("#deleteBtn").on("click", function() {
		
		console.log(communityID);
		
		if(confirm("정말 삭제하시겠습니까?")){
			$.ajax({
				url: '/admin/success/community/' + communityID + '/detail/delete',
				type: 'POST',
				contentType: 'application/json',
				data: JSON.stringify([communityID]),
				success: function(response) {
					alert('게시글이 삭제되었습니다.');
				},
				error: function(xhr, status, error) {
					alert('게시글 삭제에 실패했습니다.');
				}
			})
			
			setTimeout(function() {
				locationProcess("/admin/success/community");
			}, 100);
		}
		
	})
var communityID = $('#communityID').val();
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
$(function() {
	$(".noteLink").on("click", function() {
		const selectedReason = $(this).text().trim();  // 선택한 탈퇴 사유 가져오기
        const encodedReason = encodeURIComponent(selectedReason);  // URL에 안전하게 사용하기 위해 인코딩
        location.href = "/mypage/withdrawNote?reason=" + encodedReason;  // 쿼리 파라미터로 이유 전송
	});
	
	$("#commentLink").on("click", function() {
		location.href ="/mypage/withdrawComment";
	});
	
	$(".wdCancelBtn").on("click", function() {
        history.back(); // 이전 페이지로 돌아감
    });
	
	$(".wdSubmitBtn").on("click", function() {
		const reason = $(".withdrawText").val().trim();
		        
        if (!reason) {
            alert("탈퇴 사유를 작성해 주세요.");
            e.preventDefault(); // 폼 전송을 막음
        }
		
	})
});


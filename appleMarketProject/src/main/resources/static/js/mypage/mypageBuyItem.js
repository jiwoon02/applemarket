$(function() {
	$("#fullChk").on("click", function() {
		if ($("#fullChk").prop("checked")) {
			$(".buyChk").prop("checked", true);
		} else {
			$(".buyChk").prop("checked", false);
		}
	});
	
	$("#deleteSelectedBtn").on("click", function(e) {
		// 선택된 항목 삭제 버튼 클릭 시
		e.preventDefault();
		
        if ($(".buyChk:checked").length > 0) {
            if (confirm("선택한 항목을 삭제하시겠습니까?")) {
                $("#deleteForm").submit();
            }
        } else {
            alert("삭제할 항목을 선택하세요.");
        }
    });
	
	// 리뷰 작성 버튼 클릭 시 이벤트 처리
    $(".reviewBtn").on("click", function() {
        // 클릭한 버튼에 해당하는 상품의 데이터를 가져오기
        var row = $(this).closest("tr");
        var productName = document.getElementById("itemName").textContent
        var shopId = document.getElementById("shopId").textContent
        var productId = row.find(".buyChk").val();
        var userNo = $("#userNoHide").val(); // userNo를 서버에서 전달받아 설정
        
        // 리뷰 페이지로 이동할 URL 생성
        var reviewUrl = "/mypage/addReview/" + productName + "/" + shopId + "/" + productId + "/" + userNo;

        // 페이지 이동
        location.href = reviewUrl;
    });
});
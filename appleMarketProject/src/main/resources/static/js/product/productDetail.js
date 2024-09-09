$("#productUpdateFormBtn").click(function(){
	actionProcess("#dataForm", "post", "/product/updateForm");
});

$("#productDeleteBtn").click(function(){
	if(confirm("정말 삭제하시겠습니까?")){
		actionProcess("#dataForm", "post", "/product/productDelete");
	}
});


$("#BuyBtn").on("click", function(){
	//order-insertform.html으로 이동
	//선택한 product 데이터를 함께 전달
	actionProcess("#dataForm","get","/order/insertForm");
});


$("#ChatBtn").on("click", function(){
	//chatroom/chatroom(채팅 페이지)으로 이동
	//선택한 productID를 함께 전달
	let productID = $("#productID").val();
	locationProcess("/chatroom/chatroom?productID="+productID);
});

$(document).ready(function() {
	console.log($("#wishListNo").val());
    if ($("#wishListNo").val() != null) {
        $("#WishBtn").css("background-color", "gray");
		$("#WishBtn").text("찜완료");
    } else {
        $("#WishBtn").css("background-color", "black");
		$("#WishBtn").text("찜하기");
    }
	
	$("#WishBtn").on("click", function() {
		let productID = $("#productID").val();
		
		// 찜 목록이 이미 존재하는 경우
		if ($("#wishListNo").val() != null) {
			// 찜 취소 요청을 보냄
			$.ajax({
				url: "/wishlist/delete",  // 찜 취소 처리 URL
				method: "POST",
				contentType: "application/json",
				data: JSON.stringify({ 
					productID: productID 
				}),
				success: function(response) {
					alert("찜하기가 취소되었습니다.");
					location.reload();
				},
				error: function(error) {
					alert("찜하기 취소 중 오류가 발생했습니다.");
				}
			});
		} else {
			// 찜 추가 요청을 보냄
			$.ajax({
				url: "/wishlist/add",  // 찜 추가 처리 URL
				method: "POST",
				contentType: "application/json",
				data: JSON.stringify({ 
					productID: productID 
				}),
				success: function(response) {
					alert("찜목록에 추가되었습니다.");
					location.reload();
				},
				error: function(error) {
					alert("찜하기 추가 중 오류가 발생했습니다.");
				}
			});
		}
	});
	
});

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


$("#productUpdateFormBtn").click(function(){
	actionProcess("#dataForm", "post", "/product/updateForm");
});

$("#productDeleteBtn").click(function(){
	if(confirm("정말 삭제하시겠습니까?")){
		actionProcess("#dataForm", "post", "/product/productDelete");
	}
});

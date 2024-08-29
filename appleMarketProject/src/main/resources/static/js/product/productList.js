//페이징처리
$(".page-item a").on("click", function(e){
	e.preventDefault();
	$("#page").val($(this).data("number"));
	$("#searchForm").attr({
		"method":"get",
		"action":"/product/productList"
	});
	$("#searchForm").submit();
});

//아이템 클릭시 상세 페이지 이동을 위한 처리 이벤트
$(".productItem").click(function(){
	let no = $(this).data("no");
	console.log("아이템아이디 :" + no);
	
	location.href="/product/"+no;
});
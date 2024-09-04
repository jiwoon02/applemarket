$("#insertBtn").on("click", function() {
	if(!chkData("#categoryID","카테고리 ID를")) return;
	if(!chkData("#categoryName","카테고리 이름을")) return;
	actionProcess("#insertForm", "post", "category/insert");
	console.log(1);
	console.log(actionProcess("#insertForm", "post", "category/insert"));
	
});

$(".deleteBtn").on("click", function(){
	var categoryID = $(this).data("id");

	console.log(categoryID);
	if(confirm("정말 삭제하시겠습니까?")){
		$.ajax({
		           url: "category/delete",
		           type: "post",
		           data: { categoryID: categoryID },
		           success: function(response) {
		               // 페이지 새로고침 또는 삭제된 항목 제거 등의 작업
		               location.reload(); // 페이지 새로고침
		           },
		           error: function(xhr, status, error) {
		               alert("삭제에 실패했습니다.");
		           }
		})
	}
})
$("#insertBtn").on("click", function() {
	if(!chkData("#categoryID","카테고리 ID를")) return;
	if(!chkData("#categoryName","카테고리 이름을")) return;
	actionProcess("#insertForm", "post", "category/insert");
	console.log(1);
	
});

$("#deleteBtn").on("click", function(){
	if(confirm("정말 삭제하시겠습니까?")){
		
	actionProcess("#dataForm", "post", "category/delete");
	}
})
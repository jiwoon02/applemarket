$("#updateFormBtn").on("click", function() {
	$("#updateDiv").html("<button type='button' id='updateBtn''>확인</button>");
	$(this).remove();
	$("#userName").removeAttr("readonly");
	
	
})

$("#updateDiv").on("click", "#updateBtn", function() {
	if(!chkData("#userName","변경할 유저의 이름을"))return;
	else if(confirm("정말 변경하시겠습니까?")){		
		actionProcess("#updateForm", "post", "administration/update")
	}
});

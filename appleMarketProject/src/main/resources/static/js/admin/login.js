var adminName = $(".adminName").val();
var adminPasswd = $(".adminPasswd").val();
//관리자 로그인
$("#LoginBtn").on("click", () => {
	if(!chkData("#adminName", "관리자 ID를"))return;
	else if(!chkData("#adminPasswd", "관리자 비밀번호를"))return;
	else if(!($("#adminName").val() == adminName)){
		alert("관리자 ID가 동일하지 않습니다"); 
		return;
	}
	else if(!($("#adminPasswd").val() == adminPasswd)){
			alert("관리자 PW가 동일하지 않습니다"); 
			return;
	}

	
	locationProcess("/admin/success/administration")
	console.log(1);
});

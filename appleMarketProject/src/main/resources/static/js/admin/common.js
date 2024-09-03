function chkData(item, msg) {
	if($(item).val().replace(/\s/g,"")==""){
		alert(msg+" 입력 해주세요");
		$(item).val("");
		$(item).focus;
		return false;
	} else{
		
	return true;
	}
}

function dataCheck(item, out, msg){
	if($(item).val().replace(/\s/g,"")==""){
		$(out).html(msg + "입력해 주세요");
		$(item).val("");
		return false;
	}else{
		return true;
	}
}

function getDateFormat(dateValue){
	var year = dateValue.getFullYear();
	
	var month = dateValue.getMonth()+1;
	month = (month<10) ? "0"+month : month;
	
	var day = dateValue.getDate();
	day = (day<10) ? "0"+day : day;
	
	var result = year+"-"+month+"-"+day;
	return result;
}

const actionProcess = function(form, method, action){
	$(form).attr({
		"method":method,
		"action":action
		
	});
	$(form).submit();
}

const locationProcess = function(url){
	location.href = url;
}
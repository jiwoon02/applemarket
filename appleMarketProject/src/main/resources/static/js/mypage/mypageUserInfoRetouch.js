//비밀번호 눈모양
function togglePassword() {
    var passwordInput = document.getElementById("userPasswd");
    var toggleIcon = document.querySelector(".password-toggle");
    
    if (passwordInput.type === "password") {
        passwordInput.type = "text"; // 비밀번호 보이기
        toggleIcon.textContent = "👁️"; // 눈 감기 아이콘
    } else {
        passwordInput.type = "password"; // 비밀번호 숨기기
        toggleIcon.textContent = "👁️"; // 눈 아이콘
    }
}

$(function() {
	// 이메일 선택 시 userEmail2 input 필드에 값 설정
    $(".emailSelect").on("change", function() {
        var selectedValue = $(this).val(); // 선택된 값 가져오기
		console.log(selectedValue);
		console.log("111");
        if (selectedValue !== "") {
            $("#userEmail2").val(selectedValue); // userEmail2 필드에 값 설정
            $("#userEmail2").prop("readonly", true); // userEmail2 필드를 읽기 전용으로 설정
        } else {
            $("#userEmail2").val(""); // 값이 없는 경우 userEmail2 필드를 비움
            $("#userEmail2").prop("readonly", false); // 입력 필드를 다시 수정 가능하도록 설정
        }
    });

    // 이메일 입력 필드 변경 시 userEmail 필드를 업데이트
    $('#userEmail1, #userEmail2').on('input', updateFullEmail);

    function updateFullEmail() {
        const email1 = $('#userEmail1').val();
        const email2 = $('#userEmail2').val();
        const fullEmail = email1 + "@" + email2;

        $('#userEmail').val(fullEmail);
    }

    // 폼 제출 시 이메일을 업데이트
    $(".userBtn").on("click", function() {
        updateFullEmail();
    });
});/**
 * 
 */

document.addEventListener("DOMContentLoaded", function () {
	//이메일 기본값 설정
	var userEmail = document.getElementById("currentEmail").value;
	var emailParts = userEmail.split("@");
    if (emailParts.length === 2) {
        var userEmail1 = emailParts[0]; // 이메일 아이디
        var userEmail2 = emailParts[1]; // 도메인

        // 각각의 input 필드에 값 설정
        document.getElementById("userEmail1").value = userEmail1;
        document.getElementById("userEmail2").value = userEmail2;
	}
	
	// 공백 제거 함수: 입력된 값에서 모든 공백을 제거
    function removeSpaces(inputElement) {
        inputElement.value = inputElement.value.replace(/\s+/g, ''); // 모든 공백 제거
    }
	
	// 공백 입력을 막고 제거할 필드 목록 정의
	const fieldsToCheck = [
		'userNickname', 'userPhoneNumber', 'userEmail1', 'userEmail2'
	];

	// 각 필드에 입력 이벤트 리스너 추가: 공백 제거 로직 추가
	fieldsToCheck.forEach(function(fieldId) {
	    const field = document.getElementById(fieldId);
	    if (field) {
	        field.addEventListener('input', function() {
	            removeSpaces(field);
	        });
	    }
	});
});





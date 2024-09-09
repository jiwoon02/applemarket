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


document.addEventListener("DOMContentLoaded", function () {
	
	let isNicknameAvailable = true;
	let isPhoneAvailable = true;
	let isEmailAvailable = true;
	
	
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
	
	
	// 닉네임 유효성 검사 및 중복 확인
    const userNicknameField = document.getElementById('userNickname');
	
    userNicknameField.addEventListener('input', function() {
        removeSpaces(this);

        const userNickname = this.value.toLowerCase();
        if (userNickname.length > 0) {
            fetch(`/checkNicknameMyPage?userNickname=${userNickname}`)
                .then(response => response.json())
                .then(isDuplicate => {
                    if(isDuplicate){
						isNicknameAvailable = false; // 중복된 경우 false
					} else{
						isNicknameAvailable = true; // 사용 가능한 경우 true
					}
                });
        } else {
			isNicknameAvailable = false;
        }
    });
	

	//전화번호 유효성 검사 및 중복 확인
    const userPhoneField = document.getElementById('userPhoneNumber');
	
    userPhoneField.addEventListener('input', function() {
        removeSpaces(this);
	
		const userPhone = this.value;
        if (userPhone.length > 0){
            fetch(`/checkPhoneMyPage?userPhone=${userPhone}`)
                .then(response => response.json())
				.then(isDuplicate => {
				    if (isDuplicate){
						isPhoneAvailable = false; // 중복된 경우 false
				    } else {
						isPhoneAvailable = true; // 사용 가능한 경우 true
				    }
				});
		} else{
			isPhoneAvailable = false;
		}
    });
	
	//이메일 저장 및 중복 체크
	function updateEmailAndCheck() {
	    const userEmail1 = document.getElementById('userEmail1').value;
	    const userEmail2 = document.getElementById('userEmail2').value;
	    const userEmail = `${userEmail1}@${userEmail2}`;
	    
	    // 이메일 필드에 값 업데이트
	    document.getElementById('userEmail').value = userEmail;
	    
	    // 이메일 유효성 검사 및 중복 확인
	    if (userEmail1.length > 0 && userEmail2.length > 0) {
	        fetch(`/checkEmailMyPage?userEmail=${userEmail}`)
	            .then(response => response.json())
	            .then(isDuplicate => {
	                if (isDuplicate) {
	                    isEmailAvailable = false; // 중복된 경우 false
	                } else {
	                    isEmailAvailable = true; // 사용 가능한 경우 true
	                }
	            });
	    } else {
	        isEmailAvailable = false; // 이메일 형식이 올바르지 않은 경우 false
	    }
	}
	
	// 각 이메일 입력 필드에 이벤트 리스너 추가
	['userEmail1', 'userEmail2'].forEach(function(fieldId) {
	    const field = document.getElementById(fieldId);
	    if (field) {
	        field.addEventListener('input', updateEmailAndCheck);
	    }
	});

	
	document.getElementById('update-info').addEventListener('submit', function(event) {
		let isFormValid = true;
		let message = "";
		
		// 이메일 유효성 검사
		const userEmail1 = document.getElementById('userEmail1').value;
		const userEmail2 = document.getElementById('userEmail2').value;
		if (!/^[a-z0-9!#$%^&*._-]+$/i.test(userEmail1) || !/^[a-zA-Z]+\.[a-zA-Z]+$/.test(userEmail2)) {
		    isEmailAvailable = false;
		}
		
		if (!isNicknameAvailable) {
			isFormValid = false;
			message += "닉네임 ";
		}
		
		if (!isPhoneAvailable) {
			isFormValid = false;
			message += "전화번호 ";
		}
		
		if (!isEmailAvailable) {
		    isFormValid = false;
			message += "이메일 ";
		}
				
		// 유효하지 않은 경우 폼 제출을 막음
        if (!isFormValid) {
            event.preventDefault();
            alert(message+"이 형식에 맞지않거나 중복되었습니다.");
        }
	});
});





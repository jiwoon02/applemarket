document.addEventListener('DOMContentLoaded', function() {
	
	let isUserIDAvailable = false;
    let isPhoneAvailable = false;
    let isEmailAvailable = false;
    let isNicknameAvailable = false;
	
    // 공백 제거 함수: 입력된 값에서 모든 공백을 제거
    function removeSpaces(inputElement) {
        inputElement.value = inputElement.value.replace(/\s+/g, ''); // 모든 공백 제거
    }
    
    // 실시간으로 전화번호와 이메일을 결합하여 hidden 필드에 저장
    function updatePhoneAndEmail() {
        const phone1 = document.getElementById('phone1').value;
        const phone2 = document.getElementById('phone2').value;
        const phone3 = document.getElementById('phone3').value;
        const phone = `${phone1}-${phone2}-${phone3}`;
        document.querySelector('input[name="userPhone"]').value = phone;

        const emailId = document.getElementById('emailId').value;
        const emailDomain = document.getElementById('emailDomain').value;
        const email = `${emailId}@${emailDomain}`;
        document.querySelector('input[name="userEmail"]').value = email;
    }

    // 각 전화번호 및 이메일 입력 필드에 이벤트 리스너 추가
    ['phone1', 'phone2', 'phone3', 'emailId', 'emailDomain'].forEach(function(fieldId) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('input', updatePhoneAndEmail);
        }
    });
    
    // 공백 입력을 막고 제거할 필드 목록 정의
    const fieldsToCheck = [
        'userID', 'userPwd', 'userName', 'emailId', 
        'emailDomain', 'userNickname', 'phone1', 
        'phone2', 'phone3'
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
	
    // 아이디 유효성 검사 및 중복 확인
    document.getElementById('userID').addEventListener('input', function() {
        const userID = this.value.toLowerCase(); // 입력값 소문자로 변환
		this.value = this.value.replace(/[^a-z0-9]/g, '');
        const filteredUserID = userID.replace(/[^a-z0-9]/g, ''); // 영문 소문자와 숫자만 허용

		// 입력값이 필터링 전후로 달라진 경우, 필터링 전 입력을 무효화 
		//ex) test!입력시 replace로 test로 값이 바뀌지만 중복검사를 test!로 하기떄문에 이 코드가 필요
		if (userID !== filteredUserID) {
		    document.getElementById('userIDError').textContent = "아이디는 영문 소문자와 숫자만 가능합니다.";
		    document.getElementById('userIDError').style.color = "red";
		    isUserIDAvailable = false;
		    return; // 중복 검사를 수행하지 않음
		}
		
		this.value = filteredUserID;
		
        // 아이디 길이 및 형식 확인
        if (filteredUserID.length < 6 || filteredUserID.length > 12) {
            document.getElementById('userIDError').textContent = "아이디는 6~12자의 영문 소문자와 숫자만 가능합니다.";
            document.getElementById('userIDError').style.color = "red";
        } else {
            // 중복 검사: 서버로 아이디 전송하여 중복 여부 확인
            fetch(`/checkUserID?userID=${filteredUserID}`)
                .then(response => response.json())
                .then(isDuplicate => {
					if (isDuplicate) {
						isUserIDAvailable = false; // 중복된 경우 false
                        document.getElementById('userIDError').textContent = "이미 존재하는 아이디입니다.";
                        document.getElementById('userIDError').style.color = "red";
						
                    } else {
						isUserIDAvailable = true; // 사용 가능한 경우 true
                        document.getElementById('userIDError').textContent = "사용 가능한 아이디입니다.";
                        document.getElementById('userIDError').style.color = "blue";
                    }
				});
        }
    });
	
    // 비밀번호 유효성 검사: 영문 소문자, 숫자, 특수문자 혼합 8~20자 허용
    const userPwdField = document.getElementById('userPwd');
    userPwdField.addEventListener('input', function() {
        removeSpaces(this);
        this.value = this.value.replace(/[^a-z0-9!@#$%^&*]/g, ''); // 허용된 문자만 입력 가능
		
		const confirmPwd = document.getElementById('confirmPwd').value;
		
        // 비밀번호 길이 및 형식 확인
         if (!/^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[a-z\d!@#$%^&*]{8,20}$/.test(this.value)){
			document.getElementById('userPwdError').textContent = "비밀번호는 8~20자의 영문 소문자, 숫자, 특수문자(!@#$%^&*)를 포함해야 합니다."; 
			document.getElementById('userPwdError').style.color = "red";
		} else{
			document.getElementById('userPwdError').textContent = ""; 
		}
		if(this.value !== confirmPwd){
			document.getElementById('confirmPwdError').textContent = "비밀번호가 일치하지 않습니다.";
			document.getElementById('confirmPwdError').style.color = "red";
		} else {
            document.getElementById('userPwdError').textContent = "";
			document.getElementById('confirmPwdError').textContent = "비밀번호가 일치합니다.";
			document.getElementById('confirmPwdError').style.color = "blue";
        }
    });
	 
	//비밀번호 확인 유효성
	const userPwdConfirm = document.getElementById('confirmPwd');
	userPwdConfirm.addEventListener('input', function() {
	        removeSpaces(this);
	        this.value = this.value.replace(/[^a-z0-9!@#$%^&*.-_]/g, ''); // 허용된 문자만 입력 가능
			
			const userPwd = document.getElementById('userPwd').value;
			
	        // 비밀번호 길이 및 형식 확인
	        if (this.value !== userPwd) {
	            document.getElementById('confirmPwdError').textContent = "비밀번호가 일치하지 않습니다";
	            document.getElementById('confirmPwdError').style.color = "red";
	        } else {
	            document.getElementById('confirmPwdError').textContent = "비밀번호가 일치합니다";
				document.getElementById('confirmPwdError').style.color = "blue";
	        }
	    });
	
	
    // 이름 유효성 검사: 한글만 허용
    const userNameField = document.getElementById('userName');
    userNameField.addEventListener('input', function() {
        removeSpaces(this);
        this.value = this.value.replace(/[^ㄱ-ㅎ가-힣]/g, ''); // 한글만 허용

        // 이름이 한글인지 확인
        if (this.value.length === 0) {
            document.getElementById('userNameError').textContent = "이름은 한글만 가능합니다.";
            document.getElementById('userNameError').style.color = "red";
        } else {
            document.getElementById('userNameError').textContent = "";
        }
    });

    // 전화번호 유효성 검사 및 중복 확인
    const phoneFields = ['phone1', 'phone2', 'phone3'];
    phoneFields.forEach(function(fieldId) {
        const field = document.getElementById(fieldId);
        field.addEventListener('input', function() {
            removeSpaces(this);
            this.value = this.value.replace(/[^0-9]/g, ''); // 숫자만 허용

            let isValid = true;
            let phoneErrorMessage = "";

            // 전화번호 유효성 검사: 각 부분의 자릿수 확인
            if (fieldId === 'phone1' && (this.value.length !== 3)) {
                isValid = false;
                phoneErrorMessage = "전화번호 첫 번째 부분은 3자리 숫자여야 합니다.";
            } else if (fieldId === 'phone2' && (this.value.length !== 4)) {
                isValid = false;
                phoneErrorMessage = "전화번호 두 번째 부분은 4자리 숫자여야 합니다.";
            } else if (fieldId === 'phone3' && (this.value.length !== 4)) {
                isValid = false;
                phoneErrorMessage = "전화번호 세 번째 부분은 4자리 숫자여야 합니다.";
            }

            if (!isValid) {
                document.getElementById('phoneError').textContent = phoneErrorMessage;
                document.getElementById('phoneError').style.color = "red";
                return; // 유효하지 않으면 중복 검사하지 않음
            } else {
                document.getElementById('phoneError').textContent = "";
            }

            // 전화번호 중복 검사: 모든 전화번호 필드가 유효한 경우
            const phone1 = document.getElementById('phone1').value;
            const phone2 = document.getElementById('phone2').value;
            const phone3 = document.getElementById('phone3').value;
            const userPhone = `${phone1}-${phone2}-${phone3}`;

            if (phone1.length === 3 && phone2.length === 4 && phone3.length === 4) {
                fetch(`/checkPhone?userPhone=${userPhone}`)
                    .then(response => response.json())
					.then(isDuplicate => {
					    if (isDuplicate) {
							isPhoneAvailable = false; // 중복된 경우 false
					        document.getElementById('phoneError').textContent = "이미 존재하는 전화번호입니다.";
					        document.getElementById('phoneError').style.color = "red";
					    } else {
							isPhoneAvailable = true; // 사용 가능한 경우 true
					        document.getElementById('phoneError').textContent = "사용 가능한 전화번호입니다.";
					        document.getElementById('phoneError').style.color = "blue";
					    }
					});
            }
        });
    });

    // 이메일 ID 유효성 검사 및 중복 확인
    const emailIdField = document.getElementById('emailId');
    emailIdField.addEventListener('input', function() {
        removeSpaces(this);
        this.value = this.value.replace(/[^a-z0-9.!@#$%^&*_+-]/g, ''); // 영문 소문자와 숫자만 허용

        // 이메일 ID 형식 확인
        if (!/^[a-z0-9.!#$%^&*_+-]+$/.test(this.value)) {
            document.getElementById('emailError').textContent = "이메일 ID는 영문소문자 숫자만 가능합니다.";
            document.getElementById('emailError').style.color = "red";
        } else {
            document.getElementById('emailError').textContent = "";
            checkEmail(); // 이메일 중복 검사 호출
        }
    });

    // 이메일 Domain 유효성 검사 및 중복 확인
    const emailDomainField = document.getElementById('emailDomain');
    emailDomainField.addEventListener('input', function() {
        removeSpaces(this);
        this.value = this.value.replace(/[^a-zA-Z.]/g, ''); // 영문과 '.'만 허용

        // 이메일 도메인 형식 확인
        if (!/^[a-zA-Z]+\.[a-zA-Z]+$/.test(this.value)) {
            document.getElementById('emailError').textContent = "이메일 도메인은 올바른 형식이 아닙니다. (예: example.com)";
            document.getElementById('emailError').style.color = "red";
        } else {
            document.getElementById('emailError').textContent = "";
            checkEmail(); // 이메일 중복 검사 호출
        }
    });

    // 이메일 중복 검사 함수
    function checkEmail() {
        const emailId = document.getElementById('emailId').value.toLowerCase();
        const emailDomain = document.getElementById('emailDomain').value.toLowerCase();
        const userEmail = `${emailId}@${emailDomain}`;

        // 이메일 중복 확인
        if (emailId && emailDomain) {
            fetch(`/checkEmail?userEmail=${userEmail}`)
                .then(response => response.json())
				.then(isDuplicate => {
					if (isDuplicate) {
					    isEmailAvailable = false; // 중복된 경우 false
					    document.getElementById('emailError').textContent = "이미 존재하는 이메일입니다.";
					    document.getElementById('emailError').style.color = "red";
					} else {
					    isEmailAvailable = true; // 사용 가능한 경우 true
					    document.getElementById('emailError').textContent = "사용 가능한 이메일입니다.";
					    document.getElementById('emailError').style.color = "blue";
					}
				});
        } else {
            document.getElementById('emailError').textContent = "";
			isEmailAvailable = false;
        }
    }

    // 닉네임 유효성 검사 및 중복 확인
    const userNicknameField = document.getElementById('userNickname');
    userNicknameField.addEventListener('input', function() {
        removeSpaces(this);

        const userNickname = this.value.toLowerCase();
        if (userNickname.length > 0) {
            fetch(`/checkNickname?userNickname=${userNickname}`)
                .then(response => response.json())
                .then(isDuplicate => {
                    if(isDuplicate){
						isNicknameAvailable = false; // 중복된 경우 false
						document.getElementById('nicknameError').textContent = "이미 존재하는 닉네임입니다.";
						document.getElementById('nicknameError').style.color = "red";
					} else{
						isNicknameAvailable = true; // 사용 가능한 경우 true
						document.getElementById('nicknameError').textContent = "사용 가능한 닉네임입니다.";
						document.getElementById('nicknameError').style.color = "blue";
					}
                });
        } else {
            document.getElementById('nicknameError').textContent = "";
			isNicknameAvailable = false;
        }
    });

    // 폼 제출 시 유효성 검사 수행
    document.getElementById('join-form').addEventListener('submit', function(event) {
		let isFormValid = true;

        // 아이디 유효성 검사
        const userID = document.getElementById('userID').value.toLowerCase();
        if (!/^[a-z0-9]{6,12}$/.test(userID)) {
            document.getElementById('userIDError').textContent = "아이디는 6~12자의 영문 소문자와 숫자만 가능합니다.";
            document.getElementById('userIDError').style.color = "red";
            isFormValid = false;
        }

        // 비밀번호 유효성 검사
        const userPwd = document.getElementById('userPwd').value;
        if (!/^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*])[a-z\d!@#$%^&*]{8,20}$/.test(userPwd)) {
            document.getElementById('userPwdError').textContent = "비밀번호는 8~20자의 영문 소문자, 숫자, 특수문자(!@#$%^&*)를 포함해야 합니다.";
            document.getElementById('userPwdError').style.color = "red";
            isFormValid = false;
        }
		
		//비밀번호 확인 유효성 검사
		const confirmPwd = document.getElementById('confirmPwd').value;
		if(userPwd!==confirmPwd){
			document.getElementById('confirmPwdError').textContent = "비밀번호가 일치하지 않습니다.";
			isFormValid = false;
		} else{
			document.getElementById('confirmPwdError').textContent = "비밀번호가 일치합니다.";
		}
		
        // 이름 유효성 검사
        const userName = document.getElementById('userName').value;
        if (!/^[가-힣]+$/.test(userName)) {
            document.getElementById('userNameError').textContent = "이름은 한글만 입력 가능합니다.";
            document.getElementById('userNameError').style.color = "red";
            isFormValid = false;
        }

        // 전화번호 유효성 검사
        const phone1 = document.getElementById('phone1').value;
        const phone2 = document.getElementById('phone2').value;
        const phone3 = document.getElementById('phone3').value;
        if (!/^[0-9]{3}$/.test(phone1) || !/^[0-9]{4}$/.test(phone2) || !/^[0-9]{4}$/.test(phone3)) {
            document.getElementById('phoneError').textContent = "전화번호 형식이 잘못되었습니다.";
            document.getElementById('phoneError').style.color = "red";
            isFormValid = false;
        }
		
        // 이메일 유효성 검사
        const emailId = document.getElementById('emailId').value;
        const emailDomain = document.getElementById('emailDomain').value;
        if (!/^[a-z0-9!#$%^&*._-]+$/i.test(emailId) || !/^[a-zA-Z]+\.[a-zA-Z]+$/.test(emailDomain)) {
            document.getElementById('emailError').textContent = "이메일 형식이 잘못되었습니다.";
            document.getElementById('emailError').style.color = "red";
            isFormValid = false;
        }
		
		// 중복 검사 결과를 확인
		if (!isUserIDAvailable) {
		    isFormValid = false;
		}
		if (!isPhoneAvailable) {
		    isFormValid = false;
		}
		if (!isEmailAvailable) {
		    isFormValid = false;
		}
		if (!isNicknameAvailable) {
		    isFormValid = false;
		}
		
        // 유효하지 않은 경우 폼 제출을 막음
        if (!isFormValid) {
            event.preventDefault();
            alert("입력한 정보를 확인해 주세요.");
        }
    });
});
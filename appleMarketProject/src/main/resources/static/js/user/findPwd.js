document.addEventListener('DOMContentLoaded', function() {
	// 공백 제거 함수: 입력된 값에서 모든 공백을 제거
    function removeSpaces(inputElement) {
        inputElement.value = inputElement.value.replace(/\s+/g, ''); // 모든 공백 제거
    }
	
	// 실시간으로 이메일을 결합하여 hidden 필드에 저장
    function updateEmail() {
        const emailId = document.getElementById('emailId').value;
        const emailDomain = document.getElementById('emailDomain').value;
        const email = `${emailId}@${emailDomain}`;
        document.querySelector('input[name="userEmail"]').value = email;
    }
	
	// 각 전화번호 및 이메일 입력 필드에 이벤트 리스너 추가
    ['emailId', 'emailDomain'].forEach(function(fieldId) {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('input', updateEmail);
        }
    });
		
	// 공백 입력을 막고 제거할 필드 목록 정의
    const fieldsToCheck = ['userName','userID', 'emailId', 'emailDomain'];

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
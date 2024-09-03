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
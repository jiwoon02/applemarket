$(".goDetail").on("click", function() {
	let communityID = $(this).data("post-id")
	console.log(communityID);
	
	locationProcess("/admin/success/community/detail/" + communityID);
})
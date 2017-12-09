/**
 * Registration validation for my PS13 - Online Market
 */

"use strict";

$(function() {
	
	$("register-form").keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	
	$("#register-form").validate({
		rules: {
			email: { required: true, email: true },
			password: { required: true },
			repassword: { required: true, equalTo: "#inputPassword"}
		},
		
		messages: {
			email: { required: "An email is required.", email: "An email must contains '@' and '.' characters." },
			password: { required: "A password is required." },
			repassword: { required: "Please confirm your password.", equalTo: "The passwords doesn't match."}
		}
	});
});
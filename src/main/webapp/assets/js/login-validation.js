/**
 * Login validation for PS13 - Online Market
 */

"use strict";

$(function() {
	
	$("#login-form").keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	
	$("#login-form").validate({
		rules: {
			j_username: { required: true, email: true },
			j_password: { required: true }
		},
		
		messages: {
			j_username: { required: "An email is required.", email: "An email must contains '@' and '.' characters." },
			j_password: { required: "A password is required." }
		}
	});
});
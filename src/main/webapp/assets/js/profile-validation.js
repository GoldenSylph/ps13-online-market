/**
 * Profile validation for PS13 - Online Market
 */

"use strict";

$(function() {
	
	$("#change-email-form").keydown(function(event){
		if(event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	
	$("#change-email-form").validate({
		rules: {
			email: { required: true, email: true }
		},
		
		messages: {
			email: { required: "An email is required.", email: "An email must contains '@' and '.' characters." }
		}, 
		
		submitHandler: function (form) {
			console.log("asdada");
			$("#change-email-form").ajaxSubmit({
		    	dataType: "json",
		    	success: function (data) {
		    		console.log("asdada");
		    		if (data.isFindOldEmailError) {
		    			document.getElementById("emailLabel").innerText = "Can not obtain from database your old email. Please try again.";
		    		} else if (data.isSetNewEmailError) {
		    			document.getElementById("emailLabel").innerText = "Can not set your new email. Please try again.";
		    		} else {
		    			document.getElementById("emailLabel").innerText = "Your email: " + data.email;
		    		}
		    		document.getElementById("emailInput").value = "";
		    	},
		    	error: function(data) {
		    		console.log("bad: " + data);
		    	}
		    });
		}
	});
});
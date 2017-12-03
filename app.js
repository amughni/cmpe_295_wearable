//Page Views
var MainPageRan = false;
var SwitchPageRan = false;
var RoutinesPageRan = false;
var setupPageRan = true;

//Encrypt Key (This should be set by the user)
var EncryptKey = 166856;

//import accessurl from external file


//Databases
var Access_Token = Decrypt(localStorage.getItem("token_DB"));
var Access_Url = Decrypt(localStorage.getItem("AccessUrl_DB"));
var switches_DB = localStorage.getItem("switches_DB");
var routines_DB = localStorage.getItem("routines_DB");
//
//if(switches_DB != null){ switches_DB = Decrypt(switches_DB); }
//if(routines_DB != null){ routines_DB = Decrypt(routines_DB); }

//TAU UI Helpers (to create/destroy)
//var SwitchList_UI = null;
//var RoutineList_UI = null;

//URL for Auth Script
//Change this -- Hardcode it
var AuthScriptUrl = "http://shufordtech.com/gear.php";

//Returns TRUE if the watch has internet connection
var isOnline = navigator.onLine;

//debugging - to Bypass Login
//Access_Token = "";
//Access_Url = "https://********************.api.smartthings.com:443/api/smartapps/"; 

var AccessURL="http://24.4.244.79:8080/access_server_war/rest/smartthings/resource";
//------------------------------------------------------------------------------------On Page Changes
document.addEventListener("pageshow", function (e) {
	if(isOnline==false){
		//Maybe this should be done each time before an api call is fired incase we lose connection after starting the app..
		//If so, move isOnline = navigator.onLine also
		alert('No internet connection, please connect first.');
		tizen.application.getCurrentApplication().exit();
	}
	var page = e.target.id;
	var pageTarget = e.target;
	console.log("pageshow - pageshow event fired: Page: " + page);
	if(page == "mainPage"){
		MainPage();
	}else if(page == "switchesPage"){
		SwitchPage();
//	}else if(page == "routinesPage"){
//		RoutinesPage();
	}else if(page == "setupPage"){
		SetupPage();
	}
});
//document.addEventListener( "pagebeforehide", function(e) {
//	var page = e.target.id;
//	console.log("pagebeforehide - LEAVING PAGE: " + page);
//	if(page=="routinesPage"){
//		RoutineList_UI.destroy();
//	}else if(page=="switchesPage"){
//		SwitchList_UI.destroy();
//	}
//});
//------------------------------------------------------------------------------------On Page Changes

//------------------------------------------------------------------------------------Setup Page

//------------------------------------------------------------------------------------RoutinesPage

//------------------------------------------------------------------------------------Main Page
// Change to something meaningful
function MainPage(){
//	console.log("MainPage()");
//	if(Access_Token){
//		if(MainPageRan==false){
//			MainPageRan=true;
//			//This is the first time the user has looked at this page.
//			console.log("MainPage() - This is the first time the user has looked at this page.");
//		}else{	
//			//This page was shown before.
//			console.log("MainPage() - This page was shown before.");
//		}
//	}else{
		tau.changePage("switchesPage");
//	}
}
//(function(tau) {
//	var mPage = document.getElementById("mainPage"),
//		selector = document.getElementById("selector"),
//		selectorComponent,
//		clickBound;
//	//click event handler for the selector
//	function onClick(event) {
//		//console.log(event);		
//		var target = event.target;
//		if (target.classList.contains("ui-selector-indicator")) {
//			//console.log("Indicator clicked");
//			var ItemClicked = event.srcElement.textContent;
//			console.log("Item Clicked on home page was: " + ItemClicked);
//
//			//Handel home page click events
//			if(ItemClicked == "Switches"){
//				tau.changePage("switchesPage");
//			}else if(ItemClicked == "Clear Database"){
//				//------------------------------------------Clear Database
//				//This should be moved to it's own function.
//				console.log("Clearing Database");
//				localStorage.clear();
//				Access_Token = null;
//				Access_Url = null;
//				switches_DB = null;
//				alert("Database Cleared");
//				//Maybe we should exit here?
//				//------------------------------------------Clear Database
//			}else if(ItemClicked == "Routines"){
//				tau.changePage("routinesPage");
//			}
//			return;
//		}
//	}
//	//pagebeforeshow event handler
//	mPage.addEventListener("pagebeforeshow", function() {
//		clickBound = onClick.bind(null);
//		selectorComponent = tau.widget.Selector(selector);
//		selector.addEventListener("click", clickBound, false);
//	});
//	//pagebeforehide event handler
//	mPage.addEventListener("pagebeforehide", function() {
//		selector.removeEventListener("click", clickBound, false);
//		selectorComponent.destroy();
//	});
//}(window.tau));
//------------------------------------------------------------------------------------Main Page

//------------------------------------------------------------------------------------Switch Page
function SwitchPage(){
//	console.log("SwitchPage()");
//	if(SwitchPageRan==true){
//		//This page has been ran before...
//		//widget.scrollToPosition(0);
//		console.log("This page was shown before.");		
//		$.when(SwitchPageGetData()).done(function() {
//			SwitchPage_Buttons();
//		});
//	}else{
//		console.log("This is the first time the user has looked at this page.");
//		//This is the first time the user has looked at this page!
//		SwitchPageRan = true;
//		$.when(SwitchPageGetData()).done(function() {
		SwitchPageGetData();	
//		});
//	}
}

function SwitchPageGetData(){
	console.log("inside switchpagetdata");
	
	if($('#Switches') == null) {
		alert("Switches element not found.");
		return;
	}
	
	$('#Switches').html('');
/*	var self = this;

	$('#switchForm').submit();
*/	
//	$.get("http://24.4.244.79:8080/access_server_war/rest/smartthings/resource/switches",function(data){
//		self.AppendMyData(data[0].name);
//	});
	
	$.ajax({
		type: "GET"	,
		url: AccessURL + "/switches",
		success:function(data){
			$("#Switches").append ("<li><button id='switch1' + >" + data[0].name + "</button></li>");
			$("#Switches").append ("<li id='status1'>" + data[0].value + "</li>");
			
			
			
			 $('#switch1').click(function() {
				 
					Switch();
					
				});		
			
		}
	});
 }

 function getNextSwitchValue(current) {
	 
		if(current == "on")
			return "off";
		else 
			return "on";
		
 }

 function Switch() {
	 console.log("Old value:" + $('#status1').text());
	 var value = getNextSwitchValue($('#status1').text());
	 $.ajax({
			type: "GET"	,
			url: AccessURL + "/switches/" + value,
			
			success:function(data){
				if(data) {
						console.log("New value:" + value);
						$("#status1").text(value);
						
					
				}
			}
		});
 }
 

 function SwitchPage_Buttons(){
	console.log("SwitchPage_Buttons()");
	$(".aswitch").change(function(){
		console.log('.aswitch click called');
		//tau.changePage("processingPage");
		var WhatSwitch = this;
		
		$(WhatSwitch).hide();
		$(WhatSwitch).parent().find('.ui-processing').show();
		
		var DeviceID = $(this).attr('deviceid');
		console.log("DeviceID: " + DeviceID);
		
		if($(this).is(":checked")) {
			$.get({
			    url: Access_Url + "/switches/"+DeviceID+"/on",
			    beforeSend: function(xhr) { 
			      xhr.setRequestHeader('Authorization','Bearer ' + Access_Token);
			    },
			    success: function (data) {
			    	console.log(data);
			    	data = JSON.stringify(data);
			    	console.log(data);
					$(WhatSwitch).show();
					$(WhatSwitch).parent().find('.ui-processing').hide();
			    },
			    error: function(e){
			    	console.log(e);
			    	var Response = e.responseText;
			    	
					try {
						var obj = jQuery.parseJSON(Response);
					} catch (e) {
						alert("There was an error!");
						console.log(e);
						return;
					}				    	
			    	
			    	console.log(obj);
			    	if(obj.message){
			    		alert(obj.message);
			    	}else if(obj.error){
			    		alert(obj.error);
			    	}else{
			    		alert("There was a problem, could not turn on the device!");
			    	}
					$(WhatSwitch).show();
					$(WhatSwitch).parent().find('.ui-processing').hide();
			    }
			});
		}else{
			$.get({
			    url: Access_Url + "/switches/"+DeviceID+"/off",
			    beforeSend: function(xhr) { 
			      xhr.setRequestHeader('Authorization','Bearer ' + Access_Token);
			    },
			    success: function (data) {
			    	console.log(data);
			    	data = JSON.stringify(data);
			    	console.log(data);
					$(WhatSwitch).show();
					$(WhatSwitch).parent().find('.ui-processing').hide();
			    },
			    error: function(e){
			    	console.log(e);
			    	var Response = e.responseText;
			    	
					try {
						var obj = jQuery.parseJSON(Response);
					} catch (e) {
						alert("There was an error!");
						console.log(e);
						return;
					}					    	
			    	
			    	console.log(obj);
			    	if(obj.message){
			    		alert(obj.message);
			    	}else if(obj.error){
			    		alert(obj.error);
			    	}else{
			    		alert("There was a problem, could not turn off the device!");
			    	}
					$(WhatSwitch).show();
					$(WhatSwitch).parent().find('.ui-processing').hide();
			    }
			});
		}
	});	
}
//------------------------------------------------------------------------------------Switch Page

//------------------------------------------------------------------------------------App's Back Button Handler
window.addEventListener( 'tizenhwkey', function( ev ){
	console.log("Back Key Hit");
	var page = document.getElementsByClassName('ui-page-active')[0],
	pageid = page ? page.id : "";
	if(ev.keyName === "back") {
		if( pageid === "mainPage" ) {
           tizen.application.getCurrentApplication().exit();
		}else if(pageid === "setupPage"){
			tizen.application.getCurrentApplication().exit();
		} else {
	         tau.changePage("mainPage");
		}
	}
});
//------------------------------------------------------------------------------------App's Back Button Handler

//------------------------------------------------------------------------------------Encryption Functions
//temporary, quick but can be much stronger.
function Encrypt(str) {
    if (!str) str = "";
    str = (str == "undefined" || str == "null") ? "" : str;
    try {
        var key = EncryptKey;
        var pos = 0;
        ostr = '';
        while (pos < str.length) {
            ostr = ostr + String.fromCharCode(str.charCodeAt(pos) ^ key);
            pos += 1;
        }
        return ostr;
    } catch (ex) {
        return '';
    }
}
function Decrypt(str) {
    if (!str) str = "";
    str = (str == "undefined" || str == "null") ? "" : str;
    try {
        var key = EncryptKey;
        var pos = 0;
        ostr = '';
        while (pos < str.length) {
            ostr = ostr + String.fromCharCode(key ^ str.charCodeAt(pos));
            pos += 1;
        }
        return ostr;
    } catch (ex) {
        return '';
    }
}
//------------------------------------------------------------------------------------Encryption Functions

//------------------------------------------------------------------------------------Notes
/*
 * might use for dimmers
	window.addEventListener("rotarydetent", rotaryDetentCallback);
	function rotaryDetentCallback(ev) {
		var direction = ev.detail.direction,
		    uiScroller = $('#main').find('.ui-scroller'),
		    scrollPos = $(uiScroller).scrollTop();
		
		console.debug("onRotarydetent: " + direction);
		
		if(direction === "CW"){
		    $(uiScroller).scrollTop(scrollPos + 100); // scroll down 100px
		} else {
		    $(uiScroller).scrollTop(scrollPos - 100); // scroll up 100px
		}
	}
*/
//------------------------------------------------------------------------------------Notes



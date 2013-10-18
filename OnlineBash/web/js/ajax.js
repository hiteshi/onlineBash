
var key;
var temp = "";
	
function ajaxCall(cmd){

	new Ajax.Request("/OnlineBash/command", {
		method : "post",
		parameters : {
			commandText : cmd
		},
		onComplete : function(res){
			
			var list = eval("("+res.responseText+")");
			
			var output = "";
			var curDir = "";
			var flag = false;
			var flag1 = false;
			
			$("#cmd").val("");
			$(".texting").append(cmd);
			
			if(list.length != 0){
				
				if(list.length == 1 && list[0].startsWith("***")){
					
					curDir = list[0].substring(3);
				}else{
					if(list[list.length-1].startsWith("***"))
						flag = true;
					
					for (var i=0; i<list.length; i++){
						if(list[i].startsWith("***")){
							curDir = list[i].substring(3);
							flag1 = false;
							break;
						}else{
							flag1 = true;
							output+=list[i];
							
							if(flag == true){
								if(i < list.length-2)
									output+="<br>";
							}else{
								if(i < list.length-1)
									output+="<br>";
							}
						}
					}
					$(".texting").append("<br>"+output);
				}
			}else
				flag1 = true;
			
			if(flag1 == true)
				curDir = temp;
				
			$(".texting").append("<br>guestUser:~"+curDir+"$ ");
			$("#bar").text("Terminal@guestUser:" + curDir);
			temp = curDir;
			
			$("#container").scrollTop($("#container")[0].scrollHeight);
			$("#cmd").focus();
		}
	});
}

$(document).ready(function(){
	
	$("#max").click(function () {
		$("#container").show();
	});
	
	$("#min").click(function () {
		$("#container").hide();
	});
	
	$("#container").keypress(function(event){
		
		if(event.keyCode == 13){			
    		var command=$("#cmd").val();
    		ajaxCall(command);
    	}else if((event.keyCode == 38)) {
    		key = "up";
    		getCommand();
    	}else if(event.keyCode == 40) {
    		key = "down";
    		getCommand();
    	}else if(event.keyCode == 9) {
    		var tabText = $("#cmd").val();
    		getDirectory(tabText);
    	}
	});
});

function getCommand() {

	new Ajax.Request("/OnlineBash/getCommand", {
		method : "post",
		parameters : {
			keyText : key
		},
		onComplete : function(res) {

			$("#cmd").val("" +(res.responseText));

			$("#container").scrollTop($("#container")[0].scrollHeight);
			$("#cmd").focus();
		}
	});
}

function getDirectory(tabText) {
	
	var abc=tabText.substring(0,2);
	var str=tabText;
	var remaining="";
	if(abc=="cd"){	
		str=tabText.substring(tabText.lastIndexOf('/')+1,tabText.length);
		remaining=tabText.substring(0,tabText.lastIndexOf('/')+1)
	}
	
	new Ajax.Request("/OnlineBash/getDirectory", {
		method : "post",
		parameters : {
			tabEvent : str
		},
		onComplete : function(res) {
		
			var list = eval("(" + res.responseText + ")");		
			
			if(list.length<3){
				$("#cmd").val(remaining+list[0]);
			}else{
				var output1 = "";
				var curDir1 = "";
				
				$("#cmd").val(tabText);
				
				for(var i=0; i<list.length; i++){
					
					output1 += list[i];
					if(i < list.length-1)
						output1 += "<br>";
				}
				$(".texting").append("<br>"+output1);
				curDir1 = temp;
				$(".texting").append("<br>guestUser:~"+curDir1+"$ ");
			}
			
			$("#container").scrollTop($("#container")[0].scrollHeight);
			$("#cmd").focus();
		}
	});
}

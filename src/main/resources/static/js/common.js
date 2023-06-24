console.log("This is Script");

const toggleSidebar = () => {
if($(".sidebar").is(":visible")){
    $(".sidebar").css("display","none");
    $("content").css("margin-left","0%");
}else{
    $(".sidebar").css("display","block");
    $("content").css("margin-left","20%");
}

};

/*$(document).ready(function(){
	pagesize = 2;
	var pageCount = $('.pagin').length / pagesize;
	console.log(pageCount)
	alert(pageCount);
	
	for(var i=0; i<pageCount; i++){
		$('#pagin1').append('<li><a href="#">'+(i+1)+'</a></li>')
	}
	$("#pagin1 li").first().find("a").addClass("current")
    showPage = function(page) {
	    $(".pagin").hide();
	    $(".pagin").each(function(n) {
	        if (n >= pageSize * (page - 1) && n < pageSize * page)
	            $(this).show();
	    });        
	}
    
	showPage(1);

	$("#pagin1 li a").click(function() {
	    $("#pagin1 li a").removeClass("current");
	    $(this).addClass("current");
	    showPage(parseInt($(this).text())) 
	});
	
	
});*/

function pagination(){
	alert("1");
	
}
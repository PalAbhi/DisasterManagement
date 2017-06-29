<!DOCTYPE html>
<html>
<head>
	<title>Home</title>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<link href="css/bootstrap.min.css" rel="stylesheet" />
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js'></script>
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    <style>
	body{
        font-family: 'Raleway', sans-serif;
        padding:10px;
    }
    p{
        padding-top:10px;
        font-size:1.3em;
    }
    .node{
		padding:10px;
		font-size:1.4em;
    }
    .node:hover{
        padding-left:5px;
    }
	</style>
</head>
<body>
    <center><h2>Current Disasters</h2></center>
    <div class="container" id="linkview">
    </div>
</body>
</html>
<script>

function createNode(tablename){
	var div = document.createElement("div");
	div.setAttribute("class","row node");
	var link = document.createElement("a");
	link.setAttribute("href","/resultview.php?dataset="+tablename);
	var text = document.createTextNode("#"+tablename);
	link.appendChild(text);
	div.appendChild(link);
	return div;
}

function clearNode(element){
	while (element.firstChild) {
		element.removeChild(element.firstChild);
	}
}


function loadData(){
	
	console.log("loadData called");
	
	$.getJSON('getLocalTables.php',{}, function(data){
        if(data.length==0){
			//No data found
			window.location = "error.html";
			return;
		}
		
		var linkView = document.getElementById("linkview");
		clearNode(linkView);
		
		$.each(data, function(key, val) {
			node = createNode(val);
			linkView.appendChild(node);
		});
		
	}); 
} 

loadData();
setInterval(loadData, 3000);
</script>
<script src="js/jquery-1.10.2.js"></script>
<script src="js/bootstrap.min.js"></script>

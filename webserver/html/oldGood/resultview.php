<?php
	error_reporting(E_ALL); 
	ini_set('display_errors', 1);
	#$dataset = "ChennaiFlood";
	if(!isset($_GET["dataset"])){
		echo "Illegal request";
		exit();
	}
	$dataset = $_GET["dataset"];
?>
<!DOCTYPE html>
<html>
<head>
	<title><?php echo $dataset ?></title>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<link href="css/bootstrap.min.css" rel="stylesheet" />
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js'></script>
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    <style>
	body{
        //font-family: 'Raleway', sans-serif;
        padding:10px;
    }
    p{
        padding-top:10px;
        font-size:1.3em;
    }
    .node{
        padding:10px;
        //background:red;
        //border:1px solid black;
        height:40vh;
    }
    .node:hover{
        background:lightgray;
    }
    .nimg{
        width:100%;
        height:90%;
    }
	</style>
</head>
<body>
    <h2 style="margin-left:50px;">#<?php echo $dataset?></h2>
    <div class="row" id="clusterView">
    </div>
</body>
</html>
<script>

var firstLoad = true;
var dataset = "<?php echo $dataset ?>";
var objects = [];

function openInNewTab(url) {
    var win = window.open(url, '_blank');
    win.focus();
}

function createNode(id,image,tweets){
	var div = document.createElement("div");
	div.setAttribute("class","col-sm-12 col-md-3 node");
	var img = document.createElement("img");
	img.setAttribute("src", "databaselink/<?php echo $dataset ?>/"+image);
	//img.setAttribute("width", "304");
	img.setAttribute("class", "nimg");
	img.setAttribute("alt", image);   
	var caption = document.createElement("p");
	caption.setAttribute("id",id);
	var node = document.createTextNode(tweets+" retweets");
	caption.appendChild(node);
	div.appendChild(img);
	div.appendChild(caption);
	//div.setAttribute("onClick","openInNewTab(showbucket.php?dataset=<?php echo $dataset ?>&bucket="+id+"&image="+image+")");
	var link = document.createElement("a");
	link.setAttribute("href", "showbucket.php?dataset=<?php echo $dataset ?>&bucket="+id+"&image="+image);
	link.appendChild(div);
	return link;
}

function clearNode(element){
	while (element.firstChild) {
		element.removeChild(element.firstChild);
	}
}


function loadData(){
	
	console.log("loadData called");
	

	$.getJSON('getData.php',{tablename:dataset}, function(data){
        if(data.length==0){
			//No data found
			window.location = "error.html";
			return;
		}
		var changes = false;
		$.each(data, function(key, val) {
			if(key>=objects.length){
				var obj = new Object();
				obj["ID"] = val.ID;	//Bucket ID
				obj["image"] = val.image;
				obj["tweets"] = parseInt(val.mag);
				objects.push(obj);
				changes = true;
			} else {
				if(objects[key]["tweets"]!=val.mag){
					objects[key]["tweets"] = val.mag;
					changes = true;
				}
				//document.getElementById(objects[key]["ID"]).innerHTML = val.mag+" retweets";
			}
	    });
	    
	    nobjects = objects.slice();
	    nobjects.sort(function cmp(obj1,obj2){return (obj2["tweets"]-obj1["tweets"]);});
		//console.log(objects)
		
		if(!changes) return;
		var clusterView = document.getElementById("clusterView");
		clearNode(clusterView);
		var i;
		for(i=0;i<nobjects.length;i++){
			node = createNode(nobjects[i]["ID"],nobjects[i]["image"],nobjects[i]["tweets"]);
			clusterView.appendChild(node);
		}
	}); 
} 

loadData();
setInterval(loadData, 3000);
</script>
<script src="js/jquery-1.10.2.js"></script>
<script src="js/bootstrap.min.js"></script>

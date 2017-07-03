<?php
	error_reporting(E_ALL); 
	ini_set('display_errors', 1);
	if(!isset($_GET["dataset"]) || !isset($_GET["bucket"]) || !isset($_GET["image"]) ){
		echo "Illegal request";
		exit();
	}
	$dataset = $_GET["dataset"];
	$bucket = $_GET["bucket"];
	$image = $_GET["image"];
	//echo $dataset." ".$bucket."<br>";
	
?>
<!DOCTYPE html>
<html>
<head>
	<title>#<?php echo $dataset ?></title>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<link href="css/bootstrap.min.css" rel="stylesheet" />
    <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js'></script>
    <link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
    <style>
	html{
		height:100%;
	}
	body{
        font-family: 'Raleway', sans-serif;
    }
    .tweet{
        //padding-left:25px;
    }
    .tweet:hover{
        background:lightgray;
    }
    .node{
        padding-left: :50px;
        //background:red;
        //border:1px solid black;
        height:15vmin;
        //width: 100%;
    }
    .node:hover{
        background:lightgray;
    }
    .nimg{
        width:80%;
        height:50dip;
        padding-bottom: 10px;
        padding-left: 30dip;
    }
	
	</style>
</head>
<body>
    <div class="container-fluid">
	    <div class="row" style="background:yellow">
		    <div class="col-md-3"></div>
			<div class="col-xs-12 col-md-6">
			  	<img src='databaselink/<?php echo $dataset."/".$image; ?>' style="width:100%;height:60vmin;">
			</div>
		</div>
	    <div class="col-md-3"></div>
	</div>
	<div class="container-fluid">
	    <a><h4 id="tweetHeading">Tweets</h4></a>
	</div>
	    <!--div class="col-xs-6 col-md-4" id="image">
			  	<img src='databaselink/<?php echo $dataset."/".$image; ?>' class="img-circle" style="height: 15vmin">
		</div-->
	<div id="tweetview" class="row">
    </div>
    
	</body>
</html>

<script>

var bucket = <?php echo $bucket ?>;
var dataset = "<?php echo $dataset ?>";
var timestamp = "0";
var tweets = [];


function createNode(id,text,image){
	var div = document.createElement("div");
	div.setAttribute("class","col-sm-12 col-md-12 node");
	var div2 = document.createElement("div");
	div2.setAttribute("class","col-sm-6 col-md-3");
	var img = document.createElement("img");
	img.setAttribute("src", "databaselink/<?php echo $dataset."/"; ?>"+image);
	//img.setAttribute("height", "10%");
	//img.setAttribute("width", "100%");
	img.setAttribute("class", "img-rounded nimg");
	// //img.setAttribute("class", "col-xs-6 col-md-4");
	//img.setAttribute("alt", image);
	var div3 = document.createElement("div");
	div3.setAttribute("class","col-sm-6 col-md-9");   	
	var tweet = document.createElement("p");
	tweet.setAttribute("id",id);
	tweet.setAttribute("class","tweet");
	//tweet.setAttribute("class","col-xs-6 col-md-8");
	var node = document.createTextNode(text);
	tweet.appendChild(node);
	div3.appendChild(tweet);
	div2.appendChild(img);
	div.appendChild(div2);
	div.appendChild(div3);
	
	//div.setAttribute("onClick","openInNewTab(showbucket.php?dataset=<?php echo $dataset ?>&bucket="+id+"&image="+image+")");
	// var link = document.createElement("a");
	// link.setAttribute("href", "showbucket.php?dataset=<?php echo $dataset ?>&bucket="+id+"&image="+image);
	// link.appendChild(div);
	//return link;



	// tweet.setAttribute("id",id);
	// tweet.setAttribute("class","tweet");
	// var node = document.createTextNode(text);
	// tweet.appendChild(node);
	return div;
}

function clearNode(element){
	while (element.firstChild) {
		element.removeChild(element.firstChild);
	}
}


function loadData(){
	
	console.log("LoadData called with timestamp:"+timestamp);
	
	$.getJSON('getTweets.php',{tablename:dataset,BID:bucket,timestamp:timestamp}, function(data){
        console.log("Data "+data);
        if(data.length==0){
			console.log("timestamp not found!");
		}
		
		$.each(data, function(key, val) {
			if(key==0){
				timestamp = val;
			}
			else {
				var obj = new Object();
				obj["ID"] = val.ID;	//Bucket ID
				obj["Text"] = val.Text;
				obj["Image"] = val.Image;
				tweets.push(obj);
			}
		});
		
		var tweetview = document.getElementById("tweetview");
		clearNode(tweetview);
		var i;
		for(i=tweets.length-1;i>=0;i--){
		// 	var node = document.createElement("div");
			node = createNode(tweets[i]["ID"],tweets[i]["Text"],tweets[i]["Image"]);
			tweetview.appendChild(node);
		}
		

		// var listView = document.getElementById("clusterView");
		// clearNode(clusterView);
		// var i;
		// console.log(nobjects.length+"/"+objectLimit);
		// for(i=0;i<nobjects.length && i<objectLimit;i++){
		// 	node = createNode(nobjects[i]["ID"],nobjects[i]["image"],nobjects[i]["tweets"]);
		// 	clusterView.appendChild(node);
		// }
		
        document.getElementById("tweetHeading").innerHTML = tweets.length+" tweets found";
	}); 
	
} 

loadData();
setInterval(loadData, 3000);
</script>
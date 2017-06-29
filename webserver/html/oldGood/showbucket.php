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
        padding:5px;
    }
    .tweet:hover{
        background:lightgray;
    }
	</style>
</head>
<body>
    <div class="container-fluid">
    <div class="row" style="background:black">
    <div class="col-md-3"></div>
    <div class="col-xs-12 col-md-6">
    <img src='databaselink/<?php echo $dataset."/".$image; ?>' style="width:100%;height:60vmin;">
    </div>
    </div>
    <div class="col-md-3"></div>
    </div>
    <div class="container">
    <a><h4 id="tweetHeading">Tweets</h4></a>
    <div id="tweetview" class="row">
    </div>
    </div>
</body>

<script>

var bucket = <?php echo $bucket ?>;
var dataset = "<?php echo $dataset ?>";
var timestamp = "2017-06-03 11:24:10";
var tweets = [];


function createNode(id,text){  
	var tweet = document.createElement("p");
	tweet.setAttribute("id",id);
	tweet.setAttribute("class","tweet");
	var node = document.createTextNode(text);
	tweet.appendChild(node);
	return tweet;
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
				tweets.push(obj);
			}
		});
		
		var tweetview = document.getElementById("tweetview");
		clearNode(tweetview);
		var i;
		for(i=tweets.length-1;i>=0;i--){
			node = createNode(tweets[i]["ID"],tweets[i]["Text"]);
			tweetview.appendChild(node);
		}
		
		
        document.getElementById("tweetHeading").innerHTML = tweets.length+" tweets found";
	}); 
	
} 

loadData();
setInterval(loadData, 3000);
</script>

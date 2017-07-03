<?php
	//Requirements are "tablename" , "Bucket" and "timestamp" in GET request
	error_reporting(E_ALL); 
	ini_set('display_errors', 1);
	
	//Load the database configuration
	require 'dbConfig.php';
	// Create connection
	$conn = new mysqli($servername, $username, $password, "tweetbase");

	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	
	$sql = "SELECT TweetID,Text,Image FROM `".$_GET["tablename"]."` WHERE BID=".$_GET["BID"]." AND UTime>\"".$_GET["timestamp"]."\"";
	$result = $conn->query($sql);

	$data = array();
	$data[0] = date("Y-m-d H:i:s");
	
	if ($result && $result->num_rows > 0) {
	    // output data of each row
	    $i = 1;
	    while($row = $result->fetch_assoc()) {
			$data[$i] = array();
			$data[$i]["ID"] = $row["TweetID"];
			$data[$i]["Text"] = $row["Text"];
			$data[$i]["Image"] = $row["Image"];	
			$i++;
	    }
	}
	
	$conn->close();
	
	header("Content-type: application/json");
	$json = json_encode($data);
	echo $json;
?>

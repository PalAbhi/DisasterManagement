<?php
	error_reporting(E_ALL); 
	ini_set('display_errors', 1);
	
	//Load the database configuration
	require 'dbConfig.php';
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	
	
	//$sql = "SELECT ID,Lat,Lon,Dups,Image FROM `".$_GET["tablename"]."` WHERE Lat IS NOT NULL and Lon IS NOT NULL";
	$sql = "SELECT ID,Lat,Lon,Dups,Image FROM `".$_GET["tablename"]."` ";
	$result = $conn->query($sql);

	$data = array();
	
	if ($result->num_rows > 0) {
	    // output data of each row
	    $i = 0;
	    while($row = $result->fetch_assoc()) {
		#echo "{ lat: " . $row["Lat"]. ", lng: " . $row["Lon"]. "}<br>";
		$data[$i] = array();
		$data[$i]["ID"] = $row["ID"];
		$data[$i]["lat"] = $row["Lat"];		//Lat
		$data[$i]["lng"] = $row["Lon"];		//Long
		$data[$i]["mag"] = $row["Dups"];	//No of duplicates found also named as magnitude
		$data[$i]["image"] = $row["Image"];	//One sample image name
		$i++;
	    }
	}
	
	$conn->close();
	
	header("Content-type: application/json");
	$json = json_encode($data);
	echo $json;
?>

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
	$sql = "SHOW TABLES;";
	$result = $conn->query($sql);
	
	$data = array();
	
	if($result && $result->num_rows > 0){
		$i = 0;
		while($row = $result->fetch_assoc()) {
			$data[$i] = $row["Tables_in_".$dbname];
			$i++;
		}
	}
	
	$conn->close();
	
	header("Content-type: application/json");
	$json = json_encode($data);
	echo $json;	
?>

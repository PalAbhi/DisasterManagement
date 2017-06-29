<!DOCTYPE html>
<html>
<head>
	<style> 
		/* Optional: Makes the sample page fill the window. */
		body {
			padding: 20px;
		}
	</style>
</head>
<body>
	<h2>Available datasets</h2>
<?php
	error_reporting(E_ALL); 
	ini_set('display_errors', 1);
	//Load the database configuration
	/*require 'dbConfig.php';
	
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	} 
	
	$result = $conn->query("show tables");
	
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
			echo $row["Tables_in_$dbname"];
	    }
	}
	
	$conn->close();	*/
	$datasetpath = "databaselink";
	$directories = glob($datasetpath . '/*' , GLOB_ONLYDIR);
	//print_r($directories);
	for($i=0;$i<sizeof($directories);$i++){
		$dataset = basename($directories[$i]);
		echo "<a href='/run.php?dataset=$dataset'>".$dataset."</a><br>";
	}

?>
</body>
</html>

<!------------->
<?php 	
	ob_implicit_flush(1);
	ob_start();
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Default functionality</title>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

</head>
<body>
	<p id="para">Please wait while the task is completed</p>
	<div id="progressbar" style="width:80%"></div>
	<script>
		$( "#progressbar" ).progressbar({
		  value: 0
		});
	</script>	

</body>
</html>

<?php 
	//Flush and send everything in the output buffer to client
	flush();
	ob_flush();
	
	//Creating a subprocess with its STDOUT as $handle
	//$handle behaves like a file pointer
	$handle = popen("python modulelink/MinHash/matcher.py ChennaiFlood", 'r');
	//Loop until end of file
	while (!feof($handle)) {
		//Reach a minimum buffer size before flushing
		echo str_repeat(' ',4096);
		//$data = fgets($handle);
		//echo ".".$data.".<br>";
		//$val = intval(fgets($handle))*10;
		echo fgets($handle)."<br>";
		//echo "<script>document.getElementById('para').innerHTML = '$val';</script>";
		/*if($val>0 and $val<=100){
			echo '<script>$("#progressbar").progressbar( "value", '.$val.' );</script>';
			flush();
			ob_flush();
		}*/
	}
	
	//close the process
	pclose($handle);

?>




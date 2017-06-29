<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>Ready to transfer</title>
	<!-- BOOTSTRAP STYLES-->
	<link href="css/bootstrap.min.css" rel="stylesheet" />
	<!-- GOOGLE FONTS-->
	<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
	
	<style>
	html{
		height:100%;
	}
	body{
		font-family: 'Open Sans', sans-serif;
		height:100%;
		padding-top: 70px;
	}
	
	.titlebar{
		padding-left:10px;
		color:#0088D8;
		font-size:18px;
	}
	
	.download{
		padding:5px;
		border-left:4px solid white;	

	}
	
	.download:hover{
		border-left:4px solid #0088D8;	
	}	
	
	.download-button{
		width:80%;
		margin:0px;
	}	
	</style>
</head>

<body data-spy="scroll" data-target=".navbar" data-offset="100" >

<nav class="navbar navbar-default navbar-fixed-top" style="">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Hi Rahul !</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
			<li><a href="#intro">Profile</a></li>
            <li><a href="#about">History</a></li>
			<li><a href="#contact">Upload</a></li>
			<li><a href="#contact">Messages</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="signin.html"><span class="glyphicon glyphicon-log-out"></span> Sign out</a></li>
          </ul>
        </div>
      </div>
</nav>

<div class="container" style="height:100%">
	<div class="row">
		<div class="col-lg-6 col-sm-12">
			<h3>Available downloads</h3>
		</div>
	</div>
	<br>
	<div class="row titlebar">
		<div class="col-md-6 col-sm-6">
			Filename
		</div>
		<div class="col-md-2 col-sm-2">
			Size
		</div>
		<div class="col-md-2 col-sm-2">
			Owner
		</div>				
	</div>	
	<div class="row download">
		<div class="col-md-6">
			Vampire Diaries Season 7 (720p).zip
		</div>
		<div class="col-md-2 ">
			3.98 GB
		</div>
		<div class="col-md-2">
			By Rahul
		</div>		
		<div class="col-md-2">
			<a href="#" class="btn btn-success btn-sm download-button">Download</a>
		</div>		
	</div>
	<div class="row download">
		<div class="col-md-6 col-sm-6">
			IDM 6.27 Cracked.zip
		</div>
		<div class="col-md-2 col-sm-2">
			25 MB
		</div>
		<div class="col-md-2 col-sm-2">
			By Sayan
		</div>		
		<div class="col-md-2 col-sm-2">
			<a href="#"  class="btn btn-success btn-sm download-button">Download</a>
		</div>		
	</div>
	<div class="row download">
		<div class="col-md-6 col-sm-6">
			Supernatural S4 (480p).zip
		</div>
		<div class="col-md-2 col-sm-2">
			3.02 GB
		</div>
		<div class="col-md-2 col-sm-2">
			By Rahul
		</div>		
		<div class="col-md-2 col-sm-2">
			<a href="#" class="btn btn-success btn-sm download-button">Download</a>
		</div>		
	</div>		
</div>

</body>
</html>	

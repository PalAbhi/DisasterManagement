<?php
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
  <script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js'></script>
  <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAFmHe_o8JIPY_6thkFJpoL-b5D4oErJ6s&callback=initMap"></script>
  
    
<style>
	/* Always set the map height explicitly to define the size of the div
	* element that contains the map. */
	#map {
		height: 95%;
	}
	
	#bar{
		height:5%;
		width:100%;
		background:darkgrey;
		font-size: 14px;
	}
	      
	td{
		padding:10px 20px 10px 20px ;
		color: whitesmoke;
	}
	/*.gm-style-iw {
		height: 100px;
		width: 350px;
		padding: 8px;
	}*/
	
	.thumb {
		padding: 5px;
	}
	      
	/* Optional: Makes the sample page fill the window. */
	html, body {
		height: 100%;
		margin: 0;
		padding: 0;
	}
</style>

</head>
<body>
  	<div id="bar">
  		<table>
  		<tr>
  		<td>Currently showing : #<?php echo $dataset;?></td>
  		<td id="info1">loading..</td>
  		<td id="info2">loading..</td>
  		<td id="info2"><button id="pan" onclick="autopan();" >Autopan</button></td>
  		</tr>
  		</table>
  	</div>
    <div id="map"></div>
    <script>
      
     var map;
     var bounds;
     var markers;
     var dataset;
     var infoWindow;
     
     function autopan(){
	     map.fitBounds(bounds);
 		 map.panToBounds(bounds);
     }
     
     function getImage(path,mag) {
       	mag = mag *4;
       	if(mag>128){
       		mag = 128;
       	}
		return {
			url: 'databaselink/'+dataset+'/'+path,
			// This marker is 20 pixels wide by 32 pixels high.			
			scaledSize: new google.maps.Size(mag, mag),
			strokeColor: 'black',
			// The origin for this image is (0, 0).
			origin: new google.maps.Point(0, 0),
			// The anchor for this image is the base of the flagpole at (0, 32).
			anchor: new google.maps.Point(0, 32)
		};
	 }  
     
      
     function getCircle(magnitude){
       	if(magnitude<5) radius = 10;
       	else if(magnitude<15) radius = 30;
       	else if(magnitude<30) radius = 60;
       	else radius = 100;
		return {
			path: google.maps.SymbolPath.CIRCLE,
			fillColor: 'red',
			fillOpacity: .3,
			//scale: Math.pow(2, magnitude) / 100,
			scale: radius,
			strokeColor: 'white',
			strokeWeight: .5
		};
     }
     
     
     function createMarker(map,latLng,image,mag){
	 		var marker = new google.maps.Marker({
				position: latLng,
				map: map,
				icon: getCircle(parseInt(mag)),
				file:image,
				mag:mag,
				//icon:getImage(val.image,val.mag)
			});
			google.maps.event.addListener(marker,'click',function() {
				infoWindow.close();
				infoWindow.setContent("<heading>"+marker.getPosition().toString()+"</heading><br><b>"+marker.mag+" images found</b><br>"+
					"<a href='databaselink/"+dataset+"/"+marker.file+"'>"+
					"<img class='thumb' src='databaselink/"+dataset+"/"+marker.file+"' height='100'></a>");
				infoWindow.open(map,marker);
				//marker.setIcon('databaselink/ChennaiFlood/'+marker.file);
			});     	
			return marker;
     }
      
	/*This is an AJAX function used to load lat,lon and other data as JSON object file
	  from view.php 
	*/
	function loadData(){
		var locations = 0;
		var firstLoad = false;
		console.log("loadData called");
        if(markers==null){
        	markers = [];
        	firstLoad = true;
        	console.log("First load");
        }

		$.getJSON('getData.php',{tablename:dataset}, function(data){

			if(data.length==0){
				//No data found
				window.location = "error.html";
				return;
			}
			
			if(data.length>markers.length && !firstLoad) alert("New locations found");	
		    $.each(data, function(key, val) {
		    	//console.log("Looping through data "+key);
		    	if(key>=markers.length){
		    		var latLng;
		    		if(val.lat==null || val.lng==null) latLng = null;
		        	else latLng = new google.maps.LatLng(val.lat,val.lng);
		        	
					marker = createMarker(map,latLng,val.image,val.mag);				
					markers.push(marker);
					if(latLng==null) marker.setVisible(false);
					else bounds.extend(latLng);
				}
				else{
					//markers[key].setMap(null);
					if(!markers[key].getVisible() && val.lat!=null && val.lng!=null){					
						markers[key].setPosition(new google.maps.LatLng(val.lat,val.lng));
						markers[key].setVisible(true);
					}
					markers[key].mag = val.mag;
					markers[key].setIcon(getCircle(parseInt(val.mag)));
					//markers[key].setMap(map);
				}
				                 	
		    });
		    
		    var i;
		    for(i=0;i<markers.length;i++){
		    	if(markers[i].getVisible()==true) locations+=1;
		    }

	    
		    document.getElementById("info1").innerHTML = (locations+" locations found");
		    document.getElementById("info2").innerHTML = (markers.length+" events found");
		    
		    if(firstLoad){
		    	map.fitBounds(bounds);
				map.panToBounds(bounds);
			}
      
      	}); 
      }     
      
      function initMap(){
      	console.log("Initmap called");
      	dataset = "<?php echo $dataset ?>";      	
      	bounds = new google.maps.LatLngBounds();
        infoWindow = new google.maps.InfoWindow();      	
      	
        map = new google.maps.Map(document.getElementById('map'), {
        	zoom:5,
        	center:new google.maps.LatLng(22.552070, 88.306644),
        	mapTypeId: 'terrain'
        });    		
	
		loadData();
		setInterval(loadData, 3000);	
      }
      
    </script>
	
  </body>
</html>

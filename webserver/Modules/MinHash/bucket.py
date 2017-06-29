import numpy as np
from gps import get_coordinates
from decimal import Decimal
from os.path import basename
import re
import json


class Bucket:
	'Tuple of signature, image count and co-ordinates'
	
	#Static variables set from matcher.py
	BUCKET_COUNT = 0
	#Cursor used for writing tweets
	CURSOR = None
	#List of tweets (unparsed)
	TWEETS = None
	#path of dataset
	INPUT_PATH = None
	DATASET = None
	
	#	local variables
	size = 0
	signature = None
	bid = None 			#Unique Bucket ID (starts from 1)
	image_list = None	#Conatains redundant information that might be necessary during debugging
	Lat = None			#Latitude
	Lon = None			#Longitude
	finalised = False	#Tells if bucket was recently modified or not
	
	def __init__(self,filename,filepath,image_sign):
		print filename+" "+filepath
		self.signature = image_sign
		Bucket.BUCKET_COUNT+=1
		self.bid = Bucket.BUCKET_COUNT
		self.size = 1		
		self.image_list = [filename]
		self.loadTweets(filename)
		self.loadCoordinates(filepath)
		print "Bucket created with ID "+str(self.bid)+" coodrinates="+str(self.coordinatesExists())
		
	def addSign(self,filename,filepath,image_sign):
		self.finalised = False
		self.image_list.append(filename)
		self.size+=1
		#Try to find the tweet
		self.loadTweets(filename)
		#In case location is missing from bucket try to retrive from a duplicate
		if not self.coordinatesExists():
			self.loadCoordinates(filepath)

	def getID(self):
		return self.bid			
			
	def getSign(self):
		return self.signature
	
	def addCoordinates(self,pLat,pLon):
		self.Lat = pLat
		self.Lon = pLon
	
	def loadCoordinates(self,filepath):
		try:
			'''
			# 	(Deprecated)
			#	Try to load from sampleimage.jpg.loc file
			with open(filepath+'.loc','r') as f:
				lines = f.readlines()
		
			self.Lat = Decimal(lines[0])
			self.Lon = Decimal(lines[1])
			'''
			with open(Bucket.INPUT_PATH+'/metadata/locations.loc','r') as fp:
				lines = fp.readlines()
				
			filename = basename(filepath)
			for line in lines:
				res = re.findall('(\w+\.\w+)\s+(\S+)\s+(\S+)',line)
				if res[0][0]==filename:
					self.Lat = Decimal(res[0][1])
					self.Lon = Decimal(res[0][2])
					return									
		except:
			# If fails or file does not exist find Exif info
			(self.Lat,self.Lon) = get_coordinates(filepath)	
	
	def loadTweets(self,filename):
		if Bucket.TWEETS is None : return
		for line in Bucket.TWEETS:
			#print line
			data = json.loads(line)
			if data["media"]==filename:
				text = data["text"][:256]
				text = text.encode('ascii','ignore')
				Bucket.CURSOR.execute("INSERT INTO `"+Bucket.DATASET+"` (`BID`, `Text`, `UTime`) "\
					+"VALUES (%s,%s, CURRENT_TIMESTAMP)",(self.bid,text))
	
	def getCoordinates(self):
		return (self.Lat,self.Lon)
		
	def coordinatesExists(self):
		return (self.Lat is not None and self.Lon is not None)
		
	def isFinalised(self):
		return self.finalised
		
	def finalise(self):
		self.finalised = True
		

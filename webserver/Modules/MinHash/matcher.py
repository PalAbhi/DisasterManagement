#!/usr/bin/python
import sys
#sys.path.insert(0, "/home/rahul/.local/lib/python2.7/site-packages")

import os
import numpy as np
from os.path import isfile, join
from random import randint
from shutil import copyfile
import h5py
from siglib import createSignature, compareSignature
from comparator import Comparator
from bucket import Bucket
import datetime

# Redirecting STDERR to STDOUT
temp = sys.stderr
sys.stderr = sys.stdout

def makeDir(path):
	if not os.path.exists(path):
	    os.mkdir(path)
	return

if(len(sys.argv)<=1):
	print "Usage : <database name>"
	print "Please note: Table name in MySQL database will be the same as"\
	" the database name.."
	sys.exit("Not enough arguments")


comparator = Comparator()

#	folder for dataset images
INPUT_PATH = 'databaselink/'+sys.argv[1]
OUTPUT_PATH = 'outbaselink/'+sys.argv[1]
Bucket.INPUT_PATH = INPUT_PATH
Bucket.DATASET = sys.argv[1]
#print INPUT_PATH
critial_match = 15 #30 percent


#scan the input files
onlyfiles = [ f for f in os.listdir(INPUT_PATH) if isfile(join(INPUT_PATH,f)) ]

#MySQLdb is an interface for connecting to a MySQL database server from Python.
#It implements the Python Database API v2.0 and is built on top of the MySQL C API.
import MySQLdb

# Open database connections (change login information as per)
db = MySQLdb.connect("localhost","root","mysql123","testbase" )
db2 = MySQLdb.connect("localhost","root","mysql123","tweetbase" )

# prepare cursor objects using cursor() method
cursor = db.cursor()
cursor2 = db2.cursor()

#Create the table
query = "CREATE TABLE `testbase`.`"+sys.argv[1]+"` ( `ID` INT UNSIGNED NOT NULL AUTO_INCREMENT ,"\
		+"`Lat` DOUBLE NULL DEFAULT NULL , `Lon` DOUBLE NULL DEFAULT NULL , `Image` VARCHAR(64) NOT NULL ,"\
		+"`Dups` INT NOT NULL ,`UTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`ID`)) ENGINE = InnoDB;"

try:
	cursor.execute(query);
except:
	print "Could not create table.Already exists?"
	cursor.execute("TRUNCATE "+sys.argv[1]);


query2 = "CREATE TABLE `tweetbase`.`"+sys.argv[1]+"` ( `ID` INT UNSIGNED NOT NULL AUTO_INCREMENT , `BID` INT UNSIGNED NOT NULL ,"\
	+" `Text` VARCHAR(256) NOT NULL , `UTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`ID`)) ENGINE = InnoDB;"
try:
	cursor2.execute(query2);
except:
	print "Could not create table.Already exists?"
	cursor2.execute("TRUNCATE "+sys.argv[1]);


Bucket.CURSOR = cursor2


try:
	with open(INPUT_PATH+'/metadata/tweets.jsonp') as tweet_file: 
		Bucket.TWEETS = tweet_file.readlines()
		print "tweets loaded"
except:
	print "No tweet records found"


makeDir(OUTPUT_PATH)

#	Add the first image in the first bucket
image_path = join(INPUT_PATH,onlyfiles[0])
#	A list of buckets
b = Bucket(onlyfiles[0],image_path,comparator.createSign(image_path))
buckets = [b]
makeDir(join(OUTPUT_PATH,"bucket"+str(b.bid)))
copyfile(image_path,join(OUTPUT_PATH,"bucket"+str(b.bid),onlyfiles[0]))

filesProcessed = 0
old_bucketcount = 0

for fileindex in range(1, len(onlyfiles)):
	currentFile = onlyfiles[fileindex]
	#	Ignore .loc files
	filename, file_extension = os.path.splitext(currentFile)
	if(file_extension==".loc"):
		continue
	
	image_path = join(INPUT_PATH,currentFile)
	sign_t = comparator.createSign(image_path)
	
	if(sign_t is None):
		#	There were not enough keypoints to create signature
		#	We may discard the picture
		continue
	
	
	matched_bucket = None
	match = critial_match
	for b in buckets:
		if(b.getSign() is None):continue  #bucket with images of no proper signature
		nmatch = compareSignature(b.getSign(),sign_t)
		#print "match = "+str(nmatch*2)+"%"
		if(nmatch>=match):
			matched_bucket = b	#Chooses the bucket with best match
			
	#	Now if best match is found then add it to the corresponding bucket or
	#	create a new bucket
	
	if(matched_bucket==None):
		# creating new bucket 
		b = Bucket(currentFile,image_path,sign_t)
		buckets.append(b)
		makeDir(join(OUTPUT_PATH,"bucket"+str(b.bid)))
		copyfile(join(INPUT_PATH,currentFile),join(OUTPUT_PATH,"bucket"+str(b.bid),currentFile))
	else:
		matched_bucket.addSign(currentFile,image_path,sign_t)
		copyfile(join(INPUT_PATH,currentFile),join(OUTPUT_PATH,"bucket"+str(b.bid),currentFile))
	
	
	filesProcessed+=1
	#	After processing N number of files (and in the end) send data to database
	#	This way MySQL server can be saved from sudden huge data traffic as well
	#	as live updates can be shown
	if filesProcessed==20 or fileindex==len(onlyfiles)-1:
		print "Sending data to database ";
		filesProcessed = 0
		#	Upcoming logic is little messy , please follow
		for b in buckets:
			if not b.isFinalised():	
				# Bucket not finalised means it is recently modified			
				if b.getID()>old_bucketcount:
					#	New buckets were created so INSERT
					if b.coordinatesExists():
						cursor.execute("INSERT INTO "+sys.argv[1]+"(ID,Lat,Lon,Image,Dups) VALUES (%s, %s, %s, %s, %s)",\
						(int(b.getID()),(float)(b.Lat),(float)(b.Lon),b.image_list[0],int(b.size),))
					else:					
						cursor.execute("INSERT INTO "+sys.argv[1]+"(ID,Image,Dups) VALUES (%s, %s, %s)",(int(b.getID()),b.image_list[0],int(b.size),))	
				else:
					#	Old buckets were UPDATED
					if b.coordinatesExists():					
						cursor.execute("UPDATE `"+sys.argv[1]+"` SET `Lat` = '%s', `Lon` = '%s', `Dups` = '%s',`UTime` = %s WHERE `"+sys.argv[1]+"`.`ID` = %s",\
						((float)(b.Lat),(float)(b.Lon),int(b.size),str(datetime.datetime.now()),int(b.getID()),))
					else:
						cursor.execute("UPDATE `"+sys.argv[1]+"` SET `Dups` = '%s',`UTime` = %s WHERE `"+sys.argv[1]+"`.`ID` = %s",\
						(int(b.size),str(datetime.datetime.now()),int(b.getID()),))
				b.finalise()
		old_bucketcount = Bucket.BUCKET_COUNT
		db.commit()
		db2.commit()
	#Code prog tells server about the progress (mind the dot)
	print "prog.",(fileindex*100)/(len(onlyfiles)-1)
	print (str(fileindex)+".Reading file '"+currentFile)+"'"
	sys.stdout.flush()
		
	
# disconnect from mysql server
db.close()
db2.close()
'''
	<b>Note</b> : There is no longer a concept of a 'others' folder anymore as the process is supposed to
	be kept live even after dataset exhausts. But one can find single image buckets with bucket.size()
	function and rearrange the dirs
'''		
print "succ"	#code to tell server process ended successfully


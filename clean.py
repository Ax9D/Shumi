import sys
import os
from PIL import Image

def fix(imagePath):
	with Image.open(imagePath) as image:
			image=image.convert("RGBA")
			color=image.load()
			
			for y in range(0,image.size[1]):
				for x in range(0,image.size[0]):
					if(color[x,y][3]==0):
						color[x,y]=(0,0,0,0)
			image.save(imagePath) 

if(len(sys.argv)>1):
	if(os.path.isfile(sys.argv[1])):
		fix(sys.argv[1])
	else:
		for imageFile in os.listdir(sys.argv[1]):
			imagePath=os.path.join(sys.argv[1],imageFile)
			fix(imagePath)
		
else:
	print("Please provide a folder path")

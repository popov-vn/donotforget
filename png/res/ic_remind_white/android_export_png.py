from PIL import Image
import os, sys

def parseFileName(source):
	name = source
	extention = ""
	
	fileName = os.path.basename(source)
	names = os.path.splitext(fileName)
	namesLen = len(names)
	
	if namesLen > 0:
		name = names[0]
		if namesLen > 1:
			extention = names[-1]
	
	return name, extention
	
def processFormats(source, name, prefix, formats, exportDirectory):
	extention = parseFileName(source)[1]

	for format in formats:
		print("Export " + prefix + " name: " + format[0] + " dpi: " + str(format[1]))
		
		dpi = (format[1], format[1])
		size = (format[1], format[1])
		
		# Target directory
		resolutionDirectory = "drawable-" + str(format[0])
		targetPath = os.path.join(exportDirectory, resolutionDirectory)
		os.makedirs(targetPath, exist_ok=True)
		
		# Target file
		exportFile = os.path.join(exportDirectory, resolutionDirectory, name + prefix + extention)
		
		# Convert
		im = Image.open(source)	
		im = im.resize(size, Image.BILINEAR)
		im.save(exportFile, dpi=dpi)
		
def main(argv):
	# Main
	sourceFile = ""
	exportPath = ""
	name = ""
	
	if len(argv) > 1:
		sourceFile = os.path.realpath(argv[1])
	else:
		exit(0);
		
	if len(argv) > 2:
		exportPath = os.path.realpath(argv[2])
	else:
		exportPath = os.path.dirname(os.path.realpath(sourceFile))
	
	if len(argv) > 3:
		name = argv[3]
	else:
		name = parseFileName(sourceFile)[0]
		
	print ("Android resource export utility")
	print ("  Source: " + sourceFile)
	print ("  Export: " + exportPath)
	print ("    Name: " + name)
		

	formats_48p = ("hdpi", 72), ("mdpi", 48),("xhdpi", 96), ("xxhdpi", 144), ("xxxhdpi", 192)
	
	processFormats(sourceFile, name, "_48p", formats_48p, exportPath)
	
# start
main(sys.argv)

#!/bin/bash
file="/media/uoa/Data1/Experiments/ClassificationEL_8805.txt"
while IFS= read -r line
do
	echo "Processing $line"
	java -Xss1g -Xmx10g -cp ELK.0.4.3.jar org.semanticweb.elk.owlapi.examples.TBoxClassification /media/uoa/Data1/ore2014_dataset/ClassificationEL/$line
done <"$file"

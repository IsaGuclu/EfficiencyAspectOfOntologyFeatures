#!/bin/bash
file="/media/uoa/Data1/Experiments/ontologies_EL_8805.txt"
while IFS= read -r line
do
	echo "Processing $line"
	timeout -s KILL 1800 java -Xss1g -Xmx10g -cp TrOWL.1.5.jar trowl.examples.TBoxClassification /media/uoa/Data1/ore2014_dataset/ClassificationEL/$line
done <"$file"

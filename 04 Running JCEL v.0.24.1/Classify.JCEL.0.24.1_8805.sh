#!/bin/bash
file="/media/uoa/Data1/Experiments/ontologies_EL_8805.txt"
while IFS= read -r line
do
	echo "Processing $line"
	timeout -s KILL 1800 java -Xss1g -Xmx10g -cp JCEL.0.24.1.jar jcel.TBoxClassification /media/uoa/Data1/ore2014_dataset/ClassificationEL/$line
done <"$file"

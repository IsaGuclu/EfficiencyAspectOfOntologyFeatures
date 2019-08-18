#!/bin/bash
folder="/media/uoa/Data1/ore2014_dataset/ClassificationEL/"
java -Xss1g -Xmx10g -cp Metrics92.jar experiments.Metrics92 $folder


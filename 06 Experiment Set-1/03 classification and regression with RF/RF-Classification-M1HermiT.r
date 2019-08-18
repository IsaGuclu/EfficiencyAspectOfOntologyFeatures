
options( java.parameters = "-Xmx6g" )
getwd()
setwd("C:/ProgramExt/WS-R")
getwd()

library(plyr)                    # Progress bar
library(randomForest)
library(XLConnect)               # load XLConnect package 

# Load data fom MS Excel Spreadsheet
wk = loadWorkbook("Data_M1HermiT.xlsx") 
data = readWorksheet(wk, sheet="Classification")

data$DLExpressivity <- as.factor(data$DLExpressivity)
data$TryIt <- as.factor(data$TryIt)

nrow(data) # 6484

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

# prediction and testset data frames that we add to with each iteration over
# the folds

prediction <- data.frame()
testsetCopy <- data.frame()

#Creating a progress bar to know the status of CV
progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(TryIt ~ ., data = trainingset, ntree = 500)
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the "TryIt" Column (Our Dependant Variable)
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST CLASSIFICATION
# And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Result_M1HermiT-Classification-Results-1.xlsx")

detach("package:xlsx", TRUE)
detach("package:randomForest", TRUE)
detach("package:XLConnect", TRUE)

library(randomForest)
library(XLConnect)               # load XLConnect package 

# Load data fom MS Excel Spreadsheet
wk = loadWorkbook("Data_M1HermiT.xlsx") 
data = readWorksheet(wk, sheet="Classification")

data$DLExpressivity <- as.factor(data$DLExpressivity)
data$TryIt <- as.factor(data$TryIt)

nrow(data) # 6484

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

# prediction and testset data frames that we add to with each iteration over
# the folds

prediction <- data.frame()
testsetCopy <- data.frame()

#Creating a progress bar to know the status of CV
progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(TryIt ~ ., data = trainingset, ntree = 500)
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the "TryIt" Column (Our Dependant Variable)
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST CLASSIFICATION
# And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Result_M1HermiT-Classification-Results-2.xlsx")

detach("package:xlsx", TRUE)
detach("package:randomForest", TRUE)
detach("package:XLConnect", TRUE)


library(randomForest)
library(XLConnect)               # load XLConnect package 

# Load data fom MS Excel Spreadsheet
wk = loadWorkbook("Data_M1HermiT.xlsx") 
data = readWorksheet(wk, sheet="Classification")

data$DLExpressivity <- as.factor(data$DLExpressivity)
data$TryIt <- as.factor(data$TryIt)

nrow(data) # 6484

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

# prediction and testset data frames that we add to with each iteration over
# the folds

prediction <- data.frame()
testsetCopy <- data.frame()

#Creating a progress bar to know the status of CV
progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(TryIt ~ ., data = trainingset, ntree = 500)
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the "TryIt" Column (Our Dependant Variable)
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST CLASSIFICATION
# And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Result_M1HermiT-Classification-Results-3.xlsx")

detach("package:xlsx", TRUE)
detach("package:randomForest", TRUE)
detach("package:XLConnect", TRUE)


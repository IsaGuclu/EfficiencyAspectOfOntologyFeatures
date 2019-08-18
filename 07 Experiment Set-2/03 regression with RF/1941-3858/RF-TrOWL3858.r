
options( java.parameters = "-Xmx6g" )

getwd()
setwd("C:/ProgramExt/WS-R/")
getwd()

library(plyr)                    # Progress bar
library(randomForest)
library(XLConnect)               # load XLConnect package 

wk = loadWorkbook("Data_TrOWL3858.xlsx") 
data = readWorksheet(wk, sheet="TrOWL3858")

nrow(data) # 5878

data$DLExpressivity <- as.factor(data$DLExpressivity)

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

prediction <- data.frame()
testsetCopy <- data.frame()

progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(Materializing ~ ., data = trainingset, ntree = 500)  #, ntree = 100 , mtry=124
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the Sepal Length Column
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual Sepal Length values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")
result$Difference <- abs(result$Actual - result$Predicted)

# As an example use Mean Absolute Error as Evalution 
summary(result$Difference)
# Min.  1st Qu.   Median     Mean  3rd Qu.     Max. 

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST REGRESSION And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Results-TrOWL3858-1.xlsx")
detach("package:xlsx", TRUE)


options( java.parameters = "-Xmx6g" )

getwd()
setwd("C:/ProgramExt/WS-R/")
getwd()

library(plyr)                    # Progress bar
library(randomForest)
library(XLConnect)               # load XLConnect package 

wk = loadWorkbook("Data_TrOWL3858.xlsx") 
data = readWorksheet(wk, sheet="TrOWL3858")

nrow(data) # 5878

data$DLExpressivity <- as.factor(data$DLExpressivity)

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

prediction <- data.frame()
testsetCopy <- data.frame()

progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(Materializing ~ ., data = trainingset, ntree = 500)  #, ntree = 100 , mtry=124
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the Sepal Length Column
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual Sepal Length values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")
result$Difference <- abs(result$Actual - result$Predicted)

# As an example use Mean Absolute Error as Evalution 
summary(result$Difference)
# Min.  1st Qu.   Median     Mean  3rd Qu.     Max. 

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST REGRESSION And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Results-TrOWL3858-2.xlsx")
detach("package:xlsx", TRUE)



options( java.parameters = "-Xmx6g" )

getwd()
setwd("C:/ProgramExt/WS-R/")
getwd()

library(plyr)                    # Progress bar
library(randomForest)
library(XLConnect)               # load XLConnect package 

wk = loadWorkbook("Data_TrOWL3858.xlsx") 
data = readWorksheet(wk, sheet="TrOWL3858")

nrow(data) # 5878

data$DLExpressivity <- as.factor(data$DLExpressivity)

k = 10 #Folds

# sample from 1 to k, nrow times (the number of observations in the data)
data$id <- sample(1:k, nrow(data), replace = TRUE)
list <- 1:k  #  1  2  3  4  5  6  7  8  9 10

prediction <- data.frame()
testsetCopy <- data.frame()

progress.bar <- create_progress_bar("text")
progress.bar$init(k)

for (i in 1:k){
  # remove rows with id i from dataframe to create training set
  # select rows with id i to create test set
  trainingset <- subset(data, id %in% list[-i])
  testset <- subset(data, id %in% c(i))
  
  # run a random forest model
  mymodel <- randomForest(Materializing ~ ., data = trainingset, ntree = 500)  #, ntree = 100 , mtry=124
  
  # remove response column 1, Sepal.Length
  temp <- as.data.frame(predict(mymodel, testset[,-1]))
  # append this iteration's predictions to the end of the prediction data frame
  prediction <- rbind(prediction, temp)
  
  # append this iteration's test set to the test set copy data frame
  # keep only the Sepal Length Column
  testsetCopy <- rbind(testsetCopy, as.data.frame(testset[,1]))
  
  progress.bar$step()
}

# add predictions and actual Sepal Length values
result <- cbind(prediction, testsetCopy[, 1])
names(result) <- c("Predicted", "Actual")
result$Difference <- abs(result$Actual - result$Predicted)

# As an example use Mean Absolute Error as Evalution 
summary(result$Difference)
# Min.  1st Qu.   Median     Mean  3rd Qu.     Max. 

# Export results to see;
# WHAT IS PREDICTED IN 10-FOLD CROS-VALIDATED RANDOM-FOREST REGRESSION And what is the ACCURACY ?
library(xlsx)
write.xlsx(result, "Results-TrOWL3858-3.xlsx")
detach("package:xlsx", TRUE)


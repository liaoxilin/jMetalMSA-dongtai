postscript("EP.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data"
qIndicator <- function(indicator, problem)
{
fileMOEAD<-paste(resultDirectory, "MOEAD", sep="/")
fileMOEAD<-paste(fileMOEAD, problem, sep="/")
fileMOEAD<-paste(fileMOEAD, indicator, sep="/")
MOEAD<-scan(fileMOEAD)

fileMOEADDS<-paste(resultDirectory, "MOEADDS", sep="/")
fileMOEADDS<-paste(fileMOEADDS, problem, sep="/")
fileMOEADDS<-paste(fileMOEADDS, indicator, sep="/")
MOEADDS<-scan(fileMOEADDS)

fileNSGAII<-paste(resultDirectory, "NSGAII", sep="/")
fileNSGAII<-paste(fileNSGAII, problem, sep="/")
fileNSGAII<-paste(fileNSGAII, indicator, sep="/")
NSGAII<-scan(fileNSGAII)

fileNSGAIII<-paste(resultDirectory, "NSGAIII", sep="/")
fileNSGAIII<-paste(fileNSGAIII, problem, sep="/")
fileNSGAIII<-paste(fileNSGAIII, indicator, sep="/")
NSGAIII<-scan(fileNSGAIII)

algs<-c("MOEAD","MOEADDS","NSGAII","NSGAIII")
boxplot(MOEAD,MOEADDS,NSGAII,NSGAIII,names=algs, notch = TRUE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(3,3))
indicator<-"EP"
qIndicator(indicator, "BB11001")
qIndicator(indicator, "BB11002")
qIndicator(indicator, "BB11003")
qIndicator(indicator, "BB11004")

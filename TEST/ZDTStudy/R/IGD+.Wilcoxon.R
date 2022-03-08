write("", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex",append=FALSE)
resultDirectory<-"TEST/ZDTStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\title{StandardStudy}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\usepackage{amssymb}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\begin{document}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\maketitle", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\section{Tables}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\caption{", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(problem, "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(".IGD+.}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)

  write("\\label{Table:", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(problem, "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(".IGD+.}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)

  write("\\centering", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\begin{scriptsize}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\begin{tabular}{", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(tabularString, "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write(latexTableFirstLine, "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\hline ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("-- ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  }
  else if (i < j) {
    if (is.finite(wilcox.test(data1, data2)$p.value) & wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE) 
      }
    }
    else {
      write("--", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  }
}

latexTableTail <- function() { 
  write("\\hline", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\end{tabular}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\end{scriptsize}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
  write("\\end{table}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
}

### START OF SCRIPT 
# Constants
problemList <-c("BB11001", "BB11002", "BB11003", "BB11004") 
algorithmList <-c("MOEAD", "MOEADDS", "NSGAII", "NSGAIII") 
tabularString <-c("lccc") 
latexTableFirstLine <-c("\\hline  & MOEADDS & NSGAII & NSGAIII\\\\ ") 
indicator<-"IGD+"

 # Step 1.  Writes the latex header
latexHeader()
tabularString <-c("| l | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{4}{c|}{MOEADDS} & \\multicolumn{4}{c|}{NSGAII} & \\multicolumn{4}{c|}{NSGAIII} \\\\") 

# Step 3. Problem loop 
latexTableHeader("BB11001 BB11002 BB11003 BB11004 ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "NSGAIII") {
    write(i , "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
    write(" & ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
          } 
          if (problem == "BB11004") {
            if (j == "NSGAIII") {
              write(" \\\\ ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
            } 
            else {
              write(" & ", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
            }
          }
     else {
    write("&", "TEST/ZDTStudy/R/IGD+.Wilcoxon.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()


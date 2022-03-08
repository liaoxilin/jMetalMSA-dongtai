write("", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex",append=FALSE)
resultDirectory<-"NonPF/ZDTStudy/data"
latexHeader <- function() {
  write("\\documentclass{article}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\title{StandardStudy}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\usepackage{amssymb}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\begin{document}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\maketitle", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\section{Tables}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\caption{", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(problem, "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(".SPREAD.}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)

  write("\\label{Table:", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(problem, "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(".SPREAD.}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)

  write("\\centering", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\begin{scriptsize}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\begin{tabular}{", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(tabularString, "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write(latexTableFirstLine, "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\hline ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
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
    write("-- ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  }
  else if (i < j) {
    if (is.finite(wilcox.test(data1, data2)$p.value) & wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) <= median(data2)) {
        write("$\\blacktriangle$", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE) 
      }
    }
    else {
      write("--", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  }
}

latexTableTail <- function() { 
  write("\\hline", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\end{tabular}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\end{scriptsize}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
  write("\\end{table}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
}

### START OF SCRIPT 
# Constants
problemList <-c("BB11001", "BB11002", "BB11003", "BB11004", "BB11005", "BB11006", "BB11007", "BB11008", "BB11009", "BB11010") 
algorithmList <-c("MOEAD", "MOEADDS", "NSGAII", "NSGAIII") 
tabularString <-c("lccc") 
latexTableFirstLine <-c("\\hline  & MOEADDS & NSGAII & NSGAIII\\\\ ") 
indicator<-"SPREAD"

 # Step 1.  Writes the latex header
latexHeader()
tabularString <-c("| l | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm }p{0.15cm } | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{10}{c|}{MOEADDS} & \\multicolumn{10}{c|}{NSGAII} & \\multicolumn{10}{c|}{NSGAIII} \\\\") 

# Step 3. Problem loop 
latexTableHeader("BB11001 BB11002 BB11003 BB11004 BB11005 BB11006 BB11007 BB11008 BB11009 BB11010 ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "NSGAIII") {
    write(i , "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
    write(" & ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
          } 
          if (problem == "BB11010") {
            if (j == "NSGAIII") {
              write(" \\\\ ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
            } 
            else {
              write(" & ", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
            }
          }
     else {
    write("&", "NonPF/ZDTStudy/R/SPREAD.Wilcoxon.tex", append=TRUE)
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


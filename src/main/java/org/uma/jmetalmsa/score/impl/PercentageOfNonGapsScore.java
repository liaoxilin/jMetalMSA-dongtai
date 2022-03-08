package org.uma.jmetalmsa.score.impl;

import org.uma.jmetalmsa.problem.DynamicallyComposedProblem;
import org.uma.jmetalmsa.solution.MSASolution;
import org.uma.jmetalmsa.score.Score;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class PercentageOfNonGapsScore implements Score {
  @Override
  public <S extends MSASolution> double compute(S solution, char [][]decodedSequences) {
    double numberOfGaps = solution.getNumberOfGaps();
    double totalNumberOfElements = solution.getNumberOfVariables() * solution.getAlignmentLength();
    double nonGapsPercentage=100 * (1 - (numberOfGaps / totalNumberOfElements));

    return nonGapsPercentage;
  }

  @Override
  public boolean isAMinimizationScore() {
    return false;
  }

  @Override
  public String getName() {
    return "SP";
  }

  @Override
  public String getDescription() {
    return "Percentage of gaps in a multiple sequence";
  }//非“-”所占的百分比
}

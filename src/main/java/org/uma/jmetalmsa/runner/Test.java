//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetalmsa.runner;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetalmsa.algorithm.moead.MOEADMSABuilder;
import org.uma.jmetalmsa.crossover.SPXMSACrossover;
import org.uma.jmetalmsa.mutation.ShiftClosedGapsMSAMutation;
import org.uma.jmetalmsa.problem.BAliBASE_MSAProblem;
import org.uma.jmetalmsa.score.Score;
import org.uma.jmetalmsa.score.impl.PercentageOfAlignedColumnsScore;
import org.uma.jmetalmsa.score.impl.PercentageOfNonGapsScore;
import org.uma.jmetalmsa.solution.MSASolution;

import java.util.ArrayList;
import java.util.List;


/**
 * Class to configure and run the MOCell algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class Test {
  /**
   * Constructor
   *
   * @param builder
   */


  /**
   * Arguments: problemName dataDirectory maxEvaluations populationSize
   * @param args Command line arguments.
   */
  public static void main(String[] args) throws Exception {
    BAliBASE_MSAProblem problem;
    Algorithm<List<MSASolution>> algorithm;
    CrossoverOperator<MSASolution> crossover;
    MutationOperator<MSASolution> mutation;
    SelectionOperator selection;

    if (args.length != 4) {
      throw new JMetalException("Wrong number of arguments") ;
    }
    //????????????
    String problemName = args[0];
    String dataDirectory = args[1];
    //??????????????????
    Integer maxEvaluations = Integer.parseInt(args[2]);
    //????????????
    Integer populationSize = Integer.parseInt(args[3]);
//    String referenceParetoFront = "";


    //????????????---- ??????????????????????????????
    crossover = new SPXMSACrossover(0.8);
    //????????????---- ????????????
    mutation = new ShiftClosedGapsMSAMutation(0.2);
        //????????????   ??????selection???????????????????????????????????????????????????
    selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());

    //?????????????????????????????? --- ?????????scoreList?????????????????????
    List<Score> scoreList = new ArrayList<>();

    scoreList.add(new PercentageOfAlignedColumnsScore());//????????????????????????
    scoreList.add(new PercentageOfNonGapsScore());//??????-?????????????????????
    //scoreList.add(new SumOfPairsScore(new PAM250()));//SOP,Sum of pairs"

    problem = new BAliBASE_MSAProblem(problemName, dataDirectory, scoreList);   //??????????????????

    SolutionListEvaluator<MSASolution> evaluator;

    evaluator = new SequentialSolutionListEvaluator<>() ;//?????????????????????????????????

    algorithm = new MOEADMSABuilder(problem, MOEADMSABuilder.Variant.MOEAD)    //???????????????????????????
            .setCrossover(crossover)
            .setMutation(mutation)
            .setMaxEvaluations(maxEvaluations)
            .setPopulationSize(populationSize)
            .setDataDirectory("MOEAD_Weights")
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();  //????????????

    List<MSASolution> population = algorithm.getResult();  //?????????????????????????????????????????????


   //????????????
    long computingTime = algorithmRunner.getComputingTime();
    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    //12.8
//    JMetalLogger.logger.info("Number of evaluations: " + algorithmRunner.);

    for (MSASolution solution : population) {
      for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
        if (!scoreList.get(i).isAMinimizationScore()) {
          solution.setObjective(i, -1.0 * solution.getObjective(i));
        }
      }
    }

//    DefaultFileOutputContext varFile = new  DefaultFileOutputContext("VAR." + problem +"." + algorithm.getName()+ ".tsv");
    DefaultFileOutputContext varFile = new  DefaultFileOutputContext("VAR.tsv");
    varFile.setSeparator("\n");
//    DefaultFileOutputContext funFile = new  DefaultFileOutputContext("FUN." + problem +"." + algorithm.getName()+ ".tsv");
    DefaultFileOutputContext funFile = new  DefaultFileOutputContext("FUN.tsv");
    funFile.setSeparator("\t");

   
    new SolutionListOutput(population)
            .setVarFileOutputContext(varFile)
            .setFunFileOutputContext(funFile)
            .print();

//    if(!referenceParetoFront.equals("")){
//      printQ
//    }


    evaluator.shutdown();


  }
}

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
    //就收参数
    String problemName = args[0];
    String dataDirectory = args[1];
    //最大评估次数
    Integer maxEvaluations = Integer.parseInt(args[2]);
    //种群规模
    Integer populationSize = Integer.parseInt(args[3]);
//    String referenceParetoFront = "";


    //交叉因子---- 两个解从切割部位交叉
    crossover = new SPXMSACrossover(0.8);
    //异变因子---- 随机突变
    mutation = new ShiftClosedGapsMSAMutation(0.2);
        //选择算子   这个selection虽然定义了但是真个类中并没有用到它
    selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());

    //这个应该是存分数的了 --- 可以往scoreList中添加打分函数
    List<Score> scoreList = new ArrayList<>();

    scoreList.add(new PercentageOfAlignedColumnsScore());//已对齐列的百分比
    scoreList.add(new PercentageOfNonGapsScore());//非“-”所占的百分比
    //scoreList.add(new SumOfPairsScore(new PAM250()));//SOP,Sum of pairs"

    problem = new BAliBASE_MSAProblem(problemName, dataDirectory, scoreList);   //定义求解问题

    SolutionListEvaluator<MSASolution> evaluator;

    evaluator = new SequentialSolutionListEvaluator<>() ;//顺序解决方案列表评估器

    algorithm = new MOEADMSABuilder(problem, MOEADMSABuilder.Variant.MOEAD)    //将组件注册到算法中
            .setCrossover(crossover)
            .setMutation(mutation)
            .setMaxEvaluations(maxEvaluations)
            .setPopulationSize(populationSize)
            .setDataDirectory("MOEAD_Weights")
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();  //执行算法

    List<MSASolution> population = algorithm.getResult();  //对齐后的序列列表；即获得结果集


   //输出结果
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

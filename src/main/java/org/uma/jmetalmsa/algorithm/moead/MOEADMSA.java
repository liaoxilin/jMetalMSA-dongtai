package org.uma.jmetalmsa.algorithm.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetalmsa.problem.MSAProblem;
import org.uma.jmetalmsa.solution.MSASolution;

import java.util.ArrayList;
import java.util.List;


public class MOEADMSA extends AbstractMOEAD<MSASolution> {


  public MOEADMSA(Problem<MSASolution> problem,  //解决的问题类
                  int populationSize,  //种群大小
                  int resultPopulationSize,  //返回种群大小
                  int maxEvaluations,   //最大评估次数，即停止条件
                  MutationOperator<MSASolution> mutation,   //变异算子
                  CrossoverOperator<MSASolution> crossover,  //交叉算子
                  FunctionType functionType,
                  String dataDirectory,
                  double neighborhoodSelectionProbability,
                  int maximumNumberOfReplacedSolutions,
                  int neighborSize) {
    super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
            dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
            neighborSize);
      //构造函数里提供的组件

    randomGenerator = JMetalRandom.getInstance();
  }

  @Override
  public void run() {
    initializePopulation();   //初始化种群
    initializeUniformWeight();   //jmetal框架里的 初始化权重
    initializeNeighborhood();     //jmetal框架里的 初始化邻居
    initializeIdealPoint();       //jmetal框架里的 初始化理想点

//    initializeNeighIdealPoint();   //初始化邻域理想点
//    initializeNeighWorstPoint();   //初始化邻域最差点

    evaluations = populationSize;  //评估值为种群规模
    do {     //只要评估值<最大种群数，即终止循环条件
      int[] permutation = new int[populationSize];   //定义一个规模为种群规模的数组
      MOEADUtils.randomPermutation(permutation, populationSize);//permutation存的是1到99，打乱顺序的

      for (int i = 0; i < populationSize; i++) {  //循环
        int subProblemId = permutation[i];  //子问题的id]


        NeighborType neighborType = chooseNeighborType();
        List<MSASolution> parents = parentSelection(subProblemId, neighborType);  //选择母代

        List<MSASolution> children = crossoverOperator.execute(parents);  //执行单点交叉操作，产生的结果储存于children

        MSASolution child = children.get(0); //child为子代的第一个结果
        mutationOperator.execute(child);
        problem.evaluate(child);

        evaluations++;

        updateIdealPoint(child);
        //12.6

//        System.out.print(neighborType);
//        neighborType = changeNeighborType(child, subProblemId);
//        System.out.println("--->"+neighborType);
//        updateNeighIdealPoint( child, subProblemId);
//        updateNeighWorstPoint(child, subProblemId);
//        if(neighborType!= null){
          updateNeighborhood(child, subProblemId, neighborType);
//        }

      }
    } while (evaluations < maxEvaluations);

    for (MSASolution solution : population) {
      for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
          solution.setObjective(i, -1.0 * solution.getObjective(i));
      }
    }
  }

  protected void initializePopulation() {
    population = ((MSAProblem) problem).createInitialPopulation(populationSize);
  }

  @Override
  public List<MSASolution> getResult() {
    return population;
  }

  protected List<MSASolution> parentSelection(int subProblemId, NeighborType neighborType) {  //选择两个母代
    List<Integer> matingPool = matingSelection(subProblemId, 2, neighborType);

    List<MSASolution> parents = new ArrayList<>(3);

    parents.add(population.get(matingPool.get(0)));
    parents.add(population.get(matingPool.get(1)));

    return parents;
  }

  /**
   * @param subproblemId  the id of current subproblem
   * @param neighbourType neighbour type
   */
  protected List<Integer> matingSelection(int subproblemId, int numberOfSolutionsToSelect, NeighborType neighbourType) {
//一个随机从邻居中随机选择，添加到方案中的过程。

    int neighbourSize;   //邻居规模
    int selectedSolution;   //已选择的解决方案
    List<Integer> listOfSolutions = new ArrayList<>(numberOfSolutionsToSelect);//定义一个方案列表
    neighbourSize = neighborhood[subproblemId].length;  //邻居规模=子问题邻居的长度


    while (listOfSolutions.size() < numberOfSolutionsToSelect) {//方案列表的长度<可以选择方案的数量
      int random;
      if (neighbourType == NeighborType.NEIGHBOR) {  //如果是邻居
        random = randomGenerator.nextInt(0, neighbourSize - 1);  //随机产生一个1-邻居规模-1的数
        selectedSolution = neighborhood[subproblemId][random];  //已选择的方案=随机选择的邻居的方案
      } else {
        selectedSolution = randomGenerator.nextInt(0, populationSize - 1);
      }
      boolean flag = true;  //flag表示该方案有没有被选择过
      for (Integer individualId : listOfSolutions) {
        if (individualId == selectedSolution) {
          flag = false;
          break;
        }
      }

      if (flag) {
        listOfSolutions.add(selectedSolution);//添加方案
      }
    }

    return listOfSolutions;
  }

  @Override
  public String getName() {
    return "MOEAD";
  }

  @Override
  public String getDescription() {
    return "Version of MOEA/D for solving MSA problems";
  }
}

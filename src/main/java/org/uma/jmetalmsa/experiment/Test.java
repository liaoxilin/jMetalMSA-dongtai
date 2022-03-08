package org.uma.jmetalmsa.experiment;

import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.forester.msa.Msa;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import org.uma.jmetalmsa.algorithm.moead.MOEADMSABuilder;
import org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder;
import org.uma.jmetalmsa.algorithm.nsgaIII.NSGAIIIMSABuilder;
import org.uma.jmetalmsa.algorithm.nsgaii.NSGAIIMSABuilder;
import org.uma.jmetalmsa.crossover.SPXMSACrossover;
import org.uma.jmetalmsa.mutation.ShiftClosedGapsMSAMutation;
import org.uma.jmetalmsa.problem.BAliBASE_MSAProblem;
import org.uma.jmetalmsa.problem.MSAProblem;
import org.uma.jmetalmsa.score.Score;
import org.uma.jmetalmsa.score.impl.PercentageOfAlignedColumnsScore;
import org.uma.jmetalmsa.score.impl.PercentageOfNonGapsScore;
import org.uma.jmetalmsa.solution.MSASolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    private static final int INDEPENDENT_RUNS = 10 ;

    private static String problemName1 = "BB11001";
    private static String problemName2 = "BB11002";
    private static String problemName3 = "BB11003";
    private static String problemName4 = "BB11004";
    private static String problemName5 = "BB11005";
    private static String problemName6 = "BB11006";
    private static String problemName7 = "BB11007";
    private static String problemName8 = "BB11008";
    private static String problemName9 = "BB11009";
    private static String problemName10 = "BB11010";
    private static String dataDirectory = "E:\\Projects\\IdeaProjects\\jMetalMSA-dongtai\\example";
    //最大评估次数
    private static Integer maxEvaluations = 10000;
    //种群规模
    private static Integer populationSize =100;




    public static void main(String[] args) throws IOException, CompoundNotFoundException {
        if (args.length != 1) {
            throw new JMetalException("Missing argument: experimentBaseDirectory") ;
        }
        String experimentBaseDirectory = args[0] ;


        QualityIndicator indicator;
        List<Score> scoreList = new ArrayList<>();
        scoreList.add(new PercentageOfAlignedColumnsScore());//已对齐列的百分比
        scoreList.add(new PercentageOfNonGapsScore());//非“-”所占的百分比


        List<ExperimentProblem<MSASolution>> problemList =new ArrayList<>();
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName1,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName2,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName3,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName4,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName5,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName6,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName7,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName8,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName9,dataDirectory,scoreList)));
        problemList.add(new ExperimentProblem<>(new BAliBASE_MSAProblem(problemName10,dataDirectory,scoreList)));

        List<ExperimentAlgorithm<MSASolution,List<MSASolution>>> algorithmList =
                configureAlgorithmList(problemList);


        List<String> referenceFrontFileNames = Arrays.asList("BB11001.pf", "BB11002.pf", "BB11003.pf", "BB11004.pf",
                "BB11001.pf","BB11001.pf","BB11001.pf","BB11001.pf","BB11001.pf","BB11001.pf");

        Experiment<MSASolution, List<MSASolution>> experiment =
                new ExperimentBuilder<MSASolution, List<MSASolution>>("ZDTStudy")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setReferenceFrontDirectory("/pareto")
                        .setReferenceFrontFileNames(referenceFrontFileNames)
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        .setIndicatorList(Arrays.asList(
                                new Epsilon<MSASolution>(), new Spread<MSASolution>(), new GenerationalDistance<MSASolution>(),
                                new PISAHypervolume<MSASolution>(),
                                new InvertedGenerationalDistance<MSASolution>(),
                                new InvertedGenerationalDistancePlus<MSASolution>()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();

        new ExecuteAlgorithms<>(experiment).run();
        new ComputeQualityIndicators<>(experiment).run() ;
        new GenerateLatexTablesWithStatistics(experiment).run() ;
        new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run() ;


    }

    private static List<ExperimentAlgorithm<MSASolution, List<MSASolution>>> configureAlgorithmList(List<ExperimentProblem<MSASolution>> problemList) {
        List<ExperimentAlgorithm<MSASolution,List<MSASolution>>> algorithms = new ArrayList<>();
        Algorithm<List<MSASolution>> algorithm;
        CrossoverOperator<MSASolution> crossover;
        MutationOperator<MSASolution> mutation;
        SelectionOperator selection;
        //交叉因子---- 两个解从切割部位交叉
        crossover = new SPXMSACrossover(0.8);
        //异变因子---- 随机突变
        mutation = new ShiftClosedGapsMSAMutation(0.2);
        //选择算子   这个selection虽然定义了但是真个类中并没有用到它
        selection = new BinaryTournamentSelection(new RankingAndCrowdingDistanceComparator());

        SolutionListEvaluator<MSASolution> evaluator;
        evaluator = new SequentialSolutionListEvaluator<>() ;//顺序解决方案列表评估器
        for (int i = 0; i < problemList.size(); i++) {
            algorithm = new MOEADMSABuilder(problemList.get(i).getProblem(), MOEADMSABuilder.Variant.MOEAD)    //将组件注册到算法中
                    .setCrossover(crossover)
                    .setMutation(mutation)
                    .setMaxEvaluations(maxEvaluations)
                    .setPopulationSize(populationSize)
                    .setDataDirectory("MOEAD_Weights")
                    .build() ;
            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

        }

        for (int i = 0; i < problemList.size(); i++) {
            algorithm = new MOEADDSMSABuilder(problemList.get(i).getProblem(), MOEADDSMSABuilder.Variant.MOEAD)    //将组件注册到算法中
                    .setCrossover(crossover)
                    .setMutation(mutation)
                    .setMaxEvaluations(maxEvaluations)
                    .setPopulationSize(populationSize)
                    .setDataDirectory("MOEAD_Weights")
                    .build() ;
            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

        }

        for (int i = 0; i < problemList.size(); i++) {
            algorithm = new NSGAIIMSABuilder(problemList.get(i).getProblem(), crossover, mutation, NSGAIIBuilder.NSGAIIVariant.NSGAII)
                    .setSelectionOperator(selection)
                    .setMaxEvaluations(maxEvaluations)
                    .setPopulationSize(populationSize)
                    .setSolutionListEvaluator(evaluator)
                    .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

        }

        for (int i = 0; i < problemList.size(); i++) {
            algorithm = new NSGAIIIMSABuilder(problemList.get(i).getProblem())
                    .setCrossoverOperator(crossover)
                    .setMutationOperator(mutation)
                    .setSelectionOperator(selection)
                    .setMaxIterations(maxEvaluations)
                    .setSolutionListEvaluator(evaluator)
                    .build();

            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));

        }


        return algorithms;

    }
}

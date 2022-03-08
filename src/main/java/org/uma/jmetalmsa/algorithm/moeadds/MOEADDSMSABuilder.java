package org.uma.jmetalmsa.algorithm.moeadds;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEAD;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetalmsa.algorithm.moead.MOEADMSA;
import org.uma.jmetalmsa.algorithm.moead.MOEADMSABuilder;
import org.uma.jmetalmsa.crossover.SPXMSACrossover;
import org.uma.jmetalmsa.mutation.ShiftClosedGapsMSAMutation;
import org.uma.jmetalmsa.solution.MSASolution;


    /**
     * Builder class for algorithm MOEA/D and variants
     *
     * @author Antonio J. Nebro
     * @version 1.0
     */
    public class MOEADDSMSABuilder implements AlgorithmBuilder<AbstractMOEAD<MSASolution>> {
        public enum Variant {MOEAD} ;

        protected Problem<MSASolution> problem ;

        /** T in Zhang & Li paper */
        protected int neighborSize;
        /** Delta in Zhang & Li paper */
        protected double neighborhoodSelectionProbability;
        /** nr in Zhang & Li paper */
        protected int maximumNumberOfReplacedSolutions;

        protected MOEAD.FunctionType functionType;

        protected CrossoverOperator<MSASolution> crossover;
        protected MutationOperator<MSASolution> mutation;
        protected String dataDirectory;

        protected int populationSize;
        protected int resultPopulationSize ;

        protected int maxEvaluations;

        protected int numberOfThreads ;

        protected org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder.Variant moeadVariant ;

        /** Constructor */
        public MOEADDSMSABuilder(Problem<MSASolution> problem, org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder.Variant variant) {
            this.problem = problem ;
            populationSize = 300 ;
            resultPopulationSize = 300 ;
            maxEvaluations = 150000 ;
            crossover = new SPXMSACrossover(0.8) ;
            mutation = new ShiftClosedGapsMSAMutation(0.2);
            functionType = MOEAD.FunctionType.PBI;//MOEAD.FunctionType.TCHE ;
            neighborhoodSelectionProbability = 0.1 ;
            maximumNumberOfReplacedSolutions = 2 ;
            dataDirectory = "" ;
            neighborSize = 20 ;
            numberOfThreads = 1 ;
            moeadVariant = variant ;

        }

        /* Getters/Setters */
        public int getNeighborSize() {
            return neighborSize;
        }

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public int getPopulationSize() {
            return populationSize;
        }

        public int getResultPopulationSize() {
            return resultPopulationSize;
        }

        public String getDataDirectory() {
            return dataDirectory;
        }

        public MutationOperator<MSASolution> getMutation() {
            return mutation;
        }

        public CrossoverOperator<MSASolution> getCrossover() {
            return crossover;
        }

        public MOEAD.FunctionType getFunctionType() {
            return functionType;
        }

        public int getMaximumNumberOfReplacedSolutions() {
            return maximumNumberOfReplacedSolutions;
        }

        public double getNeighborhoodSelectionProbability() {
            return neighborhoodSelectionProbability;
        }

        public int getNumberOfThreads() {
            return numberOfThreads ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setPopulationSize(int populationSize) {
            this.populationSize = populationSize;

            return this;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setResultPopulationSize(int resultPopulationSize) {
            this.resultPopulationSize = resultPopulationSize;

            return this;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;

            return this;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setNeighborSize(int neighborSize) {
            this.neighborSize = neighborSize ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
            this.neighborhoodSelectionProbability = neighborhoodSelectionProbability ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setFunctionType(MOEAD.FunctionType functionType) {
            this.functionType = functionType ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
            this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setCrossover(CrossoverOperator<MSASolution> crossover) {
            this.crossover = crossover ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setMutation(MutationOperator<MSASolution> mutation) {
            this.mutation = mutation ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setDataDirectory(String dataDirectory) {
            this.dataDirectory = dataDirectory ;

            return this ;
        }

        public org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder setNumberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads ;

            return this ;
        }

        public AbstractMOEAD<MSASolution> build() {
            AbstractMOEAD<MSASolution> algorithm = null ;
            if (moeadVariant.equals(org.uma.jmetalmsa.algorithm.moeadds.MOEADDSMSABuilder.Variant.MOEAD)) {
                algorithm = new MOEADDSMSA(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                        crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                        maximumNumberOfReplacedSolutions, neighborSize);
            }

            return algorithm ;
        }
    }


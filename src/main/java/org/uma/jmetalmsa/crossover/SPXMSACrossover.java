package org.uma.jmetalmsa.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetalmsa.solution.MSASolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Antonio J. Nebro
 * @version 1.0 Implements a single point crossover for MSA representation  实现MSA表示的单点交叉
 */
public class SPXMSACrossover implements CrossoverOperator<MSASolution> {
  private JMetalRandom randomGenerator;//随机发生器
  private double probability;

  public SPXMSACrossover(double probability) {
    if ((probability < 0) || (probability > 1)) {
      throw new JMetalException("Crossover probability value invalid: " + probability);
    }

    this.randomGenerator = JMetalRandom.getInstance();
    this.probability = probability;
  }

  @Override
  /**
   * Checks conditions and return the result of performing single point crossover
   * @param parents
   * @return
   */
  public List<MSASolution> execute(List<MSASolution> parents) {
    if (parents == null) {
      throw new JMetalException(this.getClass().getName() + "\tNull pointer exception");
    } else if (parents.size() < 2) {
      throw new JMetalException(
              this.getClass().getName() + "\tNot enough parents for crossover:\t" + parents.size());
    } else if (parents.size() > 2) {
      throw new JMetalException(
              this.getClass().getName() + "\tToo many parents for crossover:\t" + parents.size());
    } else if (parents.get(0).getNumberOfVariables() != parents.get(1).getNumberOfVariables()) {
      throw new JMetalException(this.getClass().getName() + "\tParents of different length\t");
    }

    return doCrossover(parents);
  }

  /**
   * Performs a single point crossover of two parents. Uses the same cutting point for all sequences
   *
   * @return a list containing the generated offspring
   */

  private List<MSASolution> doCrossover(List<MSASolution> parents) {

    MSASolution parent1 = parents.get(0);
    MSASolution parent2 = parents.get(1);   //获取两个母代

    List<MSASolution> children = new ArrayList<>();  //创建子代

    children.add(MSACrossover(parent1, parent2));
    children.add(MSACrossover(parent2, parent1)); //添加子代

    return children;
  }

  private MSASolution MSACrossover(MSASolution parentA, MSASolution parentB) {

    MSASolution child;

    if (this.randomGenerator.nextDouble() < this.probability) {  //随机产生一个在0-1之间的浮点数

      int cut = selectRandomColumn(parentA);  //cut=随机生成一个1-（length-1）区间的数

      List<List<Integer>> gapsGroupFirstBloq = new ArrayList<List<Integer>>();
      List<Integer> carsCounterParentA = new ArrayList<Integer>();
      List<Integer> gapsGroup;
      int numgaps;


      for (int i = 0; i < parentA.getNumberOfVariables(); i++) {   //i代表有几列
        gapsGroup = parentA.getVariableValue(i);  //将parentsA的变量值存到gapsGroup的列表中
        List<Integer> gaps = new ArrayList<Integer>();   //创建一个gaps链表
        numgaps = 0;
        for (int j = 1; j < gapsGroup.size(); j += 2) {  //编码方式是对的形式，如（2，7）、（15，15）
          if (cut >= gapsGroup.get(j)) {  //如果要交换的列>当前列，则将（2，7）都保存到gaps里
            gaps.add(gapsGroup.get(j - 1));
            gaps.add(gapsGroup.get(j));
            numgaps += gapsGroup.get(j) - gapsGroup.get(j - 1) + 1;  //numgaps用于保存里面有“-”的数量
          } else {      //如果要交换的列<当前列，则将（2，cut）保存到gaps里，numgaps加上相应的值
            if (cut >= gapsGroup.get(j - 1)) {
              gaps.add(gapsGroup.get(j - 1));
              gaps.add(cut);
              numgaps += cut - gapsGroup.get(j - 1) + 1;
            }
            break;
          }
        }
        gapsGroupFirstBloq.add(gaps);   //gapsGroupFirstBloq用于保存切割前半部的链
        carsCounterParentA.add(cut - numgaps + 1);//carsCounterParentA 用于保存前半部分的“-”的数量
      }

      int carsCountParentB;
      List<Integer> positionsToCutParentB = new ArrayList<Integer>();

      for (int i = 0; i < parentB.getNumberOfVariables(); i++) {
        gapsGroup = parentB.getVariableValue(i);  //将parentB的变量序列存在gapsGroup中
        if (gapsGroup.size() > 0) {   //如果gapsGroup不为空
          carsCountParentB = 0;  //储存parentB中字母的个数
          for (int j = 0; j < gapsGroup.size(); j += 2) {  //循环
            if (j > 0)   //用于区别第一位和后面的
              carsCountParentB += gapsGroup.get(j) - gapsGroup.get(j - 1) - 1;  //（2，7）（15，15）之间，用15-7-1；
            else
              carsCountParentB += gapsGroup.get(j);

            if (carsCountParentB >= carsCounterParentA.get(i)) {  //如果在第i列，parentB的字母数，大于等于parentA前半部分“-”的数量
              positionsToCutParentB.add(gapsGroup.get(j) - (carsCountParentB - carsCounterParentA.get(i))); //则parentB切割位置
              break;
            }
          }

          if (carsCountParentB < carsCounterParentA.get(i)) {
            if (gapsGroup.size() > 0) {
              carsCountParentB = gapsGroup.get(gapsGroup.size() - 1) + (carsCounterParentA.get(i) - carsCountParentB) + 1;
              //if(carsCountParentB >= parent2.sizeAligment ) carsCountParentB=parent2.sizeAligment-1;
              positionsToCutParentB.add(carsCountParentB);
            }
          }
        } else {  //SeqB has not Gaps
          positionsToCutParentB.add(carsCounterParentA.get(i));
        }
      }

      Integer MinPos = Collections.min(positionsToCutParentB);
      int pos;
      List<Integer> gaps;
      int lastGap, posA;
      for (int i = 0; i < parentB.getNumberOfVariables(); i++) {  //循环
        posA = cut;
        pos = positionsToCutParentB.get(i);  //第i列B应当切割的位置
        gaps = gapsGroupFirstBloq.get(i);  //保留的A的前半段的链
        if (pos > MinPos) {    //如果切割位置>最小切割位置     则在A的后面补上gaps
          if (gaps.size() > 0) {
            lastGap = gaps.get(gaps.size() - 1);   //A前半段最后一位的位置
            if (lastGap != posA) {   //如不等于cut的位置,则添加gaps对
              gaps.add(posA + 1);
              gaps.add(posA + (pos - MinPos));
            } else {
              gaps.set(gaps.size() - 1, posA + (pos - MinPos));
            }
          } else {
            gaps.add(posA + 1);
            gaps.add(posA + (pos - MinPos));
          }

          posA += (pos - MinPos);
        }

        gapsGroup = parentB.getVariableValue(i);
        for (int j = 0; j < gapsGroup.size(); j += 2) {  //B从0开始插入

          if (gapsGroup.get(j) >= pos) {  //B从应当切割的位置开始插入到A里
            gaps.add(posA + (gapsGroup.get(j) - pos) + 1);
            gaps.add(posA + (gapsGroup.get(j + 1) - pos) + 1);
          }
        }
      }

      child = new MSASolution(parentA.getMSAProblem(), gapsGroupFirstBloq);

      child.mergeGapsGroups();

    } else {

      child = new MSASolution(parentA);

    }
    return child;
  }


  /**
   * Select a column randomly
   */
  public int selectRandomColumn(MSASolution solution) {
    return randomGenerator.nextInt(1, solution.getAlignmentLength() - 1);  //随机生成一个1-length-1区间的数
  }

  @Override
  public int getNumberOfParents() {
    return 2;
  }
}


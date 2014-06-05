/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.engine.optimizers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import orbital.algorithm.evolutionary.Gene;
import orbital.algorithm.evolutionary.GeneticAlgorithm;
import orbital.algorithm.evolutionary.GeneticAlgorithm.Configuration;
import orbital.algorithm.evolutionary.GeneticAlgorithmProblem;
import orbital.algorithm.evolutionary.Genome;
import orbital.algorithm.evolutionary.IncrementalGeneticAlgorithm;
import orbital.algorithm.evolutionary.Population;
import orbital.algorithm.evolutionary.Selectors;
import orbital.logic.functor.Function;

import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackItem;
import pl.gda.pg.eti.kernelhive.engine.interfaces.IKnapsackSolver;

public class GeneticKnapsackSolver implements IKnapsackSolver, GeneticAlgorithmProblem {
	
	public List<IKnapsackItem> currentItems;
	public int currentCapacity;
	
	private int populationSize;
	private int stopPercent;
	double maxRecombination;
	double maxMutation;
				
	public GeneticKnapsackSolver(int populationSize, int stopPercent, double maxRecombination, double maxMutation) {
		this.populationSize = populationSize;
		this.stopPercent = stopPercent;
		this.maxRecombination = maxRecombination;
		this.maxMutation = maxMutation;
	}

	@Override
	public boolean[] solve(List<IKnapsackItem> items, int capacity) {
		this.currentItems = items;
		this.currentCapacity = capacity;

        Configuration config = new GeneticAlgorithm.Configuration(this,
                                               Selectors.likelyBetter(),
	                                               maxRecombination,
	                                               maxMutation,
	                                               IncrementalGeneticAlgorithm.class);
	    //System.out.println("breeding population");
	    Population solution = (Population) config.solve();
	    //System.out.println("found solution");
	    //System.out.println(solution);
	        
	    Gene.BitSet solutionBitSet = (Gene.BitSet) solution.get(0).get(0);

	    boolean[] ret = new boolean[currentItems.size()];
	    for(int i = 0; i != currentItems.size(); i++)
	    	ret[i] = solutionBitSet.get(i);
	    return ret;
	}

	@Override
	public Function getEvaluation() {
		return new KnapsackEvaluation(this);
	}

	@Override
	public Population getPopulation() {				
		Genome prototype = new Genome(new Gene.BitSet(currentItems.size()));	
		return Population.create(prototype, this.populationSize);
	}

	@Override
	public boolean isSolution(Population arg0) {
		double[] fitnesses = arg0.getFitnessArray();
		
		int positive = 0;
		for(int i = 0; i != fitnesses.length; i++)
			if(fitnesses[i] > 0) positive++;
		
		return positive * 100 / fitnesses.length > stopPercent;
	}

}

class KnapsackEvaluation implements Function {
	private GeneticKnapsackSolver mySolver;
	
	public KnapsackEvaluation(GeneticKnapsackSolver mySolver) {
		this.mySolver = mySolver;
	}

	@Override
	public Object apply(Object arg0) {
		Genome genome = (Genome) arg0;
			
        Gene.BitSet solution = (Gene.BitSet) genome.get(0);

        int weight = 0;
	    int value = 0;
	       
	    int i = 0;
	    for(IKnapsackItem item : mySolver.currentItems) {
	    	if(solution.get(i)) {
	    		weight += item.getWeight();
	    		value += item.getValue();
	    	}
	    	i++;
	    }
	    
	    if(weight > mySolver.currentCapacity)
	    	return - 1 * (weight - mySolver.currentCapacity);
	    else return value;
	}
	
}
package com.lazygalaxy.ci.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lazygalaxy.ci.ga.function.FitnessFunction;
import com.lazygalaxy.ci.ga.gene.FloatGene;
import com.lazygalaxy.ci.ga.gene.Gene;
import com.lazygalaxy.ci.ga.gene.IntegerGene;
import com.lazygalaxy.ci.ga.gene.LongGene;
import com.lazygalaxy.ci.parameter.FloatParameter;
import com.lazygalaxy.ci.parameter.IntegerParameter;
import com.lazygalaxy.ci.parameter.LongParameter;

public class Chromosome implements Comparable<Chromosome> {

	private final Random random;
	private final FitnessFunction fitnessFunction;
	private final List<Gene> genes = new ArrayList<Gene>();
	private Float fitness = null;

	public Chromosome(Random random, FitnessFunction fitnessFunction) {
		this.random = random;
		this.fitnessFunction = fitnessFunction;
	}

	public Random getRandom() {
		return random;
	}

	public void addGene(Gene gene) {
		genes.add(gene);
	}

	public IntegerGene addIntegerGene(String name, int minValue, int maxValue) {
		IntegerGene gene = new IntegerGene(this, new IntegerParameter(name, minValue, maxValue));
		genes.add(gene);
		return gene;
	}

	public LongGene addLongGene(String name, long minValue, long maxValue) {
		LongGene gene = new LongGene(this, new LongParameter(name, minValue, maxValue));
		genes.add(gene);
		return gene;
	}

	public FloatGene addFloatGene(String name, float minValue, float maxValue, int roundDecimalPoints) {
		FloatGene gene = new FloatGene(this, new FloatParameter(name, minValue, maxValue, roundDecimalPoints));
		genes.add(gene);
		return gene;
	}

	public Gene getGene(int index) {
		return genes.get(index);
	}

	public Integer getGeneAsInteger(int index) {
		return (Integer) genes.get(index).getParameter().getValue();
	}

	public Long getGeneAsLong(int index) {
		return (Long) genes.get(index).getParameter().getValue();
	}

	public Float getGeneAsFloat(int index) {
		return (Float) genes.get(index).getParameter().getValue();
	}

	public Chromosome crossOver(Chromosome parent) {
		Chromosome newChromosome = new Chromosome(random, fitnessFunction);
		for (int i = 0; i < genes.size(); i++) {
			newChromosome.addGene(random.nextBoolean() ? genes.get(i).getClone() : parent.getGene(i).getClone());
		}
		return newChromosome;
	}

	public void mutate(float mutationRate) {
		for (int i = 0; i < genes.size(); i++) {
			genes.get(i).mutate(mutationRate);
			fitness = null;
		}
	}

	public Float getFitness() {
		if (fitness == null && fitnessFunction != null) {
			fitness = fitnessFunction.apply(this);
		}
		return fitness;
	}

	@Override
	public int compareTo(Chromosome chromosome) {
		return this.getFitness().compareTo(chromosome.getFitness());
	}

	@Override
	public String toString() {
		String string = "fitness: " + getFitness() + " ";
		for (int i = 0; i < genes.size(); i++) {
			string += genes.get(i).toString() + ", ";
		}
		return string;
	}
}

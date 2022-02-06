package com.lazygalaxy.ci.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.lazygalaxy.ci.ga.function.EquationFitnessFunction;
import com.lazygalaxy.ci.ga.gene.FloatGene;
import com.lazygalaxy.ci.ga.gene.Gene;
import com.lazygalaxy.ci.ga.gene.IntegerGene;
import com.lazygalaxy.ci.ga.gene.LongGene;

public class Chromosome implements Comparable<Chromosome> {

	private final Random random;
	private final EquationFitnessFunction fitnessFunction;
	private final List<Gene> genes = new ArrayList<Gene>();
	private Float fitness = null;

	public Chromosome(Random random, EquationFitnessFunction fitnessFunction) {
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
		IntegerGene gene = new IntegerGene(this, name, minValue, maxValue);
		genes.add(gene);
		return gene;
	}

	public LongGene addLongGene(String name, long minValue, long maxValue) {
		LongGene gene = new LongGene(this, name, minValue, maxValue);
		genes.add(gene);
		return gene;
	}

	public FloatGene addFloatGene(String name, float minValue, float maxValue) {
		FloatGene gene = new FloatGene(this, name, minValue, maxValue);
		genes.add(gene);
		return gene;
	}

	public Gene getGene(int index) {
		return genes.get(index);
	}

	public Integer getGeneAsInteger(int index) {
		return (Integer) genes.get(index).getValue();
	}

	public Float getGeneAsFloat(int index) {
		return (Float) genes.get(index).getValue();
	}

	public Chromosome crossOver(Chromosome parent) {
		Chromosome newChromosome = new Chromosome(random, fitnessFunction);
		for (int i = 0; i < genes.size(); i++) {
			newChromosome.addGene(random.nextBoolean() ? genes.get(i) : parent.getGene(i));
		}
		return newChromosome;
	}

	public void mutate(float mutationRate) {
		for (int i = 0; i < genes.size(); i++) {
			if (random.nextFloat() <= mutationRate) {
				genes.get(i).randomize();
			}
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

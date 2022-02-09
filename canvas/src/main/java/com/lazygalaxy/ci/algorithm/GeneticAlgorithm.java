package com.lazygalaxy.ci.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.fitness.Fitness;
import com.lazygalaxy.ci.parameter.Parameter;

public class GeneticAlgorithm {
	private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithm.class);

	private final Random random;
	private final int geneartionSize;
	private final int epochs;
	private Fitness fitness;

	private List<Chromosome> generation = new ArrayList<Chromosome>();

	public GeneticAlgorithm(Random random, int geneartionSize, int epochs, Fitness fitness) {
		this.random = random;
		this.geneartionSize = geneartionSize;
		this.epochs = epochs;
		this.fitness = fitness;

		for (int c = 0; c < geneartionSize; c++) {
			Chromosome chromosome = new Chromosome(random, fitness);
			for (Parameter paramter : fitness.getParameters()) {
				chromosome.addGene(paramter.getGene(chromosome));
			}
			LOGGER.info("initialized: " + c);

			generation.add(chromosome);
		}
		Collections.sort(generation);
		LOGGER.info(generation.get(0));

	}

	public void run() {
		for (int e = 1; e <= epochs; e++) {
			LOGGER.info("epoch: " + e);
			float mutationRate = 1.0f - (e / (epochs * 1.0f));

			List<Chromosome> newGeneration = new ArrayList<Chromosome>();
			int bestSize = geneartionSize / 2;
			for (int c = 0; c < bestSize; c++) {
				newGeneration.add(generation.get(c));
			}
			for (int c = bestSize; c < geneartionSize; c++) {
				Chromosome parent1 = newGeneration.get(random.nextInt(bestSize));
				Chromosome parent2 = newGeneration.get(random.nextInt(bestSize));
				Chromosome child = parent1.crossOver(parent2);
				child.mutate(mutationRate);
				newGeneration.add(child);
			}
			Collections.sort(newGeneration);
			LOGGER.info(newGeneration.get(0) + " mutationRate: " + mutationRate);

			generation = newGeneration;
		}
	}

}

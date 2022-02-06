package com.lazygalaxy.ci.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.function.EquationFitnessFunction;

public class GeneticAlgorithmRun {
	private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithmRun.class);
	public static final Random RANDOM = new Random(23);

	public static void main(String[] args) {

		List<Chromosome> generation = new ArrayList<Chromosome>();
		for (int c = 0; c < 100; c++) {
			Chromosome chromosome = new Chromosome(RANDOM, new EquationFitnessFunction());
			chromosome.addFloatGene("x", 0.0f, 4.0f);
			chromosome.addFloatGene("y", 0.0f, 2.0f);
			generation.add(chromosome);
		}
		Collections.sort(generation);
		LOGGER.info(generation.get(0));

		int generations = 500;

		for (int e = 0; e < generations; e++) {
			float mutationRate = 1.0f - (e / (generations * 1.0f));

			List<Chromosome> newGeneration = new ArrayList<Chromosome>();
			for (int c = 0; c < 20; c++) {
				newGeneration.add(generation.get(c));
			}
			for (int c = 0; c < 80; c++) {
				Chromosome parent1 = newGeneration.get(RANDOM.nextInt(20));
				Chromosome parent2 = newGeneration.get(RANDOM.nextInt(20));
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

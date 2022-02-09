package com.lazygalaxy.ci.main;

import java.util.Random;

import com.lazygalaxy.ci.algorithm.GeneticAlgorithm;
import com.lazygalaxy.ci.ga.fitness.EquationFitness;

public class GeneticAlgorithmEquationRun {
	public static final Random RANDOM = new Random(23);

	public static void main(String[] args) {

		GeneticAlgorithm algorithm = new GeneticAlgorithm(RANDOM, 20, 50, new EquationFitness());
		algorithm.run();

	}
}

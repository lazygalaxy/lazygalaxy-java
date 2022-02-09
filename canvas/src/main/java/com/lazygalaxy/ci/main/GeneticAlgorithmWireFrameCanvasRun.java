package com.lazygalaxy.ci.main;

import java.util.Random;

import com.lazygalaxy.ci.algorithm.GeneticAlgorithm;
import com.lazygalaxy.ci.ga.fitness.WireFrameCanvasFitness;

public class GeneticAlgorithmWireFrameCanvasRun {
	public static final Random RANDOM = new Random(23);

	public static void main(String[] args) {

		GeneticAlgorithm algorithm = new GeneticAlgorithm(RANDOM, 20, 50,
				new WireFrameCanvasFitness("/Users/vangos/Development/digiart/lost_love.jpg", 2048));
		algorithm.run();

	}
}

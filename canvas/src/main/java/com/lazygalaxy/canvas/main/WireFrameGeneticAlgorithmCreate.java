package com.lazygalaxy.canvas.main;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.common.Chromosome;
import com.lazygalaxy.canvas.common.FloatGene;
import com.lazygalaxy.canvas.common.IntegerGene;
import com.lazygalaxy.canvas.layer.WireFrameLayer;

public class WireFrameGeneticAlgorithmCreate {

	private static final Logger LOGGER = LogManager.getLogger(WireFrameGeneticAlgorithmCreate.class);
	public static final Random RANDOM = new Random(4);

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

		String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
		int size = 2048;

		Canvas inputCanvas = new BufferedImageCanvas(pathname);
		Canvas outputCanvas = new BufferedImageCanvas(size, size);

		try {
			IntegerGene removeThreshold = new IntegerGene("removeThreshold", 1, 150);
			IntegerGene randomSample = new IntegerGene("randomSample", 500, 7500);
			IntegerGene lineJoinDistanceThreshold = new IntegerGene("lineJoinDistanceThreshold", 10, 200);
			FloatGene lineJoinThickness = new FloatGene("lineJoinThickness", 0.1f, 5f);

			Chromosome chromosome = new Chromosome(removeThreshold, randomSample, lineJoinDistanceThreshold,
					lineJoinThickness);
			LOGGER.info(chromosome);

			new WireFrameLayer(inputCanvas, removeThreshold.getValue(), randomSample.getValue(),
					lineJoinDistanceThreshold.getValue(), lineJoinThickness.getValue()).apply(outputCanvas);

			outputCanvas.saveAsPng("/Users/vangos/Development/digiart/process/lost_love");
			LOGGER.info("writing artwork");

		} finally {
			inputCanvas.close();
			outputCanvas.close();
		}

		long endTIme = System.currentTimeMillis();
		LOGGER.info("total time: " + ((endTIme - startTime) / 1000.0) + " secs");
	}

}

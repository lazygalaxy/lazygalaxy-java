package com.lazygalaxy.canvas.main;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.WireFrameLayer;
import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.FloatGene;
import com.lazygalaxy.ci.ga.gene.IntegerGene;
import com.lazygalaxy.ci.ga.gene.LongGene;

public class WireFrameRandomCreate {

	private static final Logger LOGGER = LogManager.getLogger(WireFrameRandomCreate.class);

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

		String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
		int size = 2048;

		Canvas inputCanvas = new BufferedImageCanvas(pathname);
		Canvas outputCanvas = new BufferedImageCanvas(size, size);

		try {
			Chromosome chromosome = new Chromosome(new Random(), null);

			IntegerGene removeThreshold = chromosome.addIntegerGene("removeThreshold", 1, 150);
			LongGene randomSeed = chromosome.addLongGene("randomSeed", 0, Long.MAX_VALUE);
			IntegerGene randomSample = chromosome.addIntegerGene("randomSample", 500, 7500);
			IntegerGene lineJoinDistanceThreshold = chromosome.addIntegerGene("lineJoinDistanceThreshold", 10, 200);
			FloatGene lineJoinThickness = chromosome.addFloatGene("lineJoinThickness", 0.1f, 5f, 1);

			LOGGER.info(chromosome);

			new WireFrameLayer(inputCanvas, removeThreshold.getParameter().getValue(),
					randomSeed.getParameter().getValue(), randomSample.getParameter().getValue(),
					lineJoinDistanceThreshold.getParameter().getValue(), lineJoinThickness.getParameter().getValue())
							.apply(outputCanvas);

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

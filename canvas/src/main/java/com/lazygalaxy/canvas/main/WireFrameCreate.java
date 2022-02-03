package com.lazygalaxy.canvas.main;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.PixelJoinerCanvasLayer;
import com.lazygalaxy.canvas.layer.RectangleCanvasLayer;
import com.lazygalaxy.canvas.points.RandomCanvasPoints;
import com.lazygalaxy.canvas.points.RemoveCanvasPoints;
import com.lazygalaxy.canvas.points.TransformCanvasPoints;

public class WireFrameCreate {

	private static final Logger LOGGER = LogManager.getLogger(WireFrameCreate.class);

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

		String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
		int size = 2048;

		Canvas inputCanvas = new BufferedImageCanvas(pathname);
		Canvas outputCanvas = new BufferedImageCanvas(size, size);

		try {
			// Chromosome chromosome = new Chromosome(new IntegerGene(1, 100));
			// LOGGER.info(chromosome);
			// Object[] sequence = chromosome.getSequence();

			new RectangleCanvasLayer(size, size, Color.BLACK).apply(outputCanvas);
			Canvas removeColorCanvasPoints = new RemoveCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(), 5,
					Color.BLACK).apply(inputCanvas);
			Canvas randomCanvasPoints = new RandomCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(), 4000)
					.apply(removeColorCanvasPoints);
			Canvas transformCanvasPoints = new TransformCanvasPoints(size, size).apply(randomCanvasPoints);
			LOGGER.info("total points: " + transformCanvasPoints.getSize());
			new PixelJoinerCanvasLayer(80, transformCanvasPoints).apply(outputCanvas);

			// Save as PNG
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

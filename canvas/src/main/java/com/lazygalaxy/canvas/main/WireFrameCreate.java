package com.lazygalaxy.canvas.main;

import java.awt.Color;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.common.Chromosome;
import com.lazygalaxy.canvas.common.FloatGene;
import com.lazygalaxy.canvas.common.IntegerGene;
import com.lazygalaxy.canvas.layer.LineJoinerCanvasLayer;
import com.lazygalaxy.canvas.layer.RectangleCanvasLayer;
import com.lazygalaxy.canvas.points.RandomCanvasPoints;
import com.lazygalaxy.canvas.points.RemoveCanvasPoints;
import com.lazygalaxy.canvas.points.TransformCanvasPoints;

public class WireFrameCreate {

	private static final Logger LOGGER = LogManager.getLogger(WireFrameCreate.class);
	public static final Random RANDOM = new Random(23);

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

		String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
		int size = 2048;

		Canvas inputCanvas = new BufferedImageCanvas(pathname);
		Canvas outputCanvas = new BufferedImageCanvas(size, size);

		try {
			IntegerGene removeThreshold = new IntegerGene("removeThreshold", 1, 255);
			IntegerGene randomSample = new IntegerGene("randomSample", 500, 5000);
			IntegerGene lineJoinDistanceThreshold = new IntegerGene("lineJoinDistanceThreshold", 10, 200);
			FloatGene lineJoinThickness = new FloatGene("lineJoinThickness", 0.1f, 5f);

			Chromosome chromosome = new Chromosome(removeThreshold, randomSample, lineJoinDistanceThreshold,
					lineJoinThickness);
			LOGGER.info(chromosome);

			new RectangleCanvasLayer(size, size, Color.BLACK).apply(outputCanvas);
			Canvas removeColorCanvasPoints = new RemoveCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(),
					removeThreshold.getValue(), Color.BLACK).apply(inputCanvas);
			LOGGER.info("total points: " + removeColorCanvasPoints.getSize());
			Canvas randomCanvasPoints = new RandomCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(),
					randomSample.getValue()).apply(removeColorCanvasPoints);
			LOGGER.info("total random points: " + randomCanvasPoints.getSize());
			Canvas transformCanvasPoints = new TransformCanvasPoints(size, size).apply(randomCanvasPoints);

			new LineJoinerCanvasLayer(lineJoinDistanceThreshold.getValue(), lineJoinThickness.getValue(),
					transformCanvasPoints).apply(outputCanvas);

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

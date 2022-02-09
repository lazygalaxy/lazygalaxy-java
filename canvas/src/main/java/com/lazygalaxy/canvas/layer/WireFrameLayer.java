package com.lazygalaxy.canvas.layer;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.main.WireFrameCreate;
import com.lazygalaxy.canvas.point.RandomCanvasPoints;
import com.lazygalaxy.canvas.point.RemoveCanvasPoints;
import com.lazygalaxy.canvas.point.TransformCanvasPoints;

public class WireFrameLayer implements CanvasLayer {
	private static final Logger LOGGER = LogManager.getLogger(WireFrameCreate.class);

	private final Canvas inputCanvas;
	private final int removeThreshold;
	private final long randomSeed;
	private final int randomSampleSize;
	private final int lineJoinDistanceThreshold;
	private final float lineJoinThickness;

	public WireFrameLayer(Canvas inputCanvas, int removeThreshold, long randomSeed, int randomSampleSize,
			int lineJoinDistanceThreshold, float lineJoinThickness) {
		this.inputCanvas = inputCanvas;
		this.randomSeed = randomSeed;
		this.removeThreshold = removeThreshold;
		this.randomSampleSize = randomSampleSize;
		this.lineJoinDistanceThreshold = lineJoinDistanceThreshold;
		this.lineJoinThickness = lineJoinThickness;
	}

	@Override
	public Float apply(Canvas canvas) throws Exception {
		Canvas removeColorCanvasPoints = null;
		Canvas randomCanvasPoints = null;
		Canvas transformCanvasPoints = null;

		try {
			new RectangleCanvasLayer(canvas.getWidth(), canvas.getWidth(), Color.BLACK).apply(canvas);

			removeColorCanvasPoints = new RemoveCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(),
					removeThreshold, Color.BLACK).apply(inputCanvas);

			randomCanvasPoints = new RandomCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(), randomSeed,
					randomSampleSize).apply(removeColorCanvasPoints);
			int canvasPointSize = randomCanvasPoints.getSize();

			transformCanvasPoints = new TransformCanvasPoints(canvas.getWidth(), canvas.getWidth())
					.apply(randomCanvasPoints);

			float averageConnections = new LineJoinerCanvasLayer(lineJoinDistanceThreshold, lineJoinThickness,
					transformCanvasPoints).apply(canvas);

			float fitness = Math.abs(averageConnections - 50.0f);
			LOGGER.debug("fitness: " + fitness + ", canvasPointSize: " + canvasPointSize + ", averageConnections: "
					+ averageConnections);
			return fitness;

		} finally {
			removeColorCanvasPoints.close();
			randomCanvasPoints.close();
			transformCanvasPoints.close();
		}
	}
}

package com.lazygalaxy.canvas.layer;

import java.awt.Color;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.point.RandomCanvasPoints;
import com.lazygalaxy.canvas.point.RemoveCanvasPoints;
import com.lazygalaxy.canvas.point.TransformCanvasPoints;

public class WireFrameLayer implements CanvasLayer {

	private Canvas inputCanvas;
	private int removeThreshold;
	private int randomSample;
	private int lineJoinDistanceThreshold;
	private float lineJoinThickness;

	public WireFrameLayer(Canvas inputCanvas, int removeThreshold, int randomSample, int lineJoinDistanceThreshold,
			float lineJoinThickness) {
		this.inputCanvas = inputCanvas;
		this.removeThreshold = removeThreshold;
		this.randomSample = randomSample;
		this.lineJoinDistanceThreshold = lineJoinDistanceThreshold;
		this.lineJoinThickness = lineJoinThickness;
	}

	@Override
	public void apply(Canvas canvas) throws Exception {
		Canvas removeColorCanvasPoints = null;
		Canvas randomCanvasPoints = null;
		Canvas transformCanvasPoints = null;

		try {
			new RectangleCanvasLayer(canvas.getWidth(), canvas.getWidth(), Color.BLACK).apply(canvas);

			removeColorCanvasPoints = new RemoveCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(),
					removeThreshold, Color.BLACK).apply(inputCanvas);

			randomCanvasPoints = new RandomCanvasPoints(inputCanvas.getWidth(), inputCanvas.getHeight(), randomSample)
					.apply(removeColorCanvasPoints);

			transformCanvasPoints = new TransformCanvasPoints(canvas.getWidth(), canvas.getWidth())
					.apply(randomCanvasPoints);

			new LineJoinerCanvasLayer(lineJoinDistanceThreshold, lineJoinThickness, transformCanvasPoints)
					.apply(canvas);
		} finally {
			removeColorCanvasPoints.close();
			randomCanvasPoints.close();
			transformCanvasPoints.close();
		}
	}
}

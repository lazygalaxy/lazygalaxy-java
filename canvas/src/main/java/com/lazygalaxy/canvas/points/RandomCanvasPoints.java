package com.lazygalaxy.canvas.points;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;

public class RandomCanvasPoints extends PointCanvas {

	private int randomSize;

	public RandomCanvasPoints(int width, int height, int randomSize) {
		super(width, height);
		this.randomSize = randomSize;
	}

	@Override
	public PointCanvas apply(Canvas canvas) throws Exception {
		while (points.size() < randomSize) {
			points.add(canvas.getPoint((int) (Math.random() * canvas.getSize())));
		}
		return this;
	}
}

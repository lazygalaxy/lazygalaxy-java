package com.lazygalaxy.canvas.points;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;
import com.lazygalaxy.canvas.main.WireFrameCreate;

public class RandomCanvasPoints extends PointCanvas {

	private int sample;

	public RandomCanvasPoints(int width, int height, int sample) {
		super(width, height);
		this.sample = sample;
	}

	@Override
	public PointCanvas apply(Canvas canvas) throws Exception {
		while (points.size() < sample) {
			points.add(canvas.getPoint((int) (WireFrameCreate.RANDOM.nextInt(canvas.getSize()))));
		}
		return this;
	}
}

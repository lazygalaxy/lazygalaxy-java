package com.lazygalaxy.canvas.point;

import java.util.Random;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;

public class RandomCanvasPoints extends PointCanvas {

	private final long randomSeed;
	private final int sampleSize;

	public RandomCanvasPoints(int width, int height, long randomSeed, int sampleSize) {
		super(width, height);
		this.randomSeed = randomSeed;
		this.sampleSize = sampleSize;
	}

	@Override
	public Canvas apply(Canvas canvas) throws Exception {
		Random random = new Random(randomSeed);

		while (canvas.getSize() > 0 && points.size() < sampleSize) {
			points.add(canvas.removePoint((int) (random.nextInt(canvas.getSize()))));
		}
		return this;
	}
}

package com.lazygalaxy.canvas.point;

import java.util.Random;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;

public class RandomCanvasPoints extends PointCanvas {

	private long randomSeed;
	private int sample;

	public RandomCanvasPoints(int width, int height, long randomSeed, int sample) {
		super(width, height);
		this.randomSeed = randomSeed;
		this.sample = sample;
	}

	@Override
	public PointCanvas apply(Canvas canvas) throws Exception {
		Random random = new Random(randomSeed);
		while (points.size() < sample) {
			points.add(canvas.getPoint((int) (random.nextInt(canvas.getSize()))));
		}
		return this;
	}
}

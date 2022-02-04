package com.lazygalaxy.canvas.point;

import java.awt.Color;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;
import com.lazygalaxy.canvas.common.Point;

public class RemoveCanvasPoints extends PointCanvas {

	private int colorThreshold;
	private Color color;

	public RemoveCanvasPoints(int width, int height, int colorThreshold, Color color) {
		super(width, height);
		this.colorThreshold = colorThreshold;
		this.color = color;
	}

	@Override
	public PointCanvas apply(Canvas canvas) throws Exception {
		for (int x = 0; x < canvas.getWidth(); x++) {
			for (int y = 0; y < canvas.getHeight(); y++) {
				Color readColor = canvas.getColor(x, y);
				if (!mustAdd(readColor)) {
					points.add(new Point<Color>(x, y, readColor));
				}
			}
		}
		return this;
	}

	private boolean mustAdd(Color color) {
		return withinThreshold(color.getRed(), this.color.getRed())
				&& withinThreshold(color.getGreen(), this.color.getGreen())
				&& withinThreshold(color.getBlue(), this.color.getBlue());
	}

	private boolean withinThreshold(int value1, int value2) {
		return Math.abs(value1 - value2) <= colorThreshold;
	}

}

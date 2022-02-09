package com.lazygalaxy.canvas.layer;

import java.awt.Color;

import com.lazygalaxy.canvas.Canvas;

public class RectangleCanvasLayer implements CanvasLayer {

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final Color color;

	public RectangleCanvasLayer(int width, int height, Color color) {
		this(0, 0, width, height, color);
	}

	public RectangleCanvasLayer(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public Float apply(Canvas canvas) throws Exception {
		canvas.drawRectangle(x, y, width, height, color);
		return null;
	}
}

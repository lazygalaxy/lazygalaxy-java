package com.lazygalaxy.canvas.layer;

import java.awt.Color;

import com.lazygalaxy.canvas.Canvas;

public class RectangleCanvasLayer implements CanvasLayer {

	private int x;
	private int y;
	private int width;
	private int height;
	private Color color;

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

	public void apply(Canvas canvas) throws Exception {
		canvas.drawRectangle(x, y, width, height, color);
	}
}

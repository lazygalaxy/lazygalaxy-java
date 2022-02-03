package com.lazygalaxy.canvas;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.lazygalaxy.canvas.common.Point;

public abstract class PointCanvas extends Canvas {
	final protected List<Point<Color>> points = new ArrayList<Point<Color>>();
	final protected int width;
	final protected int height;

	public PointCanvas(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getSize() {
		return points.size();
	}

	@Override
	public Point<Color> getPoint(int index) {
		return points.get(index);
	}

	public abstract PointCanvas apply(Canvas canvas) throws Exception;
}

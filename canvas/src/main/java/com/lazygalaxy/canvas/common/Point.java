package com.lazygalaxy.canvas.common;

public class Point<T> {
	final private int x;
	final private int y;
	final private T data;

	public Point(int x, int y, T data) {
		this.x = x;
		this.y = y;
		this.data = data;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public T getData() {
		return data;
	}

	public double getDistance(Point<T> otherPoint) {
		int xDist = x - otherPoint.getX();
		int yDist = y - otherPoint.getY();
		return Math.sqrt((xDist * xDist) + (yDist * yDist));
	}
}

package com.lazygalaxy.canvas;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Image;

import com.lazygalaxy.canvas.common.Point;

public abstract class Canvas {

	public abstract int getHeight();

	public abstract int getWidth();

	public abstract int getSize();

	public Image getImage() throws Exception {
		throw new Exception("method not supported");
	}

	public Color getColor(int x, int y) throws Exception {
		throw new Exception("method not supported");
	}

	public Point<Color> getPoint(int index) throws Exception {
		throw new Exception("method not supported");
	}

	public void drawLine(int x1, int y1, int x2, int y2, GradientPaint gradient, float width) throws Exception {
		throw new Exception("method not supported");
	}

	public void drawRectangle(int x, int y, int width, int height, Color color) throws Exception {
		throw new Exception("method not supported");
	}

	public void saveAsPng(String filename) throws Exception {
		throw new Exception("method not supported");
	}

	public void saveAsJpg(String filename) throws Exception {
		throw new Exception("method not supported");
	}

	public void close() throws Exception {
	}
}
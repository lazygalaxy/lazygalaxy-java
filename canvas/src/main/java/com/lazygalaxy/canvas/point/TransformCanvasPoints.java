package com.lazygalaxy.canvas.point;

import java.awt.Color;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.PointCanvas;
import com.lazygalaxy.canvas.common.Point;

public class TransformCanvasPoints extends PointCanvas {

	public TransformCanvasPoints(int width, int height) {
		super(width, height);
	}

	@Override
	public Canvas apply(Canvas inputCanvas) throws Exception {
		for (int i = 0; i < inputCanvas.getSize(); i++) {
			Point<Color> colorPoint = inputCanvas.getPoint(i);
			points.add(new Point<Color>(((colorPoint.getX() * width) / inputCanvas.getWidth()),
					((colorPoint.getY() * height) / inputCanvas.getHeight()), colorPoint.getData()));
		}
		return this;
	}
}

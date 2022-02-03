package com.lazygalaxy.canvas.layer;

import java.awt.Color;
import java.awt.GradientPaint;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.common.Point;

public class PixelJoinerCanvasLayer implements CanvasLayer {

	private Canvas inputCanvas;
	private int distanceThreshold;

	public PixelJoinerCanvasLayer(int distanceThreshold, Canvas inputCanvas) {
		this.distanceThreshold = distanceThreshold;
		this.inputCanvas = inputCanvas;
	}

	public void apply(Canvas canvas) throws Exception {
		for (int i = 0; i < inputCanvas.getSize(); i++) {
			for (int j = i + 1; j < inputCanvas.getSize(); j++) {
				Point<Color> point1 = inputCanvas.getPoint(i);
				Point<Color> point2 = inputCanvas.getPoint(j);

				if (point1.getDistance(point2) < distanceThreshold) {
					GradientPaint gradient = new GradientPaint(point1.getX(), point1.getY(), point1.getData(),
							point2.getX(), point2.getY(), point2.getData());

					canvas.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY(), gradient);
				}
			}
		}
	}
}

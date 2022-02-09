package com.lazygalaxy.canvas.layer;

import java.awt.Color;
import java.awt.GradientPaint;

import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.common.Point;

public class LineJoinerCanvasLayer implements CanvasLayer {

	private final Canvas inputCanvas;
	private final int distanceThreshold;
	private final float thickness;

	public LineJoinerCanvasLayer(int distanceThreshold, float thickness, Canvas inputCanvas) {
		this.distanceThreshold = distanceThreshold;
		this.thickness = thickness;
		this.inputCanvas = inputCanvas;
	}

	public Float apply(Canvas canvas) throws Exception {
		int[] connectionSize = new int[inputCanvas.getSize()];
		for (int i = 0; i < inputCanvas.getSize(); i++) {
			for (int j = i + 1; j < inputCanvas.getSize(); j++) {
				Point<Color> point1 = inputCanvas.getPoint(i);
				Point<Color> point2 = inputCanvas.getPoint(j);

				if (point1.getDistance(point2) < distanceThreshold) {
					connectionSize[i]++;
					connectionSize[j]++;
					GradientPaint gradient = new GradientPaint(point1.getX(), point1.getY(), point1.getData(),
							point2.getX(), point2.getY(), point2.getData());

					canvas.drawLine(point1.getX(), point1.getY(), point2.getX(), point2.getY(), gradient, thickness);
				}
			}
		}
		float total = 0;
		for (int i = 0; i < connectionSize.length; i++) {
			total += connectionSize[i];
		}
		return total / connectionSize.length;
	}
}

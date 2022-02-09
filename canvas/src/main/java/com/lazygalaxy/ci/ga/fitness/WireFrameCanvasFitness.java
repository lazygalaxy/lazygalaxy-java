package com.lazygalaxy.ci.ga.fitness;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.WireFrameLayer;
import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.FloatParameter;
import com.lazygalaxy.ci.parameter.IntegerParameter;
import com.lazygalaxy.ci.parameter.LongParameter;
import com.lazygalaxy.ci.parameter.Parameter;

public class WireFrameCanvasFitness implements Fitness {

	private final String pathname;
	private final int canvasSize;

	public WireFrameCanvasFitness(String pathname, int canvasSize) {
		this.pathname = pathname;
		this.canvasSize = canvasSize;
	}

	@Override
	public Float apply(Chromosome chromosome) throws Exception {
		Canvas inputCanvas = new BufferedImageCanvas(pathname);
		Canvas outputCanvas = new BufferedImageCanvas(canvasSize, canvasSize);

		return new WireFrameLayer(inputCanvas, chromosome.getGeneAsInteger(0), chromosome.getGeneAsLong(1),
				chromosome.getGeneAsInteger(2), chromosome.getGeneAsInteger(3), chromosome.getGeneAsFloat(4))
						.apply(outputCanvas);
	}

	@Override
	public Parameter[] getParameters() {
		return new Parameter[] { new IntegerParameter("removeThreshold", 1, 150), new LongParameter("randomSeed"),
				new IntegerParameter("randomSample", 500, 7500),
				new IntegerParameter("lineJoinDistanceThreshold", 10, 200),
				new FloatParameter("lineJoinThickness", 0.1f, 1.0f, 1) };
	}
}
package com.lazygalaxy.ci.parameter;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.FloatGene;
import com.lazygalaxy.ci.ga.gene.Gene;

public class FloatParameter extends Parameter<Float> {

	private final float minValue;
	private final float maxValue;
	private final int roundDecimalPoints;

	public FloatParameter(String name, float minValue, float maxValue, int roundDecimalPoints) {
		this(name, null, minValue, maxValue, roundDecimalPoints);
	}

	public FloatParameter(String name, Float value, float minValue, float maxValue, int roundDecimalPoints) {
		super(name, value);

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.roundDecimalPoints = roundDecimalPoints;
	}

	public float getMinValue() {
		return minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public int getRoundDecimalPoints() {
		return roundDecimalPoints;
	}

	@Override
	public Gene getGene(Chromosome chromosome) {
		return new FloatGene(chromosome, this);
	}

	@Override
	public FloatParameter getClone() {
		return new FloatParameter(name, minValue, maxValue, roundDecimalPoints);
	}
}

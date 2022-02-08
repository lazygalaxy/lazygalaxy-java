package com.lazygalaxy.ci.parameter;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.Gene;
import com.lazygalaxy.ci.ga.gene.IntegerGene;

public class IntegerParameter extends Parameter<Integer> {

	private final int minValue;
	private final int maxValue;

	public IntegerParameter(String name, int minValue, int maxValue) {
		this(name, null, minValue, maxValue);
	}

	public IntegerParameter(String name, Integer value, int minValue, int maxValue) {
		super(name, value);

		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	@Override
	public Gene getGene(Chromosome chromosome) {
		return new IntegerGene(chromosome, this);
	}

	@Override
	public IntegerParameter getClone() {
		return new IntegerParameter(name, minValue, maxValue);
	}
}

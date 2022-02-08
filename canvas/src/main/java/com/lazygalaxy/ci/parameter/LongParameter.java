package com.lazygalaxy.ci.parameter;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.Gene;
import com.lazygalaxy.ci.ga.gene.LongGene;

public class LongParameter extends Parameter<Long> {

	private final long minValue;
	private final long maxValue;

	public LongParameter(String name, long minValue, long maxValue) {
		this(name, null, minValue, maxValue);
	}

	public LongParameter(String name, Long value, long minValue, long maxValue) {
		super(name, value);

		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public long getMinValue() {
		return minValue;
	}

	public long getMaxValue() {
		return maxValue;
	}

	@Override
	public Gene getGene(Chromosome chromosome) {
		return new LongGene(chromosome, this);
	}

	@Override
	public LongParameter getClone() {
		return new LongParameter(name, minValue, maxValue);
	}
}

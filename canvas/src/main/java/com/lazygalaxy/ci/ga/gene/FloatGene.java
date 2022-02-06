package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;

public class FloatGene extends Gene<Float> {
	final private float minValue;
	final private float maxValue;

	public FloatGene(Chromosome chromosome, String name, float minValue, float maxValue) {
		super(chromosome, name);

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.value = randomize();
	}

	public Float randomize() {
		return (chromosome.getRandom().nextFloat() * (maxValue - minValue)) + minValue;
	}

	public Float getValue() {
		return value;
	}
}

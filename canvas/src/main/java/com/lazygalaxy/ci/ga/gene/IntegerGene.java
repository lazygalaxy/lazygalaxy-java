package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;

public class IntegerGene extends Gene<Integer> {
	final private int minValue;
	final private int maxValue;

	public IntegerGene(Chromosome chromosome, String name, int minValue, int maxValue) {
		super(chromosome, name);

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.value = randomize();
	}

	public Integer randomize() {
		return chromosome.getRandom().nextInt(maxValue - minValue + 1) + minValue;
	}

	public Integer getValue() {
		return value;
	}
}

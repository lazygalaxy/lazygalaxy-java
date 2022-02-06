package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;

public class LongGene extends Gene<Long> {
	final private long minValue;
	final private long maxValue;

	public LongGene(Chromosome chromosome, String name, long minValue, long maxValue) {
		super(chromosome, name);

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.value = randomize();
	}

	public Long randomize() {
		// TODO: implement the min/max crrectly here
		return chromosome.getRandom().nextLong();
	}

	public Long getValue() {
		return value;
	}
}

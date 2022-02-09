package com.lazygalaxy.ci.parameter;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.Gene;
import com.lazygalaxy.ci.ga.gene.LongGene;

public class LongParameter extends Parameter<Long> {

	public LongParameter(String name) {
		this(name, null);
	}

	public LongParameter(String name, Long value) {
		super(name, value);
	}

	@Override
	public Gene getGene(Chromosome chromosome) {
		return new LongGene(chromosome, this);
	}

	@Override
	public LongParameter getClone() {
		return new LongParameter(name);
	}
}

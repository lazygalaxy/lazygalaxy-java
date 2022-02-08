package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.LongParameter;

public class LongGene extends Gene<Long, LongParameter> {

	public LongGene(Chromosome chromosome, LongParameter parameter) {
		super(chromosome, parameter);
		this.parameter.setValue(getRandomValue());
	}

	@Override
	protected Long getRandomValue() {
		return chromosome.getRandom().nextLong();
	}

	@Override
	public void mutate(float mutationRate) {
		Long diffValue = getRandomValue() - parameter.getValue();
		parameter.setValue(parameter.getValue() + Math.round(diffValue * Double.valueOf(mutationRate)));
	}

	@Override
	public Gene<Long, LongParameter> getClone() {
		return new LongGene(chromosome, parameter.getClone());
	}
}

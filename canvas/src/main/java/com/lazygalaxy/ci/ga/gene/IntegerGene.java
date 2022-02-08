package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.IntegerParameter;

public class IntegerGene extends Gene<Integer, IntegerParameter> {

	public IntegerGene(Chromosome chromosome, IntegerParameter parameter) {
		super(chromosome, parameter);
		this.parameter.setValue(getRandomValue());
	}

	@Override
	protected Integer getRandomValue() {
		return chromosome.getRandom().nextInt(parameter.getMaxValue() - parameter.getMinValue() + 1)
				+ parameter.getMinValue();
	}

	@Override
	public void mutate(float mutationRate) {
		Integer diffValue = getRandomValue() - parameter.getValue();
		parameter.setValue(parameter.getValue() + Math.round(diffValue * mutationRate));
	}

	@Override
	public Gene<Integer, IntegerParameter> getClone() {
		return new IntegerGene(chromosome, parameter.getClone());
	}
}

package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.FloatParameter;

public class FloatGene extends Gene<Float, FloatParameter> {
	final private float decimalPointFactor;

	public FloatGene(Chromosome chromosome, FloatParameter parameter) {
		super(chromosome, parameter);

		this.decimalPointFactor = getDecimalPointFactor(parameter.getRoundDecimalPoints());
		this.parameter.setValue(roundDecimalPoints(getRandomValue()));
	}

	private float getDecimalPointFactor(int roundDecimalPoints) {
		float decimalPointFactor = 1.0f;
		for (int i = 0; i < roundDecimalPoints; i++) {
			decimalPointFactor *= 10.0f;
		}
		return decimalPointFactor;
	}

	private float roundDecimalPoints(float value) {
		return Math.round(value * decimalPointFactor) / decimalPointFactor;
	}

	@Override
	protected Float getRandomValue() {
		return (chromosome.getRandom().nextFloat() * (parameter.getMaxValue() - parameter.getMinValue()))
				+ parameter.getMinValue();
	}

	@Override
	public void mutate(float mutationRate) {
		Float diffValue = getRandomValue() - parameter.getValue();
		parameter.setValue(parameter.getValue() + roundDecimalPoints(diffValue * mutationRate));
	}

	@Override
	public Gene<Float, FloatParameter> getClone() {
		return new FloatGene(chromosome, parameter.getClone());
	}

}

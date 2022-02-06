package com.lazygalaxy.ci.ga.function;

import com.lazygalaxy.ci.ga.Chromosome;

public class EquationFitnessFunction implements FitnessFunction {

	public EquationFitnessFunction() {

	}

	@Override
	public Float apply(Chromosome chromosome) {

		return Math.abs(((chromosome.getGeneAsFloat(0) * 2) + (chromosome.getGeneAsFloat(1) * 4)) - 8);
	}
}

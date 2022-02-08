package com.lazygalaxy.ci.ga.function;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.FloatParameter;
import com.lazygalaxy.ci.parameter.Parameter;

public class EquationFitnessFunction implements FitnessFunction {

	public EquationFitnessFunction() {

	}

	@Override
	public Float apply(Chromosome chromosome) {
		return Math.abs((chromosome.getGeneAsFloat(0) * 2) + (chromosome.getGeneAsFloat(1) * 4) - 6);
	}

	@Override
	public Parameter[] getParameters() {
		return new Parameter[] { new FloatParameter("x", 0.0f, 4.0f, 2), new FloatParameter("y", 0.0f, 2.0f, 2) };
	}
}

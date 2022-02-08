package com.lazygalaxy.ci.ga.function;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.Parameter;

public interface FitnessFunction {
	public Float apply(Chromosome chromosome);

	public Parameter[] getParameters();
}

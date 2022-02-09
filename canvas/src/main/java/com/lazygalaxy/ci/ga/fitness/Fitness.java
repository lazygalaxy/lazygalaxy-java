package com.lazygalaxy.ci.ga.fitness;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.Parameter;

public interface Fitness {
	public Float apply(Chromosome chromosome) throws Exception;

	public Parameter[] getParameters();
}

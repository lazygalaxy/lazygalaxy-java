package com.lazygalaxy.ci.ga.function;

import com.lazygalaxy.ci.ga.Chromosome;

public interface FitnessFunction {
	public Float apply(Chromosome chromosome);
}

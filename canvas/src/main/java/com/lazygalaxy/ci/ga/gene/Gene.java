package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.parameter.Parameter;

public abstract class Gene<T, S extends Parameter<T>> {
	protected final Chromosome chromosome;
	protected final S parameter;

	public Gene(Chromosome chromosome, S parameter) {
		this.chromosome = chromosome;
		this.parameter = parameter;
	}

	public S getParameter() {
		return parameter;
	}

	protected abstract T getRandomValue();

	public abstract void mutate(float mutationRate);

	public abstract Gene<T, S> getClone();

	@Override
	public String toString() {
		return parameter.getName() + ": " + parameter.getValue();
	}
}

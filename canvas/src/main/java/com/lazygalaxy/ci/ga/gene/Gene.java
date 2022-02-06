package com.lazygalaxy.ci.ga.gene;

import com.lazygalaxy.ci.ga.Chromosome;

public abstract class Gene<T> {
	final protected Chromosome chromosome;
	final protected String name;
	protected T value;

	public Gene(Chromosome chromosome, String name) {
		this.chromosome = chromosome;
		this.name = name;
	}

	public abstract T randomize();

	public abstract T getValue();

	@Override
	public String toString() {
		return name + ": " + value;
	}
}

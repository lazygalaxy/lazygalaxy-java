package com.lazygalaxy.ci.parameter;

import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.Gene;

public abstract class Parameter<T> {
	protected final String name;
	protected T value;

	public Parameter(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public abstract Gene getGene(Chromosome chromosome);

	public abstract Parameter<T> getClone();
}
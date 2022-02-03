package com.lazygalaxy.canvas.common;

public abstract class Gene<T> {
	final protected String name;
	protected T value;

	public Gene(String name) {
		this.name = name;
	}

	public abstract T randomize();

	public abstract T getValue();

	@Override
	public String toString() {
		return name + ": " + value;
	}
}

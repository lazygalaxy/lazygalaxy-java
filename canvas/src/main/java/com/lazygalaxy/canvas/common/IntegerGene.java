package com.lazygalaxy.canvas.common;

public class IntegerGene implements Gene {
	final private int minValue;
	final private int maxValue;

	private int value;

	public IntegerGene(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		randomize();
	}

	public void randomize() {
		this.value = (int) ((Math.random() * (maxValue - minValue)) + minValue);
	}

	public Object getValue() {
		return value;
	}
}

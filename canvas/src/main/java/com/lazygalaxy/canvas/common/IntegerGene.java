package com.lazygalaxy.canvas.common;

import com.lazygalaxy.canvas.main.WireFrameGeneticAlgorithmCreate;

public class IntegerGene extends Gene<Integer> {
	final private int minValue;
	final private int maxValue;

	public IntegerGene(String name, int minValue, int maxValue) {
		super(name);

		this.minValue = minValue;
		this.maxValue = maxValue;

		this.value = randomize();
	}

	public Integer randomize() {
		return WireFrameGeneticAlgorithmCreate.RANDOM.nextInt(maxValue - minValue + 1) + minValue;
	}

	public Integer getValue() {
		return value;
	}
}

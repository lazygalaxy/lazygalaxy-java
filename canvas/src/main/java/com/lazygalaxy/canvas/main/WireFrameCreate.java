package com.lazygalaxy.canvas.main;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.WireFrameLayer;

public class WireFrameCreate {

	private static final Logger LOGGER = LogManager.getLogger(WireFrameCreate.class);
	public static final Random RANDOM = new Random(4);

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

		String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
		int size = 2048;

		Canvas inputCanvas = null;
		Canvas outputCanvas = null;

		try {
			inputCanvas = new BufferedImageCanvas(pathname);
			outputCanvas = new BufferedImageCanvas(size, size);

			new WireFrameLayer(inputCanvas, 50, 1391130753486896350l, 4946, 82, 0.2f).apply(outputCanvas);

			outputCanvas.saveAsPng("/Users/vangos/Development/digiart/process/lost_love");
		} finally {
			inputCanvas.close();
			outputCanvas.close();
		}

		long endTIme = System.currentTimeMillis();
		LOGGER.info("total time: " + ((endTIme - startTime) / 1000.0) + " secs");
	}
}

package com.lazygalaxy.canvas.common;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.WireFrameLayer;

public class GeneticAlgorithmThread extends Thread {
	private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithmThread.class);

	private Frame frame;
	private JPanel canvasWallPanel;

	public GeneticAlgorithmThread(Frame frame, JPanel canvasWallPanel) {
		this.frame = frame;
		this.canvasWallPanel = canvasWallPanel;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 10; i++) {
				long startTime = System.currentTimeMillis();

				String pathname = "/Users/vangos/Development/digiart/lost_love.jpg";
				int size = 2048;

				Canvas inputCanvas = new BufferedImageCanvas(pathname);
				Canvas outputCanvas = new BufferedImageCanvas(size, size);

				try {
					IntegerGene removeThreshold = new IntegerGene("removeThreshold", 1, 150);
					IntegerGene randomSample = new IntegerGene("randomSample", 500, 7500);
					IntegerGene lineJoinDistanceThreshold = new IntegerGene("lineJoinDistanceThreshold", 10, 200);
					FloatGene lineJoinThickness = new FloatGene("lineJoinThickness", 0.1f, 5f);

					Chromosome chromosome = new Chromosome(removeThreshold, randomSample, lineJoinDistanceThreshold,
							lineJoinThickness);
					LOGGER.info(chromosome);

					new WireFrameLayer(inputCanvas, removeThreshold.getValue(), randomSample.getValue(),
							lineJoinDistanceThreshold.getValue(), lineJoinThickness.getValue()).apply(outputCanvas);

					CanvasPanel canvasPanel = new CanvasPanel(outputCanvas, 512);
					canvasWallPanel.add(canvasPanel);
					frame.repaint();

				} finally {
					inputCanvas.close();
					outputCanvas.close();
				}

				long endTIme = System.currentTimeMillis();
				LOGGER.info("total time: " + ((endTIme - startTime) / 1000.0) + " secs");
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	public class CanvasPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		private Image image = null;
		private int size;

		public CanvasPanel(Canvas canvas, int size) throws Exception {
			this.image = canvas.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
			this.size = size;
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(size, size);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage(image, 0, 0, this);
			g2d.dispose();
		}

	}
}

package com.lazygalaxy.canvas.main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.BufferedImageCanvas;
import com.lazygalaxy.canvas.Canvas;
import com.lazygalaxy.canvas.layer.WireFrameLayer;
import com.lazygalaxy.ci.ga.Chromosome;
import com.lazygalaxy.ci.ga.gene.FloatGene;
import com.lazygalaxy.ci.ga.gene.IntegerGene;
import com.lazygalaxy.ci.ga.gene.LongGene;

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
					Chromosome chromosome = new Chromosome(new Random(), null);

					IntegerGene removeThreshold = chromosome.addIntegerGene("removeThreshold", 1, 150);
					LongGene randomSeed = chromosome.addLongGene("randomSeed");
					IntegerGene randomSampleSize = chromosome.addIntegerGene("randomSampleSize", 500, 7500);
					IntegerGene lineJoinDistanceThreshold = chromosome.addIntegerGene("lineJoinDistanceThreshold", 10,
							200);
					FloatGene lineJoinThickness = chromosome.addFloatGene("lineJoinThickness", 0.1f, 5f, 1);

					LOGGER.info(chromosome);

					new WireFrameLayer(inputCanvas, removeThreshold.getParameter().getValue(),
							randomSeed.getParameter().getValue(), randomSampleSize.getParameter().getValue(),
							lineJoinDistanceThreshold.getParameter().getValue(),
							lineJoinThickness.getParameter().getValue()).apply(outputCanvas);

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

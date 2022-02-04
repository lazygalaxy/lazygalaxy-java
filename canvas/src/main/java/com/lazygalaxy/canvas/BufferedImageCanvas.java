package com.lazygalaxy.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BufferedImageCanvas extends Canvas {
	private static final Logger LOGGER = LogManager.getLogger(BufferedImageCanvas.class);

	final private BufferedImage bufferedImage;
	private Graphics2D graphics2D;
	private BasicStroke basicStroke = new BasicStroke(1);

	public BufferedImageCanvas(String pathname) throws Exception {
		bufferedImage = ImageIO.read(new File(pathname));
		LOGGER.info("read " + pathname + ": " + getWidth() + "w, " + getHeight() + "h");
		getGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		getGraphics2D().setStroke(basicStroke);
	}

	public BufferedImageCanvas(int width, int height) throws Exception {
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		getGraphics2D().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		getGraphics2D().setStroke(basicStroke);
	}

	@Override
	public Image getImage() throws Exception {
		return bufferedImage;
	}

	@Override
	public int getWidth() {
		return bufferedImage.getWidth();
	}

	@Override
	public int getHeight() {
		return bufferedImage.getHeight();
	}

	@Override
	public int getSize() {
		return bufferedImage.getWidth() * bufferedImage.getHeight();
	}

	@Override
	public Color getColor(int x, int y) throws Exception {
		return new Color(bufferedImage.getRGB(x, y));
	}

	@Override
	public void drawLine(int x1, int y1, int x2, int y2, GradientPaint gradient, float width) throws Exception {
		getGraphics2D().setPaint(gradient);
		if (basicStroke.getLineWidth() != width) {
			basicStroke = new BasicStroke(width);
			getGraphics2D().setStroke(basicStroke);
		}
		getGraphics2D().drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, Color color) throws Exception {
		getGraphics2D().setPaint(color);
		getGraphics2D().fillRect(x, y, width, height);
	}

	@Override
	public void saveAsPng(String pathname) throws Exception {
		File file = new File(pathname + ".png");
		ImageIO.write(bufferedImage, "png", file);
		LOGGER.info("write " + pathname + ": " + getWidth() + "w, " + getHeight() + "h");
	}

	@Override
	public void saveAsJpg(String pathname) throws Exception {
		File file = new File(pathname + ".jpg");
		ImageIO.write(bufferedImage, "jpg", file);
		LOGGER.info("write " + pathname + ": " + getWidth() + "w, " + getHeight() + "h");
	}

	@Override
	public void close() throws Exception {
		super.close();
		if (graphics2D != null) {
			graphics2D.dispose();
			graphics2D = null;
		}
	}

	private Graphics2D getGraphics2D() {
		if (graphics2D == null) {
			graphics2D = bufferedImage.createGraphics();
		}
		return graphics2D;
	}
}

package com.lazygalaxy.canvas.main.examples;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

	public static void main(String[] args) {
		new Test();
	}

	public Test() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				JFrame frame = new JFrame("Testing");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public class TestPane extends JPanel {

		private Image img = null;

		public TestPane() {
			try {
				img = ImageIO.read(new FileInputStream("/Users/vangos/Development/digiart/lost_love.jpg"));
				img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return img == null ? new Dimension(200, 200) : new Dimension(100, 100);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			if (img != null) {
				g2d.drawImage(img, 0, 0, this);
			}
			g2d.dispose();
		}

	}
}
package com.lazygalaxy.canvas.main;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ImageDemo {
	public static void main(String[] args) throws Exception {
		new ImageDemo(args[0]);
	}

	public ImageDemo(final String filename) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame editorFrame = new JFrame("Image Demo");
				editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(filename));
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}
}

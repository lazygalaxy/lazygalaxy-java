package com.lazygalaxy.canvas.main;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.canvas.common.GeneticAlgorithmThread;

public class GeneticAlgorithmCanvasCreator {

	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

			/* Turn off metal's use of bold fonts */
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			// Schedule a job for the event dispatchi thread:
			// creating and showing this application's GUI.
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					ApplicationFrame frame = new ApplicationFrame();
					frame.setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class ApplicationFrame extends JFrame implements ComponentListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(GeneticAlgorithmCanvasCreator.class);
	private static final int SCROLL_PANE_SIZE = 15;

	private final JPanel canvasWallPanel = new JPanel();

	public ApplicationFrame() {
		setTitle("Genetic Algorithm Canvas Creator");
		// setLayout(new FlowLayout());
		setSize(700, 350);
		addComponentListener(this);
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initComponents() {
		// set up the control panel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		Button startButton = new Button("Start");
		startButton.addActionListener(this);
		controlPanel.add(startButton);

		this.getContentPane().add(controlPanel, BorderLayout.NORTH);

		canvasWallPanel.setLayout(new FlowLayout());
		adjustToSize();

		JScrollPane scrollPane = new JScrollPane(canvasWallPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(SCROLL_PANE_SIZE, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, SCROLL_PANE_SIZE));

		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			GeneticAlgorithmThread thread = new GeneticAlgorithmThread(this, canvasWallPanel);
			thread.start();
		} catch (Exception ex) {
			LOGGER.error(ex);
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		adjustToSize();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
		adjustToSize();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	private void adjustToSize() {
		canvasWallPanel.setPreferredSize(new Dimension(getWidth() - SCROLL_PANE_SIZE, getHeight()));
	}

}

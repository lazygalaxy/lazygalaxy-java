package com.lazygalaxy.canvas.main.examples;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JavaPaintUI extends JFrame {

	private BufferedImage canvas;

	/** Creates new form JavaPaintUI */
	public JavaPaintUI() {
		initComponents();
	}

	// this was moved from the overriden paintComponent()
	// instead it update the canvas BufferedImage and calls repaint()
	public void updateCanvas() {
		Graphics2D g2d = canvas.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setPaint(getColor());

		if (tool == 1) {

			g2d.fillOval(currentX - ((int) value / 2), currentY - ((int) value / 2), (int) value, (int) value);
		} else if (tool == 2) {
			g2d.setStroke(new BasicStroke((float) value, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.drawLine(oldX, oldY, currentX, currentY);
			g2d.setStroke(new BasicStroke(1.0f));
		}
		repaint();
		System.out.println("You cleared the canvas.");
	}

	// used in both the updateCanvas and 'clear' method.
	private Color getColor() {
		Color c = null;
		if (color == 1)
			c = Color.black;
		else if (color == 2)
			c = Color.gray;
		else if (color == 3)
			c = Color.white;
		else if (color == 4)
			c = Color.red;
		else if (color == 5)
			c = Color.green;
		else if (color == 6)
			c = Color.blue;

		return c;
	}

// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		canvas = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);

		buttonGroup1 = new ButtonGroup();
		buttonGroup2 = new ButtonGroup();
		toolPanel = new JPanel();
		jRadioButton9 = new JRadioButton();
		jRadioButton10 = new JRadioButton();
		jSlider2 = new JSlider();
		jLabel1 = new JLabel();
		canvasPanel = new JPanel(new GridBagLayout());
		JLabel canvasLabel = new JLabel(new ImageIcon(canvas));
		canvasPanel.add(canvasLabel, null);

		colorPanel = new JPanel();
		jRadioButton3 = new JRadioButton();
		jRadioButton4 = new JRadioButton();
		jRadioButton5 = new JRadioButton();
		jRadioButton6 = new JRadioButton();
		jRadioButton7 = new JRadioButton();
		jRadioButton8 = new JRadioButton();
		jButton1 = new JButton();
		jButton2 = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("JavaPaint ~ Nick R");

		toolPanel.setBorder(BorderFactory.createTitledBorder("Tool"));

		buttonGroup1.add(jRadioButton9);
		jRadioButton9.setText("Pen");
		jRadioButton9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton9ActionPerformed(evt);
			}
		});

		buttonGroup1.add(jRadioButton10);
		jRadioButton10.setText("Line");
		jRadioButton10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton10ActionPerformed(evt);
			}
		});

		jSlider2.setMajorTickSpacing(10);
		jSlider2.setMaximum(51);
		jSlider2.setMinimum(1);
		jSlider2.setMinorTickSpacing(5);
		jSlider2.setPaintTicks(true);
		jSlider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				jSlider2StateChanged(evt);
			}
		});

		jLabel1.setText("Stroke Size (Radius)");

		GroupLayout jPanel4Layout = new GroupLayout(toolPanel);
		toolPanel.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jRadioButton9).addComponent(jRadioButton10))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
						.addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel1)
								.addComponent(jSlider2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(jSlider2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(jPanel4Layout.createSequentialGroup()
								.addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jRadioButton9).addComponent(jLabel1))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jRadioButton10))));

		canvasPanel.setBackground(new Color(128, 40, 128));
		canvasPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		// add the listeners to the label that contains the canvas buffered image
		canvasLabel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				jPanel2MousePressed(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				jPanel2MouseReleased(evt);
			}
		});
		canvasLabel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				jPanel2MouseDragged(evt);
			}
		});

		// this part of the code was not helping.
		// either layout a container or custom paint it.
		// only attempt both if you are very confident of what you are doing.
		/*
		 * GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		 * jPanel2.setLayout(jPanel2Layout); jPanel2Layout.setHorizontalGroup(
		 * jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING) .addGap(0,
		 * 596, Short.MAX_VALUE) ); jPanel2Layout.setVerticalGroup(
		 * jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING) .addGap(0,
		 * 357, Short.MAX_VALUE) );
		 */
		colorPanel.setBorder(BorderFactory.createTitledBorder("Color"));

		buttonGroup2.add(jRadioButton3);
		jRadioButton3.setText("Red");
		jRadioButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton3ActionPerformed(evt);
			}
		});

		buttonGroup2.add(jRadioButton4);
		jRadioButton4.setText("Black");
		jRadioButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton4ActionPerformed(evt);
			}
		});

		buttonGroup2.add(jRadioButton5);
		jRadioButton5.setText("Gray");
		jRadioButton5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton5ActionPerformed(evt);
			}
		});

		buttonGroup2.add(jRadioButton6);
		jRadioButton6.setText("Green");
		jRadioButton6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton6ActionPerformed(evt);
			}
		});

		buttonGroup2.add(jRadioButton7);
		jRadioButton7.setText("White");
		jRadioButton7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton7ActionPerformed(evt);
			}
		});

		buttonGroup2.add(jRadioButton8);
		jRadioButton8.setText("Blue");
		jRadioButton8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jRadioButton8ActionPerformed(evt);
			}
		});

		GroupLayout jPanel3Layout = new GroupLayout(colorPanel);
		colorPanel.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
								.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(jRadioButton3, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jRadioButton4, GroupLayout.Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(jRadioButton5, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
										.addComponent(jRadioButton6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(jRadioButton8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(jRadioButton7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jRadioButton4).addComponent(jRadioButton5).addComponent(jRadioButton7))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jRadioButton3).addComponent(jRadioButton6).addComponent(jRadioButton8))));

		jButton1.setText("Clear");
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("About");
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(canvasPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(colorPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(jButton2, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
										.addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))))
				.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addGroup(layout.createSequentialGroup().addGap(4, 4, 4)
								.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
						.addComponent(toolPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(colorPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(canvasPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap()));

		pack();
	}// </editor-fold>

// clear the canvas using the currently selected color.
	private void jButton1ActionPerformed(ActionEvent evt) {
		System.out.println("You cleared the canvas.");
		Graphics g = canvas.getGraphics();
		g.setColor(getColor());
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		repaint();
	}

	private void jButton2ActionPerformed(ActionEvent evt) {

		JOptionPane.showMessageDialog(null,
				"JavaPaint is a simple java based painting application." + "  Nick R 5/22/2011", "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	int currentX, currentY, oldX, oldY;

	private void jPanel2MouseDragged(MouseEvent evt) {
		currentX = evt.getX();
		currentY = evt.getY();
		updateCanvas();
		if (tool == 1) {
			oldX = currentX;
			oldY = currentY;
		}

	}

	private void jPanel2MousePressed(MouseEvent evt) {

		oldX = evt.getX();
		oldY = evt.getY();
		if (tool == 2) {
			currentX = oldX;
			currentY = oldY;
		}

	}

//Tool Selection//
	int tool = 0;

	private void jRadioButton9ActionPerformed(ActionEvent evt) {
		tool = 1;
		System.out.println("Using the pen tool.");
	}

	private void jRadioButton10ActionPerformed(ActionEvent evt) {
		tool = 2;
		System.out.println("Using the line tool.");
	}

//Slider Properties//
	double value = 5;

	private void jSlider2StateChanged(ChangeEvent evt) {
		value = jSlider2.getValue();
		System.out.println(value);
	}

//COLOR CODE//
	int color = 1;

	private void jRadioButton4ActionPerformed(ActionEvent evt) {
		color = 1; // Black
	}

	private void jRadioButton5ActionPerformed(ActionEvent evt) {
		color = 2; // Grey
	}

	private void jRadioButton7ActionPerformed(ActionEvent evt) {
		color = 3; // White
	}

	private void jRadioButton3ActionPerformed(ActionEvent evt) {
		color = 4; // Red
	}

	private void jRadioButton6ActionPerformed(ActionEvent evt) {
		color = 5; // Green
	}

	private void jRadioButton8ActionPerformed(ActionEvent evt) {
		color = 6; // Blue
	}

//mouse released//
	private void jPanel2MouseReleased(MouseEvent evt) {

		currentX = evt.getX();
		currentY = evt.getY();
		if (tool == 2) {
			System.out.println("line from " + oldX + ", " + oldY + " to " + currentX + ", " + currentY);
		}
	}

//set ui visible//
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				new JavaPaintUI().setVisible(true);
			}
		});
	}

// Variables declaration - do not modify
	private ButtonGroup buttonGroup1;
	private ButtonGroup buttonGroup2;
	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel1;
	public JPanel canvasPanel;
	private JPanel colorPanel;
	private JPanel toolPanel;
	private JRadioButton jRadioButton10;
	private JRadioButton jRadioButton3;
	private JRadioButton jRadioButton4;
	private JRadioButton jRadioButton5;
	private JRadioButton jRadioButton6;
	private JRadioButton jRadioButton7;
	private JRadioButton jRadioButton8;
	private JRadioButton jRadioButton9;
	public JSlider jSlider2;
// End of variables declaration
}

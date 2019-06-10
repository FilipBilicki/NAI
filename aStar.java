
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



import javax.swing.JComboBox;

public class aStar {
	JFrame win;
	private int startX = -1;
	private int startY = -1;
	private int endX = -1;
	private int endY = -1;
	private int field = 10;
	private int option = 0;
	private int width = 600;
	private final int height = 400;
	private final int boardSize = 200;
	private int fieldSize = boardSize / field;
	private String[] options = { "Start", "End", "Block" };
	private boolean solve = false;
	Node[][] node;
	Board board;
	private int curAlg = 0;
	Algorithm Alg = new Algorithm();
	JButton searchB = new JButton("Start");
	JButton clearButton = new JButton("Clear");
	JComboBox optionsBox = new JComboBox(options);
	JPanel opt = new JPanel();

	public static void main(String[] args) {
		new aStar();
	}

	public aStar() {

		clear();
		window();
	}

	private void window() {
		win = new JFrame();
		win.setVisible(true);
		win.setSize(width, height);
		win.setTitle("A* test");
		win.setResizable(true);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.getContentPane().setLayout(null);
		int sp = 25;
		opt.setLayout(null);
		searchB.setBounds(40, sp, 120, 25);
		opt.add(searchB);
		sp += 25;
		opt.setBounds(20, 20, 200, 300);
		sp += 25;
		clearButton.setBounds(50, sp, 80, 30);
		opt.add(clearButton);
		sp += 25;
		optionsBox.setBounds(50, sp, 80, 30);
		opt.add(optionsBox);
		win.getContentPane().add(opt);
		board = new Board();
		board.setBounds(230, 10, boardSize + 1, boardSize + 1);
		win.getContentPane().add(board);

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				clear();
				Update();
			}
		});
		optionsBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				option = optionsBox.getSelectedIndex();
			}
		});

		searchB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				if ((startX > -1 && startY > -1) && (endX > -1 && endY > -1))
					solve = true;
			}
		});

		startSearch();
	}

	public void reset() {
		solve = false;

	}

	public void clear() {
		endX = -1;
		endY = -1;
		startX = -1;
		startY = -1;
		node = new Node[field][field];
		for (int x = 0; x < field; x++) {
			for (int y = 0; y < field; y++) {
				node[x][y] = new Node(3, x, y);
			}
		}
	}

	public void startSearch() {
		if (solve) {
			switch (curAlg) {
			case 0:
				Alg.AStar();
				break;
			}
		}
		pause();
	}

	public void pause() {
		int i = 0;
		while (!solve) {
			i++;
			if (i > 500)
				i = 0;
			try {
				Thread.sleep(1);
			} catch (Exception e) {
			}
		}
		startSearch();
	}

	public void Update() {
		fieldSize = boardSize / field;
		board.repaint();
	}

	class Board extends JPanel implements MouseListener, MouseMotionListener {
		public Board() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void paintComponent(Graphics color) {
			super.paintComponent(color);
			for (int x = 0; x < field; x++) {
				for (int y = 0; y < field; y++) {
					switch (node[x][y].getFieldType()) {
					case 0:
						color.setColor(Color.BLUE);
						break;
					case 1:
						color.setColor(Color.GREEN);
						break;
					case 2:
						color.setColor(Color.BLACK);
						break;
					case 3:
						color.setColor(Color.WHITE);
						break;
					case 4:
						color.setColor(Color.RED);
						break;

					case 5:
						color.setColor(Color.YELLOW);
						break;
					}
					color.fillRect(x * fieldSize, y * fieldSize, fieldSize, fieldSize);
					color.setColor(Color.BLACK);
					color.drawRect(x * fieldSize, y * fieldSize, fieldSize, fieldSize);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			try {
				int x = event.getX() / fieldSize;
				int y = event.getY() / fieldSize;
				Node current = node[x][y];
				if ((option == 2 || option == 3) && (current.getFieldType() != 0 && current.getFieldType() != 1))
					current.setFieldType(option);
				Update();
			} catch (Exception z) {
			}
		}

		@Override
		public void mouseMoved(MouseEvent event) {
		}

		@Override
		public void mouseClicked(MouseEvent event) {
		}

		@Override
		public void mouseEntered(MouseEvent event) {
		}

		@Override
		public void mouseExited(MouseEvent event) {
		}

		@Override
		public void mousePressed(MouseEvent e) {

			try {
				int x = e.getX() / fieldSize;
				int y = e.getY() / fieldSize;
				Node current = node[x][y];
				switch (option) {
				case 0: {
					if (current.getFieldType() != 2) {
						if (startX > -1 && startY > -1) {
							node[startX][startY].setFieldType(3);
							node[startX][startY].setLeap(-1);
						}
						current.setLeap(0);
						startX = x;
						startY = y;
						current.setFieldType(0);
					}
					break;
				}
				case 1: {
					if (current.getFieldType() != 2) {
						if (endX > -1 && endY > -1)
							node[endX][endY].setFieldType(3);
						endX = x;
						endY = y;
						current.setFieldType(1);
					}
					break;
				}
				default:
					if (current.getFieldType() != 0 && current.getFieldType() != 1)
						current.setFieldType(option);
					break;
				}
				Update();
			} catch (Exception z) {
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	class Algorithm {
		public void AStar() {
			ArrayList<Node> priority = new ArrayList<Node>();
			priority.add(node[startX][startY]);
			while (solve) {
				if (priority.size() <= 0) {
					solve = false;
					break;
				}
				int hops = priority.get(0).getLeap() + 1;
				ArrayList<Node> explored = exploreNeighbors(priority.get(0), hops);
				if (explored.size() > 0) {
					priority.remove(0);
					priority.addAll(explored);
					Update();
				} else {
					priority.remove(0);
				}
				sortQue(priority);
			}
		}

		public ArrayList<Node> sortQue(ArrayList<Node> sort) {
			int c = 0;
			while (c < sort.size()) {
				int sm = c;
				for (int i = c + 1; i < sort.size(); i++) {
					if (sort.get(i).Distance() + sort.get(i).getLeap() < sort.get(sm).Distance()
							+ sort.get(sm).getLeap())
						sm = i;
				}
				if (c != sm) {
					Node temp = sort.get(c);
					sort.set(c, sort.get(sm));
					sort.set(sm, temp);
				}
				c++;
			}
			return sort;
		}

		public ArrayList<Node> exploreNeighbors(Node current, int leap) {
			ArrayList<Node> explored = new ArrayList<Node>();
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					int xbound = current.getX() + a;
					int ybound = current.getY() + b;
					if ((xbound > -1 && xbound < field) && (ybound > -1 && ybound < field)) {
						Node neighbor = node[xbound][ybound];
						if ((neighbor.getLeap() == -1 || neighbor.getLeap() > leap) && neighbor.getFieldType() != 2) {
							explore(neighbor, current.getX(), current.getY(), leap);
							explored.add(neighbor);
						}
					}
				}
			}
			return explored;
		}

		public void explore(Node current, int lastx, int lasty, int leap) {
			if (current.getFieldType() != 0 && current.getFieldType() != 1)
				current.setFieldType(4);
			current.setNode(lastx, lasty);
			current.setLeap(leap);

			if (current.getFieldType() == 1) {
				backtrack(current.getLastX(), current.getLastY(), leap);
			}
		}

		public void backtrack(int lx, int ly, int leap) {
			while (leap > 1) {
				Node current = node[lx][ly];
				current.setFieldType(5);
				lx = current.getLastX();
				ly = current.getLastY();
				leap--;
			}
			solve = false;
		}
	}

	class Node {
		private int x;
		private int y;
		private int fieldType = 0;
		private int leap;
		private int lastX;
		private int lastY;
		private double End = 0;

		public Node(int type, int x, int y) {
			fieldType = type;
			this.x = x;
			this.y = y;
			leap = -1;
		}

		public double Distance() {
			int xdis = Math.abs(x - endX);
			int ydis = Math.abs(y - endY);
			End = Math.sqrt((xdis * xdis) + (ydis * ydis));
			return End;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getLastX() {
			return lastX;
		}

		public int getLastY() {
			return lastY;
		}

		public int getFieldType() {
			return fieldType;
		}

		public int getLeap() {
			return leap;
		}

		public void setFieldType(int type) {
			fieldType = type;
		}

		public void setNode(int x, int y) {
			lastX = x;
			lastY = y;
		}

		public void setLeap(int leap) {
			this.leap = leap;
		}
	}
}
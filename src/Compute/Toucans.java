package Compute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Toucans implements MouseListener, ActionListener {
	// Toucans is a strategy game where the goal is to win
	// You can win through DOMINATION (35 tiles), TECH (35 points), ECONOMY
	// (350,000 K GDP (sounds hard, but with 20 squares it won't be)
	// Domination Victory: Every tile on the board is conquered. Enemies are
	// vanquished.
	// Economical Victory: A combined sum of trade (per turn), subsidies to
	// other tribes (per turn), production per
	// turn, and GDP over 35K. On hard, if you near this all other countries
	// will sanction you, meaning inner country
	// must control majority of resources
	// You can die through being CONQUERED or CONCEDING (lit lit lit lit lit)
	// Death through Conquered: You are taken over by another tribe.
	// Conceding/giving up - This is the command '/concede'
	boolean toucanIsAlive = true;
	boolean parrotIsAlive = true;
	boolean macawIsAlive = true;
	boolean dodoIsAlive = true;
	JButton[] buttons = new JButton[36];
	int turnOf = -1;
	int turns = 0;
	int onepower = 0;
	int oneoutput = 0;
	int twopower = 0;
	int twooutput = 0;
	int twopattern = 1;
	int threepower = 0;
	int threeoutput = 0;
	int threepattern = 1;
	int fourpower = 0;
	int fourpattern = 1;
	int fouroutput = 0;
	int[] status = new int[36];
	// Wars available:

	JFrame startframe = new JFrame();
	JPanel startpanel = new JPanel();
	JPanel moves = new JPanel();
	JPanel playpanel = new JPanel();

	GamePanel gamePanel;
	public static final int width = 600;
	public static final int height = 900;
	boolean boardInitialized = false;

	Toucans() {
		startframe.setSize(width, height);
		startframe.setVisible(true);
		gamePanel = new GamePanel();
		gamePanel.setSize(600, 900);
		setup();
		gamePanel.startGame();
		turnOf = 1;
		turns++;
	}

	JFrame frame = new JFrame();

	public void createBoard() {
		int locx = 0;
		int locy = 0;

		frame.add(playpanel);
		frame.setSize(600, 600);
		frame.getContentPane().setBackground(new Color(0, 153, 85));
		playpanel.addMouseListener(this);
		playpanel.setLayout(new GridLayout(6, 6));
		for (int i = 0; i < 36; i++) {
			buttons[i] = new JButton();
			buttons[i].setPreferredSize(new Dimension(100, 100));
			buttons[i].setLocation(locx * 100, locy * 100 + 100);
			buttons[i].setVisible(true);
			buttons[i].setOpaque(true);
			buttons[i].addMouseListener(this);
			buttons[i].setBorder(BorderFactory.createLineBorder(new Color(0, 153, 87), 2));
			locx++;
			if (locx == 6) {
				locy++;
				locx = 0;
			}
			playpanel.add(buttons[i]);
		}
		// frame.pack();
		frame.setVisible(true);
	}

	void setup() {
		startframe.add(gamePanel);
		startframe.addMouseListener(this);
		startframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startframe.addKeyListener(gamePanel);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Toucans runner = new Toucans();
	}

	public void playGame() {
		// Blitz through the following: use Object to get source of block,
		// then after Object do is the thing to do
		// Take object do and do it to object block, be sure to start stuff
		// like wars, etc.
		/**
		 * Toucan starts at 0 - Status is ONE Parrot starts at 5 - Status is TWO
		 * Macaw starts at 30 - Status is THREE Dodo starts at 35 - Status is
		 * FOUR
		 */

		// Player, not AI
		if (turnOf == 1 && toucanIsAlive) {
			loadImages();
			updateGraphics();
			String move = JOptionPane.showInputDialog(
					"TOUCAN: Action to Perform:\n- /claim <row (letter)><column (number)> - Claim a square\n- /attack <row (letter)><column (number)> - Attack a claimed square\n- /develop <infrastructure, tech, education> - Will boost output\n- /train - Train troops\n- /endmove - End move\n- /help - See the rules and instructions\n- /concede - Quit");
			if (move.equals("/concede")) {
				concede();
			}
			if (move.equals("/help")) {
				Instrucs in = new Instrucs();
			}
			// Moves that DO NOT require usage of squares WHICH MAKES LIFE
			// SO MUCH EASIER
			if (move.equals(null)) {
				System.out.println("Player pressed cancel. Quitting now...");
				System.exit(0);
			}
			if (move.equals("/train")) {
				train();
				check();
			}
			if (move.equals("/develop tech")) {
				devTech();
				check();
			}
			if (move.equals("/develop education")) {
				devEdu();
				check();
			}
			if (move.equals("/develop infrastructure")) {
				devInfrastructure();
				check();
			}
			if (move.equals("/endmove")) {
				check();
			}
			if (move.contains("/claim")) {
				char[] findSquare = move.toCharArray();
				char column = findSquare[findSquare.length - 2];
				char row = findSquare[findSquare.length - 1];
				System.out.println(column + " " + row);
				int notChar = Integer.parseInt(row + "");
				int numToCheck = 0;
				// Find numToCheck
				if (column == 'A' || column == 'a') {
					numToCheck = -1 + notChar;
				}
				if (column == 'B' || column == 'b') {
					numToCheck = 5 + notChar;
				}
				if (column == 'C' || column == 'c') {
					numToCheck = 11 + notChar;
				}
				if (column == 'D' || column == 'd') {
					numToCheck = 17 + notChar;
				}
				if (column == 'E' || column == 'e') {
					numToCheck = 23 + notChar;
				}
				if (column == 'F' || column == 'f') {
					numToCheck = 29 + notChar;
				}
				// Claim or check on
				if (move.equalsIgnoreCase("/claim C6") && status[16] == 1) {
					// Exceptions for things that are throwing errors
					status[17] = 1;
				} else if (moveIsLegal(numToCheck, 1)) {
					if (status[numToCheck] == 0) {
						status[numToCheck] = 1;
						System.out.println("Square claimed successfully");
						check();
					} else {
						System.out.println("You can't claim an occupied square, silly!");
						check();
					}
				} else {
					System.out.println("That square is too far away, silly!");
					check();
				}
			}
			if (move.contains("/attack")) {
				char[] findSquare = move.toCharArray();
				char column = findSquare[findSquare.length - 2];
				char row = findSquare[findSquare.length - 1];
				System.out.println(column + " " + row);
				int notChar = Integer.parseInt(row + "");
				int numToCheck = 0;
				// Find numToCheck
				if (column == 'A' || column == 'a') {
					numToCheck = -1 + notChar;
				}
				if (column == 'B' || column == 'b') {
					numToCheck = 5 + notChar;
				}
				if (column == 'C' || column == 'c') {
					numToCheck = 11 + notChar;
				}
				if (column == 'D' || column == 'd') {
					numToCheck = 17 + notChar;
				}
				if (column == 'E' || column == 'e') {
					numToCheck = 23 + notChar;
				}
				if (column == 'F' || column == 'f') {
					numToCheck = 29 + notChar;
				}
				if (moveIsLegal(numToCheck, 1)) {
					// The move is legal.
					// Is square occupied?
					if (status[numToCheck] == 0 || status[numToCheck] == 1) {
						System.out.println(
								"Error attacking square: No occupying army to attack, or this square belongs to you");
						if (status[numToCheck] != 1) {
							int claimInstead = JOptionPane.showConfirmDialog(null,
									"Do you want to claim this square instead?");
							if (claimInstead == JOptionPane.YES_OPTION) {
								status[numToCheck] = 1;
								System.out.println("Square claimed!");
							} else {
								System.out.println("Square not taken!");
								check();
							}
						} else {
							System.out.println(
									"This square is yours! If you want to end the game, play /concede on your next move");
						}
					} else {
						// Attacking the square!
						if (status[numToCheck] == 2) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(3);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + onepower;
							int defendingForce = oppoLuck + twopower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 1;
								onepower = onepower - (onepower - twopower);
								twopower = twopower - (onepower - twopower + 2);
								twooutput = twooutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								onepower = onepower - (twopower - onepower - 1);
								twopower = twopower - (twopower - onepower + 1);
								oneoutput = oneoutput - 1;
							} else {
								onepower = onepower - 1;
								twopower = twopower - 1;
								oneoutput = oneoutput - 1;
								twooutput = twooutput - 1;
							}
						}
						if (status[numToCheck] == 3) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(2);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + onepower;
							int defendingForce = oppoLuck + threepower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 1;
								onepower = onepower - (onepower - threepower);
								threepower = threepower - (onepower - threepower + 2);
								threeoutput = threeoutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								onepower = onepower - (threepower - onepower - 1);
								threepower = threepower - (threepower - onepower + 1);
								oneoutput = oneoutput - 1;
							} else {
								onepower = onepower - 1;
								threepower = threepower - 1;
								oneoutput = oneoutput - 1;
								threeoutput = threeoutput - 1;
							}
						}
						if (status[numToCheck] == 4) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(2);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + onepower;
							int defendingForce = oppoLuck + fourpower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 1;
								onepower = onepower - (onepower - fourpower);
								fourpower = fourpower - (onepower - fourpower + 2);
								fouroutput = fouroutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								onepower = onepower - (fourpower - onepower - 1);
								fourpower = fourpower - (fourpower - onepower + 1);
								oneoutput = oneoutput - 1;
							} else {
								onepower = onepower - 1;
								fourpower = fourpower - 1;
								oneoutput = oneoutput - 1;
								fouroutput = fouroutput - 1;
							}
						}

					}
				} else {
					// THE MOVE IS ILLEGAL. ALL ACTION PASSED ONTO THE NEXT
					// PLAYER.
					System.err.println("Invalid move! You do not border this square.");
					check();
				}
			}
			// AI Moves:
		}
		// AI Move: Parrot
		// Parrot will use rotating int - claim, train, dev, dev, train, claim,
		// dev...
		else if (turnOf == 2 && parrotIsAlive) {
			loadImages();
			updateGraphics();
			loadImages();
			updateGraphics();
			String move = JOptionPane.showInputDialog(
					"PARROT: Action to Perform:\n- /claim <row (letter)><column (number)> - Claim a square\n- /attack <row (letter)><column (number)> - Attack a claimed square\n- /develop <infrastructure, tech, education> - Will boost output\n- /train - Train troops\n- /endmove - End move\n- /help - See the rules and instructions\n- /concede - Quit");
			if (move.equals("/concede")) {
				concede();
			}
			if (move.equals("/help")) {
				Instrucs in = new Instrucs();
			}
			// Moves that DO NOT require usage of squares WHICH MAKES LIFE
			// SO MUCH EASIER
			if (move.equals(null)) {
				System.out.println("SOMEone wasted their turn! lol");
			}
			if (move.equals("/train")) {
				train();
				check();
			}
			if (move.equals("/develop tech")) {
				devTech();
				check();
			}
			if (move.equals("/develop education")) {
				devEdu();
				check();
			}
			if (move.equals("/develop infrastructure")) {
				devInfrastructure();
				check();
			}
			if (move.equals("/endmove")) {
				check();
			}
			if (move.contains("/claim")) {
				char[] findSquare = move.toCharArray();
				char column = findSquare[findSquare.length - 2];
				char row = findSquare[findSquare.length - 1];
				System.out.println(column + " " + row);
				int notChar = Integer.parseInt(row + "");
				int numToCheck = 0;
				// Find numToCheck
				if (column == 'A' || column == 'a') {
					numToCheck = -1 + notChar;
				}
				if (column == 'B' || column == 'b') {
					numToCheck = 5 + notChar;
				}
				if (column == 'C' || column == 'c') {
					numToCheck = 11 + notChar;
				}
				if (column == 'D' || column == 'd') {
					numToCheck = 17 + notChar;
				}
				if (column == 'E' || column == 'e') {
					numToCheck = 23 + notChar;
				}
				if (column == 'F' || column == 'f') {
					numToCheck = 29 + notChar;
				}
				// Claim or check on
				if (move.equalsIgnoreCase("/claim C6") && status[16] == 2) {
					// Exceptions for things that are throwing errors
					status[17] = 2;
					check();
				}
				else if (move.equalsIgnoreCase("/claim F5") && status[35] == 2) {
					status[34] = 2;
					check();
				}
				else if (move.equalsIgnoreCase("/claim E6") && status[35] == 2) {
					status[29] = 2;
					check();
				}
				else if (moveIsLegal(numToCheck, 2)) {
					if (status[numToCheck] == 0) {
						status[numToCheck] = 2;
						System.out.println("Square claimed successfully");
						check();
					} else {
						System.out.println("You can't claim an occupied square, silly!");
						check();
					}
				} else {
					System.out.println("That square is unclaimable, silly!");
					check();
				}
			}
			if (move.contains("/attack")) {
				char[] findSquare = move.toCharArray();
				char column = findSquare[findSquare.length - 2];
				char row = findSquare[findSquare.length - 1];
				System.out.println(column + " " + row);
				int notChar = Integer.parseInt(row + "");
				int numToCheck = 0;
				// Find numToCheck
				if (column == 'A' || column == 'a') {
					numToCheck = -1 + notChar;
				}
				if (column == 'B' || column == 'b') {
					numToCheck = 5 + notChar;
				}
				if (column == 'C' || column == 'c') {
					numToCheck = 11 + notChar;
				}
				if (column == 'D' || column == 'd') {
					numToCheck = 17 + notChar;
				}
				if (column == 'E' || column == 'e') {
					numToCheck = 23 + notChar;
				}
				if (column == 'F' || column == 'f') {
					numToCheck = 29 + notChar;
				}
				if (moveIsLegal(numToCheck, 2)) {
					// The move is legal.
					// Is square occupied?
					if (status[numToCheck] == 0 || status[numToCheck] == 2) {
						System.out.println(
								"Error attacking square: No occupying army to attack, or this square belongs to you");
						if (status[numToCheck] != 1) {
							int claimInstead = JOptionPane.showConfirmDialog(null,
									"Do you want to claim this square instead?");
							if (claimInstead == JOptionPane.YES_OPTION) {
								status[numToCheck] = 2;
								System.out.println("Square claimed!");
							} else {
								System.out.println("Square not taken!");
								check();
							}
						} else {
							System.out.println(
									"This square is yours! If you want to end the game, play /concede on your next move");
						}
					} else {
						// Attacking the square!
						if (status[numToCheck] == 1) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(3);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + twopower;
							int defendingForce = oppoLuck + onepower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 2;
								twopower = twopower - (twopower - onepower);
								onepower = onepower - (twopower - onepower + 2);
								oneoutput = oneoutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								twopower = twopower - (onepower - twopower - 1);
								onepower = onepower - (onepower - twopower + 1);
								twooutput = twooutput - 1;
							} else {
								onepower = onepower - 1;
								twopower = twopower - 1;
								oneoutput = oneoutput - 1;
								twooutput = twooutput - 1;
							}
						}
						if (status[numToCheck] == 3) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(2);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + twopower;
							int defendingForce = oppoLuck + threepower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 2;
								twopower = twopower - (twopower - threepower);
								threepower = threepower - (twopower - threepower + 2);
								threeoutput = threeoutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								twopower = twopower - (threepower - twopower - 1);
								threepower = threepower - (threepower - twopower + 1);
								oneoutput = oneoutput - 1;
							} else {
								twopower = twopower - 1;
								threepower = threepower - 1;
								twooutput = twooutput - 1;
								threeoutput = threeoutput - 1;
							}
						}
						if (status[numToCheck] == 4) {
							int luck = new Random().nextInt(6);
							int actualLuck = luck - 3;
							// Gives options -3 through 3
							int oppoLuck = new Random().nextInt(2);
							// Battle Numbers - Who Wins?
							int attackingForce = actualLuck + twopower;
							int defendingForce = oppoLuck + fourpower;
							if (attackingForce > defendingForce) {
								System.out.println("Attackers took square");
								status[numToCheck] = 2;
								twopower = twopower - (twopower - fourpower);
								fourpower = fourpower - (twopower - fourpower + 2);
								fouroutput = fouroutput - 1;
							} else if (attackingForce < defendingForce) {
								System.out.println("Defense remained in control");
								twopower = twopower - (fourpower - twopower - 1);
								fourpower = fourpower - (fourpower - twopower + 1);
								twooutput = twooutput - 1;
							} else {
								twopower = twopower - 1;
								fourpower = fourpower - 1;
								twooutput = twooutput - 1;
								fouroutput = fouroutput - 1;
							}
						}

					}
				} else {
					// THE MOVE IS ILLEGAL. ALL ACTION PASSED ONTO THE NEXT
					// PLAYER.
					System.err.println("Invalid move! You do not border this square.");
					check();
				}
			}
		}
		// AI Move: Macaw
		// Macaw will play aggressively - train, claim, claim, train, dev,
		// train, dev... (7)
		else if (turnOf == 3 && macawIsAlive) {
			loadImages();
			updateGraphics();
			loadImages();
			updateGraphics();
			check();
		}
		// AI Move: Dodo
		// Dodo will play peacefully - train, dev, dev, claim... (4)
		else if (turnOf == 4 && dodoIsAlive) {
			loadImages();
			updateGraphics();
			loadImages();
			updateGraphics();
			check();}
			}
		// Only reason every group starts with train is to ensure that an
		// early assault is not successful


	@Override
	public void mouseClicked(MouseEvent e) {
		if (!boardInitialized) {
			createBoard();
			boardInitialized = true;
			status[0] = 1;
			status[35] = 2;
		} else {
			playGame();
		}
	}

	public void concede() {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure?");
		if (confirm == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			System.out.println("Aborted game ending!");
			turnOf = 2;
			turns++;
		}
	}

	public void train() {
		if (turnOf == 1) {
			int armiesTrained = new Random().nextInt(2) + 1;
			onepower = onepower + armiesTrained;
			System.out.println("Player trained troops");
		}
		else if(turnOf == 2) {
			int armiesTrained = new Random().nextInt(2) + 1;
			twopower = twopower + armiesTrained;
			System.out.println("Player trained troops");
		}
		else if(turnOf == 3) {
			int armiesTrained = new Random().nextInt(2) + 1;
			threepower = threepower + armiesTrained;
			System.out.println("Player trained troops");
		}
		else if(turnOf == 4) {
			int armiesTrained = new Random().nextInt(2) + 1;
			fourpower = fourpower + armiesTrained;
			System.out.println("Player trained troops");
		}
	}

	public void devTech() {
		// Tech is a gamble. You can win big (+5) or waste a turn (0), or turn
		// out somewhere in between.
		int rand = new Random().nextInt(5);
		int add = rand * getNumberOfSquares(1);
		if(turnOf == 1) {
			oneoutput = oneoutput + add;
		}
		else if(turnOf == 2) {
			twooutput = twooutput + add;
		}
		else if(turnOf == 3) {
			threeoutput = threeoutput + add;
		}
		else if(turnOf == 4) {
			fouroutput = fouroutput + add;
		}
		System.out.println("Player gained " + add + " output");
	}

	public void devEdu() {
		// Education earns less on gambling but is always positive
		int rand = new Random().nextInt(2) + 1;
		int add = rand * getNumberOfSquares(1);
		if(turnOf == 1) {
			oneoutput = oneoutput + add;
		}
		else if(turnOf == 2) {
			twooutput = twooutput + add;
		}
		else if(turnOf == 3) {
			threeoutput = threeoutput + add;
		}
		else if(turnOf == 4) {
			fouroutput = fouroutput + add;
		}
		System.out.println("Player gained " + add + " output");
	}

	public void devInfrastructure() {
		// Infrastructure is a solid investment but only gains 1
		if(turnOf == 1) {
			oneoutput = oneoutput * getNumberOfSquares(1);
		}
		else if(turnOf == 2) {
			twooutput = twooutput * getNumberOfSquares(2);
		}
		else if(turnOf == 3) {
			threeoutput = threeoutput * getNumberOfSquares(3);
		}
		else if(turnOf == 4) {
			fouroutput = fouroutput * getNumberOfSquares(4);
		}
		System.out.println("Player gained infrastructure");
	}

	public void check() {
		updateLife();
		checkForWinner();
		if (turnOf == 1) {
			// The player is checking the action
			System.out.println("Player checks action");
		} else {
			// An AI player is checking the action
			if (turnOf == 2) {
				// Parrot checking!
				System.out.println("Parrot checks action");
			} else if (turnOf == 3) {
				// Macaw is checking
				System.out.println("Macaw checks action");
			} else if (turnOf == 4) {
				// Dodo is checking the action
				System.out.println("Dodo checks action");
			}
		}
		if (turnOf == 1) {
			// The turn can just be given one here - examples: 1 to 2, 2 to 3, 3
			// to 4
			if (parrotIsAlive) {
				turnOf = 2;
			} else if (macawIsAlive) {
				turnOf = 3;
			} else {
				turnOf = 4;
			}
		} else if (turnOf == 2) {
			if (macawIsAlive) {
				turnOf = 3;
			} else if (dodoIsAlive) {
				turnOf = 4;
			} else {
				turnOf = 1;
			}
		} else if (turnOf == 3) {
			if (dodoIsAlive) {
				turnOf = 4;
			} else if (toucanIsAlive) {
				turnOf = 1;
			} else {
				turnOf = 2;
			}
		} else if (turnOf == 4) {
			// This is crucial. Without it, the game would transition from 4 to
			// 5 instead of 4 to 1, and because it could never call the method
			// the game would be over
			if (toucanIsAlive) {
				turnOf = 1;
			} else if (parrotIsAlive) {
				turnOf = 2;
			} else {
				turnOf = 3;
			}
		} else {
			// This is just for grammar purposes, but I added an Easter Egg just
			// for fun. It shouldn't ever be called.
			System.err.println("This program has encountered the following error: the logic operator has failed.");
		}
		turns++;
		System.out.println("\n\nAI playing...");
		// Backlash n FOUR TIMES on next console action
	}

	ImageIcon toucanImage;
	ImageIcon unclaimedImage;
	ImageIcon parrotImage;
	ImageIcon macawImage;
	ImageIcon dodoImage;

	public void loadImages() {
		try {
			Image toucanImg = ImageIO.read(getClass().getResource("Toucan.png"));
			Image parrotImg = ImageIO.read(getClass().getResource("Parrot.png"));
			Image macawImg = ImageIO.read(getClass().getResource("Macaw.png"));
			Image dodoImg = ImageIO.read(getClass().getResource("Dodo.png"));
			Image unclaimedImg = ImageIO.read(getClass().getResource("Unclaimed.png"));
			toucanImage = new ImageIcon(toucanImg);
			unclaimedImage = new ImageIcon(unclaimedImg);
			parrotImage = new ImageIcon(parrotImg);
			macawImage = new ImageIcon(macawImg);
			dodoImage = new ImageIcon(dodoImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateGraphics() {
		for (int i = 0; i < 36; i++) {
			if (status[i] == 0) {
				buttons[i].setIcon(unclaimedImage);
			} else if (status[i] == 1) {
				buttons[i].setIcon(toucanImage);
			} else if (status[i] == 2) {
				buttons[i].setIcon(parrotImage);
			} else if (status[i] == 3) {
				buttons[i].setIcon(unclaimedImage);
			} else if (status[i] == 4) {
				buttons[i].setIcon(unclaimedImage);
			} else {
				// This should never be called.
			}
			buttons[i].repaint();
		}

	}

	public boolean checkIfIsAlive(int team) {
		for (int i = 0; i < 36; i++) {
			if (status[i] == team) {
				return true;
			}
		}
		return false;
	}

	public void updateLife() {
		toucanIsAlive = checkIfIsAlive(1);
		parrotIsAlive = checkIfIsAlive(2);
		macawIsAlive = checkIfIsAlive(3);
		dodoIsAlive = checkIfIsAlive(4);
	}

	public void checkForWinner() {
		if ((toucanIsAlive && !parrotIsAlive && !macawIsAlive && !dodoIsAlive) || oneoutput > 3500) {
			JOptionPane.showMessageDialog(null, "Toucan wins!");
			System.exit(0);
		} else if ((!toucanIsAlive && parrotIsAlive && !macawIsAlive && !dodoIsAlive) || twooutput > 3500) {
			JOptionPane.showMessageDialog(null, "Parrot wins!");
			System.exit(0);
		} else if ((!toucanIsAlive && !parrotIsAlive && macawIsAlive && !dodoIsAlive) || threeoutput > 3500) {
			JOptionPane.showMessageDialog(null, "Macaw wins!");
			System.exit(0);
		} else if ((!toucanIsAlive && !parrotIsAlive && !macawIsAlive && dodoIsAlive) || fouroutput > 3500) {
			JOptionPane.showMessageDialog(null, "Dodo wins!");
			System.exit(0);
		}

	}

	public int[] determineNeighbors(int square) {
		int[] nums;
		if (square != 0 && square != 1 && square != 2 && square != 3 && square != 4 && square != 5 && square != 6
				&& square != 11 && square != 12 && square != 17 && square != 18 && square != 23 && square != 24
				&& square != 29 && square != 30 && square != 31 && square != 32 && square != 33 && square != 34
				&& square != 35) {
			// Internal squares with all corners
			nums = new int[8];
			nums[0] = square - 7;
			nums[1] = square - 6;
			nums[2] = square - 5;
			nums[3] = square--;
			nums[4] = square++;
			nums[5] = square + 5;
			nums[6] = square + 6;
			nums[7] = square + 7;
			// It's symetrical!
			return nums;
		} else {
			// Checking non-internal squares to see if they are corner or line
			if (square != 0 && square != 5 && square != 30 && square != 35) {
				// Line squares, non-internal - now which side
				if (square > 0 && square < 5) {
					nums = new int[5];
					nums[0] = square--;
					nums[1] = square++;
					nums[2] = square + 5;
					nums[3] = square + 6;
					nums[4] = square + 7;
					return nums;
				} else if (square == 11 || square == 17 || square == 23 || square == 29) {
					nums = new int[5];
					nums[0] = square - 7;
					nums[1] = square - 6;
					nums[2] = square--;
					nums[3] = square + 5;
					nums[4] = square + 6;
					// Yeet - also semisymetrical
					return nums;
				} else if (square == 6 || square == 12 || square == 18 || square == 24) {
					nums = new int[5];
					nums[0] = square - 6;
					nums[1] = square - 5;
					nums[2] = square++;
					nums[3] = square + 6;
					nums[4] = square + 7;
					// Yeet - also semisymetrical
					return nums;
				} else {
					nums = new int[5];
					nums[0] = square--;
					nums[1] = square++;
					nums[2] = square - 5;
					nums[3] = square - 6;
					nums[4] = square - 7;
					return nums;
				}
			} else {
				// Corner squares! Yay!
				if (square == 0) {
					nums = new int[3];
					nums[0] = 1;
					nums[1] = 6;
					nums[2] = 7;
					return nums;
				} else if (square == 5) {
					nums = new int[3];
					nums[0] = 4;
					nums[1] = 10;
					nums[2] = 11;
					return nums;
				} else if (square == 30) {
					nums = new int[3];
					nums[0] = 24;
					nums[1] = 25;
					nums[2] = 31;
					return nums;
				} else {
					nums = new int[3];
					nums[0] = 28;
					nums[1] = 29;
					nums[2] = 34;
					return nums;
				}
			}
		}
	}

	// int i < x.length; so on
	public boolean moveIsLegal(int square, int team) {
		// Find Options for attacking - go through for() loop to determine if
		// the move is legal
		// Go backwards! Rework the unit to find all of the neighbors, then take
		// those and work it out
		int[] validMovesForAttack = determineNeighbors(square);
		//yee
		for (int i = 0; i < validMovesForAttack.length; i++) {
			if (status[validMovesForAttack[i]] == team) {
				// Move was legal!!! Attacker came from square that borders it
				return true;
			}
		}
		// The method has been ran through with no success.
		// The move is invalidated
		// Player will check action in the real life
		return false;
	}

	public int getNumberOfSquares(int team) {
		int squaresOfTeam = 0;
		for (int i = 0; i < 36; i++) {
			if (team == 1) {
				if (status[i] == 1) {
					squaresOfTeam++;
				} else {
					// Don't do anything here - the int doesn't need to change.
				}
			} else if (team == 2) {
				if (status[i] == 2) {
					squaresOfTeam++;
				} else {
					// Don't do anything here - the int doesn't need to change.
				}
			} else if (team == 3) {
				if (status[i] == 3) {
					squaresOfTeam++;
				} else {
					// Don't do anything here - the int doesn't need to change.
				}
			} else if (team == 4) {
				if (status[i] == 4) {
					squaresOfTeam++;
				} else {
					// Don't do anything here - the int doesn't need to change.
				}
			} else {
				// Only for grammar
			}
		}
		return squaresOfTeam;
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
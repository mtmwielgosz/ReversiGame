import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSplitPane;

public class GameBoard extends Applet{

	/**
	 * 
	 */
	public static int countWhite = 0;
	public static int countBlack = 0;
	public static int timeWhite = 0;
	public static int timeBlack = 0;
	public static int countMovesWhite = 0;
	public static int countMovesBlack = 0;
	public static boolean stopWhite = false;
	public static boolean stopBlack = false;
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private static int width = 50;
	private JTable columns;
	private JTable rows;
	private static int N = 8;
	public static int[][] boardTable;
	private static JLabel[][] GUIBoardTable;
	public static int player = 2;
	public static boolean stop = false;
	public static boolean onlyAI = true;
	
	public static int deep = 6;									// deepness of recuration
	public static int deep2 = 6;
	public static boolean minimax = false;						// if false - alphabeta
	public static boolean minimax2 = true;
	public static boolean pointsCounts = true; 					// if false - leftMovesCounts
	public static boolean pointsCounts2 = true;
	public static String searchHeuristic = "mostEdges"; 		// choose: mostPoints, mostMovesLeft, mostCorners, mostEdges, mostWaves
	public static String searchHeuristic2 = "mostCorners";
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GameBoard window = null;
				try {
					window = new GameBoard();
					window.frame.setVisible(true);
					
					if(onlyAI)
					{
						refreshElements(4, 5, true, boardTable);
						if(player == 2)
							GUIBoardTable[4][5].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\black.png"));
						else
							GUIBoardTable[4][5].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\white.png"));
						player = player == 1 ? 2 : 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @thcols InterruptedException 
	 */
	public GameBoard() throws InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @thcols InterruptedException 
	 */
	private void initialize() throws InterruptedException {
		
		boardTable = new int[N][N];
		
		for(int i = 0; i < boardTable.length; i++)
			for(int j = 0; j < boardTable[0].length; j++)
			{
				boardTable[i][j] = 0;
			}
		
		boardTable[3][3] = 1;
		boardTable[4][4] = 1;
		boardTable[3][4] = 2;
		boardTable[4][3] = 2;
				
		
		frame = new JFrame();
		frame.setBounds(100, 100, 10 * width, (21 * width) / 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		columns = new JTable();
		columns.setBounds((3 * width) / 2, width/4, 8 * width, width);
		columns.setModel(new DefaultTableModel(
			new Object[][] {
				{"A", "B", "C", "D", "E", "F", "G", "H"},
			},
			new String[] {
				"New column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for(int i = 0; i < columns.getColumnCount(); i++)
		{
			columns.getColumnModel().getColumn(i).setWidth(width);
			columns.getColumnModel().getColumn(i).setMaxWidth(width);
			columns.getColumnModel().getColumn(i).setMinWidth(width);
			columns.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			columns.setFont(new Font("BOLD", 20, 20));
			columns.setRowHeight(width);
			columns.setBorder(new LineBorder(new Color(0, 0, 0)));
			columns.getColumnModel().getColumn(i).setResizable(false);
		}
		frame.getContentPane().add(columns);
		
		rows = new JTable();
		rows.setBounds(width/4, (3 * width) / 2, width, 8 * width);
		rows.setModel(new DefaultTableModel(
			new Object[][] {
				{"1"},
				{"2"},
				{"3"},
				{"4"},
				{"5"},
				{"6"},
				{"7"},
				{"8"},
			},
			new String[] {
				"New rowumn"
			}
		));
		for(int i = 0; i < rows.getColumnCount(); i++)
		{
			rows.getColumnModel().getColumn(i).setWidth(width);
			rows.getColumnModel().getColumn(i).setMaxWidth(width);
			rows.getColumnModel().getColumn(i).setMinWidth(width);
			rows.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			rows.setFont(new Font("BOLD", 20, 20));
			rows.setRowHeight(width);
			rows.setBorder(new LineBorder(new Color(0, 0, 0)));
			rows.getColumnModel().getColumn(i).setResizable(false);
		}
		frame.getContentPane().add(rows);
		
		GUIBoardTable = new JLabel[N][N];
		
		for(int i = 0; i < GUIBoardTable.length; i++)
			for(int j = 0; j < GUIBoardTable[0].length; j++)
			{
				GUIBoardTable[i][j] = new JLabel(i + " " + j);
				GUIBoardTable[i][j].setBounds(( (2*i + 3) * width) / 2, ((2*j + 3) * width) / 2, width, width);
				GUIBoardTable[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				GUIBoardTable[i][j].setVerticalAlignment(SwingConstants.CENTER);
				
				if(!onlyAI)
				{
					GUIBoardTable[i][j].addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {               
		                    	Object o;
		                    	if((o = e.getSource()) instanceof JLabel){
		                            JLabel source = (JLabel) o;
	                            	String[] indexesS = source.getText().split(" ");
	                            	int col = Integer.parseInt(indexesS[0]);
	                            	int row = Integer.parseInt(indexesS[1]);
	                            	if(refreshElements(col, row, true, boardTable))
	                            	{
	                            		if(player == 2)
	                            		{
	                            			boardTable[col][row] = 2;
	                            			source.setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\black.png"));
	                            		}
	                            		
	                            		player = player == 1 ? 2 : 1;
	                            		
	                            		if(player == 1)
											try {
												moveAI();
											} catch (InterruptedException e1) { }
	                            		refresh();
	                            		player = player == 1 ? 2 : 1;
	                            	}
		                    	}	
		                }
					});
				}
				
				if(onlyAI)
				{
					GUIBoardTable[i][j].addMouseListener(new MouseAdapter() {
		                @Override
		                public void mouseClicked(MouseEvent e) {               
	            					
	            					stop = false;
	            					while(!stop)
	            					{
	                                		if(player == 1)
	                                		{
	                                			long before = new Date().getTime();
	            								try {
	            									moveAI();
	            								} catch (InterruptedException e1) {}
	            								long after = new Date().getTime();
	            								timeWhite += after - before;
	                                		}
	                                		
	                                		if(player == 2)
	                                		{
	                                			long before = new Date().getTime();
	            								try {
	            									moveAIBlack();
	            								} catch (InterruptedException e1) {}
	            								long after = new Date().getTime();
	            								timeBlack += after - before;
	                                		}
	                                		
	                                		player = player == 1 ? 2 : 1;
	                                		refresh();
	                                	
	            					}
	            					
	            					int[] counts = count(boardTable);
	            					countWhite = counts[0];
	            					countBlack = counts[1];
	            					
	            					System.out.println("Game Over. \n Whites - moves: " + countMovesWhite + ", time: " + timeWhite + ", scores: " + countWhite);
	            					System.out.println( " Blacks - moves: " + countMovesBlack + ", time: " + timeBlack + ", scores: " + countBlack);
		                    		
		                }
					});
				}
				
				if(boardTable[i][j] == 0)
					GUIBoardTable[i][j].setIcon(null);
				if(boardTable[i][j] == 1)
					GUIBoardTable[i][j].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\white.png"));
				if(boardTable[i][j] == 2)
					GUIBoardTable[i][j].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\black.png"));
				frame.getContentPane().add(GUIBoardTable[i][j]);
			}
	
        
	}
	
	public static void refresh()
	{
		for(int i = 0; i < GUIBoardTable.length; i++)
			for(int j = 0; j < GUIBoardTable[0].length; j++)
			{	
				if(boardTable[i][j] == 0)
					GUIBoardTable[i][j].setIcon(null);
				if(boardTable[i][j] == 1)
				{
					System.out.println("col: " + i + ", row: " + j + "zmieniam na bia³e");
					GUIBoardTable[i][j].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\white.png"));
				}
				if(boardTable[i][j] == 2)
					GUIBoardTable[i][j].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\black.png"));
				frame.getContentPane().add(GUIBoardTable[i][j]);
			}
	}
	
	public static boolean refreshElements(int col, int row, boolean change, int[][] board)
	{
		if(board[col][row] != 0)
			return false;
		
		boolean isAllowed = false;
		int opponent = player == 1 ? 2 : 1;
		
		int[][] copyOfBoard = new int[board.length][board[0].length];
		
		for(int i = 0; i < N; i++)
			for(int j = 0; j < N; j++)
				copyOfBoard[i][j] = new Integer(board[i][j]);
		
		for(int i = col + 2; i < N - 1; i++)
		{
			if(board[i - 1][row] != opponent)
				break;
			
			if(board[i][row] == player)
			{
				for(int j = col + 1; j < i; j++)
				{
					if(board[j][row] == opponent)
					{
						copyOfBoard[j][row] = player;
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = col - 2; i > 0; i--)
		{
			if(board[i + 1][row] != opponent)
				break;
			
			if(board[i][row] == player)
			{
				for(int j = col - 1; j > i; j--)
				{
					if(board[j][row] == opponent) {
						copyOfBoard[j][row] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = row + 2; i < N - 1; i++)
		{
			if(board[col][i - 1] != opponent)
				break;
			
			if(board[col][i] == player)
			{
				for(int j = row + 1; j < i; j++)
				{
					if(board[col][j] == opponent) {
						copyOfBoard[col][j] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = row - 2; i > 0; i--)
		{
			if(board[col][i + 1] != opponent)
				break;
			
			if(board[col][i] == player)
			{
				for(int j = row - 1; j > i; j--)
				{
					if(board[col][j] == opponent) {
						copyOfBoard[col][j] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		// NA UKOS:
		
		for(int i = col + 2, j = row + 2; i < N - 1 && j < N - 1; i++, j++)
		{
			if(board[i - 1][j - 1] != opponent)
				break;
			
			if(board[i][j] == player)
			{
				for(int k = col + 1, l = row + 1; k < i && l < j; k++, l++)
				{
					if(board[k][l] == opponent) {
						copyOfBoard[k][l] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = col + 2, j = row - 2; i < N - 1 && j > 0; i++, j--)
		{
			if(board[i - 1][j + 1] != opponent)
				break;
			
			if(board[i][j] == player)
			{
				for(int k = col + 1, l = row - 1; k < i && l > j; k++, l--)
				{
					if(board[k][l] == opponent) {
						copyOfBoard[k][l] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = col - 2, j = row + 2; i > 0 && j < N - 1; i--, j++)
		{
			if(board[i + 1][j - 1] != opponent)
				break;
			
			if(board[i][j] == player)
			{
				for(int k = col - 1, l = row + 1; k > i && l < j; k--, l++)
				{
					if(board[k][l] == opponent) {
						copyOfBoard[k][l] = player; 	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		for(int i = col - 2, j = row - 2; i > 0 && j > 0; i--, j--)
		{
			if(board[i + 1][j + 1] != opponent)
				break;
			
			if(board[i][j] == player)
			{
				for(int k = col - 1, l = row - 1; k > i && l > j; k--, l--)
				{
					if(board[k][l] == opponent) {
						copyOfBoard[k][l] = player;	
						isAllowed = true;
					}
				}
				break;
			}
		}
		
		if(change)
			for(int i = 0; i < N; i++)
				for(int j = 0; j < N; j++)
					board[i][j] = new Integer(copyOfBoard[i][j]);
		
		return isAllowed; 
		
	}
	
	public static void moveAI() throws InterruptedException
	{
		stopWhite = false;
		ArrayList<Move> moves = new ArrayList<Move>();
		for(int i = 0; i < GameBoard.boardTable.length; i++)
			for(int j = 0; j < GameBoard.boardTable[0].length; j++)
			{
				System.out.println("zliczam kroki");
				if(GameBoard.refreshElements(i, j, false, boardTable))
					moves.add(new Move(i, j));
			}
		
		int[] movesValues = new int[moves.size()];
		if(moves.isEmpty())
		{
			stopWhite = true;
			if(stopBlack)
				stop = true;
		}
		else
		{					
			Move bestMove = moves.get(0);
			int bestIndex = 0;
			

			for(int i = 0; i < movesValues.length; i++)
			{
				int[][] copyOfBoard = new int[boardTable.length][boardTable[0].length];
				
				for(int j = 0; j < boardTable.length; j++)
					for(int k = 0; k < boardTable[0].length; k++)
						copyOfBoard[j][k] = new Integer(boardTable[j][k]);
				
				if(pointsCounts)
				{
					if(minimax)
						movesValues[i] = Algorithms.minimax( moves.get(i).col, moves.get(i).row, true, copyOfBoard, deep, searchHeuristic);
					else
						movesValues[i] = Algorithms.alphabeta( moves.get(i).col, moves.get(i).row, Algorithms.minBest, Algorithms.maxBest, true, copyOfBoard, deep, searchHeuristic);
				}
				else
				{
					movesValues[i] = Algorithms.getMoves(moves.get(i).col, moves.get(i).row, copyOfBoard).size();
				}
				
				if(movesValues[bestIndex] < movesValues[i])
				{
					bestMove = moves.get(i);
					bestIndex = i;
				}
			}

			
	    	if(refreshElements(bestMove.col, bestMove.row, true, boardTable))
	    	{
	    		System.out.println("raz zmienilem");
	    		countMovesWhite ++;
	    		boardTable[bestMove.col][bestMove.row] = 1;
	    		GUIBoardTable[bestMove.col][bestMove.row].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\white.png"));	
	    	}
		}
	}
	
	public static void moveAIBlack() throws InterruptedException
	{
		stopBlack = false;
		ArrayList<Move> moves = new ArrayList<Move>();
		for(int i = 0; i < GameBoard.boardTable.length; i++)
			for(int j = 0; j < GameBoard.boardTable[0].length; j++)
			{
				System.out.println("zliczam kroki black");
				if(GameBoard.refreshElements(i, j, false, boardTable))
					moves.add(new Move(i, j));
			}
		
		int[] movesValues = new int[moves.size()];
		if(moves.isEmpty())
		{
			stopBlack = true;
			if(stopWhite)
				stop = true;
		}
		else
		{	
			Move bestMove = moves.get(0);
			int bestIndex = 0;
			for(int i = 0; i < movesValues.length; i++)
			{
				int[][] copyOfBoard = new int[boardTable.length][boardTable[0].length];
				
				for(int j = 0; j < boardTable.length; j++)
					for(int k = 0; k < boardTable[0].length; k++)
						copyOfBoard[j][k] = new Integer(boardTable[j][k]);
				
				if(pointsCounts2)
				{
					if(minimax2)
						movesValues[i] = Algorithms.minimax( moves.get(i).col, moves.get(i).row, true, copyOfBoard, deep2, searchHeuristic2);
					else
						movesValues[i] = Algorithms.alphabeta( moves.get(i).col, moves.get(i).row, Algorithms.minBest, Algorithms.maxBest, true, copyOfBoard, deep2, searchHeuristic2);
				}
				else
				{
					movesValues[i] = Algorithms.getMoves(moves.get(i).col, moves.get(i).row, copyOfBoard).size();
				}
				
				if(movesValues[bestIndex] < movesValues[i])
				{
					bestMove = moves.get(i);
					bestIndex = i;
				}
			}
			
	    	if(refreshElements(bestMove.col, bestMove.row, true, boardTable))
	    	{
	    		System.out.println("raz zmienilem black");
	    		countMovesBlack ++;
	    		boardTable[bestMove.col][bestMove.row] = 2;
	    		GUIBoardTable[bestMove.col][bestMove.row].setIcon(new ImageIcon("C:\\Users\\mtmwi\\workspace\\ReversiGame\\src\\images\\black.png"));	
	    	}
		}
	}
	
	public static int[] count(int[][] board)
	{
		int whites = 0;
		int blacks = 0;
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[0].length; j++)
			{
				if(board[i][j] == 1)
					whites ++;
				if(board[i][j] == 2)
					blacks ++;
			}
		
		return new int[] {whites, blacks};
	}
}

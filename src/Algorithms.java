import java.util.ArrayList;
import java.util.List;

public class Algorithms {
	
	public static int minBest = -10;
	public static int maxBest = 50;
			
	public static Integer minimax(int col, int row, boolean maxP, int[][] board, int deep, String heuristic) // mostPoints, mostMovesLeft, mostCorners, mostEdges, mostWaves
	{
		ArrayList<Move> moves = getMoves(col, row, board);
		
		if(moves.size() == 0 || deep == 0)
		{
			if(heuristic.equals("mostPoints"))
				return GameBoard.count(board)[GameBoard.player - 1];
			
			if(heuristic.equals("mostMovesLeft"))
				return moves.size();
				
			if(heuristic.equals("mostCorners"))
				return boardValuesCorners[col][row];
			
			if(heuristic.equals("mostEdges"))
				return boardValuesEdges[col][row];
			
			if(heuristic.equals("mostWaves"))
				return boardValuesWaves[col][row];
		}
		
		if(maxP)
		{
			int best = minBest;
			for(Move m : moves)
			{
				int[][] copyOfBoard = new int[board.length][board[0].length];
				
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[0].length; j++)
						copyOfBoard[i][j] = new Integer(board[i][j]);
				
				int val = minimax(m.col, m.row, false, copyOfBoard, deep - 1, heuristic);
				best = Math.max(best, val);
			}
			return best;
		}
		else
		{
			int best = maxBest;
			for(Move m : moves)
			{
				int[][] copyOfBoard = new int[board.length][board[0].length];
				
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[0].length; j++)
						copyOfBoard[i][j] = new Integer(board[i][j]);
				int val = minimax(m.col, m.row, true, copyOfBoard, deep - 1, heuristic);
				best = Math.min(best, val);
			}
			return best;
		}
	}
	
	public static Integer alphabeta(int col, int row, int alpha, int beta, boolean maxP, int[][] board, int deep, String heuristic)
	{
		ArrayList<Move> moves = getMoves(col, row, board);
		
		if(moves.size() == 0 || deep == 0)
		{
			if(heuristic.equals("mostPoints"))
				return GameBoard.count(board)[GameBoard.player - 1];
			
			if(heuristic.equals("mostMovesLeft"))
				return moves.size();
				
			if(heuristic.equals("mostCorners"))
				return boardValuesCorners[col][row];
			
			if(heuristic.equals("mostEdges"))
				return boardValuesEdges[col][row];
			
			if(heuristic.equals("mostWaves"))
				return boardValuesWaves[col][row];
		}
		
		if(maxP)
		{
			int val = minBest;
			for(Move m : moves)
			{
				int[][] copyOfBoard = new int[board.length][board[0].length];
				
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[0].length; j++)
						copyOfBoard[i][j] = new Integer(board[i][j]);
				val = Math.max(val, alphabeta(m.col, m.row, alpha, beta, false, copyOfBoard, deep - 1, heuristic));
				alpha = Math.max(alpha, val);
				if(beta <= alpha)
					break;
			}
			return val;
		}
		else
		{
			int val = maxBest;
			for(Move m : moves)
			{
				int[][] copyOfBoard = new int[board.length][board[0].length];
				
				for(int i = 0; i < board.length; i++)
					for(int j = 0; j < board[0].length; j++)
						copyOfBoard[i][j] = new Integer(board[i][j]);
				val = Math.min(val, alphabeta(m.col, m.row, alpha, beta, true, copyOfBoard, deep - 1, heuristic));
				beta = Math.min(beta, val);
				if(beta <= alpha)
					break;
			}
			return val;
		}
	}
	
	public static ArrayList<Move> getMoves(int col, int row, int[][] board)
	{
		GameBoard.refreshElements(col, row, true, board);
		ArrayList<Move> moves = new ArrayList<Move>();
		for(int i = 0; i < GameBoard.boardTable.length; i++)
			for(int j = 0; j < GameBoard.boardTable[0].length; j++)
			{
				if(GameBoard.refreshElements(i, j, false, board))
					moves.add(new Move(i, j));
			}
		
		return moves;
	}
	
	private static int[][] boardValuesCorners = {
			{50, -1, 5, 2, 2, 5, -1, 50},
			{-1, -10, 1, 1, 1, 1,-10, -1},
			{5 , 1,  1, 1, 1, 1,  1,  5},
			{2 , 1,  1, 0, 0, 1,  1,  2},
			{2 , 1,  1, 0, 0, 1,  1,  2},
			{5 , 1,  1, 1, 1, 1,  1,  5},
			{-1,-10, 1, 1, 1, 1,-10, -1},
			{50, -1, 5, 2, 2, 5, -1, 50}};
	
	private static int[][] boardValuesEdges = {
			{50, 50, 50, 50, 50, 50, 50, 50},
			{50, 10, 10, 10, 10, 10, 10, 50},
			{50, 10,  1, 1, 1, 1,  10, 50},
			{50, 10,  1, 0, 0, 1,  10, 50},
			{50, 10,  1, 0, 0, 1,  10, 50},
			{50, 10,  1, 1, 1, 1,  10, 50},
			{50, 10, 10, 10, 10, 10, 10, 50},
			{50, 50, 50, 50, 50, 50, 50, 50}};
	
	private static int[][] boardValuesWaves = {
			{50, 50, 50, 50, 50, 50, 50, 50},
			{50, -10, -10, -10, -10, -10, -10, 50},
			{50, -10,  10, 10, 10, 10,  -10, 50},
			{50, -10,  10, 0, 0, 10,  -10, 50},
			{50, -10,  10, 0, 0, 10,  -10, 50},
			{50, -10,  10, 10, 10, 10,  -10, 50},
			{50, -10, -10, -10, -10, -10, -10, 50},
			{50, 50, 50, 50, 50, 50, 50, 50}};
}



import java.util.*;
import javax.swing.*;
import java.io.Serializable;

public class MineSweeperModel implements Serializable
{
	private static final int BOMBS_EASY = 10;
	private static final int BOMBS_MED = 25;
	private static final int BOMBS_HARD = 50;
	private static final int SIZE_EASY = 10;
	private static final int SIZE_MED = 15;
	private static final int SIZE_HARD = 20;
	private int bombs;
	public int flagsLeft;
	public int numRowsCols; 				//Always same number of rows and columns
	private Random rng = new Random();
	private boolean gameOver = false;
	
	private int[][] gameGrid;
	private int[][] clicked;
	/*
	Values of gameGrid have the following meaning
	
	0-8: number of bombs in surrounding grid squares
	9: Bomb
	
	Values of clicked have the following meaning
	0: Not clicked
	1: clicked
	2: Flag
	3: Unflagged Bomb (for grid printing purposes)
	*/
	
	
	public MineSweeperModel(int diff)
	{
		newGame(diff);
	}
	
	public void newGame(int diff)
	{
		//Easy
		if(diff == 0)
		{
			bombs = BOMBS_EASY;
			numRowsCols = SIZE_EASY;
		}
		//Medium
		else if(diff == 1)
		{
			bombs = BOMBS_MED;
			numRowsCols = SIZE_MED;
		}
		//Hard
		else if(diff == 2)
		{
			bombs = BOMBS_HARD;
			numRowsCols = SIZE_HARD;
		}
		//Defaults to easy
		else
		{
			bombs = BOMBS_EASY;
			numRowsCols = SIZE_EASY;
		}
		
		gameGrid = new int[numRowsCols][numRowsCols];
		clicked = new int[numRowsCols][numRowsCols];
		flagsLeft = bombs;
		gameOver = false;
		setBombs();
		setNumbers(0,0);
	}
	
	public void setBombs()
	{
		int numBombs = bombs;
		while(numBombs > 0)
		{
			for(int r = 0; r<numRowsCols; r++)
			{
				for(int c = 0; c<numRowsCols; c++)
				{
					if(r == rng.nextInt(100) && c == rng.nextInt(100))
					{
						gameGrid[r][c] = 9;
						clicked[r][c] = 3;
						numBombs--;
					}
				}
			}
		}	
	}
	
	public void setNumbers(int r, int c)
	{
		// If the square is  not a bomb, count the neighboring bombs
		if(gameGrid[r][c] != 9)
		{
			countNeighbors(r,c);
		}
		
		if(r == (numRowsCols - 1) && c == (numRowsCols - 1))
		{
			return;
		}
		else if(c == (numRowsCols - 1))
		{
			setNumbers(r+1,0);
		}
		else
		{
			setNumbers(r,c+1);
		}
	}
	
	public void countNeighbors(int r,int c)
	{
		int bombsNear = 0;
		
		for(int i = r-1; i <= r+1; i++)
		{
			for(int j = c-1; j <= c+1; j++)
			{
				try
				{
					if(gameGrid[i][j] == 9)
						bombsNear++;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					// ArrayIndexOutOfBoundsException is expected here so
					// Do nothing
				}
			}
		}
		
		gameGrid[r][c] = bombsNear;
	}
	
	public String getGridVal(int r, int c)
	{
		if(clicked[r][c] == 1 && gameGrid[r][c]!=9)
		{
			if(gameGrid[r][c] == 0) {
				return " ";
			} else {
				return "" + gameGrid[r][c];
			}
		}
		else if(clicked[r][c] == 2)
		{
			return "F";
		}
		else if(gameGrid[r][c] == 9 && gameOver == true)
		{
			return "*";
		}
		else
		{
			return "?";
		}
	}
	
	public void click(int r,int c)
	{
		if(!gameOver)
		{
			//If the input is out of bounds, print error and return
			try
			{
				//If x and y are out of bounds, or the block is flagged, return
				if(clicked[r][c] == 2)
				{
					return;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Invalid input");
				return;
			}
			
			clicked[r][c] = 1;
			
			//If input is valid, check if it is bomb
			if(gameGrid[r][c] == 9)
			{
				gameOver = true;
				//notice("Game Over");
			}
			
			//If input is valid, and there are not adjacent bombs,
			//then click all neighbors
			if(gameGrid[r][c] == 0)
			{
				clickNeighbors(r,c);
			}
		}
	}
	
	public void clickNeighbors(int r, int c)
	{
		for(int i = r-1; i <= r+1; i++)
		{
			for(int j = c-1; j <= c+1; j++)
			{
				try
				{
					if(clicked[i][j] == 0)
					{
						click(i,j);
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					// ArrayIndexOutOfBoundsException is expected here so
					// Do nothing
				}
			}
		}
	}
	
	public void flag(int r, int c)
	{
		//If flagged, and a bomb, return clicked value to unflagged bomb
		if(clicked[r][c] == 2 && gameGrid[r][c] == 9)
		{
			clicked[r][c] = 3;
			flagsLeft++;
		}
		//If flagged, and not a bomb, set clicked value to unflagged
		else if(clicked[r][c] == 2 && gameGrid[r][c] != 9)
		{
			clicked[r][c] = 0;
			flagsLeft++;
		}
		//Otherwise, flag the block
		else
		{
			clicked[r][c] = 2;
			flagsLeft--;
		}
	}
	
	public boolean checkWin()
	{
		//If all flags are not used, return immediately
		if(flagsLeft != 0)
		{
			return false;
		}
		
		int correctFlags = 0;
		for(int r=0;r<numRowsCols;r++)
		{
			for(int c=0;c<numRowsCols;c++)
			{
				if(clicked[r][c] == 2 && gameGrid[r][c] == 9)
				{
					correctFlags++;
				}
			}
		}
		
		//System.out.println("Correct Flags" + correctFlags);
		if(correctFlags == bombs)
		{
			gameOver = true;
			return true;
		}
		return false;
	}
	
	public boolean checkLose()
	{
		return gameOver;
	}
}
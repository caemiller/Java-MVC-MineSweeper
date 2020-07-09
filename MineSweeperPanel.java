import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MineSweeperPanel extends JPanel
{
	private static final int CELL_SIZE = 30;
	private MineSweeperFrame frame;
	private JLabel[][] gameGrid;
	private int rows, cols;

	
	public MineSweeperPanel(MineSweeperFrame f)
	{		
		frame = f;
		printNewGrid(frame.getModel().numRowsCols);
	}
	
	public void printNewGrid(int n)
	{
		// Remove all components to prevent errors in new game
		removeAll(); 
		
		cols = n; 
		rows = n; 
		MouseHandler mouseListener = new MouseHandler();
		setLayout(new GridLayout(rows, cols));
		gameGrid = new JLabel[rows][cols];
		Dimension cellDim = new Dimension(CELL_SIZE,CELL_SIZE);
		
		for(int r=0;r<rows;r++)
		{
			for(int c=0; c<cols; c++)
			{
				JLabel temp = new JLabel();
				temp.setPreferredSize(cellDim);
				temp.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
				temp.setText(frame.getModel().getGridVal(r,c));
				temp.addMouseListener(mouseListener);
				add(temp);
				gameGrid[r][c] = temp;
				gameGrid[r][c].setVisible(true);
			}
		}
	}
	
	public void updateGrid()
	{
		for(int r=0;r<rows;r++)
		{
			for(int c=0; c<cols; c++)
			{
				gameGrid[r][c].setText(frame.getModel().getGridVal(r,c));
			}
		}
		
		if(frame.getModel().checkLose())
		{
			JOptionPane.showMessageDialog(frame, "You Lose");
		}
		
		if(frame.getModel().checkWin())
		{
			JOptionPane.showMessageDialog(frame, "You Win");
		}
		repaint();
	}
	
	public int[] getCoords(JLabel l)
	{
		int coords[] = new int[2];
		
		for(int r=0;r<rows;r++)
		{
			for(int c=0;c<cols;c++)
			{
				if(l == gameGrid[r][c])
				{
					coords[0] = r;
					coords[1] = c;
					return coords;
				}
			}
		}
		return coords;
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		frame.pack();
	}
	
	private class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent e) 
		{
			int[] coords = getCoords((JLabel)e.getSource());
			
			
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				frame.getModel().click(coords[0],coords[1]);
				updateGrid();
			}
			else if(e.getButton() == MouseEvent.BUTTON3)
			{
				frame.getModel().flag(coords[0],coords[1]);
				frame.updateLabel();
				updateGrid();
			}
		}
	}
}
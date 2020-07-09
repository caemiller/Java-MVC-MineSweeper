import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class MineSweeperFrame extends JFrame
{
	//Frame Vars
	private MineSweeperFrame thisFrame;
	private MineSweeperModel model;
	private MineSweeperPanel panel;
	private int width, height;
	private int difficulty;
	private JLabel bombsLeftLabel;
	
	//Menu Vars
	private JMenuBar menuBar;
	private JMenu gameMenu;
	private JMenuItem newGame, quit, save, load;
	
	//File Chooser
	final JFileChooser fc = new JFileChooser();
	
	
	public MineSweeperFrame()
	{
		//Create model and panel
		model = new MineSweeperModel(difficulty);
		panel = new MineSweeperPanel(this);
		thisFrame = this;
		
		
		//Create Menu Bar
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		//Game Menu
		gameMenu = new JMenu("Game");
		menuBar.add(gameMenu);
		
		newGame = new JMenuItem("New Game");
		save = new JMenuItem("Save");
		load = new JMenuItem("Load");
		quit = new JMenuItem("Quit");
		gameMenu.add(newGame);
		gameMenu.addSeparator();
		gameMenu.add(save);
		gameMenu.add(load);
		gameMenu.addSeparator();
		gameMenu.add(quit);
		
		//Game Menu Listeners
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(thisFrame);
				
				try
				{
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = fc.getSelectedFile();
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
						
						out.writeObject(model);
						
						out.close();
					}
				}
				catch(IOException ex)
				{
					JOptionPane.showMessageDialog(thisFrame, "There was an error writing the file", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		
		load.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				int returnVal = fc.showOpenDialog(thisFrame);
				
				try
				{
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						File file = fc.getSelectedFile();
						ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
						
						model = (MineSweeperModel)in.readObject();
						
						in.close();
						
						panel.printNewGrid(model.numRowsCols);
						panel.updateGrid();
					}
				}
				catch(IOException ex)
				{
					JOptionPane.showMessageDialog(thisFrame, "File does not exist or is inaccessible", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				catch(ClassNotFoundException ex)
				{
					JOptionPane.showMessageDialog(thisFrame, "File is not a valid MineSweeper save file", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//Add content
		Container pane = this.getContentPane();
		bombsLeftLabel = new JLabel("Bombs Left: " + model.flagsLeft);
		pane.add(bombsLeftLabel, BorderLayout.PAGE_START);
		pane.add(panel,BorderLayout.CENTER);
		
		//Build Frame
		setTitle("MineSweeper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	public void newGame()
	{
		Object[] options = {"Easy", "Medium", "Hard"};
		difficulty = JOptionPane.showOptionDialog(this,
			"Pick a difficulty",
			"Difficulty",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			options,
			null);
		model.newGame(difficulty);
		panel.printNewGrid(model.numRowsCols);
		panel.updateGrid();
		updateLabel();
	}
	
	public void updateLabel()
	{
		bombsLeftLabel.setText("Bombs Left: " + model.flagsLeft);
	}

	public MineSweeperModel getModel()
	{
		return model;
	}
}
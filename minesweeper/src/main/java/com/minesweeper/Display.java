package com.minesweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Display{					
	
	public JFrame frame = new JFrame("Finding Dragon Balls");			

	public JButton levelButton = new JButton("   Level   ");	
	public JButton customLevelButton = new JButton("   Custom   "); 
	public JButton playAgainButton = new JButton("   Play Again   ");  	
	public JButton rankingsButton = new JButton("   Rankings  "); 
	public JButton soundTurningButton = new JButton("   Sound   "); 

	public static JLabel textLabel = new JLabel(); 
	public JPanel boardPanel = new JPanel();			
	public JPanel buttonPanel = new JPanel(new FlowLayout());  

	public static ImageIcon flagIcon = new ImageIcon(Display.class.getResource("/resources/image/flag.png"));
	public static ImageIcon bombIcon = new ImageIcon(Display.class.getResource("/resources/image/bomb.png"));
	public static ImageIcon unclickedIcon = new ImageIcon(Display.class.getResource("/resources/image/unclicked.png"));
	public static ImageIcon nullIcon = new ImageIcon(Display.class.getResource("/resources/image/null.png"));

	

	public static ImageIcon[] numberIcons = new ImageIcon[9];	


	public Display(int rows, int cols){

		frame.setSize(cols * MineTile.TILESIZE, rows * MineTile.TILESIZE); 
		frame.setLocationRelativeTo(null); 									
		frame.setResizable(false);				
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setLayout(new BorderLayout());				
		ImageIcon gameIcon = new ImageIcon(Display.class.getResource("/resources/image/gameicon.png")); 
		frame.setIconImage(gameIcon.getImage()); 
		

		for (int i = 1; i <= 8; i++)
            numberIcons[i] = new ImageIcon(Display.class.getResource("/resources/image/" + i + ".png"));	
	


		textLabel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, getTextSize())); 
		textLabel.setHorizontalAlignment(JLabel.CENTER);			

		textLabel.setOpaque(true);		
		textLabel.setBorder(BorderFactory.createCompoundBorder(
        		BorderFactory.createLineBorder(Color.GRAY, 3), 
        		BorderFactory.createEmptyBorder(10, 10, 10, 10) 

		));
		
		
		buttonPanel.add(playAgainButton);
		buttonPanel.add(levelButton);			
		buttonPanel.add(rankingsButton);
		buttonPanel.add(customLevelButton);
		buttonPanel.add(soundTurningButton);


		levelButton.setFocusable(false);	
		levelButton.setBackground(Color.WHITE);		
		levelButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); 

		customLevelButton.setFocusable(false);
		customLevelButton.setBackground(Color.WHITE);
		customLevelButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		playAgainButton.setFocusable(false);
		playAgainButton.setBackground(Color.WHITE);
		playAgainButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		rankingsButton.setFocusable(false);
		rankingsButton.setBackground(Color.WHITE);
		rankingsButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		soundTurningButton.setFocusable(false);
		soundTurningButton.setBackground(Color.WHITE);
		soundTurningButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
		frame.add(buttonPanel, BorderLayout.SOUTH); 
		frame.add(textLabel, BorderLayout.NORTH);	
		frame.add(boardPanel);				
		

		boardPanel.setLayout(new GridLayout(rows, cols)); 

	}

	public int getTextSize(){
		if(Level.getNumCols() < 7)  
			return 15;
		else 
			return 25;
	}
 
	public void visible(boolean n){
		frame.setVisible(n);
	}
	public static void text(String x){
		textLabel.setText(x);
	}

	public void soundTurningButton(){   		
		soundTurningButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				if(Sound.getSoundEnabled() == true)
					Sound.stopSound(3);
				else	
					Sound.startSound(3);
				Sound.toggleSound();
			}
		});
	}


	public void rankingsButton(){				
		rankingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				switch(Level.getLevelInNumber()){						
					case 1:
						JOptionPane.showMessageDialog(frame,ScoreFileHandler.toStringScore("/resources/record/EasyLevelTimeRecords.txt")); 
						break;											
					case 2:
						JOptionPane.showMessageDialog(frame,ScoreFileHandler.toStringScore("/resources/record/MediumLevelTimeRecords.txt"));
						break;
					case 3:
						JOptionPane.showMessageDialog(frame,ScoreFileHandler.toStringScore("/resources/record/HardLevelTimeRecords.txt"));
						break;
					default:
						JOptionPane.showMessageDialog(frame,"No recording of completion times for the custom games! :) ");
				}
			}
		});
	}
													
	public void playAgainButton(){      			
		playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				SwingUtilities.getWindowAncestor(boardPanel).dispose();
						
				new Minesweeper();
			}
		});
	}
												
    public void customLevelButton() {				
        customLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                
                String input2 = JOptionPane.showInputDialog(frame, "Enter length (5 < Length < 25): ");
                String input3 = JOptionPane.showInputDialog(frame, "Enter width (5 < Width < 15): ");
				
                try {
                  
                    
                    int cols = Integer.parseInt(input2);
                    int rows = Integer.parseInt(input3);
					
					int upperBound = cols * rows -30;
					String input1 = JOptionPane.showInputDialog(frame, "Enter number of mines (5 < Mines < "+upperBound+"): ");
					int mines = Integer.parseInt(input1);
                 
					if(cols <= 5 || cols  >= 25 || rows <=5 || rows >= 15 || mines <= 5 || mines >= upperBound){
						JOptionPane.showMessageDialog(frame, "Cannot create a new game with these characteristics. Try again!");
					}
					else{
						Level.setLevel(mines,rows,cols);
						SwingUtilities.getWindowAncestor(boardPanel).dispose();
						

						new Minesweeper();	
					}
					

                } catch (NumberFormatException ex) {
                   
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter integers.");
                }
            }
        });
    }
												
	public void levelButton(){    							
		levelButton.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
				String[] options = {"Easy", "Medium", "Hard"};
        			int choice = JOptionPane.showOptionDialog(frame, 
                			"Please, choose the level:",
               				"Choose Level",
                			JOptionPane.YES_NO_CANCEL_OPTION,
                			JOptionPane.QUESTION_MESSAGE,
                			null,	
                			options,
                			null);
				

        			switch (choice) {
            			case JOptionPane.YES_OPTION:
                			Level.setLevelEasy();
                			break;
            			case JOptionPane.NO_OPTION:
                			Level.setLevelMedium();
                			break;
            			case JOptionPane.CANCEL_OPTION:
                			Level.setLevelHard();
                			break;
           				default:
                			System.out.println("Console: No level selected");
                			return;
                				
        			}

				SwingUtilities.getWindowAncestor(boardPanel).dispose();
				

				new Minesweeper();	
			}
		});	
	}
	


}
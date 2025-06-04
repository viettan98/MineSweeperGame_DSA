package com.minesweeper;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

public class Minesweeper{
	private ArrayList<MineTile> mineList;                                                    			
	private MineTile[][] mainBoard = new MineTile[Level.getNumRows()][Level.getNumCols()];				

	Random random = new Random();																			
	private int tilesClicked = 0;			
	private boolean gameOver = false;									
	Display display = new Display(Level.getNumRows(),Level.getNumCols());			
	public int numOfPlantedFlags;				

	private boolean firstClick;			
	
	Time time = new Time();			

	public Minesweeper(){			
		runGame();	
	}

	private void runGame(){					
		Sound.playSound(3);

		Level.setWinGame(false); 
		setFirstClick(true);				
		
		setNumOfFlags(Level.getMineCount());		

		textUpdate();								

		for(int r = 0; r< Level.getNumRows(); r++)					
			for(int c =0; c< Level.getNumCols(); c++){
				MineTile tile = new MineTile(r,c);					
				mainBoard[r][c] = tile;							
				
				
				tile.setFocusable(false);									
				tile.setMargin(new Insets(0, 0, 0, 0));		
				tile.setIcon(Display.unclickedIcon);								
				
				

				tile.addMouseListener(new MouseAdapter(){					
					@Override
					public void mousePressed (MouseEvent e){								
						if(Level.getWinGame()){									
							return; 						
						}	
						
						if(gameOver)									
							return;
							
						MineTile tile = (MineTile) e.getSource();		
						if(e.getButton() == MouseEvent.BUTTON1){ 
	
							while (!gameOver || !Level.getWinGame()) {              
								
								if (mineList.contains(tile) || (numOfMinesAround(tile.getR(),tile.getC()) > 0
								 && getFirstClick())) { 
																	
									if (getFirstClick()) {			
										
										initializeMines(tile.getR(),tile.getC());			
													
										
									} else {					
										Sound.stopSound(3);
										Sound.playSound(1);

										revealMines();									
										System.out.println("Console: End Game");
										setFirstClick(false);							
										break; 
									}
									
								} else {	
									Sound.playSound(2);
									if(getFirstClick())
										time.receiveSignalA();			
								
									checkMine(tile.getR(),tile.getC());		
									setFirstClick(false);				
									textUpdate();	

									break; 
								}
							}

							
						}else if (e.getButton() == MouseEvent.BUTTON3){  
							
							if(tile.getIcon() == Display.unclickedIcon && tile.isEnabled() && numOfPlantedFlags >0){ 
								
								Sound.playSound(5);
								tile.setIcon(Display.flagIcon);				
								plantingFlag();								

							}else if(tile.getIcon() == Display.flagIcon){		
								Sound.playSound(5);
								tile.setIcon(Display.unclickedIcon);		
								removingFlag();							
							}	
							textUpdate();					
						}
					}
				});		
				display.boardPanel.add(tile);			
			
			}	
		initializeMines(random.nextInt(Level.getNumRows()),random.nextInt(Level.getNumCols())); 

		display.levelButton(); 	
		display.customLevelButton();	
		display.playAgainButton();   
		display.rankingsButton();
		display.soundTurningButton();	
		display.visible(true); 	
	}


	private void initializeMines(int r0, int c0){     
		mineList = new ArrayList<>();  
		mineList.removeAll(mineList);   
		int remains = Level.getMineCount();    
		System.out.println("Console: processing1 (disadvatage_first_click -> renew the board)"); 
		while(remains > 0){     

			int r = random.nextInt(Level.getNumRows());
			int c = random.nextInt(Level.getNumCols());
			
			if( (r== r0-1 && c== c0-1 )|| (r== r0-1 && c== c0 )|| ( r== r0-1 && c== c0+1 )||
				(r== r0 && c== c0-1 )  || (r==r0 && c==c0 )    || (r== r0 && c== c0+1  )  ||
				(r== r0+1 && c== c0-1) || (r==r0+1 && c==c0)   || (r== r0+1 && c==c0+1	)){
					System.out.println("Console: check2 - used to set the first-click on a advantage place");
				}
			else{
				MineTile tile = mainBoard[r][c];    
					
					tile.setEnabled(true);					
					tile.setIcon(Display.unclickedIcon);		

					if(!mineList.contains(tile)){   
						mineList.add(tile);					
						remains--;			
					}
			}
			
		} 
		System.out.println(numOfMinesAround(r0,c0));
	}


	 
	private int countTheMinePosition(int r, int c){
		return( r>=0 && r< Level.getNumRows() && c >=0 && 
		c < Level.getNumCols() && mineList.contains(mainBoard[r][c]) ) ? 1:0;
	}	

	
	private int numOfMinesAround(int r, int c){
		return countTheMinePosition(r - 1, c - 1)+ 
		countTheMinePosition(r - 1, c) + 
		countTheMinePosition(r - 1, c + 1) +
		countTheMinePosition(r, c - 1) + 
		countTheMinePosition(r, c + 1) + 
		countTheMinePosition(r + 1, c - 1) +
		countTheMinePosition(r + 1, c) + 
		countTheMinePosition(r + 1, c + 1);
	}

	
	private void checkMine(int r, int c){
		if(r<0 || r>=Level.getNumRows() || c < 0 || c>= Level.getNumCols() || !mainBoard[r][c].isEnabled())
			return;		
		MineTile tile = mainBoard[r][c]; 
		if (tile.getIcon() == Display.flagIcon)						
        		return;
		tile.setEnabled(false);   
		tilesClicked++;				
		int minePositions = numOfMinesAround(r,c); 
		if(minePositions > 0){ 
			tile.setIcon(Display.numberIcons[minePositions]); 
			tile.setDisabledIcon(Display.numberIcons[minePositions]); 
		}else{
			tile.setIcon(Display.nullIcon); 
			tile.setDisabledIcon(Display.nullIcon); 
			checkMine(r - 1, c - 1); 
			checkMine(r - 1, c);		
			checkMine(r - 1, c + 1);		
			checkMine(r, c - 1);	
			checkMine(r, c + 1);		
			checkMine(r + 1, c - 1);
			checkMine(r + 1, c);		
			checkMine(r + 1, c + 1);		
		}
		if (tilesClicked == Level.getNumRows() * Level.getNumCols() - mineList.size()){ 
			Sound.stopSound(3);
			Sound.playSound(4);
			Level.setWinGame(true); 		
			time.receiveSignalB();			
			long newTime = time.getTimeDifferenceInSeconds();
			System.out.println("Time: "+newTime+" seconds");	
			ScoreFileHandler.saveScore(newTime);
			Display.text("Mines Cleared!, You Won."); 
			JOptionPane.showMessageDialog(display.frame, 
			"Congratulation! You won.\nTime: "+newTime+" seconds."); 
		}
	}


	private void revealMines(){ 
		for(MineTile tile: mineList)
			tile.setIcon(Display.bombIcon);
		
		gameOver = true;
		Display.text("Game Over!, You Lose.");
		JOptionPane.showMessageDialog(display.frame, "Oops! You lose"); 
	}

	private void textUpdate(){																				
		Display.text("Mines: "+Level.getMineCount()+" | Remaining Flags: "+ 
		numOfPlantedFlags);
	}



	
	public void setTilesClicked(int n){
		this.tilesClicked = n;	
	}
	public void setFirstClick(boolean n){
			this.firstClick = n;
	}
	public boolean getFirstClick(){
			return this.firstClick;
	}
	public void plantingFlag(){
		numOfPlantedFlags--;
	}
	public void removingFlag(){
		numOfPlantedFlags++;
	}
	public void setNumOfFlags(int n){
		this.numOfPlantedFlags = n;
	}
	
}
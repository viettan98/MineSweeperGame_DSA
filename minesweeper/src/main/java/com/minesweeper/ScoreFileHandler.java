package com.minesweeper;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.Collections;;

public class ScoreFileHandler {                             

    public static void saveScoreToFile(String fileName, long score) {  
        try (FileWriter fileWriter = new FileWriter(fileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            bufferedWriter.write(Long.toString(score));
            bufferedWriter.newLine();
            System.out.println("Score saved to file "+fileName+": " + score +" seconds");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Long> readScoreFromFile(String fileName) {       
        List<Long> scoreValues = new ArrayList<>();

        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    try {
                        long scoreValue = Long.parseLong(line);
                        scoreValues.add(scoreValue);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line as a long: " + line);
                        
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoreValues;
    }

    public static void saveScore(long score) {      
        int levelNumber = Level.getLevelInNumber();     
        String fileName;
    
        switch (levelNumber) {              
            case 1:
                fileName = "/resources/record/EasyLevelTimeRecords.txt";
                break;
            case 2:
                fileName = "/resources/record/MediumLevelTimeRecords.txt";
                break;
            case 3:
                fileName = "/resources/record/HardLevelTimeRecords.txt";
                break;
            default:
                return;
        }
    
        saveScoreToFile(fileName, score);       
    }

    public static String toStringScore(String fileName) {                       
        List<Long> list = readScoreFromFile(fileName); 
        Collections.sort(list);                 
            
        String listScore = "Completion time rankings (10 places) in this level:         \n";
        int count = 10;     

        for(Long score : list){
            if(count == 0)
                break;
            listScore += score;
            listScore += "s.\n";
            count--;            
        }

        if(count != 0){     
            for(int i = count; i>= 0 ; i--)
                listScore += "Null.\n";        
        }
        return listScore;
        
    }
}

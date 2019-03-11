/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication20;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Kangwenn
 */
public class JavaApplication20 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        //C:\Users\Kangwenn\Desktop\Trip.csv
        LinkedList trip = readCSV("/Users/Kangwenn/Desktop/Trip.csv");

        Double totalDistance = 0.0;
        long diffTotal = 0L;
        int totalStop=0;
        int numOfStops = 0;
        
        //Loop through every line of csv file
        for (int i = 2; i < trip.size(); i++) {
            //Get first and second line
            String[] line = (String[]) trip.get(i);
            String[] prevLine = (String[]) trip.get(i - 1);
            //Get coordinates for both line
            Double lat = Double.parseDouble(line[1]);
            Double prevLat = Double.parseDouble(prevLine[1]);
            Double longi = Double.parseDouble(line[2]);
            Double prevLongi = Double.parseDouble(prevLine[2]);
            //Calculate and sums up distance
            Double distance = calculateDistance(lat, prevLat) + calculateDistance(longi, prevLongi);
            totalDistance += distance;
            //Check whether stops for traffic
            String timeStart = "";
            String timeEnd = "";
            if(distance ==0){
                timeStart = line[0];
                numOfStops++;
            }else{
                if(numOfStops>0){
                    timeEnd = prevLine[0];
                    numOfStops =0;
                    //System.out.println("time start: "+ timeStart + "time end: "+ timeEnd);
                    totalStop++;
                }
                diffTotal += calculateTime(prevLine[0],line[0]);
            }
            
        }
        //Display all the details
        System.out.println("Total distance traveled: " + totalDistance + " m");
        System.out.println("Time taken: " + diffTotal + " seconds");
        System.out.println("Average Driving Speed: " + (totalDistance/diffTotal)*20/5 + " km/hr");
        System.out.println("number of stops: "+totalStop);
    }
    
    public static long calculateTime(String first, String second) throws ParseException{
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(first);
            Date date2 = format.parse(second);
            long diff = date2.getTime() - date1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            //System.out.print(diffDays + " days, ");
            //System.out.print(diffHours + " hours, ");
            //System.out.print(diffMinutes + " minutes, ");
            //System.out.println(diffSeconds + " seconds.");
            return diffSeconds;
    }

    public static Double calculateDistance(Double first, Double second) {
        Double difference = 0.0;
        if (first > second) {
            difference = first - second;
        } else {
            difference = second - first;
        }
        return difference / 0.00001 * 1.11;
    }

    public static LinkedList readCSV(String path) {
        LinkedList<String[]> queue = new LinkedList<>();
        String[] lineFile = {};
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                lineFile = line.split(cvsSplitBy);
                queue.add(lineFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return queue;
    }

}

import java.util.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

class Airport
{
    String airline, destination;
    int passengers_number;
    public Airport() {} //The constructor with no elements
    public Airport(String airline, String destination, int passengers_number) //The conctructor with 3 elements
    {
        this.airline = airline;
        this.destination = destination;
        this.passengers_number = passengers_number;
    }
    //flights_count() - A function that returns the number of flights there are to "Frankfurt"
    public static int flights_count(ArrayList<Airport> list, String destination) 
    {
        if (list.isEmpty()) return 0;
        int sum = 0;
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).destination.equals(destination)) sum += 1;
        return sum;
    }
    //max_passengers() - A function that returns an object of Airport class whose number of passengers is the highest
    public static Airport max_passengers(ArrayList<Airport> list) 
    {
        if (list.isEmpty()) return null;
        int max_number = 0;
        Airport max_airport = new Airport();
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).passengers_number > max_number)
            {
                max_number = list.get(i).passengers_number;
                max_airport = list.get(i);
            }
        }
        return max_airport;
    }
    //less_than100() - A function that returns the first to be found object of Airport class whose passengers number is less than 100
    public static Airport less_than100(ArrayList<Airport> list) 
    {
        if (list.isEmpty()) return null;
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).passengers_number < 100) return list.get(i);
        return null;
    }
    //total_number() - A function that returns a map containing the name of the airline that has the biggest total number of passengers and the total number itself
    public static Map<String, Integer> total_number(ArrayList<Airport> list)
    {
        if (list.isEmpty()) return null;
        Map<String, Integer> airlines = new HashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++)
        {
            if (!airlines.containsKey(list.get(i).airline)) airlines.put(list.get(i).airline, list.get(i).passengers_number);
            else
            {
                int pass_n = airlines.get(list.get(i).airline) + list.get(i).passengers_number;
                airlines.replace(list.get(i).airline, pass_n);
            }
        }
        int max = 0; String airline = "";
        for( String key : airlines.keySet())
        {
            if (airlines.get(key) > max)
            {
                airline = key;
                max = airlines.get(key);
            }
        }
        airlines = Map.of(airline, max);
        return airlines;
    }
    public static void main(String[] args)
    {
        ArrayList<Airport> airports = new ArrayList<>();
        //Reading input.txt file
        try (FileReader fr = new FileReader(new File("input.txt"));
            BufferedReader br = new BufferedReader(fr)) 
        {
            String line;
            while ((line = br.readLine())!=null) {
                String airline = ""; String destination = ""; String str_passengers_number = "";
                String temp = ""; int tp = 0;
                line = line.replaceAll("\\s+",""); //Removing all whitespaces
                int x1 = 0; int x2 = 0;
                for (int i=0; i < line.length(  ); i++)
                {
                    if (Character.isUpperCase(line.charAt(i))) tp += 1; //Calculating the number of capital letters
                    if ((Character.isUpperCase(line.charAt(i)))&&(x1 == 0))
                    {
                        airline += line.charAt(i); //Collecting capital letter to airline name
                        x1 = 1;
                        continue;
                    }
                    if ((!Character.isUpperCase(line.charAt(i)))&&(x1 == 1)&&(x2 == 0)) airline += line.charAt(i); //Collecting small letter to airline name
                    if ((Character.isUpperCase(line.charAt(i)))&&(x1 == 1)&&(x2 == 0))
                    {
                        destination += line.charAt(i); //Collecting capital letter destination name
                        x2 = 1;
                        continue;
                    }
                    if ((!Character.isDigit(line.charAt(i)))&&(x1 == 1)&&(x2 == 1)) destination += line.charAt(i); //Collecting small letter to destination name
                    if (Character.isDigit(line.charAt(i))) str_passengers_number += line.charAt(i); //Collecting the number of passengers
                }
                tp -= 1;
                if (tp > 1) //If destination has more than 1 capital letter, it means we have a part of airline name in the destination string
                {
                    int tmp = 0;
                    for (int j = 0; j < destination.length(); j++)
                    {
                        if (Character.isUpperCase(destination.charAt(j))&&(tmp == tp - 1)) break;
                        if ((Character.isUpperCase(destination.charAt(j)))&&(tmp < tp - 1))
                        {
                            temp += destination.charAt(j);
                            tmp += 1;
                        }
                        else if (tmp < tp) temp += destination.charAt(j);
                    }
                    airline += temp; //Adding missing part to airline name
                    destination = destination.replace(temp, ""); //Removing the wrong part fron destination name
                }
                airports.add(new Airport(airline, destination, Integer.parseInt(str_passengers_number))); //Creating a new Airport class object and pushing it to arraylist
            }
            
        } catch (FileNotFoundException e) {} 
        catch (IOException e) {}
        System.out.println(Airport.flights_count(airports, "Frankfurt")); //Ex.1: printing how many flights there are to "Frankfurt"

        Airport obj = Airport.max_passengers(airports); //Ex.2: the flight that has the most passengers
        if (obj == null) System.out.println("The file is empty!");
        else System.out.println(obj.airline + " " + obj.destination + " " + obj.passengers_number); //Printing the result of ex.2

        obj = Airport.less_than100(airports); //Ex.3: the first flight with passengers less than 100
        if (obj == null) System.out.println("There is no flight with passengers less than 100.");
        else System.out.println(obj.airline + " " + obj.destination + " " + obj.passengers_number); //Printing the result of ex.3

        Map<String, Integer> newMap = Airport.total_number(airports); //Ex.4: the airline with the most total number of passengers and the number itself
        if (newMap == null) System.out.println("The file is empty!");
        else if (newMap.keySet() == null) System.out.println("The file is empty!");
        else
            for(String key : newMap.keySet())
                System.out.println(key + " " + newMap.get(key)); //Printing the result of ex.4
    }
}
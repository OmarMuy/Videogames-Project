/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omarmuy.videogames;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Omar Muy
 * Main class will run the program all together. 
 */
public class Main {

    // Make variables to access throughout program
    private static Gson gson;
    private static GsonBuilder builder = new GsonBuilder();
    private static List<VideoGame> v = new ArrayList<>();
    private static String fileName;
    private static String databaseURL = "";
    private static Connection connect = null;
    private static VideoGame vg = new VideoGame("", 0.0, "");
    // Will have to make methods to do each case. 

    /**
     * Main method, runs the program and calls the methods pertaining to the case. 
     * @param args main method start
     */
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int userChoice = 0;
        int userInput = 0; // counter
        //Loop for simultaenous display after each command
        while (userInput <= 10) {
            // Time to print out the UI 
            System.out.println("\nVideo Game UI \n"
                    + "1 - Import DB from JSON\n"
                    + "2 - Export DB to JSON\n"
                    + "3 - Add new video game\n"
                    + "4 - Show all video games\n"
                    + "5 - Show all video games higher than a given price (user enters price)\n"
                    + "6 - Show video game with a given title\n"
                    + "7 - Show average video game price\n"
                    + "8 - Delete video game by title\n"
                    + "9 - Delete all video games from DB\n"
                    + "10 - Exit\n"
                    + "Enter Choice");
            // User choice will be placed into userChoice <-- int
            userChoice = s.nextInt();
            switch (userChoice) {
                // Import FROM JSON
                //DONE***********
                case 1 -> {
                    // Importing from Json should replace collection and DB
                    v.clear();
                    MakeConnection();
                    //ClearVGFromDB();
                    // Don't clear from DB we must APPEND the json file ONTOP of the DB. 
                    ImportFromJSON();
                    AddToTheDB();
                    printVideoGames();
                    userInput = 1;
                }
                // Export TO JSON.
                //DONE************
                case 2 -> {
                    //DATABASE TO JSON
                    //Clear whatever is in collection so there won't be duplicates from DB when we call ExportToAL();
                    MakeConnection();
                    v.clear();
                    ExportToAL();
                    ExportToJSON();
                    userInput = 2;
                }
                // Add a new video game and store it into variables
                //DONE*********
                case 3 -> {
                    String t3;
                    double d3;
                    String r3;
                    Scanner s3 = new Scanner(System.in);
                    Scanner s32 = new Scanner(System.in);

                    System.out.println("Please enter a Title: ");
                    t3 = s3.nextLine();

                    System.out.println("Please enter the price: ");
                    d3 = s3.nextDouble();

                    System.out.println("Enter the ESRB: ");
                    r3 = s32.nextLine();

                    MakeConnection();
                    AddToCollectionAndDB(t3, d3, r3);
                    userInput = 3;
                }
                // Show all video games PRINT NEATLY (padding on printf)
                //DONE************
                case 4 -> {
                    MakeConnection();
                    System.out.println("Printing out video games in the database");
                    printVideoGames();
                    userInput = 4;
                }
                // Show all video games higher than given price USE STREAMS THIS IS A QUERY. STREAM FOR FILTERING AND PRINTING.
                //DONE*************** 
                case 5 -> {
                    // DB HAS TO RETURN ALL RESULTS. 
                    v.clear();
                    MakeConnection();
                    PrintVGHigher();
                    userInput = 5;
                }
                //Show videogame with a given title USE STREAMS
                case 6 -> {
                    v.clear();
                    MakeConnection();
                    //ExportToAL();
                    FindVideoGame();

                    userInput = 6;
                }
                // SHOW AVG GAME PRICE USE STREAMS ONCE AGAIN
                // DONE.***********
                case 7 -> {
                    v.clear();
                    MakeConnection();
                    ExportToAL();
                    getAverageOfGames();
                    userInput = 7;
                }
                // DELETE VIDEO GAME BY TITLE ASK USER INPUT THEN COMPARE AND DELETE
                // DONE.*************
                case 8 -> {
                    Scanner sclear = new Scanner(System.in);
                    String ans;
                    System.out.println("Please enter the VG title to delete.");
                    ans = sclear.nextLine(); // <-- contains the video game title

                    MakeConnection();
                    DeleteVGByTitle(ans);
                    printVideoGames();
                    userInput = 8;
                }
                //CLEAR ALL VIDEOGAMES FROM DB
                // Call the connection then call the delete method
                //DONE************
                case 9 -> {
                    MakeConnection();
                    ClearVGFromDB();
                    System.out.println("Video games have been cleared from the database");
                    userInput = 9;
                }
                // Case 10 Exits
                //DONE**********
                case 10 -> {
                    System.out.println("Program successfully terminated");
                    userInput = 10;
                    System.exit(0);
                }
                // Exception Handling
                default -> {
                    System.out.println("Please input a valid number between 1-10, Thank you.");
                    userInput = 0;
                }
            }
        }
    }

    /**
     * Method that makes a connection to JDBC so we don't always have to type it
     * when we use it.
     */
    public static void MakeConnection() {
        try {
            databaseURL = "jdbc:ucanaccess://.//Videogames.accdb";
            connect = DriverManager.getConnection(databaseURL);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that clears all video games from our database, returns the rows that were deleted. 
     */
    public static void ClearVGFromDB() {
        try {
            String sql = "DELETE FROM Videogame"; //<-- TABLE NAME in DB // HAS TO BE TABLE NAME NOT DB NAME
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            int deleteRows = preparedStatement.executeUpdate();
            if (deleteRows > 0) {
                System.out.printf("Deleted row: %d\n", deleteRows);
            }
        } catch (SQLException e) {

        }

    }

    // Case 8 

    /**
     * Method that will ask for user Input, Query the database and delete titles relating to the user Input from the Database.
     * @param userInput to delete by title
     */
    public static void DeleteVGByTitle(String userInput) {

        try {
            String sql = "DELETE FROM Videogame WHERE Title=" + "'" + userInput + "'"; //<-- TABLE NAME in DB // HAS TO BE TABLE NAME NOT DB NAME
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            int deleteRows = preparedStatement.executeUpdate();
            if (deleteRows > 0) {
                System.out.printf("Deleted row: %d\n", deleteRows);
            }
        } catch (SQLException e) {
            System.out.println("Game not found.");
        }
    }

    /**
     * Method that takes user input and places it into a collection and
     * database.
     *
     * @param title User input title.
     * @param price User input price (DOUBLE value).
     * @param esrb User input esrb rating.
     */
    public static void AddToCollectionAndDB(String title, double price, String esrb) {
        // Make a new object of videogame and add it to the database. 
        //Convert user string to a collection and place it into the DB through string
        vg.setTitle(title);
        vg.setPrice(price);
        vg.setEsrb(esrb);
        v.add(vg);       //<-- THIS ADDS IT TO THE COLLECTION
        //System.out.println("VideoGame has been saved to the collection"); // <-- use as tracker/reference

        try {
            String sql = "INSERT INTO Videogame (Title, Price, ESRB) VALUES (?, ?, ?)"; //<-- column name in DB // id column auto generated 
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, vg.getTitle());
            preparedStatement.setDouble(2, vg.getPrice());
            preparedStatement.setString(3, vg.getEsrb());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("Row inserted");
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Method will print all video games, an instance of the database will be saved into the a collection, collection will be returned. 
     */
    public static void printVideoGames() {
        // THIS HAS TO PRINT FROM THE DATABASE.
        try {
            String tableName = "Videogame";
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            // -22 adds padding into it, very good to make it nice and displayable.
            ResultSetMetaData res = result.getMetaData();
            //String c1 = res.getColumnName(1);
            String c2 = res.getColumnName(2);
            String c3 = res.getColumnName(3);
            String c4 = res.getColumnName(4);
            System.out.printf("%24s %24s %24s\n", c2, c3, c4);
            while (result.next()) {

                //int num = result.getInt("ID");
                String title = result.getString("Title");
                double price = result.getDouble("Price");
                String esrb = result.getString("ESRB");
                // -22 adds padding into it, very good to make it nice and displayable.
                System.out.printf("%24s %24.2f %24s\n", title, price, esrb); // <- print formatting
            }
        } catch (SQLException except) {
            except.printStackTrace();
        }
    }

    /**
     * Method that imports from a user input JSON file. 
     */
    public static void ImportFromJSON() {
        Scanner s1 = new Scanner(System.in);
        gson = builder.create();
        System.out.println("Enter your JSON file which you'd like to import from ");
        try {
            // Read the next line
            fileName = s1.nextLine();
            FileReader fr = new FileReader(fileName);
            //VideoGame vg = gson.fromJson(fr, VideoGame.class);
            v = gson.fromJson(fr, new TypeToken<ArrayList<VideoGame>>() {
            }.getType());

            System.out.println("Success!");
            //System.out.println(v);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * Adds an instance of what's in the collection into the database, good for adding a new game (case 3).
     */
    public static void AddToTheDB() {
        try {
            String sql = "INSERT INTO Videogame (Title, Price, ESRB) VALUES (?, ?, ?)"; //<-- column name in DB // id column auto generated 
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            for (VideoGame vg : v) {
                preparedStatement.setString(1, vg.getTitle());
                preparedStatement.setDouble(2, vg.getPrice());
                preparedStatement.setString(3, vg.getEsrb());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException except) {

        }

    }

    /**
     * This method queries the database and inputs all relevant data into a collection. 
     */
    public static void ExportToAL() {

        try {
            String tableName = "Videogame";
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            // connect and get from the table. and set values of videogame to it.
            while (result.next()) {
                VideoGame vg = new VideoGame("", 0.0, "");
                vg.setTitle(result.getString("Title"));
                vg.setPrice(result.getDouble("Price"));
                vg.setEsrb(result.getString("ESRB"));
                v.add(vg);
            }
            //System.out.println("Instance of DB saved in V");
            //System.out.println(v); // <-- prints out collection
        } catch (SQLException except) {

        }
    }

    // Take ArrayList and export it into JSON FORMAT

    /**
     * Method that exports what's in the collection into a JSON file (user named file).
     */
    public static void ExportToJSON() {
        Scanner expS = new Scanner(System.in);
        String jAns;
        gson = builder.create(); //<-- makes the builder, gson won't work without it. 
        System.out.println("Please enter a file name, don't forget the .json at the end");
        jAns = expS.nextLine();
        // getting the collection v into json formatting to go into json file
        String jsonString = gson.toJson(v);
        try {
            PrintStream ps = new PrintStream(jAns);
            ps.printf("%s\n", jsonString);

            ps.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method that queries the database, returns rows that are placed into a collection which then a stream is used to find the higher price, then prints it out. 
     */
    public static void PrintVGHigher() {
        List<VideoGame> tempAG = new ArrayList<>();
        Scanner p1 = new Scanner(System.in);
        int userP;
        System.out.println("Enter a price");
        userP = p1.nextInt(); // <-- userP has the userInput price
        try {
            String tableName = "Videogame";
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            // connect and get from the table. and set values of videogame to it.
            while (result.next()) {
                VideoGame temp = new VideoGame("", 0.0, "");
                temp.setTitle(result.getString("Title"));
                temp.setPrice(result.getDouble("Price"));
                temp.setEsrb(result.getString("ESRB"));
                tempAG.add(temp);
                //System.out.println(tempAG);
            }
        } catch (SQLException except) {

        }
        tempAG.stream().filter(game -> game.getPrice() > userP).forEach(System.out::println); // <-- stream used. 
    }

    /**
     * This method gets an average of the games within the database. 
     */
    public static void getAverageOfGames() {
        // MUST USE A STREAM FOR THIS ANSWER !!!
        double res = v.stream().mapToDouble(x -> x.getPrice()).average().getAsDouble();
        System.out.printf("Average Price: " + "%.2f", res);
    }

    /**
     * This method queries the database for a user input video game title and returns any instance of it. 
     */
    public static void FindVideoGame() {
        List<VideoGame> anTemp = new ArrayList<>();
        Scanner sFVG = new Scanner(System.in);
        System.out.println("Enter a video game title");
        String userAns = sFVG.nextLine();
        try {
            String tableName = "Videogame";
            Statement stmt = connect.createStatement();
            ResultSet result = stmt.executeQuery("select * from " + tableName);
            // connect and get from the table. and set values of videogame to it.
            while (result.next()) {
                VideoGame temp = new VideoGame("", 0.0, "");
                temp.setTitle(result.getString("Title"));
                temp.setPrice(result.getDouble("Price"));
                temp.setEsrb(result.getString("ESRB"));
                anTemp.add(temp);
                //System.out.println(tempAG);
            }
        } catch (SQLException except) {

        }
        anTemp.stream().filter(game -> game.getTitle().equals(userAns)).forEach(System.out::println);
    }
}

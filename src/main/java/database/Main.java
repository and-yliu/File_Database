package database;
import java.sql.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static DatabaseHandler handler;

    /**
     * To add a file into a table
     * @param name Name of the file; requires to be in String
     * @param path Path of the file; requires to be in String
     * @param extension Extension of the file; requires to be in String
     * @param size Size of the file in bytes; requires to be in String
     */
    public static void addFile(String name, String path, String extension, String size){ // Add files infos from given folder through its absolute path
        String qu = "INSERT INTO "+ DatabaseHandler.tableName +" (name, path, extension, size) VALUES (" +
                "'" + name + "'," +
                "'" + path + "'," +
                "'" + extension + "'," +
                "'" + size + "')";
        handler.execAction(qu);
    }

    /**
     * To retrieve all the files from a table
     * @param table the name of the table; requires to be in Strings
     */
    public static void retrieveInfoFile(String table) {
        String qu = "SELECT * FROM " + table;
        ResultSet resultSet = handler.execQuery(qu);
        try{
            while (resultSet.next()){
                String name = resultSet.getString("NAME");
                String path = resultSet.getString("PATH");
                String extension = resultSet.getString("EXTENSION");
                String fileSize = resultSet.getString("SIZE");
                System.out.println("File Name: " + name + "\tPath: " + path + "\tExtension: " + extension + "\tFile Size: " + fileSize);
            }
        } catch (SQLException e) {
            System.out.println("There is no such table.");
            e.printStackTrace();
        }
    }

    /**
     * Input a folder of files into the database
     * @param folder the folder of files; requires to be a file
     */
    public static void readFile(File folder){
        for(File file: folder.listFiles()){
            if(!file.isDirectory()){
                String name = file.getName();
                String path = file.getPath();
                String extension = "";
                int index = name.lastIndexOf('.');
                if (index >= 0) {
                    extension = name.substring(index+1);
                }
                long size = file.length();
                addFile(name, path, extension, String.valueOf(size));
                //System.out.println("File name = " + name + "\nFile path = " + path + "\nFile extension = " + extension + "\nFile size = " + size);
            }else{
                readFile(file);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Option for user to either input the absolute path of the folder and create table or to retrieve files from a table or to see all the tables.
        System.out.println("Enter 1 to create a Table. Enter 2 to retrieve files. Enter 3 to see all the tables");
        String input = scanner.nextLine();
        if (input.equals("1")){
            System.out.println("Enter the absolute path of the folder: ");
            String path = scanner.nextLine();
            File folder = new File(path);
            // Set up handler through folder name
            DatabaseHandler.tableName = folder.getName().toUpperCase();
            handler = DatabaseHandler.getHandler();
            readFile(folder);
            System.out.println("Done!");
        }
        else if(input.equals("2")){
            System.out.println("Enter your folder name to retrieve your information: ");
            DatabaseHandler.tableName = scanner.nextLine().toUpperCase();
            handler = DatabaseHandler.getHandler();
            retrieveInfoFile(DatabaseHandler.tableName);
        }
        else if (input.equals("3")){
            // Set up a null handler since no folder name is given
            handler = DatabaseHandler.getHandler();
            try {
                handler.showTable();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Invalid Input!");
        }
    }
}
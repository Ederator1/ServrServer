package com.company;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/** December 13, 2017
 *  Eddy Yao
 *
 *      Displays currently logged in users information
 */

public class ServerAPI {

    private static Statement st;
    private static ResultSet rs;

    private static final String getAllUsers = "SELECT * FROM Users;";
    private static final String getAllServices = "SELECT * FROM Services;";

    private static final String insertUser = "INSERT INTO Users (Username, PIN, Email, Phone, City, Country)" +
            "VALUES(";
    private static final String deleteUser = "DELETE FROM Users WHERE UserID = ";
    private static final String getUser = "SELECT * FROM Users WHERE UserID = ";
    private static final String lastUser = "SELECT MAX(UserID) FROM Users;";

    private static final String resetIncrementUsers = "ALTER TABLE Users AUTO_INCREMENT = ";
    private static final String resetIncrementServices = "ALTER TABLE Services AUTO_INCREMENT = ";
    private static final String checkCredential = "SELECT * FROM Users WHERE Email = '";

    private static final String flushServices = "DELETE FROM Services;";
    private static final String addService = "INSERT INTO Services (UserID, ServiceName, ServiceDescription, Price) " +
            "VALUES(";
    private static final String deleteService = "DELETE FROM Services WHERE ServiceID = ";
    private static final String getUserServices = "SELECT * FROM Services WHERE UserID = ";

    private static final String searchServices = "SELECT * FROM SERVICES WHERE ServiceName LIKE ";
    private static final String getStreamServices = "SELECT * FROM Services ORDER BY ServiceID DESC LIMIT 10;";

    private static String query;
    private static ArrayList<ArrayList<String>> serviceQuery;
    private static ArrayList<String> keywordQuery;

    private static ArrayList<String> createUser(ResultSet result){
        try {
            return new ArrayList<>(Arrays.asList(Integer.toString(result.getInt("UserID")),
                    result.getString("Username"), result.getString("PIN"), result.getString("Email"),
                    result.getString("Phone"), result.getString("city"), result.getString("Country")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> createService(ResultSet result){
        try {
            return new ArrayList<>(Arrays.asList(Integer.toString(result.getInt("ServiceID")),
                    Integer.toString(result.getInt("UserID")), result.getString("ServiceName"),
                    result.getString("ServiceDescription"), result.getString("Price")));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void getUsers(){
        query = getAllUsers;
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                System.out.println(createUser(rs));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getServices(){
        query = getAllServices;
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                System.out.println(createService(rs));
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(ArrayList<String> user){
        query = insertUser + "'" + user.get(0) + "'";
        for (int i = 1; i < user.size(); i++) {
            query += ", '" + user.get(i) + "'";
        }
        query += ");";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully added " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getLastUserID(){
        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(lastUser);
            rs.next();
            return rs.getInt("MAX(UserID)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteUser(int id){
        query = deleteUser + id + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully deleted User # " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getUser(int id){
        query = getUser + id + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            if (rs.next()){
                return createUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setAutoIncrementUsers(int increment){
        query = resetIncrementUsers + increment + ";";

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully reset auto-increment to " + increment);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> isValidCredential(String credential){
        query = checkCredential + credential.split(":")[0] + "' AND PIN = " + credential.split(":")[1];

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            if (rs.next()){
                return createUser(rs);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Rejected Login");
        return null;
    }

    public static void addService(ArrayList<String> service){
        query = addService + "'" + service.get(0) + "'";
        for (int i = 1; i < service.size(); i++) {
            query += ", '" + service.get(i) + "'";
        }
        query += ");";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully added " + service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteService(int serviceID){
        query = deleteService + serviceID + ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully removed service #" + serviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAutoIncrementServices(int increment){
        query = resetIncrementServices + increment + ";";

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            st.close();
            System.out.println("Successfully reset auto-increment to " + increment);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> getUserServices(int userID){
        query = getUserServices + userID + ";";
        serviceQuery = new ArrayList<>();

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            System.out.println(serviceQuery);
            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void flushServices(){
        query = flushServices;

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            System.out.println("Successfully flushed services table");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> searchServices(String searchQuery){
        serviceQuery = new ArrayList<>();
        if(searchQuery.contains(" ")){
            keywordQuery = new ArrayList<>(Arrays.asList(searchQuery.split(" ")));
        } else {
            keywordQuery = new ArrayList<>(Arrays.asList(searchQuery));
        }
        query = searchServices + "'%" + keywordQuery.get(0) + "%'";

        for (int i = 1; i < keywordQuery.size(); i++) {
            query += " OR ServiceName LIKE '%" + keywordQuery.get(i) + "%'";
        }
        query += ";";

        try {
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            System.out.println(serviceQuery);
            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ArrayList<String>> getStreamServices(){
        serviceQuery = new ArrayList<>();
        query = getStreamServices;

        try{
            st = DatabaseConnection.connection.createStatement();
            rs = st.executeQuery(query);
            while (rs.next())
            {
                serviceQuery.add(createService(rs));
            }
            st.close();

            System.out.println(serviceQuery);
            return serviceQuery;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

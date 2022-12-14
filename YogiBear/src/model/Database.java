/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author csab
 */
public class Database {
    
    private Connection conn;
    
    public Database(){
        String connectionString = "jdbc:mysql://localhost:3306/topten?user=csab&password=asd1234&serverTimezone=UTC";
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void insertToTable(String name, int level, int time) {
        try {
//            String sql = "INSERT INTO HighScore (playerName, gameLevel, elapsedTime) " + " VALUES ("+ "'" +  name + "'"  + "," + level + "," + time + ")";
            String sql = "INSERT INTO HighScore (playerName, gameLevel, elapsedTime) " + " VALUES (?,?,?)";
            
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setString(1, name);
            preparedStmt.setInt(2, level);
            preparedStmt.setInt(3, time);
            preparedStmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getTopTen() {
        try {
            Statement stmt = conn.createStatement();
            String QUERY = "SELECT * FROM TOPTEN.HighScore ORDER BY gameLevel DESC, elapsedTime";
            ResultSet rs = stmt.executeQuery(QUERY);
            int i = 0;
            String column[] = {"Rank","Player","Max level", "Elapsed time"};
            String[][] data = new String[10][4];
            while (rs.next() && i < 10) {
                int rank = i+1;
                String name = rs.getString("playerName");
                String level = rs.getString("gameLevel");
                String time = rs.getString("elapsedTime");
                data[i][0] = Integer.toString(rank);
                data[i][1] = name; data[i][2] = level; data[i][3] = time;
//                System.out.println(rank + " " + name + " " + level + " " + time);
                i++;
            }
            JTable table = new JTable(data,column);
            JOptionPane.showMessageDialog(null, new JScrollPane(table), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

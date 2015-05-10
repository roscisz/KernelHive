/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class MySQLManager implements ManagerInterface {
   
    private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost/";
    private String USER = "root";
    private String PASS = "";

    public MySQLManager() {
    }
 
    @Override
    public void intiateKernelHiveManager() {
        createDB();
        createTable();
    }
    
    private void createDB() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "CREATE DATABASE " + DATABASE;
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private void createTable() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL + DATABASE, USER, PASS);
            stmt = conn.createStatement();
            String sql = "CREATE TABLE " + MAIN_TABLE +
                    "(" + COL_ID + " BIGINT not NULL, " + 
                    COL_DESC + " VARCHAR(255), " +
                    COL_DATA + " LONGBLOB not NULL, " +
                    "PRIMARY KEY (" + COL_ID + "))";                    
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public long uploadPackage(DataPackage dataPack) {
        long time = System.currentTimeMillis();
        try {
            Connection conn = DriverManager.getConnection(DB_URL + DATABASE, USER, PASS);
 
            String sql = "INSERT INTO " + MAIN_TABLE + " ("+ COL_ID +", "+ COL_DATA + ", " + COL_DESC + ") values (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, time);
            statement.setBytes(2, dataPack.getData());
            statement.setString(3, dataPack.getDescription());
            
            int row = statement.executeUpdate();
            if (row > 0) {
                //Success
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return time;
    }

    @Override
    public DataPackage downloadPackage(long packageID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DataPackage> listPackages() {
        List<DataPackage> packageList = new ArrayList<>();
        Connection conn;
        try {
            conn = DriverManager.getConnection(DB_URL + DATABASE, USER, PASS);

            String sql = "SELECT " + COL_ID + ", " + COL_DESC + " FROM " + MAIN_TABLE + " ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                DataPackage packageInfo = new DataPackage();
                long id = resultSet.getLong(1);
                String desc = resultSet.getString(2);
                packageInfo.setId(id);
                packageInfo.setDescription(desc);
                packageList.add(packageInfo);
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packageList;
    }

}

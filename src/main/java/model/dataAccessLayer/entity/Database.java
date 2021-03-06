/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dataAccessLayer.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import oracle.jdbc.OracleDriver;

/**
 *
 * @author Eman-PC
 */
public class Database {

    private static Connection con;
    private static Database opr;
    private static PreparedStatement ps = null;
    private static PreparedStatement psUpdatable = null;

    static {
        try {
            DriverManager.registerDriver(new OracleDriver());
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }

    private Database() {
    }

    public static Database getInstance() {
        if (opr == null) {
            opr = new Database();
        }
        return opr;
    }

    private static Connection getConnection() throws SQLException {
        if (con == null) {
            con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "STORE_Y", "store_y");
        }
        return con;
    }

    public PreparedStatement getPreparedStatement(String query) {
        try {
            ps = getConnection().prepareStatement(query);
        } catch (SQLException ex) {
            release();
            ex.printStackTrace(System.out);
        }

        return ps;
    }

    public PreparedStatement getPreparedStatementUpdatable(String query) {
        try {
            psUpdatable = getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        } catch (SQLException ex) {
            release();
            ex.printStackTrace(System.out);
        }

        return psUpdatable;
    }

    public void release() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
    }
}

package com.wq.utils;

import java.sql.*;

public class DBUtil
{
    private static final String URL = "jdbc:mysql://XXXXXXX:3306/halodb";
    private static final String UNAME = "root";
    private static final String PWD = "Nuchina@1029";

    private static Connection conn = null;

    static
    {
        try
        {
            // 1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获得数据库的连接
            conn = DriverManager.getConnection(URL, UNAME, PWD);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getConnection()
    {
        return conn;
    }





    /**
     * 关闭ResultSet
     * @param rs
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * 关闭Statement
     * @param stmt
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    /**
     * 关闭ResultSet、Statement
     * @param rs
     * @param stmt
     */
    public static void closeStatement(ResultSet rs,Statement stmt) {
        closeResultSet(rs);
        closeStatement(stmt);
    }
    /**
     * 关闭PreparedStatement
     * @param pstmt
     * @throws sqlException
     */
    public static void fastcloseStmt(PreparedStatement pstmt) throws Exception
    {
        pstmt.close();
    }
    /**
     * 关闭ResultSet、PreparedStatement
     * @param rs
     * @param pstmt
     * @throws sqlException
     */
    public static void fastcloseStmt(ResultSet rs,PreparedStatement pstmt) throws Exception
    {
        rs.close();
        pstmt.close();
    }
    /**
     * 关闭ResultSet、Statement、Connection
     * @param rs
     * @param stmt
     * @param con
     */
    public static void closeConnection(ResultSet rs,Statement stmt,Connection con) {
        closeResultSet(rs);
        closeStatement(stmt);
        closeConnection(con);
    }
    /**
     * 关闭Statement、Connection
     * @param stmt
     * @param con
     */
    public static void closeConnection(Statement stmt,Connection con) {
        closeStatement(stmt);
        closeConnection(con);
    }
    /**
     * 关闭Connection
     * @param con
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }




}
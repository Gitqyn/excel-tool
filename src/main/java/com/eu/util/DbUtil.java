package com.eu.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class DbUtil {

//    spring.datasource.url=jdbc:mysql://rm-uf68ttbq3m4gxolvuo.mysql.rds.aliyuncs.com:3306/trans?characterEncoding=utf8&useUnicode=true
//    spring.datasource.username=trans
//    spring.datasource.password=qwe!@#QWE123


    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://rm-uf68ttbq3m4gxolvuo.mysql.rds.aliyuncs.com:3306/trans?characterEncoding=utf8&useUnicode=true";

    //  Database credentials
    static final String USER = "trans";
    static final String PASS = "qwe!@#QWE123";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            if (conn!=null){
                System.out.println("连接成功！");
            }


            //STEP 4: Execute a query
            System.out.println("Creating statement...");
//            stmt = conn.createStatement();
//            String sql = "SELECT ruleid , rulefile FROM t_traffrule_db ";
//            ResultSet rs = stmt.executeQuery(sql);

            String updateSql = "UPDATE t_traffrule_db SET rulefile = ? WHERE ruleid = ?";
            ps = conn.prepareStatement(updateSql);
            File file = new File("e:"+ File.separator+"TraffQuery.xls");

            InputStream inputStream = new FileInputStream(file);
           // Workbook book = new HSSFWorkbook(inputStream);
            ps.setBinaryStream(1,inputStream,inputStream.available());
            ps.setInt(2,1);

            ps.executeUpdate();

            //STEP 5: Extract data from result set
//            while(rs.next()){
//                //Retrieve by column name
//                int id  = rs.getInt("ruleid");
//                Blob rulefile = rs.getBlob("rulefile");
//               // byte[] rulefile = rs.getBytes("rulefile");
//
//                //Display values
//                System.out.println("ruleid: " + id);
//                System.out.println("rulefile: " + rulefile);
//
//            }
            //STEP 6: Clean-up environment
            ps.close();
           // stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main


}

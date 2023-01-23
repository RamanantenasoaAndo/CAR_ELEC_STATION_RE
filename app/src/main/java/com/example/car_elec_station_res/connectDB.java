package com.example.car_elec_station_res;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class connectDB {
    Connection con;
    String ip,port,db,un,pass;
    @SuppressLint("NewApi")
    public Connection conclass() {
        ip = "192.168.100.126";
        port = "1433";
        db = "andodb";
        un = "ando";
        pass = "ramanantenasoa";
        StrictMode.ThreadPolicy tpolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tpolicy);
        Connection connection =null;
        String ConnectionURL= null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL= "jdbc:jtds:sqlserver://"+ip+":"+port+";"+"databaseName="+ db+ ";user="+un+";password="+pass+";";
            con= DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex)
        {
            Log.e("Error : ", ex.getMessage());
        }

        return con;
    }
}

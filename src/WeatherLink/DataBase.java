/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeatherLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

/**
 *
 * @author francisco
 */
public class DataBase {
    Connection c = null;    
    String SERVER  = "";
    String PORT  = "";
    String DB = "";

    public void open(){
        c = null;
        try {
            Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection("jdbc:postgresql://"+SERVER+":"+PORT+"/"+DB,"postgres", "kipuse3");
        } catch (ClassNotFoundException | SQLException e) {            
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        }
    }
    
    public static String getDateStr(){
        Calendar calendario = Calendar.getInstance();
        int hora, minutos,mes,dia, ano;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        calendario.get(Calendar.SECOND);
        mes = calendario.get(Calendar.MONTH)+1;
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        ano = calendario.get(Calendar.YEAR);
        String date = ano+"-"+mes+"-"+dia+" "+hora+":"+minutos+":00";
        return date;
    }

    public void save( String dewpoint_c, String heat_index_c,String pressure_mb, String relative_humidity, String temp_c,String wind_degrees,String wind_dir,String wind_mph,String windchill_c){
        open();
        Statement stmt = null;
        try {      
         stmt = c.createStatement();
         String sql= "INSERT INTO public.\"weatherLink\"( date, dewpoint_c, heat_index_c,pressure_mb,relative_humidity,temp_c,wind_degrees,wind_dir,wind_mph,windchill_c)"
                 + " VALUES ( '"+getDateStr()+"', "+dewpoint_c+", "+heat_index_c+","+pressure_mb+","+relative_humidity+","+temp_c+","+wind_degrees+",'"+wind_dir+"',"+wind_mph+","+windchill_c+");";
         System.out.println(sql);
         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch (SQLException e) {
         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
      }
    }
    

 

    
    
}

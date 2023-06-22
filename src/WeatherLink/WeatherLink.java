/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WeatherLink;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author francisco
 */
public class WeatherLink {

    /**
     * @param args the command line arguments
     */
    private static final String USER= "";
    private static final String PASS= "";
    private static final String KEY= "";
    
    public static void main(String[] args) {
        DataBase db = new DataBase();
        while(true){
            try {
                TimeUnit.SECONDS.sleep(getWaitTime());
                readWeatherLinkAPI(db);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(WeatherLink.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static String getTime(){
        //System.out.println("getTime00");
        Calendar calendario = Calendar.getInstance();
        int ano, mes, dia,hora, minutos, segundos;
        ano =calendario.get(Calendar.YEAR);
        mes =calendario.get(Calendar.MONTH)+1;
        dia =calendario.get(Calendar.DAY_OF_MONTH);
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND); 
        String date = ano+"-"+mes+"-"+dia+" "+hora+":"+minutos+":00";
        System.out.println(date);
        return date;
    }
    
    public static int getWaitTime(){        
        Calendar calendario = Calendar.getInstance();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        int decena = minutos/10;
        int unidad = minutos%10;
        int segundosEspera = ((9-unidad)*60)+(60-segundos);
        System.out.println("dec: "+decena+"\t uni: "+unidad);
        System.out.println("esperar: "+segundosEspera+" seg.");
        System.out.println("Hora inicio: "+hora+":"+minutos+":"+segundos);
        return segundosEspera;
    }
    
    public static void readWeatherLinkAPI(DataBase db){
        try {            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(WeatherLink.class.getName()).log(Level.SEVERE, null, ex);
            }
            URL url = new URL("https://api.weatherlink.com/v1/NoaaExt.xml?user="+USER+"&pass="+PASS+"&apiToken="+KEY);
            String strURL = url.toExternalForm();
            Document document = builder.parse(strURL);
            document.getDocumentElement().normalize();
            Element root = document.getDocumentElement();
            System.out.println(root.getNodeName());
            NodeList nList = document.getElementsByTagName("current_observation");
            System.out.println("============================");
            String dateToSave ="";
            
            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                Node node = nList.item(temp);
                System.out.println("");    //Just a separator
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                   Element eElement = (Element) node;
                   dateToSave = getTime();
                   System.out.println("date : "    + dateToSave);
                   System.out.println("dewpoint_c : "  + eElement.getElementsByTagName("dewpoint_c").item(0).getTextContent());
                   System.out.println("heat_index_c : "  + eElement.getElementsByTagName("heat_index_c").item(0).getTextContent());                   
                   System.out.println("pressure_mb : "  + eElement.getElementsByTagName("pressure_mb").item(0).getTextContent());
                   System.out.println("relative_humidity : "  + eElement.getElementsByTagName("relative_humidity").item(0).getTextContent());
                   System.out.println("temp_c : "  + eElement.getElementsByTagName("temp_c").item(0).getTextContent());
                   System.out.println("wind_degrees : "  + eElement.getElementsByTagName("wind_degrees").item(0).getTextContent());
                   System.out.println("wind_dir : "  + eElement.getElementsByTagName("wind_dir").item(0).getTextContent());
                   System.out.println("wind_mph : "  + eElement.getElementsByTagName("wind_mph").item(0).getTextContent());
                   System.out.println("windchill_c : "  + eElement.getElementsByTagName("windchill_c").item(0).getTextContent());
                   System.out.println("observation_time_rfc822 : "  + eElement.getElementsByTagName("observation_time_rfc822").item(0).getTextContent());
                   db.save(eElement.getElementsByTagName("dewpoint_c").item(0).getTextContent(), eElement.getElementsByTagName("heat_index_c").item(0).getTextContent(), eElement.getElementsByTagName("pressure_mb").item(0).getTextContent(), eElement.getElementsByTagName("relative_humidity").item(0).getTextContent(), eElement.getElementsByTagName("temp_c").item(0).getTextContent(), eElement.getElementsByTagName("wind_degrees").item(0).getTextContent(), eElement.getElementsByTagName("wind_dir").item(0).getTextContent(), eElement.getElementsByTagName("wind_mph").item(0).getTextContent(), eElement.getElementsByTagName("windchill_c").item(0).getTextContent());
                }
            }            
            
        } catch (SAXException | IOException ex) {
            Logger.getLogger(WeatherLink.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
    

}

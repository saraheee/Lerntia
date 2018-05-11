package at.ac.tuwien.sepm.assignment.groupphase.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    InputStream inputStream;
    Properties prop;

    public ConfigReader(String config){

        String filePath = "config/" + config + ".properties";

        this.prop = new Properties();
        this.inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

        try {
            this.prop.load( this.inputStream );
        } catch (IOException e) {

            try {
                this.inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    public String getValue(String key){
        return this.prop.getProperty(key);
    }

    public int getValueInt(String key){
        return Integer.parseInt(this.prop.getProperty(key));
    }

    public void close(){
        try {
            this.inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

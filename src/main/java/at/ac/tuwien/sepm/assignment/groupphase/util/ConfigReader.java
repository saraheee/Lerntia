package at.ac.tuwien.sepm.assignment.groupphase.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class ConfigReader {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    InputStream inputStream;
    Properties prop;

    public ConfigReader(String config){

        String propsPath = System.getProperty("user.home");
        propsPath += File.separator + "Lerntia" + File.separator + "config" + File.separator + config + ".properties";

        this.prop = new Properties();
        File propsFile = new File(propsPath);

        LOG.debug("Reading config properties: propsPath: *{}* propsFile: *{}*", propsPath, propsFile);

        try {
            this.inputStream = new FileInputStream(propsFile);
            LOG.debug("Opened input stream: {}", inputStream);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key){
        return this.prop.getProperty(key);
    }

    public int getValueInt(String key){
        return Integer.parseInt(this.prop.getProperty(key));
    }

    public boolean getValueBoolean(String key){
        return Boolean.valueOf(this.prop.getProperty(key));
    }

    public void close(){
        try {
            this.inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

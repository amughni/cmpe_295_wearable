package com.access;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by amu on 5/6/2017.
 */
public class PropertyLoader {
    public static Properties load() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "config.properties";
            input = PropertyLoader.class.getClassLoader().getResourceAsStream(filename);

            // load a properties file
            prop.load(input);

            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getValue(String key) {
        Properties prop = load();
        if(prop != null) {
            // get the property value and print it out
            return prop.getProperty(key);
        }
        return null;
    }
}

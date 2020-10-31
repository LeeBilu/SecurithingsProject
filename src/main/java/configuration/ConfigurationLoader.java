package configuration;

import enums.EPropertiesFields;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton configuration loader for application.properties file
 */
public class ConfigurationLoader {

    private static Properties properties;

    private static ConfigurationLoader configurationLoader;

    //load the configuration file
    private ConfigurationLoader() {
        try {
            InputStream inputStream = null;
            try {
                properties = new Properties();
                inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigurationLoader getInstance() {
        if (configurationLoader == null) {
            //synchronized block to remove overhead
            synchronized (ConfigurationLoader.class) {
                if (configurationLoader == null) {
                    // if instance is null, initialize
                    configurationLoader = new ConfigurationLoader();
                }
            }
        }
        return configurationLoader;
    }

    /**
     * get filed from property file
     * must use Enum EPropertiesFields to make sure all fields are mapped
     * @param field EPropertiesFields
     * @return the value from the property file
     */
    public String getProperty(EPropertiesFields field) {
        String fieldValue = null;
        if (properties != null) {
            fieldValue = properties.getProperty(field.getFieldName());
        }
        return fieldValue;

    }

}

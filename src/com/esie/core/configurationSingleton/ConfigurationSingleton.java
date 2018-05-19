package com.esie.core.configurationSingleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.esie.core.other.ANSI.ANSI.*;

final public class ConfigurationSingleton {

    public static volatile ConfigurationSingleton configuration;
    private Map<String, String> file = new ConcurrentHashMap<>();
    private FileInputStream stream;
    private byte[] data;

    public static synchronized ConfigurationSingleton getInstance(String s) {
        if (configuration == null) {
            try {
                configuration = new ConfigurationSingleton(s);
                configuration.read();
                return configuration;
            } catch (Exception e) {
                System.out.println(ANSI_RED + "No config file found!" + ANSI_RESET);
                e.printStackTrace();
                return null;
            }
        } else return configuration;
    }


    @Deprecated
    private ConfigurationSingleton(File filePath) throws IOException {
        stream = new FileInputStream(filePath);
        data = new byte[stream.available()];
    }


    private ConfigurationSingleton(String s) throws IOException {
        try {
            stream = new FileInputStream(s);
        } catch (IOException e) {
            stream = new FileInputStream("configuration.properties");
        }
        data = new byte[stream.available()];
    }


    private void read() throws IOException {
        stream.read(data);
        String tempData = new String(data);
        String [] firstContainer = tempData.split("\n");

        for (String tempContainerItem : firstContainer) {
           if (tempContainerItem.contains("=")) {
               String[] secondContainer = tempContainerItem.split("=");
               file.put(secondContainer[0].replaceAll(" ", "").replaceAll("\t", ""),
                        secondContainer[1].replaceAll(" ", "").replaceAll("\t", ""));
           }
        }
    }


    public String getValue(String key){
        return file.get(key);
    }

}


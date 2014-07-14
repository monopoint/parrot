package net.monopoint.parrot;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Espen Hjertø / Kantega AS / 7/13/14
 */
public class Parrot {

    final String settingsFileName = "parrot.properties";

    public Parrot() {

        Properties properties = new Properties();

        try {
            properties.load(getPropertiesUrl().openStream());
        } catch (IOException e) {
            System.out.println("Unable to load settings file.");
            e.printStackTrace();
        }  catch (NullPointerException e){
            System.out.println("Could not find settings file, so I created it. You should edit it for your needs.");
            createSettingsFile();
        }



    }

    private URL getAppLocationPath(){
        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        return url;
    }

    private URL getPropertiesUrl(){
        URL appUrl = getAppLocationPath();
        URL propertiesUrl = getClass().getResource(appUrl.toString() + settingsFileName);
        return propertiesUrl;
    }

    private void createSettingsFile(){
        File settingsFile = new File("settings.properties");


        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFileName), "utf-8"));
            writer.write("Something\n");
            writer.write("other\n");
            writer.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

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

        // Loading properties
        Properties properties = new Properties();

        try {
            if (!settingsFileExists()){
                createSettingsFile();
                System.out.println("Could not find settings file, so I created it. You should edit it for your needs.");
            }

            properties.load(getPropertiesUrl().openStream());


        } catch (IOException e) {
            System.out.println("Unable to load settings file.");
            e.printStackTrace();
        }  catch (NullPointerException e){
            System.out.println("Unable to load settings file.");
            e.printStackTrace();
        }
    }


    // TODO: should return path as atring, not URL, as this includes "file:/"

    private URL getAppLocationPath(){
        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        return url;
    }

    private URL getPropertiesUrl(){
        URL appUrl = getAppLocationPath();
        URL propertiesUrl = getClass().getResource(appUrl.toString() + settingsFileName);
        return propertiesUrl;
    }

    private boolean settingsFileExists(){
        URL path = getAppLocationPath();
        File file = new File(path.toString() + settingsFileName);
        return file.exists();
    }

    private void createSettingsFile(){
        File settingsFile = new File(getAppLocationPath().toString()+settingsFileName);

        try {
            settingsFile.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getAppLocationPath().toString()+settingsFileName), "utf-8"));
            writer.write("# Settings for parrot.\n");
            writer.write("\n");
            writer.write("# files in this directory are ignored\n");
            writer.write("# ignore_dir = \n");
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");
            writer.write("\n");

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

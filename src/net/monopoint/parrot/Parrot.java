package net.monopoint.parrot;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * spin / monopoint
 */
public class Parrot {

    final String settingsFileName = "parrot.properties";

    public Parrot(String torrentPath, String torrentName) {

        log("Path is: " + getAppLocationPath() + "\n\n");

        // Loading properties
        Properties properties = new Properties();

        try {
            if (!settingsFileExists()){
                createSettingsFile();
                log("Could not find settings file, so I created it. You should edit it for your needs.");
            }
            properties.load(getPropertiesStream());

        } catch (FileNotFoundException e) {
            log("Cannot find properties file.");
            e.printStackTrace();
        }  catch (NullPointerException e){
            log("Unable to load settings file.");
            e.printStackTrace();
        } catch (IOException e) {
            log("Unable to load settings file.");
            e.printStackTrace();
        }


        // some testing.
        log("properties test:" + properties.getProperty("test"));
        log("arguments test: " + torrentPath + torrentName);

        String destination = properties.getProperty("destination_dir");
        if (!destination.endsWith("/")) destination += "/";
        String ignore = properties.getProperty("ignore_dir");

        // Some uploaders create torrents with spaces in their names. Lets fill the gaps
        String filledTorrentName = torrentName.replace(' ', '.');

        // sometimes the torrent is a directory and sometimes not. we need to be sure
        boolean torrentIsDirectory = false;
        File torrentFile = new File("'"+torrentPath+torrentName+"'");
        if (torrentFile.isDirectory()) torrentIsDirectory = true;

        // Setting up destination
        execute("mkdir " + destination + filledTorrentName);

        // Unpacking or copying files
        if (torrentIsDirectory){
            // Attempting ot unpack rars. this should also handle rars in part*.rar format
            ExtensionFilter rarFilter = new ExtensionFilter(".rar");
            String[] contents = torrentFile.list(rarFilter);
            for (String filename : contents){
                if (!filename.endsWith(".rar")){
                    String command = String.format("unrar x '%s%s/%s' %s%s", torrentPath, torrentName, filename, destination, filledTorrentName);
                    log(command);
                    execute(command);
                }
            }

            // attempting to move any movie files.
            execute(String.format("cp '%s%s/*.mkv' %s%s", torrentPath, torrentName, destination, filledTorrentName));
            execute(String.format("cp '%s%s/*.mp4' %s%s", torrentPath, torrentName, destination, filledTorrentName));
            execute(String.format("cp '%s%s/*.avi' %s%s", torrentPath, torrentName, destination, filledTorrentName));


        } else {
            // if torrent is not a directory
            if (torrentName.endsWith(".rar")) {
                String command = String.format("unrar x '%s%s' %s%s", torrentPath, torrentName, destination, filledTorrentName);
                execute(command);
            }
            // copy if torrent is a movie file
            if ( torrentName.endsWith(".mkv") || torrentName.endsWith(".mp4") || torrentName.endsWith(".avi")){
                String command = String.format("cp '%s%s' %s%s", torrentPath, torrentName, destination, filledTorrentName);
                execute(command);
            }

        }

    }

    private void execute(String command){
        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = stdInput.readLine()) != null) {
                log(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getAppLocationPath(){
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        //log("raw Path is: " + path + "\n\n");

        if (!path.endsWith("/")){
            // the resolved path is the jar file. we back up a bit..
            path = path.substring(0, path.lastIndexOf("/")+1);
        }

        return path;
    }

    private InputStream getPropertiesStream() throws FileNotFoundException {

        //InputStream propertiesUrl = getClass().getResourceAsStream(getAppLocationPath() + settingsFileName);
        FileInputStream propStream = new FileInputStream(getAppLocationPath() + settingsFileName);

        return propStream;
    }

    private boolean settingsFileExists(){
        File file = new File(getAppLocationPath() + settingsFileName);
        return file.exists();
    }

    private void createSettingsFile(){
        File settingsFile = new File(getAppLocationPath()+settingsFileName);

        try {
            settingsFile.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getAppLocationPath()+settingsFileName), "utf-8"));
            writer.write("# Settings for parrot.\n");
            writer.write("# If you mess up these settings, delete this file and a default will be created.\n");
            writer.write("\n");
            writer.write("# files in this directory are ignored\n");
            writer.write("# ignore_dir = \n");
            writer.write("\n");
            writer.write("# Destination directory. Change this to an existing dir\n");
            writer.write("destination_dir = ./\n");
            writer.write("\n");
            writer.write("# Test property\n");
            writer.write("test = we are ok\n");
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
    
    private void log(String s){
        System.out.println(s);
        Writer writer;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getAppLocationPath()+"parrot.log"), "utf-8"));
            writer.write(dateFormat.format(date) + " > " + s + "\n");
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

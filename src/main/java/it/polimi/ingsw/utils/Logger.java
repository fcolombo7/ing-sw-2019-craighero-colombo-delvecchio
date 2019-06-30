package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.client.Client;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static String filename="none";

    public static void print(String msg){
        System.out.println(msg);
    }

    public static void flush(){
        System.out.flush();
    }

    public static void log(String msg){
        printToFile("LOG: "+msg);
    }

    private static synchronized void createLogFile() throws URISyntaxException {
        String inExecutionFile = new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        int last = 0;
        int last2=0;
        for (int i = 0; i < inExecutionFile.length(); i++) {
            if (inExecutionFile.charAt(i) == '/')
                last = i;
        }
        for (int i = 0; i < inExecutionFile.length(); i++) {
            if (inExecutionFile.charAt(i) == '\\')
                last2 = i;
        }
        String folderName;
        if(last>last2)
            folderName=inExecutionFile.substring(0, last + 1);
        else
            folderName=inExecutionFile.substring(0, last2 + 1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd_HH/mm/ss");
        Date date = new Date();
        String name="log_"+dateFormat.format(date).replaceAll("/","")+".txt";

        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles!=null) {
            try {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().equals(name)) {
                        name = name + "_2";
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("FILES ERROR");
            }
        }
        filename=folderName+name;
        print("LOGGER: "+filename);
    }

    public static void logServer(String msg){
        printToFile("LOG: "+msg);
        print("LOG: "+msg);
    }

    public static void logErr(String msg){
        printToFile("ERR: "+msg);
        System.err.println("ERR: "+msg);
    }
    private static synchronized void printToFile(String msg){
        if(filename.equals("none")) {
            try {
                createLogFile();
            } catch (Exception e) {
                System.err.println("LOGGER ERROR: "+e.getMessage());
                return;
            }
        }
        try {
            File file=new File(filename);
            if(!file.exists())
                file.createNewFile();
            Files.write(Paths.get(filename), (msg+"\r\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.err.println("LOGGER ERROR: "+e.getMessage());
        }
    }
    private Logger(){

    }
}

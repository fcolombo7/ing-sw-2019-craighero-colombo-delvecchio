package it.polimi.ingsw.utils;

public class Logger {

    public static void log(String msg){
        System.out.println("LOG: "+msg);
    }

    public static void logErr(String msg){
        System.err.println("ERR: "+msg);
    }
    private Logger(){

    }
}

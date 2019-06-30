package it.polimi.ingsw.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConsoleInput {
    private ExecutorService ex;
    private boolean reading;
    public ConsoleInput() {
        reading=false;
    }

    public synchronized String readLine() {
        ex = Executors.newSingleThreadExecutor();
        reading=true;
        String input = null;
        try {
            Future<String> result = ex.submit(new ConsoleInputReadTask());
            try {
                input = result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } finally {
            ex.shutdownNow();
        }
        return input;
    }

    public void cancel(){
        if(isReading()){
            reading=false;
            ex.shutdownNow();
        }
    }
    public boolean isReading(){
        return reading;
    }
}
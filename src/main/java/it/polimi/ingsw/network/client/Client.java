package it.polimi.ingsw.network.client;

import it.polimi.ingsw.GUI.MainWindow;
import it.polimi.ingsw.ui.Cli;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;

public class Client {
    public static void main(String[] args) throws IOException, NotBoundException, URISyntaxException {
        if(args.length!=2){
            System.out.println("Proper Usage: CLI | GUI hostname");
            return;
        }
        if((args[0].equalsIgnoreCase("CLI")||args[0].equalsIgnoreCase("GUI"))&&!args[1].equalsIgnoreCase("")) {
            System.out.println("Starting "+args[0]+"...");
            if(args[0].equalsIgnoreCase("CLI")) {
                new Cli(args[1]);
            }
            else {
            }
        }
    }
}

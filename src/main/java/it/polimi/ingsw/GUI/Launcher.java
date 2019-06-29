package it.polimi.ingsw.GUI;

import it.polimi.ingsw.utils.Constants;

//launcher
public class  Launcher {
    public static void main(String[] args) {
        MainWindow.setHostname(Constants.RMI_HOSTNAME);
        MainWindow.main(args);}
}
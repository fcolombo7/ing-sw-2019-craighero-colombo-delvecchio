package it.polimi.ingsw.ui.GUI;

import it.polimi.ingsw.utils.Constants;


public class  Launcher {
    public static void main(String[] args) {
        MainWindow.setHostname(Constants.RMI_HOSTNAME);
        MainWindow.main(args);}
}
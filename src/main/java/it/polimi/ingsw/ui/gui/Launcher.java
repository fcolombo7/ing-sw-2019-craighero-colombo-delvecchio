package it.polimi.ingsw.ui.gui;

public class  Launcher {
    public static void main(String[] args) {
        MainWindow.setHostname(args[0]);
        MainWindow.main(args);}
}
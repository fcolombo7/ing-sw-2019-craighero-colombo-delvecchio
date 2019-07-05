package it.polimi.ingsw.networkbots;

import it.polimi.ingsw.ui.Cli;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;

public class SimpleCLILauncher {
    public static void main(String[] args) throws NotBoundException, IOException, URISyntaxException {
        new Cli(Constants.RMI_HOSTNAME);
    }
}

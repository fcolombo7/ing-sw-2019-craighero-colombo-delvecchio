package it.polimi.ingsw.network.client;

import it.polimi.ingsw.ui.AdrenalineGUI;
import it.polimi.ingsw.ui.AdrenalineUI;

public class Client {
    public static void main(String[] args){
        if(args.length==0){
            //no args passed --> default
            System.out.println("STARTING DEFAULT");
            AdrenalineGUI.main(new String[]{});
        }else if(args.length==1&&args[0].equalsIgnoreCase("CLI")) {
            System.out.println("STARTING CLI");
            //AdrenalineUI ui=new AdrenalineCLI();
        }else if(args.length==1&&args[0].equalsIgnoreCase("GUI")) {
            System.out.println("STARTING GUI");
            //AdrenalineUI ui=AdrenalineGUI.create();
        }else
            System.out.println("Proper Usage: CLI | GUI [default GUI]");
    }
}

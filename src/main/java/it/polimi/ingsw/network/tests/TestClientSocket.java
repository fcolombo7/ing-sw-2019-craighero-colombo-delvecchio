package it.polimi.ingsw.network.tests;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.client.RMIServerConnection;
import it.polimi.ingsw.network.client.ServerConnection;
import it.polimi.ingsw.network.client.SocketServerConnection;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.ui.AdrenalineUI;
import it.polimi.ingsw.utils.ConsoleInput;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.MatrixHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestClientSocket {
    private static class DebugUI implements AdrenalineUI{
        private static int count=0;
        private ServerConnection connection;
        private boolean rmi;
        DebugUI(boolean rmi){
            this.rmi=rmi;
        }
        Scanner stdin=new Scanner(System.in);
        ConsoleInput consoleInput;
        ExecutorService ex = Executors.newFixedThreadPool(5);

        public void setUpConnection() throws IOException, NotBoundException, URISyntaxException {
            if(rmi)this.connection = new RMIServerConnection("localhost",this);
            else this.connection=new SocketServerConnection(Constants.RMI_HOSTNAME,this);
            consoleInput= new ConsoleInput();
        }

        @Override
        public void onJoinRoomAdvise(String nickname) {
            ex.submit(()->{
                System.out.println("User "+ nickname+" has join the room...");
            });
        }

        @Override
        public void onExitRoomAdvise(String nickname) {
            ex.submit(()->{
                System.out.println("User "+ nickname+" has left the room...");
            });
        }

        @Override
        public void onFirstInRoomAdvise() {
            ex.submit(()->{
                System.out.println("You are the first player in the room...");
                consoleInput.cancel();
                System.out.println(consoleInput.readLine());
            });

        }

        @Override
        public void onMatchCreation(List<SimplePlayer> players, int playerTurnNumber) {
            System.out.println(count+" MATCH CREATED ("+players.size()+","+playerTurnNumber+")");
            count++;
            connection.boardPreference(1);
        }

        @Override
        public void onInvalidMessageReceived(String msg) {

        }

        @Override
        public void onBoardUpdate(SimpleBoard gameBoard) {
            System.out.println(count+" BOARD UPDATE");
            count++;
        }

        @Override
        public void onMatchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
            System.out.println(count+" MATCH UPDATE");
            count++;
        }

        @Override
        public void onRespwanRequest(List<Card> powerups) {
            ex.submit(()->{
                System.out.println(count+" RESPAWN REQUEST");
                count++;
                consoleInput.cancel();
                int val=Integer.parseInt(consoleInput.readLine());
                connection.respawnPlayer(powerups.get(val));
            });
        }

        @Override
        public void onRespwanCompleted(SimplePlayer player, Card discardedPowerup) {
            System.out.println(count+" RESPAWN COMPLETED");
            count++;
        }

        @Override
        public void onGrabbedTile(SimplePlayer player, AmmoTile grabbedTile) {

        }

        @Override
        public void onGrabbedPowerup(SimplePlayer player, Card powerup) {

        }

        @Override
        public void onGrabbableWeapons(List<Card> weapons) {

        }

        @Override
        public void onDiscardWeapon(List<Card> weapons) {

        }

        @Override
        public void onGrabbedWeapon(SimplePlayer player, Card weapon) {

        }

        @Override
        public void onReloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {

        }

        @Override
        public void onReloadableWeapons(List<Card> weapons) {

        }

        @Override
        public void onTurnActions(List<String> actions) {
            System.out.println(count+" TURN ACTIONS");
            count++;
            boolean found=false;
            for (String action : actions) {
                if (action.equals("RUN")) {
                    found=true;
                }
                System.out.println(action);
            }
            if(found) {
                connection.selectAction("RUN");
            }
        }

        @Override
        public void onTurnEnd() {
            System.out.println(count+" TURN END");
            count++;
            connection.closeTurn();
        }

        @Override
        public void onMoveAction(SimplePlayer player) {

        }

        @Override
        public void onMoveRequest(MatrixHelper matrix, String targetPlayer) {

        }

        @Override
        public void onMarkAction(String player, SimplePlayer selected, int value) {

        }

        @Override
        public void onDamageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {

        }

        @Override
        public void onDiscardedPowerup(SimplePlayer player, Card powerup) {

        }

        @Override
        public void onTurnCreation(String currentPlayer) {
            System.out.println(count+" TURN CREATION");
            count++;
        }

        @Override
        public void onSelectablePlayers(List<List<String>> selectable, SimpleTarget target) {

        }

        @Override
        public void onCanUsePowerup() {

        }

        @Override
        public void onCanStopRoutine() {

        }

        @Override
        public void onUsableWeapons(List<Card> usableWeapons) {

        }

        @Override
        public void onAvailableEffects(List<String> effects) {

        }

        @Override
        public void onPayEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {

        }

        @Override
        public void onUsedCard(Card card) {

        }

        @Override
        public void onAvailablePowerups(List<Card> powerups) {

        }

        @Override
        public void onRunCompleted(SimplePlayer player, int[] newPosition) {

        }

        @Override
        public void onRunRoutine(MatrixHelper matrix) {
            System.out.println("Run routine...");
            for(int i=0;i<matrix.getRowLength();i++){
                for(int j=0;j<matrix.getColLength();j++) {
                    if (matrix.toBooleanMatrix()[i][j]) {
                        connection.runAction(new int[]{i, j});
                        return;
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        String nick;
        Scanner stdin=new Scanner(System.in);
        System.out.print("nick: ");
        nick=stdin.nextLine();
        try {
            DebugUI ui2=new DebugUI(false);
            ui2.setUpConnection();
            ui2.connection.login(nick,"MOTTO");
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

package it.polimi.ingsw.network.client;

import com.google.gson.Gson;
import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.RMIClientHandler;
import it.polimi.ingsw.network.RMIServerHandler;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.ui.AdrenalineUI;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIServerConnection extends ServerConnection implements RMIClientHandler {
    private String session;
    private RMIServerHandler stub;
    private ExecutorService sendingPool;
    private ExecutorService receivingPool;
    private boolean disconnected=false;

    public RMIServerConnection(String hostname, int port, AdrenalineUI ui) throws IOException, NotBoundException, URISyntaxException {
        super(hostname, port, ui);
        String codebase= new File(Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        Logger.log("Codebase: "+codebase);
        String ip="";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        }
        Logger.log("ip: "+ip);
        if(ip.equalsIgnoreCase("")) throw new IOException("Can not get the ip address.");
        System.setProperty("java.rmi.server.hostname",ip);
        System.setProperty("java.rmi.server.codebase","file://"+codebase);

        Logger.log("Trying creating RMI connection...");
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        StringBuilder builder=new StringBuilder();
        builder.append("RMI registry bindings:");
        String[] regs = registry.list();
        for (String e:regs)
            builder.append(e);
        Logger.log(builder.toString());

        stub = (RMIServerHandler) Naming.lookup("rmi://"+ this.getHostname()+":"+this.getPort()+"/"+Constants.RMI_SERVER_NAME);
        Logger.log("STUB INITIALIZED");
        sendingPool =Executors.newFixedThreadPool(5);
        receivingPool =Executors.newFixedThreadPool(5);

    }

    public RMIServerConnection(String hostname, AdrenalineUI ui) throws IOException, NotBoundException, URISyntaxException {
        this(hostname,Constants.RMI_PORT, ui);
    }

    /*------ SERVER CONNECTION METHODS ------*/
    @Override
    public String login(String nickname, String motto) {
        try {
            RMIClientHandler client= (RMIClientHandler) UnicastRemoteObject.exportObject(this, getPort()+1);
            String msg=stub.login(nickname,motto,client);
            if(msg!=null) {
                session = msg;
                setNickname(nickname);
                return Constants.MSG_SERVER_POSITIVE_ANSWER;
            }
            else
                return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        } catch (RemoteException /*| AlreadyBoundException */e) {
            Logger.logErr(e.getMessage());
            return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        }
    }

    @Override
    public void logout() {
        sendingPool.submit(()->{
            try {
                if(stub.deregister(session))
                    Logger.log("Successfully closed RMI connection.");
                else
                    Logger.logErr("Error occurs during the RMI connection closing");
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call logout().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void boardPreference(int value) {
        if(!disconnected) {
            sendingPool.submit(() -> {
                try {
                    stub.boardPreference(session, value);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call boardPreference().");
                    Logger.logErr(e.getMessage());
                }
            });
        }
    }

    @Override
    public void respawnPlayer(Card powerup) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.respawnPlayer(session,powerup);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call respawnPlayer().");
                    Logger.logErr(e.getMessage());
                }});
        }
    }

    @Override
    public void closeTurn() {
        sendingPool.submit(()->{
        try {
            stub.closeTurn(session);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call closeTurn().");
            Logger.logErr(e.getMessage());
        }});
    }

    @Override
    public void selectAction(String action) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.selectAction(session,action);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call selectAction().");
                    Logger.logErr(e.getMessage());
                }});
        }
    }

    @Override
    public void movePlayer(String target, int[] newPosition) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.movePlayer(session,target,newPosition);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call movePlayer().");
                    Logger.logErr(e.getMessage());
                }});
        }
    }

    @Override
    public void discardWeapon(Card weapon) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.discardWeapon(session,weapon);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call discardWeapon().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void selectEffect(String effectName) {
        sendingPool.submit(()->{
            try {
                stub.selectEffect(session,effectName);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call selectEffect().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void loadableWeapon(Card weapon) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.loadableWeapon(session,weapon);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call loadableWeapon().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void runAction(int[] newPosition) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.runAction(session,newPosition);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call runAction().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void selectPlayers(List<List<String>> selected) {
        Gson gson= new Gson();
        String string=gson.toJson(selected);
        Logger.logAndPrint(string);
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.selectPlayers(session,string);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call selectPlayers().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void selectPowerup(Card powerup) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.selectPowerup(session,powerup);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call selectPowerup().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void stopRoutine(boolean stop) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.stopRoutine(session,stop);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call stopRoutine().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void usePowerup(boolean use) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.usePowerup(session,use);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call usePowerup().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void selectWeapon(Card weapon) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.selectWeapon(session,weapon);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call selectWeapon().");
                    Logger.logErr(e.getMessage());
                }
            });
        }

    }

    @Override
    public void counterAttackAnswer(boolean counterAttack) {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.counterAttackAnswer(session,counterAttack);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call counterAttackAnswer().");
                    Logger.logErr(e.getMessage());
                }
            });
        }
    }

    @Override
    public void confirmEndGame() {
        if(!disconnected) {
            sendingPool.submit(()->{
                try {
                    stub.confirmEndGame(session);
                } catch (RemoteException e) {
                    Logger.logErr("RemoteException has been thrown when call confirmEndGame().");
                    Logger.logErr(e.getMessage());
                }
            });
        }
    }

    /*------ RMIClientHandler REMOTE METHODS [called on server] ------*/

    /*------ ROOM METHODS ------*/
    @Override
    public void joinPlayer(String nickname) {
        if(!disconnected) {
            getUi().onJoinRoomAdvise(nickname);
        }
    }

    @Override
    public void exitPlayer(String nickname) {
        if(!disconnected) {
            getUi().onExitRoomAdvise(nickname);
        }
    }

    @Override
    public void firstInRoom() {
        if(!disconnected) {
            receivingPool.submit(()->getUi().onFirstInRoomAdvise());
        }
    }

    @Override
    public void recoverPlayer(String nickname) {
        if(!disconnected) {
            getUi().onRecoverPlayerAdvise(nickname);
        }
    }

    @Override
    public void keepAlive() {
        Logger.log("[KEEPING ALIVE (RMI)]");
    }

    /*------ MATCH METHODS ------*/
    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        if(!disconnected) {
            getUi().onMatchCreation(players,playerTurnNumber);
        }
    }

    @Override
    public void invalidMessageReceived(String msg) {
        if(!disconnected) {
            getUi().onInvalidMessageReceived(msg);
        }
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {
        if(!disconnected) {
            getUi().onBoardUpdate(gameBoard);
        }
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        if(!disconnected) {
            getUi().onMatchUpdate(players,gameBoard,frenzy);
        }
    }

    @Override
    public void respwanRequest(List<Card> powerups) {
        if(!disconnected) {
            getUi().onRespwanRequest(powerups);
        }
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup) {
        if(!disconnected) {
            getUi().onRespwanCompleted(player,discardedPowerup);
        }
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        if(!disconnected) {
            getUi().onGrabbedTile(player,grabbedTile);
        }
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup) {
        if(!disconnected) {
            getUi().onGrabbedPowerup(player,powerup);
        }
    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {
        if(!disconnected) {
            getUi().onGrabbableWeapons(weapons);
        }
    }

    @Override
    public void discardWeapon(List<Card> weapons) {
        if(!disconnected) {
            getUi().onDiscardWeapon(weapons);
        }
    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {
        if(!disconnected) {
            getUi().onGrabbedWeapon(player,weapon);
        }
    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        if(!disconnected) {
            getUi().onReloadedWeapon(player,weapon,discardedPowerups,totalCost);
        }
    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {
        if(!disconnected) {
            getUi().onReloadableWeapons(weapons);
        }
    }

    @Override
    public void turnCreation(String currentPlayer) {
        if(!disconnected) {
            getUi().onTurnCreation(currentPlayer);
        }
    }

    @Override
    public void turnActions(List<String> actions) {
        if(!disconnected) {
            getUi().onTurnActions(actions);
        }
    }

    @Override
    public void turnEnd() {
        if(!disconnected) {
            getUi().onTurnEnd();
        }
    }

    @Override
    public void moveAction(SimplePlayer player) {
        if(!disconnected) {
            getUi().onMoveAction(player);
        }
    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {
        if(!disconnected) {
            getUi().onMoveRequest(matrix,targetPlayer);
        }
    }

    @Override
    public void markAction(String player, SimplePlayer target, int value) {
        if(!disconnected) {
            getUi().onMarkAction(player, target,value);
        }
    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        if(!disconnected) {
            getUi().onDamageAction(player,selected,damageValue,convertedMarks);
        }
    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        if(!disconnected) {
            getUi().onGrabbedPowerup(player,powerup);
        }
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        if(!disconnected) {
            getUi().onSelectablePlayers(selectable,target);
        }
    }

    @Override
    public void canUsePowerup() {
        if(!disconnected) {
            getUi().onCanUsePowerup();
        }
    }

    @Override
    public void canStopRoutine() {
        if(!disconnected) {
            getUi().onCanStopRoutine();
        }
    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {
        if(!disconnected) {
            getUi().onUsableWeapons(usableWeapons);
        }
    }

    @Override
    public void availableEffects(List<String> effects) {
        if(!disconnected) {
            getUi().onAvailableEffects(effects);
        }
    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        if(!disconnected) {
            getUi().onPayEffect(player,discardedPowerups,usedAmmo);
        }
    }

    @Override
    public void usedCard(Card card) {
        if(!disconnected) {
            getUi().onUsedCard(card);
        }
    }

    @Override
    public void availablePowerups(List<Card> powerups) {
        if(!disconnected) {
            getUi().onAvailablePowerups(powerups);
        }
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        if(!disconnected) {
            getUi().onRunCompleted(player,newPosition);
        }
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        if(!disconnected) {
            getUi().onRunRoutine(matrix);
        }
    }

    @Override
    public void wakeUpPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        if(!disconnected) {
            getUi().onPlayerWakeUp(players,gameBoard,frenzy);
        }
    }

    @Override
    public void fullOfPowerup() {
        if(!disconnected) {
            getUi().onFullOfPowerup();
        }
    }

    @Override
    public void canCounterAttack() {
        if(!disconnected) {
            getUi().onCanCounterAttack();
        }
    }

    @Override
    public void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {
        if(!disconnected) {
            getUi().onCounterAttack(currentPlayer,player,powerup);
        }
    }

    @Override
    public void counterAttackTimeOut() {
        if(!disconnected) {
            getUi().onCounterAttackTimeOut();
        }
    }

    @Override
    public void notifyDisconnection() {
        if(!disconnected) {
            disconnected=true;
            getUi().onDisconnectionAdvise();
        }
    }

    @Override
    public void gameEnd(List<SimplePlayer> players) throws RemoteException {
        if(!disconnected) {
            getUi().onGameEnd(players);
        }
    }

    @Override
    public void sendLeaderboard(List<String> nicknames, List<Integer> points) throws RemoteException {
        if(!disconnected) {
            getUi().onLeaderboardReceived(nicknames,points);
        }
    }
}
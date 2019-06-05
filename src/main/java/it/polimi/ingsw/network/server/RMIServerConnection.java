package it.polimi.ingsw.network.server;

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

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIServerConnection extends ServerConnection implements RMIClientHandler {
    private String session;

    private RMIServerHandler stub;
    private ExecutorService pool;


    public RMIServerConnection(AdrenalineUI ui) throws RemoteException, NotBoundException {
        super(ui);

        System.setProperty("java.rmi.server.hostname", "192.168.43.118");
        Registry registry = LocateRegistry.getRegistry("192.168.43.54", Constants.RMI_PORT);

        StringBuilder builder=new StringBuilder();
        builder.append("RMI registry bindings:");
        String[] regs = registry.list();
        for (String e:regs)
            builder.append(e);
        Logger.log(builder.toString());

        stub = (RMIServerHandler) registry.lookup(Constants.RMI_SERVER_NAME);
        pool=Executors.newFixedThreadPool(5);
    }

    /*------ SERVER CONNECTION METHODS ------*/
    @Override
    public String login(String nickname, String motto) {
        try {
            Registry registry = LocateRegistry.getRegistry("192.168.43.54", Constants.RMI_PORT);
            RMIClientHandler client= (RMIClientHandler) UnicastRemoteObject.exportObject(this, Constants.RMI_PORT+1);
            registry.bind(nickname,client);
            String msg=stub.login(nickname,motto,client);
            if(msg!=null) {
                session = msg;
                setNickname(nickname);
                return Constants.MSG_SERVER_POSITIVE_ANSWER;
            }
            else
                return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        } catch (RemoteException | AlreadyBoundException e) {
            Logger.logErr(e.getMessage());
            return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        }
    }

    @Override
    public void logout() {
        pool.submit(()->{
            try {
                if(stub.deregister(session))
                    Logger.log("Successfully closed RMI connection.");
                else
                    Logger.logErr("Error occures during the RMI connection closing");
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call logout().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void boardPreference(int value) {
        pool.submit(()->{
            try {
                stub.boardPreference(session,value);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call boardPreference().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void respawnPlayer(Card powerup) {
        pool.submit(()->{
            try {
                stub.respawnPlayer(session,powerup);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call respawnPlayer().");
                Logger.logErr(e.getMessage());
        }});
    }

    @Override
    public void closeTurn() {
        pool.submit(()->{
        try {
            stub.closeTurn(session);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call closeTurn().");
            Logger.logErr(e.getMessage());
        }});
    }

    @Override
    public void selectAction(String action) {
        pool.submit(()->{
            try {
            stub.selectAction(session,action);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call selectAction().");
            Logger.logErr(e.getMessage());
        }});
    }

    @Override
    public void movePlayer(String target, int[] newPosition) {
        pool.submit(()->{
            try {
            stub.movePlayer(session,target,newPosition);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call movePlayer().");
            Logger.logErr(e.getMessage());
        }});
    }

    @Override
    public void discardWeapon(Card weapon) {
        pool.submit(()->{
            try {
                stub.discardWeapon(session,weapon);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call discardWeapon().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void selectEffect(String effectName) {
        pool.submit(()->{
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
        pool.submit(()->{
            try {
                stub.loadableWeapon(session,weapon);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call loadableWeapon().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void runAction(int[] newPosition) {
        pool.submit(()->{
            try {
                stub.runAction(session,newPosition);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call runAction().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void selectPlayers(List<List<String>> selected) {
        pool.submit(()->{
            try {
                stub.selectPlayers(session,selected);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call selectPlayers().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void selectPowerup(Card powerup) {
        pool.submit(()->{
            try {
                stub.selectPowerup(session,powerup);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call selectPowerup().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void stopRoutine(boolean stop) {
        pool.submit(()->{
            try {
                stub.stopRoutine(session,stop);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call stopRoutine().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void usePowerup(boolean use) {
        pool.submit(()->{
            try {
                stub.usePowerup(session,use);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call usePowerup().");
                Logger.logErr(e.getMessage());
            }
        });

    }

    @Override
    public void selectWeapon(Card weapon) {
        pool.submit(()->{
            try {
                stub.selectWeapon(session,weapon);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call selectWeapon().");
                Logger.logErr(e.getMessage());
            }
        });

    }


    /*------ RMIClientHandler REMOTE METHODS [called on server] ------*/

    /*------ ROOM METHODS ------*/
    @Override
    public void joinPlayer(String nickname) {
        getUi().onJoinRoomAdvise(nickname);
    }

    @Override
    public void exitPlayer(String nickname) {
        getUi().onExitRoomAdvise(nickname);
    }

    @Override
    public void firstInRoom() {
        getUi().onFirstInRoomAdvise();
    }

    @Override
    public void keepAlive() {
        Logger.log("[KEEPING ALIVE (RMI)]");
    }

    /*------ MATCH METHODS ------*/
    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        getUi().onMatchCreation(players,playerTurnNumber);
    }

    @Override
    public void invalidMessageReceived(String msg) {
        getUi().onInvalidMessageReceived(msg);
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {
        getUi().onBoardUpdate(gameBoard);
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        getUi().onMatchUpdate(players,gameBoard,frenzy);
    }

    @Override
    public void respwanRequest(List<Card> powerups) {
        getUi().onRespwanRequest(powerups);
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup) {
        getUi().onRespwanCompleted(player,discardedPowerup);
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        getUi().onGrabbedTile(player,grabbedTile);
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup) {
        getUi().onGrabbedPowerup(player,powerup);
    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {
        getUi().onGrabbableWeapons(weapons);
    }

    @Override
    public void discardWeapon(List<Card> weapons) {
        getUi().onDiscardWeapon(weapons);
    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {
        getUi().onGrabbedWeapon(player,weapon);
    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        getUi().onReloadedWeapon(player,weapon,discardedPowerups,totalCost);
    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {
        getUi().onReloadableWeapons(weapons);
    }

    @Override
    public void turnCreation(String currentPlayer) {
        getUi().onTurnCreation(currentPlayer);
    }

    @Override
    public void turnActions(List<String> actions) {
        getUi().onTurnActions(actions);
    }

    @Override
    public void turnEnd() {
        getUi().onTurnEnd();
    }

    @Override
    public void moveAction(SimplePlayer player) {
        getUi().onMoveAction(player);
    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {
        getUi().onMoveRequest(matrix,targetPlayer);
    }

    @Override
    public void markAction(String player, SimplePlayer target, int value) {
        getUi().onMarkAction(player, target,value);
    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        getUi().onDamageAction(player,selected,damageValue,convertedMarks);
    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        getUi().onGrabbedPowerup(player,powerup);
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        getUi().onSelectablePlayers(selectable,target);
    }

    @Override
    public void canUsePowerup() {
        getUi().onCanUsePowerup();
    }

    @Override
    public void canStopRoutine() {
        getUi().onCanStopRoutine();
    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {
        getUi().onUsableWeapons(usableWeapons);
    }

    @Override
    public void availableEffects(List<String> effects) {
        getUi().onAvailableEffects(effects);
    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        getUi().onPayEffect(player,discardedPowerups,usedAmmo);
    }

    @Override
    public void usedCard(Card card) {
        getUi().onUsedCard(card);
    }

    @Override
    public void availablePowerups(List<Card> powerups) {
        getUi().onAvailablePowerups(powerups);
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        getUi().onRunCompleted(player,newPosition);
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        getUi().onRunRoutine(matrix);
    }
}
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.RMIClientHandler;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the connection interface with the client using RMI
 */
public class RMIClientConnection extends ClientConnection{

    /**
     * This attribute represents teh server linked to the connection
     */
    private RMIServer server;

    /**
     * This attribute represents the stub sent to the client
     */
    private RMIClientHandler clientStub;

    /**
     * This attribute represents the session name
     */
    private String session;

    /**
     * This attribute represents the executor service that handle threads connection
     */
    private ExecutorService pool;

    /**
     * This constructor sets up parameters for the RMI connection
     * @param clientStub represents the stub of the client
     * @param server represents the server linked to the connection
     */
    public RMIClientConnection(RMIClientHandler clientStub,RMIServer server){
        super();
        this.clientStub=clientStub;
        this.server=server;
        pool=Executors.newFixedThreadPool(5);
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public void closeConnection() {
        server.deregister(session);
    }

    @Override
    public void keepAlive() {
        pool.submit(() -> {
            try {
                clientStub.keepAlive();
                getRoom().isAlive(this);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call keepAlive().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void joinRoomAdvise(String nickname) {
        pool.submit(() -> {
            try {
                clientStub.joinPlayer(nickname);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call joinRoomAdvise().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void recoverAdvise(String nickname) {
        Logger.logAndPrint("[RMI] RECOVER ADVISE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.recoverPlayer(nickname);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call recoverAdvise().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void canCounterAttack() {
        Logger.logAndPrint("[RMI] CAN COUNTER ATTACK to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.canCounterAttack();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call canCounterAttack().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void counterAttack(SimplePlayer currentPlayer, SimplePlayer player, Card powerup) {
        Logger.logAndPrint("[RMI] COUNTER ATTACK to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.counterAttack(currentPlayer,player,powerup);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call counterAttack().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void counterAttackTimeOut() {
        Logger.logAndPrint("[RMI] CAN COUNTER ATTACK TIMEOUT to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.counterAttackTimeOut();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call counterAttackTimeOut().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void notifyDisconnetion() {
        Logger.logAndPrint("[RMI] DISCONNECTION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.notifyDisconnection();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call notifyDisconnetion().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void gameEnd(List<SimplePlayer> players) {
        Logger.logAndPrint("[RMI] GAME END to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.gameEnd(players);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call gameEnd().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void sendLeaderboard(List<String> nicknames, List<Integer> points) {
        Logger.logAndPrint("[RMI] SEND LEADERBOARD to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.sendLeaderboard(nicknames,points);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call sendLeaderboard().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void exitRoomAdvise(String nickname) {
        pool.submit(() -> {
            try {
                clientStub.exitPlayer(nickname);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call exitRoomAdvise().");
                Logger.logErr(e.getMessage());
            }
        });
    }

    @Override
    public void firstInRoomAdvise() {
        pool.submit(() -> {
            try {
                clientStub.firstInRoom();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call firstInRoom().");
                Logger.logErr(e.getMessage());
            }
        });
    }


    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        Logger.logAndPrint("[RMI] MATCH CREATION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.matchCreation(players,playerTurnNumber);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call matchCreation().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void invalidMessageReceived(String msg) {
        Logger.logAndPrint("[RMI] INVALID MESSAGE RECEIVED to "+getNickname());
        Logger.logAndPrint(msg);
        pool.submit(() -> {
            try {
                clientStub.invalidMessageReceived(msg);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call invalidMessageReceived().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {
        Logger.logAndPrint("[RMI] BOARD UPDATE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.boardUpdate(gameBoard);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call boardUpdate().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        Logger.logAndPrint("[RMI] MATCH UPDATE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.matchUpdate(players,gameBoard,frenzy);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call matchUpdate().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void recoveringPlayer(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        Logger.logAndPrint("[RMI] RECOVERING PLAYER to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.wakeUpPlayer(players,gameBoard,frenzy);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call recoveringPlayer().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void respwanRequest(List<Card> powerups, List<Color> colors) {
        Logger.logAndPrint("[RMI] RESPAWN REQUEST to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.respwanRequest(powerups,colors);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call respwanRequest().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup, Color color) {
        Logger.logAndPrint("[RMI] RESPAWN COMPLETED to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.respwanCompleted(player,discardedPowerup,color);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call respwanCompleted().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        Logger.logAndPrint("[RMI] GRABBED TILE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.grabbedTile(player,grabbedTile);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call grabbedTile().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup, Color color) {
        Logger.logAndPrint("[RMI] GRABBED POWERUP to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.grabbedPowerup(player,powerup,color);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call grabbedPowerup().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {
        Logger.logAndPrint("[RMI] GRABBABLE WEAPONS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.grabbableWeapons(weapons);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call grabbableWeapons().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void discardWeapon(List<Card> weapons) {
        Logger.logAndPrint("[RMI] DISCARD WEAPON to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.discardWeapon(weapons);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call discardWeapon().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {
        Logger.logAndPrint("[RMI] GRABBED WEAPON to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.grabbedWeapon(player,weapon);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call grabbedWeapon().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        Logger.logAndPrint("[RMI] RELOADED WEAPON to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.reloadedWeapon(player,weapon,discardedPowerups,totalCost);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call reloadedWeapon().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {
        Logger.logAndPrint("[RMI] RELOADABLE WEAPONS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.reloadableWeapons(weapons);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call reloadableWeapons().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void turnActions(List<String> actions) {
        Logger.logAndPrint("[RMI] TURN ACTIONS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.turnActions(actions);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call turnActions().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void turnEnd() {
        Logger.logAndPrint("[RMI] TURN END to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.turnEnd();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call turnEnd().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void moveAction(SimplePlayer player) {
        Logger.logAndPrint("[RMI] MOVE ACTION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.moveAction(player);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call moveAction().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {
        Logger.logAndPrint("[RMI] MOVE REQUEST to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.moveRequest(matrix,targetPlayer);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call moveRequest().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void markAction(String player, SimplePlayer selected, int value) {
        Logger.logAndPrint("[RMI] MARK ACTION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.markAction(player,selected,value);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call markAction().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        Logger.logAndPrint("[RMI] DAMAGE ACTION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.damageAction(player,selected,damageValue,convertedMarks);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call damageAction().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        Logger.logAndPrint("[RMI] DISCARDED POWERUP to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.discardedPowerup(player,powerup);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call discardedPowerup().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void fullOfPowerup() {
        Logger.logAndPrint("[RMI] FULL OF POWERUP to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.fullOfPowerup();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call fullOfPowerup().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void turnCreation(String currentPlayer) {
        Logger.logAndPrint("[RMI] TURN CREATION to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.turnCreation(currentPlayer);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call turnCreation().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        Logger.logAndPrint("[RMI] SELECTABLE PLAYERS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.selectablePlayers(selectable,target);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call selectablePlayers().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void canUsePowerup() {
        Logger.logAndPrint("[RMI] CAN USE POWERUP to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.canUsePowerup();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call canUsePowerup().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void canStopRoutine() {
        Logger.logAndPrint("[RMI] CAN STOP ROUTINE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.canStopRoutine();
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call canStopRoutine().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {
        Logger.logAndPrint("[RMI] USABLE WEAPONS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.usableWeapons(usableWeapons);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call usableWeapons().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void availableEffects(List<String> effects) {
        Logger.logAndPrint("[RMI] AVAILABLE EFFECTS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.availableEffects(effects);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call availableEffects().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        Logger.logAndPrint("[RMI] PAY EFFECT to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.payEffect(player,discardedPowerups,usedAmmo);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call payEffect().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void usedCard(Card card) {
        Logger.logAndPrint("[RMI] USED CARD to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.usedCard(card);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call usedCard().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void availablePowerups(List<Card> powerups,List<Color> colors) {
        Logger.logAndPrint("[RMI] AVAILABLE POWERUPS to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.availablePowerups(powerups,colors);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call availablePowerups().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        Logger.logAndPrint("[RMI] RUN COMPLETED to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.runCompleted(player,newPosition);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call runCompleted().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        Logger.logAndPrint("[RMI] RUN ROUTINE to "+getNickname());
        pool.submit(() -> {
            try {
                clientStub.runRoutine(matrix);
            } catch (RemoteException e) {
                Logger.logErr("RemoteException has been thrown when call runRoutine().");
                Logger.logErr(e.getMessage());
                closeConnection();
            }
        });
    }

}
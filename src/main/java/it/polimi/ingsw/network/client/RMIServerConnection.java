package it.polimi.ingsw.network.client;

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

public class RMIServerConnection extends ServerConnection implements RMIClientHandler {
    private static final long serialVersionUID = -8477832774353743391L;

    private String session;

    private RMIServerHandler stub;

    public RMIServerConnection(AdrenalineUI ui) throws RemoteException, NotBoundException {
        super(ui);

        Registry registry = LocateRegistry.getRegistry(Constants.RMI_PORT);

        StringBuilder builder=new StringBuilder();
        builder.append("RMI registry bindings:");
        String[] regs = registry.list();
        for (String e:regs)
            builder.append(e);
        Logger.log(builder.toString());

        stub = (RMIServerHandler) registry.lookup(Constants.RMI_SERVER_NAME);
    }

    public String getSession() {
        return session;
    }

    @Override
    public String login(String nickname, String motto) {
        try {
            Registry registry = LocateRegistry.getRegistry(Constants.RMI_PORT);
            RMIClientHandler client= (RMIClientHandler) UnicastRemoteObject.exportObject(this, Constants.RMI_PORT+1);
            registry.bind(nickname,client);
            String msg=stub.login(nickname,motto,client);
            if(msg!=null) {
                session = msg;
                return Constants.MSG_SERVER_POSITIVE_ANSWER;
            }
            else
                return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        } catch (RemoteException | AlreadyBoundException e) {
            Logger.logErr(e.getMessage());
            return Constants.MSG_SERVER_NEGATIVE_ANSWER;
        }
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
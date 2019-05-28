package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.enums.Color;
import it.polimi.ingsw.network.controller.messages.SimpleBoard;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.network.controller.messages.SimpleTarget;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.MatrixHelper;

import java.rmi.RemoteException;
import java.util.List;

public class RMIClientConnection extends ClientConnection{
    private RMIServer server;
    private RMIClientHandler clientStub;
    private String session;

    public RMIClientConnection(RMIClientHandler clientStub,RMIServer server){
        super();
        this.clientStub=clientStub;
        this.server=server;
    }

    public void setSession(String session) {
        this.session = session;
    }

    @Override
    public void closeConnection() {
        server.deregister(session);
    }

    @Override
    public void joinRoomAdvise(String nickname) {
        try {
            clientStub.onJoin(nickname);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call joinRoomAdvise().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void exitRoomAdvise(String nickname) {
        try {
            clientStub.onExit(nickname);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call exitRoomAdvise().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void firstInRoomAdvise() {
        try {
            clientStub.firstInRoom();
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call firstInRoomAdvise().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void pingAdvise() {

    }

    @Override
    public void matchCreation(List<SimplePlayer> players, int playerTurnNumber) {
        try {
            clientStub.matchCreation(players,playerTurnNumber);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call matchCreation().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void invalidMessageReceived(String msg) {
        try {
            clientStub.invalidMessageReceived(msg);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call invalidMessageReceived().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void boardUpdate(SimpleBoard gameBoard) {
        try {
            clientStub.boardUpdate(gameBoard);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call boardUpdate().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void matchUpdate(List<SimplePlayer> players, SimpleBoard gameBoard, boolean frenzy) {
        try {
            clientStub.matchUpdate(players,gameBoard,frenzy);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call matchUpdate().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void respwanRequest(List<Card> powerups) {
        try {
            clientStub.respwanRequest(powerups);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call respwanRequest().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void respwanCompleted(SimplePlayer player, Card discardedPowerup) {
        try {
            clientStub.respwanCompleted(player,discardedPowerup);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call respwanCompleted().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void grabbedTile(SimplePlayer player, AmmoTile grabbedTile) {
        try {
            clientStub.grabbedTile(player,grabbedTile);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call grabbedTile().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void grabbedPowerup(SimplePlayer player, Card powerup) {
        try {
            clientStub.grabbedPowerup(player,powerup);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call grabbedPowerup().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void grabbableWeapons(List<Card> weapons) {
        try {
            clientStub.grabbableWeapons(weapons);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call grabbableWeapons().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void discardWeapon(List<Card> weapons) {
        try {
            clientStub.discardWeapon(weapons);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call discardWeapon().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void grabbedWeapon(SimplePlayer player, Card weapon) {
        try {
            clientStub.grabbedWeapon(player,weapon);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call grabbedWeapon().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void reloadedWeapon(SimplePlayer player, Card weapon, List<Card> discardedPowerups, List<Color> totalCost) {
        try {
            clientStub.reloadedWeapon(player,weapon,discardedPowerups,totalCost);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call reloadedWeapon().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void reloadableWeapons(List<Card> weapons) {
        try {
            clientStub.reloadableWeapons(weapons);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call reloadableWeapons().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void turnActions(List<String> actions) {
        try {
            clientStub.turnActions(actions);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call turnActions().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void turnEnd() {
        try {
            clientStub.turnEnd();
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call turnEnd().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void moveAction(SimplePlayer player) {
        try {
            clientStub.moveAction(player);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call moveAction().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void moveRequest(MatrixHelper matrix, String targetPlayer) {
        try {
            clientStub.moveRequest(matrix,targetPlayer);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call moveRequest().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void markAction(String player, SimplePlayer selected, int value) {
        try {
            clientStub.markAction(player,selected,value);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call markAction().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void damageAction(String player, SimplePlayer selected, int damageValue, int convertedMarks) {
        try {
            clientStub.damageAction(player,selected,damageValue,convertedMarks);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call damageAction().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void discardedPowerup(SimplePlayer player, Card powerup) {
        try {
            clientStub.discardedPowerup(player,powerup);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call discardedPowerup().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void turnCreation(String currentPlayer) {
        try {
            clientStub.turnCreation(currentPlayer);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call turnCreation().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void selectablePlayers(List<List<String>> selectable, SimpleTarget target) {
        try {
            clientStub.selectablePlayers(selectable,target);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call selectablePlayers().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void canUsePowerup() {
        try {
            clientStub.canUsePowerup();
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call canUsePowerup().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void canStopRoutine() {
        try {
            clientStub.canStopRoutine();
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call canStopRoutine().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void usableWeapons(List<Card> usableWeapons) {
        try {
            clientStub.usableWeapons(usableWeapons);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call usableWeapons().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void availableEffects(List<String> effects) {
        try {
            clientStub.availableEffects(effects);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call availableEffects().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void payEffect(SimplePlayer player, List<Card> discardedPowerups, List<Color> usedAmmo) {
        try {
            clientStub.payEffect(player,discardedPowerups,usedAmmo);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call payEffect().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void usedCard(Card card) {
        try {
            clientStub.usedCard(card);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call usedCard().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void availablePowerups(List<Card> powerups) {
        try {
            clientStub.availablePowerups(powerups);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call availablePowerups().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void runCompleted(SimplePlayer player, int[] newPosition) {
        try {
            clientStub.runCompleted(player,newPosition);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call runCompleted().");
            Logger.logErr(e.getMessage());
        }
    }

    @Override
    public void runRoutine(MatrixHelper matrix) {
        try {
            clientStub.runRoutine(matrix);
        } catch (RemoteException e) {
            Logger.logErr("RemoteException has been thrown when call runRoutine().");
            Logger.logErr(e.getMessage());
        }
    }
}

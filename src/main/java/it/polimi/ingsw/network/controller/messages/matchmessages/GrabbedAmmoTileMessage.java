package it.polimi.ingsw.network.controller.messages.matchmessages;

import it.polimi.ingsw.model.AmmoTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.controller.messages.SimplePlayer;
import it.polimi.ingsw.utils.Constants;

public class GrabbedAmmoTileMessage extends MatchMessage {
    private static final long serialVersionUID = -629740339447609218L;
    private SimplePlayer player;
    private AmmoTile grabbedTile;

    public GrabbedAmmoTileMessage(SimplePlayer player, AmmoTile grabbedTile) {
        super(null, Constants.GRABBED_TILE_MESSAGE);
        this.grabbedTile=grabbedTile;
        this.player=(player);
    }

    public SimplePlayer getPlayer() {
        return player;
    }

    public AmmoTile getGrabbedTile() {
        return grabbedTile;
    }
}

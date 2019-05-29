package it.polimi.ingsw.network.client;

import it.polimi.ingsw.ui.AdrenalineUI;

public abstract class ServerConnection {
    private static final long serialVersionUID = 8681502936497815461L;
    private AdrenalineUI ui;
    private String nickname;

    ServerConnection(AdrenalineUI ui){
        this.ui=ui;
    }

    public AdrenalineUI getUi() {
        return ui;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }


    /*------ CLIENT --> SERVER ------*/
    public abstract String login(String nickname, String motto);
}

package it.polimi.ingsw.network.controller.messages;


import java.io.Serializable;

public class LoginMessage implements Serializable {
    String nickname;
    String motto;

    public LoginMessage(String nickname,String motto){
        this.nickname=nickname;
        this.motto=motto;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMotto() {
        return motto;
    }
}

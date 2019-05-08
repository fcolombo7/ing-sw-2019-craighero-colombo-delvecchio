package it.polimi.ingsw.utils;

public class Costants {
    /* NETWORKING PARAMETERS */
    public static final int SOCKET_PORT=12345;
    public static final int RMI_PORT=12346;
    public static final String RMI_SERVER_NAME="ADRENALINE_SERVER";

    /* SOCKET MESSAGES */
    public static final String MSG_CLIENT_LOGIN="LoginRequest";
    public static final String MSG_CLIENT_CLOSE = "CloseConnectionRequest";
    public static final String MSG_SERVER_POSITIVE_ANSWER="OK";
    public static final String MSG_SERVER_NEGATIVE_ANSWER="LOGIN ERROR";
    public static final String MSG_SERVER_CLOSE = "CLOSING CONNECTION";

    /* ROOM PARAMETERS */
    public static final int ROOM_MIN_PLAYERS=2;
    public static final int ROOM_MAX_PLAYERS=5;
    public static final int WAITING_ROOM_TIMER=10; //seconds

    private Costants(){}
}

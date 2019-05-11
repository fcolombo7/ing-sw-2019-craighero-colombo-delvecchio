package it.polimi.ingsw.utils;

public class Costants {
    /*CONFIGURATION RESOURCES*/
    public static final String GAME_CONFIG_FILEPATH="src/main/Resources/config.xml";
    public static final String BOARD_FOLDER = "src/main/Resources/boards";
    public static final String BOARD1_FILEPATH="src/main/Resources/boards/board1.xml";
    public static final String BOARD2_FILEPATH="src/main/Resources/boards/board2.xml";
    public static final String BOARD3_FILEPATH="src/main/Resources/boards/board3.xml";
    public static final String BOARD4_FILEPATH="src/main/Resources/boards/board4.xml";

    /* NETWORKING PARAMETERS */
    public static final int SOCKET_PORT=12345;
    public static final int RMI_PORT=12346;
    public static final String RMI_SERVER_NAME="ADRENALINE_SERVER";

    /* SOCKET MESSAGES */
    public static final String MSG_CLIENT_LOGIN="LoginRequest";
    public static final String MSG_CLIENT_CLOSE = "CloseConnectionRequest";
    public static final String MSG_CLIENT_ANSWER = "SendAnswerRequest";
    public static final String MSG_SERVER_POSITIVE_ANSWER="OK";
    public static final String MSG_SERVER_NEGATIVE_ANSWER="LOGIN ERROR";
    public static final String MSG_SERVER_ALREADY_LOGGED ="CLIENT ALREADY LOGGED IN";
    public static final String MSG_SERVER_CLOSE = "CLOSING CONNECTION";

    /* ROOM PARAMETERS */
    public static final int ROOM_MIN_PLAYERS=1;
    public static final int ROOM_MAX_PLAYERS=2;
    public static final int WAITING_ROOM_TIMER=10; //seconds

    /*MODEL MESSAGES*/
    public static final String CREATION_MESSAGE="MATCH CREATED";
    public static final String UPDATE_MESSAGE="MATCH UPDATE";

    /*MODEL ANSWER*/
    public static final String BOARD_SETTING_ANSWER="BOARD PREFERENCE";

    private Costants(){}
}

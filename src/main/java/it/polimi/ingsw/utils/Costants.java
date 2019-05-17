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
    public static final String MSG_ROOM_RECEIVED = "RoomMessageRequest";
    public static final String MSG_SERVER_POSITIVE_ANSWER="OK";
    public static final String MSG_SERVER_NEGATIVE_ANSWER="LOGIN ERROR";
    public static final String MSG_SERVER_ALREADY_LOGGED ="CLIENT ALREADY LOGGED IN";
    public static final String MSG_SERVER_CLOSE = "CLOSING CONNECTION";

    /* ROOM PARAMETERS */
    public static final int ROOM_MIN_PLAYERS=1;
    public static final int ROOM_MAX_PLAYERS=2;
    public static final long WAITING_ROOM_TIMER=10; //seconds
    public static final long WAITING_ROOM_PONG =5;

    /*MODEL MESSAGES*/
    public static final String CREATION_MESSAGE="MATCH CREATED";
    public static final String BOARD_UPDATE_MESSAGE ="BOARD CREATED";
    public static final String UPDATE_MESSAGE="MATCH UPDATE";
    public static final String RESPAWN_REQUEST_MESSAGE ="RESPAWN REQUEST";
    public static final String RESPAWN_COMPLETED_MESSAGE ="RESPAWN REQUEST";
    public static final String INVALID_ANSWER = "INVALID ANSWER RECEIVED";
    public static final String TURN_ROUTINE_MESSAGE ="ROUTINE MESSAGE";
    public static final String TURN_AVAILABLE_ACTIONS = "AVAILABLE ACTIONS";
    public static final String AVAILABLE_POWERUPS_MESSAGE = "AVAILABLE POWERUPS";
    public static final String LOADABLE_WEAPONS_MESSAGE = "LOADABLE WEAPONS";

    /*ROUTINE MESSAGES*/
    public static final String RUN_ROUTINE_MESSAGE ="RUN ROUTINE";
    public static final String RUN_ROUTINE_COMPLETED = "RUN ROUTINE RESULT";

    /*MODEL ANSWERS*/
    public static final String BOARD_SETTING_ANSWER="BOARD PREFERENCE";
    public static final String RESPAWN_ANSWER ="RESPAWN ACTION";
    public static final String TURN_ROUTINE_ANSWER ="ROUTINE ANSWER";
    public static final String ACTION_SELECTED = "ACTION SELECTED";

    /*ROUTINE ANSWERS*/
    public static final String RUN_ROUTINE_ANSWER ="RUN ROUTINE ANSWER";

    /*ROOM MESSAGE*/
    public static final String PLAYER_EXIT ="PLAYER EXIT";
    public static final String PLAYER_JOIN ="PLAYER JOIN";
    public static final String PING_CHECK ="PING";
    public static final String PONG_ANSWER ="PONG";
    public static final String FIRST_PLAYER = "FIRST IN ROOM";

    private Costants(){}
}

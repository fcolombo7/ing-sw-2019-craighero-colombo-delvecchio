package it.polimi.ingsw.utils;

public class Constants {
    /*CONFIGURATION RESOURCES*/
    public static final boolean BUILDING_JAR = true;

    /* NETWORKING PARAMETERS */
    public static final String RMI_HOSTNAME = "localhost";
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

    public static final int ROOM_MIN_PLAYERS=2;
    public static final int ROOM_MAX_PLAYERS=3;
    public static final long WAITING_ROOM_TIMER = 30; //SEC
    public static final long KEEP_ALIVE_FREQUENCY=8;//SEC
    public static final long KEEP_ALIVE_WAITING_TIME=4;//SEC


    /* CONTROLLER PARAMETERS */
    public static final int DEFAULT_SKULL = 8;
    public static final long TURN_TIMER=120;//SEC
    public static final long QUICK_MOVE_TIMER=15;//SEC


    /*MODEL MESSAGES*/
    public static final String CREATION_MESSAGE="MATCH CREATED";
    public static final String BOARD_UPDATE_MESSAGE ="BOARD UPDATE";
    public static final String UPDATE_MESSAGE="MATCH UPDATE";
    public static final String RESPAWN_REQUEST_MESSAGE ="RESPAWN REQUEST";
    public static final String RESPAWN_COMPLETED_MESSAGE ="RESPAWN COMPLETE";
    public static final String INVALID_ANSWER = "INVALID ANSWER RECEIVED";
    public static final String TURN_ROUTINE_MESSAGE ="ROUTINE MESSAGE";
    public static final String TURN_AVAILABLE_ACTIONS = "AVAILABLE ACTIONS";
    public static final String RELOADABLE_WEAPONS_MESSAGE = "LOADABLE WEAPONS";
    public static final String EFFECT_MOVE_REQUEST_MESSAGE = "MOVE REQUEST";
    public static final String EFFECT_MARK_MESSAGE = "MARK MESSAGE";
    public static final String EFFECT_DAMAGE_MESSAGE = "DAMAGE MESSAGE";
    public static final String RUN_COMPLETED = "RUN ROUTINE RESULT";
    public static final String RELOAD_COMPLETED = "RELOAD RESULT";
    public static final String USED_CARD_MESSAGE = "USING CARD";
    public static final String EFFECT_MOVE_MESSAGE = "MOVE MESSAGE";
    public static final String PAY_EFFECT_MESSAGE = "PAY EFFECT";
    public static final String GRABBED_TILE_MESSAGE = "GRABBED AMMO TILE";
    public static final String GRABBED_WEAPON_MESSAGE = "GRABBED WEAPON";
    public static final String DISCARDED_POWERUP_MESSAGE = "DISCARDED POWERUP";
    public static final String TURN_CREATION_MESSAGE = "NEW TURN STARTED";
    public static final String TURN_END_MESSAGE = "TURN END MESSAGE";
    public static final String RECOVERING_PLAYER = "RECOVERING PLAYER";
    public static final String GAME_END_MESSAGE = "GAME END";
    public static final String LEADERBOARD_MESSAGE = "LEADERBOARD";

    /*ROUTINE MESSAGES*/
    public static final String RUN_ROUTINE_MESSAGE ="RUN ROUTINE";
    public static final String USABLE_WEAPONS_MESSAGE ="AVAILABLE WEAPONS";
    public static final String AVAILABLE_EFFECTS_MESSAGE = "AVAILABLE EFFECTS";
    public static final String AVAILABLE_POWERUPS_MESSAGE = "AVAILABLE POWERUPS";
    public static final String SELECTABLE_PLAYERS_MESSAGE = "SELECTABLE PLAYER";
    public static final String CAN_STOP_ROUTINE = "CAN STOP ROUTINE";
    public static final String CAN_USE_POWERUP = "CAN USE POWERUP";
    public static final String GRABBED_POWERUP ="GRABBED POWERUP";
    public static final String GRABBABLE_WEAPONS_MESSAGE = "GRABBABLE WEAPONS";
    public static final String DISCARD_WEAPON_MESSAGE = "DISCARD WEAPON";
    public static final String CAN_COUNTER_ATTACK = "CAN COUNTER ATTACK";
    public static final String COUNTER_ATTACK_TIMEOUT = "COUNTER ATTACK TIMEOUT" ;
    public static final String COUNTER_ATTACK_COMPLETED = "COUNTER ATTACK COMPLETED";

    /*MODEL ANSWERS*/
    public static final String BOARD_SETTING_ANSWER="BOARD PREFERENCE";
    public static final String RESPAWN_ANSWER ="RESPAWN ACTION";
    public static final String TURN_ROUTINE_ANSWER ="ROUTINE ANSWER";
    public static final String ACTION_SELECTED = "ACTION SELECTED";
    public static final String EFFECT_MOVE_ANSWER = "MOVE ANSWER";
    public static final String TURN_END_ANSWER = "TURN END ANSWER";
    public static final String CONFIRM_END_GAME = "END GAME CONFIRM";

    /*ROUTINE ANSWERS*/
    public static final String RUN_ROUTINE_ANSWER ="RUN ROUTINE ANSWER";
    public static final String WEAPON_ANSWER = "SELECTED WEAPON";
    public static final String POWERUP_ANSWER = "POWERUP SELECTED";
    public static final String LOADABLE_WEAPON_SELECTED = "LOADABLE WEAPON SELECTED";
    public static final String EFFECT_ANSWER = "SELECTED EFFECT";
    public static final String SELECTED_PLAYERS_ANSWER = "SELECTED PLAYERS";
    public static final String STOP_ROUTINE_ANSWER = "STOP ROUTINE ANSWER";
    public static final String USE_POWERUP_ANSWER = "USE POWERUP ANSWER";
    public static final String DISCARDED_WEAPON_ANSWER = "DISCARDED WEAPON";
    public static final String FULL_OF_POWERUP = "FULL OF POWERUP";
    public static final String COUNTER_ATTACK_ANSWER = "COUNTER ATTACK ANSWER";

    /*ROOM MESSAGE*/
    public static final String PLAYER_EXIT ="PLAYER EXIT";
    public static final String PLAYER_JOIN ="PLAYER JOIN";
    public static final String PING_CHECK ="PING";
    public static final String PONG_ANSWER ="PONG";
    public static final String FIRST_PLAYER = "FIRST IN ROOM";
    public static final String PLAYER_RECOVER = "PLAYER RECOVER";
    public static final String DISCONNECTION = "DISCONNECTION";


    /*----------------------------------------------------------------------------------------------------------------*/

    /*CLI CHARACTERS AND COLORS*/
    public static final String RESET = "\u001b[0m";
    public static final String	BLACK_W				= "\u001B[30m";
    public static final String	RED_W				= "\u001B[31m";
    public static final String	GREEN_W				= "\u001B[32m";
    public static final String	YELLOW_W			= "\u001B[33m";
    public static final String	BLUE_W				= "\u001b[34m";
    public static final String	MAGENTA_W			= "\u001B[35m";
    public static final String	CYAN_W				= "\u001B[36m";
    public static final String	WHITE_W				= "\u001B[37m";
    public static final String BOLD = "\u001b[1m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String REVERSE = "\u001b[7m";
    public static final String	BACKGROUND_WHITE	= "\u001B[47m";

    private static final String UH_BLOCK = "\u2580";
    private static final String DH_BLOCK = "\u2584";
    public static final String RH_BLOCK = "\u2590";
    public static final String LH_BLOCK = "\u258C";
    private static final String UT_BLOCK = "\u2594";
    private static final String DT_BLOCK = "\u2581";
    public static final String RT_BLOCK = "\u2595";
    public static final String LT_BLOCK = "\u258F";
    public static final String UR_QUAD = "\u259D";
    public static final String UL_QUAD = "\u2598";
    public static final String DR_QUAD = "\u2597";
    public static final String DL_QUAD = "\u2596";
    private static final String UR_CORNER = "\u259C";
    private static final String UL_CORNER = "\u259B";
    private static final String DR_CORNER = "\u259F";
    private static final String DL_CORNER = "\u2599";
    public static final String GUN = "\uD83D\uDD2B";
    public static final String SPAWN = "\u271D";
    public static final String AMMO = "\u2022";
    public static final String DAMAGE = "\u2B24";
    public static final String HAND = "\u270B";
    public static final String DROPLET = "\uD83D\uDCA7";
    public static final String SKULL = "\uD83D\uDC80";
    public static final String OVERKILL = "\u25CE";
    public static final String MARK = "\u2B22";
    public static final String SQUARE = "\u25A0";
    public static final String PLAYER = "\uD83D\uDEB6";
    public static final String CLAP = "\uD83D\uDC4F";
    public static final String ARROW = "\u21D2";
    public static final String FIRST = "\u2460";
    public static final String CROSS = "\u274C";

    public static final String UCLASSIC_SIDE = UL_CORNER + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UR_CORNER;
    public static final String UDOOR_SIDE = UL_CORNER + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UL_QUAD + "  " + UR_QUAD + UH_BLOCK + UH_BLOCK + UH_BLOCK + UH_BLOCK + UR_CORNER;
    public static final String USAME_ROOM_SIDE = UL_CORNER + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UT_BLOCK + UR_CORNER;
    public static final String DCLASSIC_SIDE = DL_CORNER + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DR_CORNER;
    public static final String DDOOR_SIDE = DL_CORNER + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DL_QUAD + "  " + DR_QUAD + DH_BLOCK + DH_BLOCK + DH_BLOCK + DH_BLOCK + DR_CORNER;
    public static final String DSAME_ROOM_SIDE = DL_CORNER + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DT_BLOCK + DR_CORNER;
    public static final String COL_FORMAT = "%s%s%13s";
    public static final String WEAPON_FORMAT = "%s%s%2s%s%10s";
    public static final String AMMO_FORMAT = "%s%s%2s%10s";

    private Constants(){}
}

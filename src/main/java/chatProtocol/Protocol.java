package chatProtocol;

public class Protocol {

    public static final String SERVER = "DESKTOP-3PSQNKC";
    //nombre de la maquina DESKTOP-3PSQNKC
    //maquina Bryan "DESKTOP-40SBQ7S"
    public static final int PORT = 1234;
    public static final int LOGIN=1;
    public static final int UPDATE_STATUS =11;
    public static final int LOGOUT=2;
    public static final int POST=3;
    public static final int REGISTER=4;
    public static final int CONTACT=5;
    public static final int CONTACT_RESPONSE=6;
    public static final int UNREADMESSAGES=7;
    public static final int UNREADMESSAGES_RESPONSE=8;
    public static final int DELETEREADMESSAGES=9;

    public static final int DELIVER=15;

    public static final int ERROR_NO_ERROR=0;
    public static final int ERROR_LOGIN=1;
    public static final int ERROR_LOGOUT=2;
    public static final int ERROR_POST=3;
    public static final int ERROR_REGISTER=4;
    public static final int ERROR_CONTACT=5;
    public static final int ERROR_CONTACT_EQUAL=20;
    public static final int ERROR_UNREAD_MESSAGES=7;
}
package car.tp1.ftp;

/**
 * Class CodeCmd
 * Contains all of commands accepts by the server
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class CodeCmd {

    /**
     * The command "USER"
     */
    public static final String USER = "USER";

    /**
     * The command "PASS"
     */
    public static final String PASS = "PASS";

    /**
     *The command "LIST"
     */
    public static final String LIST = "LIST";

    /**
     * The command "RETR"
     */
    public static final String RETR = "RETR";

    /**
     * The command "STOR"
     */
    public static final String STOR = "STOR";

    /**
     * The command "QUIT"
     */
    public static final String QUIT = "QUIT";

    /**
     * The command "PORT"
     */
    public static final String PORT = "PORT";

    /**
     * The command "SYST"
     */
    public static final String SYST = "SYST";

    /**
     * The command "PWD"
     */
    public static final String PWD = "PWD";

    /**
     * The command "CWD"
     */
    public static final String CWD = "CWD";

    /**
     * The command "CDUP"
     */
    public static final String CDUP = "CDUP";

    /**
     * The command "MKD"
     */
    public static final String MKD = "MKD";

    /**
     * The command "DELE"
     */
    public static final String DELE = "DELE";

    /**
     * The command "RNFO"
     */
    public static final String RNTO = "RNTO";

    /**
     * The command "RNFR"
     */
    public static final String RNFR = "RNFR";

    /**
     * The command "PASV"
     */
    public static final String PASV = "PASV";

    /**
     * The command "RMD"
     */
    public static final String RMD = "RMD";


    /**
     * End of command "END_CMD"
     */
    public static final String END_CMD = "\r\n";

    /**
     * Parent directory ".."
     */
    public static final String HOME = "..";

    /**
     * /
     */
    public static final String RACINE_DIRECTORY = "/";


}

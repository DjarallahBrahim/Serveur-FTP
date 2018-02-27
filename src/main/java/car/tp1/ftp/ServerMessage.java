package car.tp1.ftp;

/**
 * Class ServerMessage
 * Contains all of the code and messages of the server
 * This code and message will be send to the client
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class ServerMessage {

    // Message de bienvenue / Welcome message
    public static int _BIENVENU = 220;

    // Mauvaise commande / Bad command
    public static int _TOMP_ERR_CODE = 421;
    public static String _TOMP_ERR_MSG = "La commande n'a pas ete acceptee , retapez la commande";


    // Impossible d'ouvrir une connexion / Cannot open connection
    public static int _CANNOT_OPEN_CONNEXION_CODE = 425;
    public static String _CANNOT_OPEN_CONNEXION_MSG = "Impossible d'etablir une connexion de donnees.";
    public static int INVALIDE_CMD_CODE = 503;
    public static String INVALIDE_CMD_MSG = "Mauvaise séquence de commande";

    // Mauvais argument / invalid argument
    public static int ARGUMENT_INVALID_CODE = 504;
    public static String ARGUMENT_INVALID_MSG = "Les arguments saisis sont invalides.";

    // Command USER
    public static int _ASK_PASS_CODE = 331;
    public static String _ASK_PASS_MSG = "Entrez votre mot de passe : ";
    public static int USER_INVALID_CODE = 530;
    public static String USER_INVALID_MSG = "L'utilisateur n'existe pas.";


    // Command PASS
    public static int PASS_TRUE_CODE = 230;
    public static String PASS_TRUE_MSG = "Bienvenue sur le serveur ftp";
    public static int PASS_FALSE_CODE = 530;
    public static String PASS_FALSE_MSG = "Le mot de passe saisi n'est pas correct.";

    // Command LIST
    public static int START_SEND_FILEOK_CODE = 150;
    public static String START_SEND_FILEOK_MSG = "Here comes the directory listing";
    public static int ENDSEND_FILEOK_CODE = 226;
    public static String ENDSEND_FILEOK_MSG = "Directory send OK.";


    // Command MKDIR
    public static int CREAT_FOLDOK_CODE = 257;
    public static String CREAT_FOLDOK_MSG = "est cree";
    public static int CREAT_FOLDKO_CODE = 550;
    public static String CREAT_FOLDKO_MSG = "Impossible de creer le repertoire %s ";
    public static String CREAT_FOLDEXICTE_MSG="Le nom de dossier existe deja";

    // Command SYST
    public static int SYST_CODE = 215;
    public static String SYST_MSG = "UNIX type: l8\n";


    // Command PWD
    public static int PWD_OK_CODE = 257;

    // Commande CWD
    public static int CWD_OK_CODE = 250;
    public static String CWD_OK_MSF = "Vous etes dans le repertoire ";
    public static int CWD_KO_CODE = 550;
    public static String CWD_KO_MSF = "Le repertoire n'existe pas";

    // Command CDUP
    public static int CDUP_OK_CODE = 200;
    public static String CDUP_OK_MSG = "Repertoire parent";
    public static int CDUP_KO_CODE = 550;
    public static String CDUP_KO_MSG = "Erreur d'acces";


    // Commande DELE
    public static int DELE_OK_CODE = 200;
    public static String DELE_OK_MSG = "Répertoire supprime";
    public static int DELE_KO_CODE = 550;
    public static String DELE_KO_MSG = "Impossible de supprimer le fichier";
    public static int DELE_ERR_CODE = 550;
    public static String DELE_ERR_MSG = "Le fichier n'existe pas";

    // Command QUIT
    public static int QUIT_OK_CODE = 221;
    public static String QUIT_OK_MSG = "Deconnexion";

    // Command PORT
    public static int PORT_OK_CODE = 200;
    public static String PORT_OK_MSG = "Port ouvert ";
    public static int PORT_KO_CODE = 425;
    public static String PORT_KO_MSG = "Impossible d'etablir une connexion de donnees. ";

    // Commmand RNFR
    public static int RNFR_FILEOK_CODE = 350;
    public static String RNFR_FILEOK_MSG = "En attente d'informations complementaires";
    public static int RNFR_FILEKO_CODE = 550;
    public static String RNFR_FILEKO_MSG = "Fichier non trouvé";
    public static String RNFR_FILEKO_MSG_ERRO = "Probleme durant le renommage";

    // Command RNTO
    public static int RNTO_FILEOK_CODE = 250;
    public static String RNTO_FILEOK_MSG = "Renommage du fichier executee avec succes.";
    public static int RNTO_FILEKO_CODE = 550;
    public static String RNTO_FILEKO_MSG = "Renommage du fichier non execute ";

    // Command STOR
    public static int STOR_START_CODE = 125;
    public static String STOR_START_MSG = "Debut du transfert";
    public static int STOR_END_CODE = 226;
    public static String STOR_END_MSG = "Fin du transfert";
    public static int ERROR_STOR_CODE = 451;
    public static String ERROR_STOR_MSG = "Une erreur est survenue";

    // Command ERROR
    public static int ERROR_CONNEX_DONNE_CODE = 425;
    public static String ERROR_CONNEX_DONNE_MSG = "Impossible d'etablir une connexion de donnees.";

    // Command PASV
    public static int PASSIV_OK_CODE = 227;
}

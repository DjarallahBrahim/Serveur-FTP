package car.tp1.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class Serveur
 * This class is the main class of the Server FTP implemented.
 *
 * To change the parameters of the server, please go to Param Class.
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */


public class Serveur {

    private static final Logger logger = LoggerFactory.getLogger(Serveur.class);

    public static void main(String args[])  {

        logger.debug("Starting debugging");

        Serveur.launchServeur(args);
    }

    /**
     * Main methods that launch the server ftp
     */
    public static void launchServeur(final String [] args)
    {
        Param mParam = Param.getInstance();

        if(args.length>0){
            File fileRoot = new File(args[0]); // Répertoire ROOT
            if(fileRoot.exists()){
                mParam.setRepCourant(args[0]); // Répertoire courant
                logger.info("[Server] Server Path accepted");
            }else{
                logger.error("[Server] Server Path not found");
                logger.info("[Server] Server Default Path set");
            }
        }else{
            File fileRoot = new File(System.getProperty("user.home")); // Répertoire ROOT
            if(fileRoot.exists()){
                mParam.setRepCourant(System.getProperty("user.home")); // Répertoire courant
                logger.info("[Server] Server Default Path set");
            }else{
                logger.error("[Server] Problem with server path");
            }
        }

        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(mParam.getPort());
            System.out.print("Serveur attend des client sur le port : "+ mParam.getPort());
        }catch(IOException e){
            System.out.println("&_Serveur_& Problème avec creation FTP server");
            e.printStackTrace();
            return;
        }


        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (final IOException e) {
                System.out
                        .println("Serveur:>$ Impossible de créer une socket pour une session ftp.");
                e.printStackTrace();
            }
            System.out.printf("Serveur:>$ nouveau client [%s]\n",
                    socket.getInetAddress());
            FtpRequest ftpRequest;
            try {
                ftpRequest = new FtpRequest(socket);
                ftpRequest.start();
            } catch (final IOException e) {
                System.out
                        .println("Serveur:>$ Impossible d'initialisé une nouvelle session ftp");
                e.printStackTrace();
            }

        }
    }
}

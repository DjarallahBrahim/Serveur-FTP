package car.tp1.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class FtpRequest
 * Process an FTP Session
 * This class receives messages received by the server and will process them.
 * (Calls to the class CmdControleur)
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 **/

public class FtpRequest extends Thread{

    /**
     * Socket of the client (socket return by the server)
     */
    private Socket mSocket;

    /**
     * IP address of the client
     */
    private final InetAddress cIpAddress;


    /**
     * Parameters of the ftp server
     */
    private Param mParam;

    /**
     * Username of the client
     */
    private String userName;

    /**
     * Username status. Checks if the username exists in the database
     */
    private boolean isExisteName;

    /**
     * A buffer that contains the orders received
     */
    private final BufferedReader mBuffer;

    /**
     * Login Status. Checks if client is authentificated
     */
    private boolean isLogin;

    /**
     * The client's data channel socket
     */
    private Socket dataSocketClt;

    /**
     * The server's data channel socket
     */
    private ServerSocket dataSocketServ;

    /**
     * OutputStream of data channel socket
     */
    private OutputStream dataStream;

    /**
     * OutputStream of data channel socket
     */
    private final OutputStream clientStream;

    /**
     * The current path of the ftp session
     */
    private String currentPath;

    /**
     * The root path (root directory) of the ftp session
     */
    private String root;

    /**
     * Checks if the client has send command "QUIT"
     */
    private boolean quit;

    /**
     * Log all of the commands, actions, requests
     */
    private static final Logger logger = LoggerFactory.getLogger(FtpRequest.class);

    /**
     * Constructor of FtpRequest
     *
     * @param socket the socket of the Client
     * @throws IOException throws when when socket initialization do an error
     */
    public FtpRequest(final Socket socket) throws IOException {
        this.mParam = Param.getInstance();
        this.mSocket = socket;
        this.cIpAddress = socket.getInetAddress();
        this.userName = null;
        this.isLogin = false;
        this.isExisteName=false;
        this.currentPath = this.mParam.getRepCourant();
        this.mParam.setRoot(this.currentPath);
        this.root=this.currentPath;
        final InputStream is = socket.getInputStream();
        final InputStreamReader inReader = new InputStreamReader(is);
        this.mBuffer = new BufferedReader(inReader);
        this.clientStream = this.mSocket.getOutputStream();
        this.quit = false;
        logger.info("Starting debugging new session of ftpRequest [ip] ={} [port] {} ",this.cIpAddress,this.mParam.getPort());
    }

    /**
     * Method to launch the Thread
     * (Launch method runRequest)
     */
    @Override
    public void run() {
        this.runRequest();
    }

    /**
     * Check if the client is authentificated
     *
     * @param password password of the client
     * @return true if username+password is valid, false otherwise
     */
    public boolean isAuthenticated(final String password) {
        if(!this.isLogin){
            final Users usersInfo= new Users();
            this.isLogin=usersInfo.login(this.userName,password);
            logger.info("[{}] client is isAuthenticated",this.userName);
        }
        return this.isLogin;
    }


    /**
     * Receives commands and calls method to process them
     */
    public void runRequest() {
        final CmdControleur controleur = new CmdControleur(this);

        this.sendResponse(this.mParam.getBienvenu(),ServerMessage._BIENVENU);         // Affichage du message d'acceuil
        String command = null;
        while (this.quit != true) {         // Traitement du gros de la session
            command = this.extractLine();
            if (command == null) {
                logger.error("[{}] client send null command",this.userName);
                this.sendResponse(ServerMessage._TOMP_ERR_MSG,
                        ServerMessage._TOMP_ERR_CODE);
                this.quit = true;
            } else {
                logger.info("[{}] client send \"{}\" command",this.userName,command);
                controleur.executCommand(command);
            }
        }
        try {
            this.mSocket.close();
            logger.info("[{}] server cancelled connexion ",this.userName);
        } catch (final IOException e) {
//            System.out.printf(
//                    "[%s] Erreur lors de la fermeture du socket du client.\n",
//                    this.userName);
            logger.error("[{}] server could not cancel the connexion ",this.userName);
        }
        logger.info("[{}] end of session for [{}] ",this.userName,this.userName);
        //System.out.printf("[%s] Fin de la session.\n", this.userName);
    }

    /**
     * Gets the command from mBuffer
     *
     * @return the command send by the client
     */
    private String extractLine() {
        String command = null;
        try {
            command = this.mBuffer.readLine();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return command;
    }

    /**
     * Send response to the client
     *
     * @param ReplayCode Code of the response
     * @param ReplayMessage Message/Content of the response
     */
    public void sendResponse(String ReplayMessage , int ReplayCode ){
        final String response =ReplayCode + " "
                + ReplayMessage + "\r\n";
        try {
            this.clientStream.write(response.getBytes());
            this.clientStream.flush();
            logger.info("[server] send message : [{}] ",response);
        } catch (final IOException e) {
//            System.out.printf(
//                    "[%s] Erreur lors de la fermeture du socket du client.\n",
//                    this.userName);
            logger.error("[server] could not send : [{}] ",response);
        }
    }

    /**
     * Send data to the client (by data channel)
     *
     * @param message data to send
     */
    public void envDonnee(final String message){
        if(!this.dataSocketClt.isClosed()) {
            try {
                this.dataStream = new DataOutputStream(this.dataSocketClt.getOutputStream());
                this.dataStream.write(message.getBytes());
                this.dataStream.flush();
                logger.info("[server] send data : [{}] ",message);
            } catch (IOException e) {
                logger.error("[server] could not  send the data : [{}] ",message);
            }
            //close data canal
            this.fermeConnexion();
        }else{
            this.sendResponse(ServerMessage.ERROR_CONNEX_DONNE_MSG,ServerMessage.ERROR_CONNEX_DONNE_CODE);
        }
    }

    /**
     * Open a connection (of data channel)
     * (Socket data channel client and outputStream of data channel socket)
     *
     * @param port port of the socket to open
     */
    public boolean ouvrirConnexion(int port) {

        try {

            this.dataSocketClt = new Socket(this.cIpAddress,port);
            this.dataStream = dataSocketClt.getOutputStream();
            logger.info("[{}] Client open a data canal : [{}] ",this.userName,this.dataSocketClt.getPort());

            return true;
        } catch (final IOException e) {
            logger.error("[{}] Client could not open a data canal : [{}] ",this.userName);
            this.sendResponse(ServerMessage._CANNOT_OPEN_CONNEXION_MSG
                    ,ServerMessage._CANNOT_OPEN_CONNEXION_CODE);
            return false;
        }
    }

    /**
     * Close connection (of data channel)
     * (Socket data channel client and outputStream of data channel socket)
     */
    public void fermeConnexion() {

        if(this.dataSocketClt!=null && !this.dataSocketClt.isClosed()) {
            try {
                this.dataSocketClt.close();
                logger.info("[server] close data canal. ");
            } catch (final IOException e) {
//                System.out
//                        .printf("Erreur lors de la fermeture le socket du canal de données de l'utilisateur %s\n",
//                                this.userName);
                logger.error("[server] could not close data canal. ");

            }
            this.dataSocketClt = null;
            this.dataStream = null;
        }
    }


    /**
     * Generate the response of the command PASV
     * (format : 127,0,0,1,147,211)
     */
    public String generatePasvRepense() {
        try {
            this.dataSocketServ=new ServerSocket(0);
            final int port=this.dataSocketServ.getLocalPort();
            final int xx=port/256;
            final int yy=port%256;

            final String ipAdress = this.cIpAddress.toString().replace('/',' ').replace('.', ',');

            final String ipPort=ipAdress+","+xx+","+yy;
            logger.info("[server] generated a  data canal : {} .",ipPort);

            return ipPort;
        } catch (IOException e) {
            logger.error("[server] could not generate a  data canal .");
            return null;
        }
    }

    /**
     * Accept the data port suggest by the server
     */
    public void acceptPortDonne() {
        try {
            this.dataSocketClt=this.dataSocketServ.accept();
            logger.info("[{}] Client accept the data canal [{}] proposed by the server.",this.userName,
                    this.dataSocketServ.getLocalPort());

        } catch (IOException e) {
            //e.printStackTrace();
            logger.error("[{}] Client could not accept the data canal [{}] proposed by the server.",
                    this.userName,this.dataSocketServ.getLocalPort());

        }
    }
    /**
     * Gets the InputStream Object of dataSocketClt (= data channel socket of client)
     *
     * @return InputStream object
     */
    public InputStream getInputStream(){
        try {
            final InputStream inP=this.dataSocketClt.getInputStream();
            logger.info("[server] get the Input Stream");
            return inP;
        } catch (IOException e) {
            logger.error("[server] could not get the Input Stream");
            return  null;
        }
    }

    /**
     * Gets the OutputStream of dataSocketClt (= data channel socket of client)
     *
     * @return OutputStream object
     */
    public OutputStream getOutputStream(){
        try {
            final OutputStream outP=this.dataSocketClt.getOutputStream();
            logger.info("[server] get the Output Stream");
            return outP;
        } catch (IOException e) {
            logger.error("[server] could not get the Output Stream");

            return  null;
        }
    }

    /**
     * Gets the size of the sendBuffer of dataSocketClt (= data channel socket of client)
     * @return the size of sendBuffer
     */
    public int getSendBufferSize(){
        try {
            final int sizeSendBuffer=this.dataSocketClt.getSendBufferSize();
            logger.info("[server] get the size of Buffer sended");
            return sizeSendBuffer;
        } catch (SocketException e) {
            logger.error("[server] could not get the size of Buffer sended");
            return 0;
        }
    }

    /**
     * Set the username of the client
     *
     * @param userName username of the client
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Get the username of the client of the ftp session
     *
     * @return username of the client
     */
    public String getUserName() {
        return userName;
    }


    /**
     * Get the current path of the serveur
     * @return the current path
     */
    public String getCurrentPath() {
        return currentPath;
    }

    /**
     * Set the current path
     * @param currentPath the current path of server
     */
    public void setCurrentPath(String currentPath) {
        this.currentPath=currentPath;
        logger.info("[server] the new current path is : {}",currentPath);
    }

    /**
     * Set the root directory of the session ftp
     * @param newroot the new root directory
     */
    public void setRoot(final String newroot) {
        this.mParam.setRoot(newroot);
        logger.info("[server] the new root is : {}",newroot);
    }

    /**
     * Get the current root directory
     * @return the current root directory
     */
    public String getRoot() {
        return this.mParam.getRoot();
    }

    /**
     * Set attribute quit to true
     * (= client quit the server)
     */
    public void quit(){
        this.quit=true;
    }


}

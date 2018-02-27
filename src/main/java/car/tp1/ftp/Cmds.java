package car.tp1.ftp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Class Cmds
 * Contains all methods to process commands received
 * (Methods are launched from Class CmdControleur)
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class Cmds {

    private  FtpRequest ftpRequest;
    private  CmdControleur cmdControleur;
    private  String argument;
    public   String argumentSuplimentaire;

    /**
     * Constructor of class Cmds
     * @param ftpRequest Current object FtpRequest
     * @param cmdControleur Current object CmdControleur
     */
    public Cmds(final FtpRequest ftpRequest,
                final CmdControleur cmdControleur) {
        this.ftpRequest = ftpRequest;
        this.cmdControleur = cmdControleur;
        this.argument="";
        this.argumentSuplimentaire="";

    }

    /**
     * Gets the argument of the command received
     * @param cmd argument of command received
     */
    private void recupArgument(String cmd){
        if(!cmd.isEmpty())
            this.argument=cmd;
    }

    /**
     * Process the ftp command "USER"
     * Checks if login is valid
     * @param cmd username of the user
     */
    public void userCmd(final String cmd) {
        this.recupArgument(cmd);
        final String userName = this.argument;
        final Users usersInfo= new Users();
        final boolean isExisteName=usersInfo.chercheName(userName);

        if(isExisteName){
            this.ftpRequest.setUserName(userName);
            this.ftpRequest.sendResponse(ServerMessage._ASK_PASS_MSG,
                    ServerMessage._ASK_PASS_CODE);
            this.cmdControleur.setOkLogin(true);
        }else {
            this.ftpRequest.sendResponse(ServerMessage.USER_INVALID_MSG, ServerMessage.USER_INVALID_CODE);
        }

    }

    /**
     * Process the ftp command "PASS"
     * Checks if the login and password are valid
     * @param cmd password of the user
     */
    public void processPass(final String cmd) {
        this.recupArgument(cmd);
        final String password =this.argument;

        if (this.ftpRequest.isAuthenticated(password)) {
            //Dériger chaque user vers leur dossier si non crée un dossier
            File userRep=new File(this.ftpRequest.getCurrentPath()+File.separator); //+this.ftpRequest.getUserName());
            if(userRep.exists()) {
                this.ftpRequest.setCurrentPath(userRep.getPath());
                this.ftpRequest.setRoot(userRep.getPath());
                this.ftpRequest.sendResponse(ServerMessage.PASS_TRUE_MSG
                        + this.ftpRequest.getUserName(), ServerMessage.PASS_TRUE_CODE);
                this.cmdControleur.setOkPassword(true);
            }else{
                if(userRep.mkdir()) {
                    this.ftpRequest.setCurrentPath(userRep.getPath());
                    this.ftpRequest.setRoot(userRep.getPath());
                    this.ftpRequest.sendResponse(ServerMessage.PASS_TRUE_MSG
                            + this.ftpRequest.getUserName(), ServerMessage.PASS_TRUE_CODE);
                    this.cmdControleur.setOkPassword(true);
                }
            }
        } else {
            this.ftpRequest.sendResponse(ServerMessage.PASS_FALSE_MSG
                    + this.ftpRequest.getUserName(), ServerMessage.PASS_FALSE_CODE);
        }

    }

    /**
     * Process the ftp command "LIST"
     * List the current path (of the server) and its subdirectories
     */
    public void processList() throws IOException {
        final File curentFile = new File(this.ftpRequest.getCurrentPath());

        final File[] childFiles= curentFile.listFiles();

        String listContenu="";

        for (File file : childFiles){
            listContenu+= listFile(file) +CodeCmd.END_CMD;
        }

        this.ftpRequest.sendResponse(ServerMessage.START_SEND_FILEOK_MSG, ServerMessage.START_SEND_FILEOK_CODE);
        this.ftpRequest.envDonnee(listContenu);
        this.ftpRequest.sendResponse(ServerMessage.ENDSEND_FILEOK_MSG, ServerMessage.ENDSEND_FILEOK_CODE);
    }
    /**
     * Process the ftp command "MKDIR"
     * Create a new folder in the current path (of the server)
     * @param cmd name of the new folder
     */
    public void passMkdir(final String cmd) {
        this.recupArgument(cmd);
        File dir= new File(this.ftpRequest.getCurrentPath()+File.separator+this.argument);

        if(this.argument!=""){
            if(dir.exists()){
                this.ftpRequest.sendResponse( this.argument+" " + ServerMessage.CREAT_FOLDEXICTE_MSG,
                        ServerMessage.CREAT_FOLDKO_CODE);
                return;
            }
            boolean created= dir.mkdir();
            if(created){
                this.ftpRequest.sendResponse(this.argument+" " + ServerMessage.CREAT_FOLDOK_MSG ,
                        ServerMessage.CREAT_FOLDOK_CODE);
            }else{
                this.ftpRequest.sendResponse( this.argument+" " + ServerMessage.CREAT_FOLDKO_MSG,
                        ServerMessage.CREAT_FOLDKO_CODE);
            }
        }
    }
    /**
     * Process the ftp command "SYST"
     * (Only send valid response)
     */
    public void passSyst() {

        //String listContenu= " "+System.getProperty("os.name")+"\r\n";
        this.ftpRequest.sendResponse(ServerMessage.SYST_MSG,
                ServerMessage.SYST_CODE);
    }

    /**
     * Process the ftp command "PWD"
     * Sends to the server the current path
     */
    public void processPwd() {
        this.ftpRequest.sendResponse("\""+this.ftpRequest.getCurrentPath()+"\"",
                ServerMessage.PWD_OK_CODE);
        //ServerMessage.PWD_OK_CODE = 257
    }

    /**
     * Process the ftp command "CWD"
     * Change the working directory of the server
     * @param cmd the new working directory
     */
    public void processCWD(String cmd) {

        this.recupArgument(cmd);
        if(this.argument.equals(CodeCmd.HOME)){
            this.processCdup(cmd);
            return;
        }
        if(!this.argument.startsWith("/") && this.argument!=""){
            boolean exist=new File(this.ftpRequest.getCurrentPath()+File.separator+this.argument).exists();
            if(exist){
                this.ftpRequest.setCurrentPath(this.ftpRequest.getCurrentPath()+File.separator+this.argument);
                this.ftpRequest.sendResponse(this.ftpRequest.getCurrentPath(), ServerMessage.CWD_OK_CODE);
                return;
            }else{
                this.ftpRequest.sendResponse("Aucun fichier ou dossier de ce type", ServerMessage.CWD_KO_CODE);
                return;
            }
        }if(stopRoot(this.argument)){
            boolean exist=new File(this.argument).exists();
            if(exist){

                this.ftpRequest.setCurrentPath(this.argument);
                this.ftpRequest.sendResponse(ServerMessage.CWD_OK_MSF, ServerMessage.CWD_OK_CODE);
                return;
            }else{
                this.ftpRequest.sendResponse("Le fichier n'existe pas ", ServerMessage.CWD_KO_CODE);
                return;
            }

        }else{
            this.ftpRequest.sendResponse("Erreur d'acces ", ServerMessage.CWD_KO_CODE);

        }
    }
    /**
     * Process the ftp command "CDUP"
     * Change current path to parent directory
     * @param cmd parent directory
     */
    public void processCdup(String cmd) {
        this.recupArgument(cmd);

        final String dirName= this.ftpRequest.getCurrentPath();
        final File currentDir=new File(dirName);
        String pérePath=currentDir.getParent();


        if(pérePath==null){
            this.ftpRequest.sendResponse("Le chemin est vide", ServerMessage.CWD_KO_CODE);
            return;
        }
        if(stopRoot(pérePath)) {
            this.ftpRequest.setCurrentPath(pérePath);
            this.ftpRequest.sendResponse(ServerMessage.CDUP_OK_MSG, ServerMessage.CDUP_OK_CODE);
            //this.ftpRequest.envDonnee("Current Path est : " + this.ftpRequest.getCurrentPath() + "\n\r");
        }else{
            System.out.println("NO 2");
            this.ftpRequest.sendResponse("Erreur d'acces", ServerMessage.CDUP_KO_CODE);
        }
    }

    /**
     * Process the ftp command "RMD"
     * Delete a directory
     * @param cmd name of directory to delete
     */
    public void processRMD(String cmd){
        this.recupArgument(cmd);
        final String fileDelet=this.argument;
        final File deletedFile=new File(this.ftpRequest.getCurrentPath()+File.separator+fileDelet);

        if(deletedFile.exists()){
            if(deletedFile.delete()){
                this.ftpRequest.sendResponse(ServerMessage.DELE_OK_MSG,ServerMessage.DELE_OK_CODE);
            }else{
                //cant delet
                this.ftpRequest.sendResponse(ServerMessage.DELE_KO_MSG,ServerMessage.DELE_KO_CODE);

            }
        }else{
            //non trouvé
            this.ftpRequest.sendResponse(ServerMessage.DELE_ERR_MSG,ServerMessage.DELE_ERR_CODE);

        }
    }
    /**
     * Process the ftp command "DELE"
     * Delete a file
     * @param cmd name of file to delete
     */
    public void processDELE(String cmd){
        this.recupArgument(cmd);
        final String fileDelet=this.argument;
        final File deletedFile=new File(this.ftpRequest.getCurrentPath()+File.separator+fileDelet);

        if(deletedFile.exists()){
            if(deletedFile.delete()){
                this.ftpRequest.sendResponse(ServerMessage.DELE_OK_MSG,ServerMessage.DELE_OK_CODE);
            }else{
                //cant delet
                this.ftpRequest.sendResponse(ServerMessage.DELE_KO_MSG,ServerMessage.DELE_KO_CODE);
            }
        }else{
            //non trouvé
            this.ftpRequest.sendResponse(ServerMessage.DELE_ERR_MSG,ServerMessage.DELE_ERR_CODE);
        }
    }


    /**
     * Process the ftp command "PORT"
     * Specify an address and a connection port.
     * @param cmd the adresse and connection port (format : 127,0,0,1,147,211)
     */
    public void processPort(String cmd){
        this.recupArgument(cmd);
        //decouper message de client en ip,port (x,xx,xx,xx,xx,xx)
        final String [] demandeClient = this.argument.split(",");
        final int lengthDemandeClient=demandeClient.length;
        final int partie1Port=Integer.parseInt(demandeClient[lengthDemandeClient-2]);
        final int partie2Port=Integer.parseInt(demandeClient[lengthDemandeClient-1]);

        final int port = partie1Port*256 + partie2Port;
        this.ftpRequest.fermeConnexion();
        this.ftpRequest.ouvrirConnexion(port);
        this.ftpRequest.sendResponse(ServerMessage.PORT_OK_MSG,ServerMessage.PORT_OK_CODE);
        //this.ftpRequest.ouvrirConnexion();
    }

    /**
     * Process the ftp command "STOR"
     * Store a file on the server ftp
     * @param cmd name of file to store
     */
    public void processSTOR(String cmd) throws Exception {
        final File nouveauFichier = new File(this.ftpRequest.getCurrentPath() + File.separator
                + cmd);

        nouveauFichier.createNewFile();

        this.ftpRequest.sendResponse(ServerMessage.STOR_START_MSG,ServerMessage.STOR_START_CODE);

        final FileOutputStream fileOut = new FileOutputStream(nouveauFichier);

        try {
            final InputStream donnéeEntrer = this.ftpRequest.getInputStream();

            final byte[] buf = new byte[this.ftpRequest.getSendBufferSize()];
            int lire = 0;
            while ((lire = donnéeEntrer.read(buf)) != -1) {
                fileOut.write(buf, 0, lire);
            }
            this.ftpRequest.fermeConnexion();
            fileOut.close();
            donnéeEntrer.close();
            this.ftpRequest.sendResponse(ServerMessage.STOR_END_MSG, ServerMessage.STOR_END_CODE);
        }catch (IOException e){
            this.ftpRequest.sendResponse(ServerMessage.ERROR_STOR_MSG, ServerMessage.ERROR_STOR_CODE);
        }
    }

    /**
     * Process the ftp command "RETR"
     * Download a copy of a file (of the server)
     * @param cmd name of file to download
     */
    public void processRETR(String cmd) throws Exception {
        final File nouveauFichier = new File(this.ftpRequest.getCurrentPath() + File.separator
                + cmd);
        nouveauFichier.createNewFile();

        this.ftpRequest.sendResponse(ServerMessage.STOR_START_MSG,ServerMessage.STOR_START_CODE);

        FileInputStream fileOut = new FileInputStream(nouveauFichier);

        try {
            final OutputStream donnée = this.ftpRequest.getOutputStream();
            final byte[] buf = new byte[this.ftpRequest.getSendBufferSize()];

            int lire = 0;

            while ((lire = fileOut.read(buf)) >0) {
                donnée.write(buf, 0, lire);
            }

            this.ftpRequest.fermeConnexion();
            fileOut.close();
            donnée.close();
            this.ftpRequest.sendResponse(ServerMessage.STOR_END_MSG, ServerMessage.STOR_END_CODE);

        }catch (IOException e){
            this.ftpRequest.sendResponse(ServerMessage.ERROR_STOR_MSG, ServerMessage.ERROR_STOR_CODE);
        }
    }

    /**
     * Process the ftp command "QUIT"
     * Disconnects client from the server
     */
    public void processExit(){
        this.ftpRequest.sendResponse(ServerMessage.QUIT_OK_MSG,ServerMessage.QUIT_OK_CODE);
        this.ftpRequest.quit();
    }


    /**
     * Process the ftp command "PASV"
     * Change the server connection in passive mode.
     */
    public void processPasv() throws Exception {
        final String respens=this.ftpRequest.generatePasvRepense();
        this.ftpRequest.sendResponse(respens,ServerMessage.PASSIV_OK_CODE);
        this.ftpRequest.acceptPortDonne();

    }

    /**
     * Process the ftp command "RNFR"
     * File to rename (rename from)
     * @param cmd current name of the file
     */
    public void processRNFR(String cmd){
        this.cmdControleur.setArgumentSuplimentaire(cmd);
        this.ftpRequest.sendResponse(ServerMessage.RNFR_FILEOK_MSG,ServerMessage.RNFR_FILEOK_CODE);
    }

    /**
     * Process the ftp command "RNTO"
     * Rename to (rename to)
     * @param cmd new name of the file
     */
    public void processRNTO(String cmd){
        this.argumentSuplimentaire=this.cmdControleur.getArgumentSuplimentaire();
        this.argument=cmd;
        File oldName=new File(this.ftpRequest.getCurrentPath()+File.separator
                +this.argumentSuplimentaire);
        File newNAme=new File(this.ftpRequest.getCurrentPath()+File.separator
                +this.argument);

        if(!newNAme.exists()){
            boolean renommé=oldName.renameTo(newNAme);
            if(renommé){
                this.ftpRequest.sendResponse(ServerMessage.RNTO_FILEOK_MSG,ServerMessage.RNTO_FILEOK_CODE);
            }else {
                this.ftpRequest.sendResponse(ServerMessage.RNTO_FILEKO_MSG,ServerMessage.RNTO_FILEKO_CODE);
            }
        }else{
            this.ftpRequest.sendResponse(ServerMessage.RNTO_FILEKO_MSG,ServerMessage.RNTO_FILEKO_CODE);
        }
    }

    /**
     * Checks that the client isn't going over the root directory
     *@param newPath the new path requested by the client
     */
    private boolean stopRoot(String newPath){
        newPath+="/";
        return newPath.startsWith(this.ftpRequest.getRoot().toString());
    }

    /**
     * Get meta data of file to list it
     *@param file
     *@return meta data of file
     */
    private String listFile(final File file) throws IOException {


        final Path pathFile = Paths.get(file.toURI());

        final PosixFileAttributes posixFileAttributes = Files.readAttributes(pathFile,PosixFileAttributes.class);

        final BasicFileAttributes basicFileAttributes = Files.readAttributes(pathFile, BasicFileAttributes.class);
        final FileTime fileTime=basicFileAttributes.creationTime();


        String typeFile;

        if(posixFileAttributes.isDirectory())
            typeFile="d";
        else
            typeFile="-";

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd  HH:mm", Locale.ENGLISH);
        //System.out.println(attr.creationTime());
        String DateToStr = dateFormat.format(fileTime.toMillis());
        return typeFile + PosixFilePermissions.toString(posixFileAttributes.permissions())+" "
                +posixFileAttributes.owner()+" "
                +posixFileAttributes.group() + " "
                +file.length() + " "
                + DateToStr + " "
                + file.getName();
    }
}

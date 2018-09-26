package car.tp1.ftp;

import java.io.IOException;
import java.util.Arrays;
/**
 * Class CmdControleur :
 * This class receives messages from class FtpRequest
 * and will start the methods (of class Cmds)
 * according to the message ( = command) received by FtpRequest
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class CmdControleur {

    /**
     * Login status (True if login is valid, false otherwise)
     */
    private boolean okLogin;

    /**
     * Password status (True if password is valid, false otherwise)
     */
    private boolean okPassword;

    /**
     * Argument of ftp command received
     */
    private    String argumentSuplimentaire;

    /**
     * Current FtpRequest object
     */
    private FtpRequest ftpRequest;

    /**
     * Current Cmds object
     */
    protected Cmds cmdExecute;

    /**
     * Constructor of CmdControleur
     *
     * @param ftpRequest Current FtpRequest object
     */
    public CmdControleur(final FtpRequest ftpRequest) {
        this.ftpRequest = ftpRequest;
        this.okPassword = false;
        this.okLogin = false;
    }

    /**
     * Call the specific method of the command received
     *
     * @param command command to execute
     */
    public void executCommand(final String command) {

        final String [] lineCmd=command.split(" ");
        final String cmd=lineCmd[0].toUpperCase();         //Gets command
        String[] arguments = Arrays.copyOfRange(lineCmd, 1, lineCmd.length);         //Gets arguments

        String argumentsSend = "";         //Transform arguments of type String[] to type String
        if(arguments.length>=1) {
            for (int k = 0; k < arguments.length; k++)
                argumentsSend += arguments[k] + " ";
            argumentsSend = argumentsSend.substring(0, argumentsSend.length() - 1);    //supprimer le dernier espace " "
        }

        setCmdExecute();

        if(cmd.equals(CodeCmd.USER)) {
            if (this.okLogin == false) {
                cmdExecute.userCmd(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.PASS)){
            if (this.okLogin == true && this.okPassword == false) {
                cmdExecute.processPass(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else   if(cmd.equals(CodeCmd.LIST)){
            if (this.okPassword == true) {
                try {
                    cmdExecute.processList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.MKD)) {
            if (this.okPassword == true) {
                cmdExecute.passMkdir(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.SYST)) {
            if (this.okPassword == true) {
                cmdExecute.passSyst();
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.PWD)) {
            if (this.okPassword == true) {
                cmdExecute.processPwd();
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.CWD)) {
            if (this.okPassword == true) {
                cmdExecute.processCWD(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.CDUP)) {
            if (this.okPassword == true) {
                cmdExecute.processCdup(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.RNFR)) {
            if (this.okPassword == true) {
                cmdExecute.processRNFR(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.RNTO)) {
            if (this.okPassword == true) {
                cmdExecute.processRNTO(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.DELE)) {
            if (this.okPassword == true) {
                cmdExecute.processDELE(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else  if(cmd.equals(CodeCmd.RMD)) {
            if (this.okPassword == true) {
                cmdExecute.processRMD(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.PORT)) {
            if (this.okPassword == true) {
                cmdExecute.processPort(argumentsSend);
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.QUIT)) {
            if (this.okPassword == true) {
                cmdExecute.processExit();
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.PASV)) {
            if (this.okPassword == true) {
                try {
                    cmdExecute.processPasv();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.STOR)) {
            if (this.okPassword == true) {
                try {
                    cmdExecute.processSTOR(argumentsSend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else if(cmd.equals(CodeCmd.RETR)) {
            if (this.okPassword == true) {
                try {
                    cmdExecute.processRETR(argumentsSend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                this.ftpRequest.sendResponse(ServerMessage.INVALIDE_CMD_MSG,
                        ServerMessage.INVALIDE_CMD_CODE);
            }
        }
        else{
            this.ftpRequest.sendResponse("",200);
        }
    }


    /**
     * Set the current object Cmds
     */
    public void setCmdExecute() {
        this.cmdExecute=new Cmds(ftpRequest,this);
    }

    /**
     * Set attribute okLogin
     * @param okLogin (boolean)
     *
     */
    public void setOkLogin(final boolean okLogin) {
        this.okLogin = okLogin;
    }

    /**
     * Set attribute okPassword
     * @param okPassword (boolean)
     **/
    public void setOkPassword(final boolean okPassword) {
        this.okPassword = okPassword;
    }

    /**
     * Check if password is valid
     * @return true is password is valid, false otherwise
     */
    public boolean isOkPassword() {
        return this.okPassword;
    }

    /**
     * Get the argument of command received
     * @return argument of command
     */
    public String getArgumentSuplimentaire() {
        return argumentSuplimentaire;
    }

    /**
     * Set the argument of command received
     * @param argumentSuplimentaire argument receive
     */
    public void setArgumentSuplimentaire(String argumentSuplimentaire) {
        this.argumentSuplimentaire = argumentSuplimentaire;
    }
}

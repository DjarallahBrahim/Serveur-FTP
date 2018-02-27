package car.tp1.ftp;

/**
 * Class Param
 *
 * Contains the parameters of the ftp server
 * You can easily edit them !
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class Param {

    /**
     * Instance of parameters
     */
    private static Param instance=null;

    /**
     * Port of the server
     */
    private final int port=8899;

    /**
     * Port of data channel of the server
     */
    private final int portD=14100;

    /**
     * Welcome message of the server
     */
    private final String bienvenu="Bienvenue sur le FTP :";

    /**
     * Current directory of the server
     */
    private String repCourant;

    /**
     * Root directory of the server
     */
    private String root;

    /**
     * Root status - Checks if the root directory is save
     * */
    private boolean isRootSave;

    /**
     * Constructor of Param
     */
    public Param(){
        this.repCourant=System.getProperty("user.home");
        this.root=System.getProperty("user.home");
        this.isRootSave=true;
    }

    /**
     * Gets the instance of the parameters of the server
     * @return a new object Param if it is not already defined
     */
    synchronized public static Param getInstance() {
        if (Param.instance == null) {
            Param.instance = new Param();
        }
        return Param.instance;
      //  return new Param();
    }

    /**
     * Gets the port of the server
     *
     * @return port of the server
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Gets the welcome message of the server
     *
     * @return welcome message
     */
    public String getBienvenu() {
        return this.bienvenu;
    }

    /**
     * Gets the current path of the server
     * @return current path
     */
    public String getRepCourant(){
        return this.repCourant;
    }

    /**
     * Set the current path of the server
     * @param repCourant current path
     */
    public void setRepCourant(String repCourant){

        this.repCourant=repCourant;
        }

    /**
     * Get the current root directory of the server
     * @return current root directory
     */
    public String getRoot() {
        return this.root;
    }

    /**
     * Set the root directory of the server
     */
    public void setRoot(String root) {
        this.root = root;
    }

    /**
     * Get the port of data channel of the server
     * @return port of data channel of the server
     */
    public int getPortD() {
        return portD;
    }

}

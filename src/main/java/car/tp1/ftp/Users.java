package car.tp1.ftp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class Users
 * Represents a client of the server
 *
 * @author Brahim DJARALLAH et Romain LAMOOT
 * @version 1.0
 */
public class Users {

    /**
     * The path of the file which contains the informations (username + password) of all the clients
     * (format : user1;pass1;user2;pass2;user3;pass3;...)
     */
    private String pathUserInfo = "users.txt";

    /**
     * Contains the line of the file
     */
    private String[] line;

    /**
     * Checks if the username entered by the client exists in the file
     * @param user username to check
     * @return true if username exists, false otherwise
     */
    public boolean chercheName(String user){
        this.line = openFile(pathUserInfo);
        for(int i = 0; i < line.length; i+=2)
        {
            if(user.equals(line[i]))
            {
                System.out.println("User ok");
                return true;
            }
        }
        System.out.println("User NON ok");//Connexion ok
        return false;
    }

    /**
     * Checks if the username and the password entered by the client is correct
     * @param user username of the client
     * @param pass password of the client
     * @return true if username and password are valid, false otherwise
     */
    public boolean login(String user, String pass){
        line = openFile(pathUserInfo);
        for(int i = 1; i < line.length; i+=2)
        {
            if(pass.equals(line[i]) && user.equals(line[i-1]))
            {
                System.out.println("Password ok");
                return true;
            }
        }
        System.out.println("Password NON ok");//Connexion No ok
        return false;
    }


    /**
     * Open file which contains the usernames and passwords
     * @param path file "users.txt" which contains users informations
     * @return String[] contains {user1,passeword1,user2,password2 ...}
     */
    public String[] openFile(String path){
        BufferedReader lecteurAvecBuffer = null;
        String ligne;
        String[] line = {""};
        try
        {
            lecteurAvecBuffer = new BufferedReader(new FileReader(path));
        }
        catch(FileNotFoundException exc)
        {
            System.out.println("Le fichier n'a pas pu être ouvert.");
            return null;
        }
        try {
            while ((ligne = lecteurAvecBuffer.readLine()) != null) {
                if((ligne == "") || (ligne == "\n")|| (ligne.length() == 0))
                {
                    break;
                }
                line = ligne.split(";");
            }
            lecteurAvecBuffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

}

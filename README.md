### TP1 - CAR - Serveur FTP    
######  Brahim DJARALLAH, Romain LAMOOT     
###### 21/02/2018    
_____________________________________

### Introduction

Ce projet implémente un serveur FTP et respecte les normes du [RFC 959](https://www.ietf.org/rfc/rfc959.txt).
Il implémente les fonctions de base (et nécessaire) au bon fonctionnement du serveur avec un client FTP tel que FileZilla.

Ce projet a été réalisé dans le cadre de l'unité d'enseignement de [CAR](http://www.fil.univ-lille1.fr/portail/index.php?dipl=MInfo&sem=S8&ue=CAR&label=Pr%C3%A9sentation) (Construction d'Applications Réparties) de l'université de Lille 1. (Master1)

Les commandes FTP implémentées sont :
  - USER username
  - PASS password
  - LIST
  - MKDIR newfolder
  - SYST
  - PWD
  - CWD folder
  - CDUP folder
  - RMD folder
  - DELE file
  - PORT data
  - STOR file
  - RETR file
  - PASV
  - RNFR oldname
  - RNTO newname
  - QUIT


##### Compilation / Execution
 - Pour compiler
    * __mvn package__

 - Pour générer la Javadoc
    * __mvn javadoc:javadoc__

 - Pour exécuter le jar exécutable (= lancer le serveur)
    * __java -jar target/CAR-TP1-1.0-SNAPSHOT-jar-with-dependencies.jar__



### Architecture
  - __Package__ : car.tp1.ftp
  - __Classes__
      * __Serveur__ : la MainClass
      * __ServerMessage__ : contient les messages et code réponses relatifs au serveur
      * __Param__ : contient les paramètres (port, chemin ,root, etc) du serveur
      * __Users__ : permet de représenter un client du serveur
      * __CodeCmd__ : contient toutes les commandes implémentées dans le serveur
      * __Cmds__ : permet de traiter toutes les commandes du serveur
      * __CmdControleur__ : contrôle la réponse reçu et fait appel à la bonne méthode de Cmds
      * __FtpRequest__ : reçoit les commandes du client et les transmet au CmdControleur


  - Bibliothèque utilisée pour les logs :
      * [Log4j](https://logging.apache.org/log4j/2.0/)
      * Les fichiers de configurations se trouve dans le dossier src/main/resources


  - Les tests se trouvent dans le dossier src/test + package :
      * __Classes de tests__ :
         * __CmdControleurUnitTest__ : tests unitaires de la classe CmdControleur
         * __CmdsUnitTest__ : tests unitaires de la classe Cmds
         * __UsersTest__ : tests unitaires de la classe Users
         * __CmdTest__ : tests d'intégrations de la classe Cmd
         * __FtpRequestTest__ : tests d'intégrations de la classe FtpRequestTest


##### Design pattern
  - La classe "Param" utilise le pattern design __"Singleton"__


##### Gestion d'erreurs
 - Les résolutions d'erreurs (catch) renvoient un message écrit dans un fichier de log (mylogs.log) qui contient tous les actions du serveur et du client (connexion, déconnexion, erreurs, commandes effectuées, etc)



### Code Samples

##### Extrait 1
Pour envoyer les données du serveur vers le client par le canal de données,
on transmet par ce canal, par exemple, la liste du dossier. (Avec exemple d'utilisation des logs)

```java
public void envDonnee(final String message){   
       if(!this.dataSocketClt.isClosed()) {     
           try {      
               this.dataStream = new  DataOutputStream(this.dataSocketClt.getOutputStream());
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
```

##### Extrait 2
La méthode processPort traite la commande ftp PORT, c'est le client qui spécifie l'adresse et le port de connexion. On traite l'adresse et le port reçu pour ouvrir le port de données correspondant

```java
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
```

##### Extrait 3
La méthode generatePasvRepense génére un canal de données et permet au serveur d'accepter le mode passif. Elle renvoie un port de canal de données valide.
Cette méthode assure la connexion multiple de clients au serveur (chaque client a un port de données).

```java
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
```

##### Extrait 4
La méthode processSTOR traite la commande STOR, elle va mettre en ligne sur le serveur le fichier (dossier) choisi (en local) par le client.
On crée un nouveau fichier (dossier) dans le dossier courant du serveur, on lit les données du client transmis par le canal de données et on enregistre dans le nouveau fichier créé.
```java
public void processSTOR(String cmd) throws Exception {
        final File nouveauFichier = new File(this.ftpRequest.getCurrentPath() + File.separator + cmd);
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
```

##### Extrait 5

La méthode listFile permet de lister un dossier (suite à la demande du client) et renvoie la liste en respectant le format accepté par les clients FTP (ex fileZilla), le format similaire à la commande ls -al.
On récupère les droits, le propriétaire, groupe, taille, date et heures de créations et noms des fichiers.

```java
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
  ```

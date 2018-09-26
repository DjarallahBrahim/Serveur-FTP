package car.tp1.ftp;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

public class CmdsUnitTest {

    static Param p = new Param();
    static Socket socketClient;
    static Cmds cmds;
    static MockFtpRequest ftp = null;
    static CmdControleur cmdControleur;

    @Before
    public void beforeClass()
    {
        socketClient = null;
        try {
            socketClient = new Socket("127.0.0.1",p.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmdControleur = new CmdControleur(ftp);
        try {
            ftp = new MockFtpRequest(socketClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmds = new Cmds(ftp, cmdControleur);
    }

    @Test
    public void userCmdTest()
    {
        System.out.println("<----- USER CMD TEST ------>");
        /* Teste user valide */
        ftp.setReplayMessageWaiting(ServerMessage._ASK_PASS_MSG);
        ftp.setReplayCodeWaiting(ServerMessage._ASK_PASS_CODE);
        cmds.userCmd("romain");

        /* Teste user invalid */
        ftp.setReplayMessageWaiting(ServerMessage.USER_INVALID_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.USER_INVALID_CODE);
        cmds.userCmd("jbazegz");
        assertNotNull(cmds);

    }

    @Test
    public void passCmdTest()
    {
        System.out.println("<----- PASS CMD TEST ------>");

        ftp.setUserName("romain");
        ftp.setReplayMessageWaiting(ServerMessage.PASS_FALSE_MSG + ftp.getUserName());
        ftp.setReplayCodeWaiting(ServerMessage.PASS_FALSE_CODE);
        cmds.processPass("sam1");


        ftp.setReplayMessageWaiting(ServerMessage.PASS_TRUE_MSG + ftp.getUserName());
        ftp.setReplayCodeWaiting(ServerMessage.PASS_TRUE_CODE);
        cmds.processPass("sam2");
    }

    @Test
    public void processListTest()
    {
        System.out.println("<---- PROCESS LIST TEST ---->");
        ftp.setReplayMessageWaiting(ServerMessage.START_SEND_FILEOK_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.START_SEND_FILEOK_CODE);

        try {
            cmds.processList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
    // public void passMkdirTest()
    // {
    //     System.out.println("<---- PASS MKDIR TEST ---->");
    //     ftp.setReplayMessageWaiting("Documents " + ServerMessage.CREAT_FOLDEXICTE_MSG);
    //     ftp.setReplayCodeWaiting(ServerMessage.CREAT_FOLDKO_CODE);
    //
    //     cmds.passMkdir("Documents");
    //
    //     final File deletedFile=new File(ftp.getCurrentPath()+File.separator+"Nouveau Dossier-RomainBrahim");
    //     if(deletedFile.exists()) deletedFile.delete();
    //
    //     ftp.setReplayMessageWaiting("Nouveau Dossier-RomainBrahim " + ServerMessage.CREAT_FOLDOK_MSG);
    //     ftp.setReplayCodeWaiting(ServerMessage.CREAT_FOLDOK_CODE);
    //     cmds.passMkdir("Nouveau Dossier-RomainBrahim");
    // }


    @Test
    public void passSystTest()
    {
        System.out.println("<---- PASS SYST TEST ---->");
        ftp.setReplayCodeWaiting(ServerMessage.SYST_CODE);
        ftp.setReplayMessageWaiting(ServerMessage.SYST_MSG);

        cmds.passSyst();
    }


    @Test
    public void processPwd()
    {
        System.out.println("<---- PROCESS PWD TEST ---->");
        ftp.setReplayMessageWaiting("\""+ftp.getCurrentPath()+"\"");
        ftp.setReplayCodeWaiting(ServerMessage.PWD_OK_CODE);
        cmds.processPwd();
    }
    //
    // @Test
    // public void processCWDTest()
    // {
    //     System.out.println("<---- PROCESS CWD TEST ---->");
    //     ftp.setReplayCodeWaiting(ServerMessage.CWD_KO_CODE);
    //     ftp.setReplayMessageWaiting("Aucun fichier ou dossier de ce type");
    //
    //     cmds.processCWD("m573ohgirz");
    //     ftp.setReplayCodeWaiting(ServerMessage.CWD_KO_CODE);
    //     ftp.setReplayMessageWaiting("Erreur d'acces");
    //     cmds.processCWD("..");
    //
    //     ftp.setReplayMessageWaiting(ftp.getCurrentPath() + File.separator + "Documents");
    //     ftp.setReplayCodeWaiting(ServerMessage.CWD_OK_CODE);
    //     cmds.processCWD("Documents");
    // }

    // @Test
    // public void processCdupTest()
    // {
    //     System.out.println("<---- PROCESS CDUP TEST ---->");
    //     ftp.setReplayCodeWaiting(ServerMessage.CDUP_KO_CODE);
    //     ftp.setReplayMessageWaiting("Erreur d'acces");
    //     cmds.processCWD("..");
    //
    //
    //     ftp.setReplayMessageWaiting(ftp.getCurrentPath() + File.separator + "Documents");
    //     ftp.setReplayCodeWaiting(ServerMessage.CWD_OK_CODE);
    //     cmds.processCWD("Documents");
    //     ftp.setReplayMessageWaiting(ServerMessage.CDUP_OK_MSG);
    //     ftp.setReplayCodeWaiting(ServerMessage.CDUP_OK_CODE);
    //     cmds.processCdup("..");
    //
    // }

//    @Test
//    public void processRMDTest()
//    {
//        System.out.println("<---- PROCESS RMD TEST ---->");
//        ftp.setReplayMessageWaiting(ServerMessage.DELE_ERR_MSG);
//        ftp.setReplayCodeWaiting(ServerMessage.DELE_ERR_CODE);
//
//        cmds.processRMD("mlnhziper");
//
//        ftp.setReplayCodeWaiting(ServerMessage.DELE_OK_CODE);
//        ftp.setReplayMessageWaiting(ServerMessage.DELE_OK_MSG);
//        File file = new File(ftp.getCurrentPath()+File.separator + "fichier_test");
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        cmds.processRMD(file.getName());
//    }

    @Test
    public void processDELETest()
    {
        System.out.println("<---- PROCESS RMD TEST ---->");
        ftp.setReplayMessageWaiting(ServerMessage.DELE_ERR_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.DELE_ERR_CODE);

        cmds.processRMD("mlnhziper");

        ftp.setReplayCodeWaiting(ServerMessage.DELE_OK_CODE);
        ftp.setReplayMessageWaiting(ServerMessage.DELE_OK_MSG);
        File file = new File(ftp.getCurrentPath()+File.separator + "fichier_test");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cmds.processRMD(file.getName());
    }

    @Test
    public void processPortTest()
    {
        System.out.println("<---- PROCESS PORT TEST ---->");

        ftp.setReplayMessageWaiting(ServerMessage.PORT_OK_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.PORT_OK_CODE);
        cmds.processPort("127,0,0,1,147,211");
    }

    @Test
    public void processSTORTest()
    {
        System.out.println("<---- PROCESS STOR TEST ---->");

        File deletedFile = new File(ftp.getCurrentPath()+File.separator+"new_file.txt");
        if(deletedFile.exists()) deletedFile.delete();

        ftp.setReplayCodeWaiting(ServerMessage.STOR_START_CODE);
        ftp.setReplayMessageWaiting(ServerMessage.STOR_START_MSG);
        try {
            cmds.processSTOR("new_file.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void processRETRTest()
    {
        System.out.println("<---- PROCESS RETR TEST ---->");

        File deletedFile = new File(ftp.getCurrentPath()+File.separator+"new_fileRETR.txt");
        if(deletedFile.exists()) deletedFile.delete();

        ftp.setReplayCodeWaiting(ServerMessage.STOR_START_CODE);
        ftp.setReplayMessageWaiting(ServerMessage.STOR_START_MSG);

        try {
            cmds.processRETR("new_fileRETR.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void processExitTest()
    {
        System.out.println("<---- PROCESS EXIT TEST ---->");
        ftp.setReplayMessageWaiting(ServerMessage.QUIT_OK_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.QUIT_OK_CODE);

        cmds.processExit();
    }

    @Test
    public void processPasvTest()
    {
        System.out.println("<---- PROCESS PASV TEST ---->");

        ftp.setReplayCodeWaiting(ServerMessage.PASSIV_OK_CODE);
        ftp.setReplayMessageWaiting("VALID");

        try {
            cmds.processPasv();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void processRNFRTest()
    {
        System.out.println("<---- PROCESS RNFR TEST ---->");

        ftp.setReplayCodeWaiting(ServerMessage.RNFR_FILEOK_CODE);
        ftp.setReplayMessageWaiting(ServerMessage.RNFR_FILEOK_MSG);

        cmds.processRNFR("new_file.txt");

    }

    @Test
    public void processRNTOTest()
    {
        System.out.println("<---- PROCESS RNTO TEST ---->");
        ftp.setReplayMessageWaiting(ServerMessage.RNTO_FILEKO_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.RNTO_FILEKO_CODE);
        cmds.processRNTO("new_file.txt");

        ftp.setReplayMessageWaiting(ServerMessage.RNTO_FILEOK_MSG);
        ftp.setReplayCodeWaiting(ServerMessage.RNTO_FILEOK_CODE);

        cmdControleur.setArgumentSuplimentaire("new_file.txt");
        cmds.processRNTO("rename_fileRNTO.txt");
    }


    @AfterClass
    public static void afterClass()
    {
        File deletedFile = new File(ftp.getCurrentPath()+File.separator+"Nouveau Dossier-RomainBrahim");
        if(deletedFile.exists()) deletedFile.delete();

        File deletedFile2 = new File(ftp.getCurrentPath()+File.separator+"new_file.txt");
        if(deletedFile2.exists()) deletedFile2.delete();

        File deletedFile3 = new File(ftp.getCurrentPath()+File.separator+"new_fileRETR.txt");
        if(deletedFile3.exists()) deletedFile3.delete();

        File deletedFile4 = new File(ftp.getCurrentPath()+File.separator+"rename_fileRNTO.txt");
        if(deletedFile4.exists()) deletedFile4.delete();
    }

    private static class MockFtpRequest extends car.tp1.ftp.FtpRequest
    {
        String ReplayMessageWaiting = "";
        int ReplayCodeWaiting;

        /**
         * nouveau FTPRequest crée par le ServerSocket.
         *
         * @param socket le socket "utilisateur"
         * @throws IOException en cas d'erreur d'initialisation du socket.
         */
        public MockFtpRequest(Socket socket) throws IOException {
            super(socket);
        }

        @Override
        public void sendResponse(String ReplayMessage , int ReplayCode ){
//            System.out.println("<--- sendResponse -->");
//            System.out.println(ReplayMessage);
//            System.out.println(ReplayCode);

            assertEquals(getReplayMessageWaiting(),ReplayMessage);
            assertEquals(getReplayCodeWaiting(),ReplayCode);

        }

        @Override
        public void envDonnee(String content)
        {
            assertTrue(1 == 1);
            setReplayCodeWaiting(ServerMessage.ENDSEND_FILEOK_CODE);
            setReplayMessageWaiting(ServerMessage.ENDSEND_FILEOK_MSG);
        }

        @Override
        public void fermeConnexion() {
            setReplayMessageWaiting(ServerMessage.STOR_END_MSG);
            setReplayCodeWaiting(ServerMessage.STOR_END_CODE);
            assertTrue(1 == 1);
        }

        @Override
        public boolean ouvrirConnexion(int port) {
            setReplayMessageWaiting(ServerMessage.PORT_OK_MSG);
            setReplayCodeWaiting(ServerMessage.PORT_OK_CODE);
            assertTrue(1 == 1);
            return true;
        }

        @Override
        public InputStream getInputStream() {
            InputStream anyInputStream = new ByteArrayInputStream("fakedata".getBytes());
            return anyInputStream;
        }

        @Override
        public OutputStream getOutputStream() {
            return new PipedOutputStream();
        }

        @Override
        public int getSendBufferSize() {
            return 8;
        }

        @Override
        public String generatePasvRepense() {
            return "VALID";
        }

        @Override
        public void acceptPortDonne() {
            assertTrue(1 == 1);
        }

        public String getReplayMessageWaiting() {
            return ReplayMessageWaiting;
        }

        public void setReplayMessageWaiting(String replayMessageWaiting) {
            ReplayMessageWaiting = replayMessageWaiting;
        }

        public int getReplayCodeWaiting() {
            return ReplayCodeWaiting;
        }

        public void setReplayCodeWaiting(int replayCodeWaiting) {
            ReplayCodeWaiting = replayCodeWaiting;
        }

    }

}

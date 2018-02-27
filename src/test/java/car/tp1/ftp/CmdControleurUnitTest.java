package car.tp1.ftp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class CmdControleurUnitTest {

    Param p = new Param();
    Socket socketClient;
    MockCmds cmds = null;
    MockFtpRequest ftp = null;
    MockCmdControleur cmdControleur;

    @Before
    public void beforeClass()
    {
        socketClient = null;
        try {
            socketClient = new Socket("127.0.0.1",p.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ftp = new MockFtpRequest(socketClient);
            cmdControleur = new MockCmdControleur(ftp);
            cmds = new MockCmds(ftp, cmdControleur);
            cmdControleur.setCmdExecute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void constructorTest()
    {
        assertNotNull(ftp);
        assertNotNull(cmds);
        assertNotNull(cmdControleur);
    }

    @Test
    public void commandUserTest()
    {
        System.out.println("<-------- commandUserTest -------->");
        cmdControleur.setOkLogin(true);
        cmdControleur.executCommand("USER romain");

        cmdControleur.setOkLogin(false);
        cmdControleur.executCommand("USER romain");
    }

    @Test
    public void commandPassTest()
    {
        System.out.println("<-------- commandPassTest -------->");
        cmdControleur.setOkLogin(true);
        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("PASS pass");

        cmdControleur.setOkLogin(true);
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("PASS sam2");
    }

    @Test
    public void commandListTest()
    {
        System.out.println("<-------- commandListTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("LIST");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("LIST");
    }

    @Test
    public void commandMkdirTest()
    {
        System.out.println("<-------- commandMkdirTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("MKDIR Nouveau");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("MKDIR NewFolder");

    }

    @Test
    public void commandSystTest()
    {
        System.out.println("<-------- commandSystTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("SYST");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("SYST");

    }

    @Test
    public void commandPwdTest()
    {
        System.out.println("<-------- commandPwdTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("PWD");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("PWD");
    }

    @Test
    public void commandCwdTest()
    {
        System.out.println("<-------- commandCwdTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("CWD Images");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("CWD Documents");
    }

    @Test
    public void commandCdupTest()
    {
        System.out.println("<-------- commandCdupTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("CDUP Documents");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("CDUP Bureau");
    }

    @Test
    public void commandRnfrTest()
    {
        System.out.println("<-------- commandRnfrTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("RNFR newfile");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("RNFR file1");
    }

    @Test
    public void commandRntoTest()
    {
        System.out.println("<-------- commandRntoTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("RNTO newname");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("RNTO filerename");
    }

    @Test
    public void commandDeleTest()
    {
        System.out.println("<-------- commandDeleTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("DELE file");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("DELE file_to_erase");
    }

    @Test
    public void commandRmdTest()
    {
        System.out.println("<-------- commandRmdTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("RMD MonRep");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("RMD MyFolder");
    }

    @Test
    public void commandPortTest()
    {
        System.out.println("<-------- commandPortTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("PORT 0,0,0,0,0,0");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("PORT 127,0,0,1,147,211");
    }

    @Test
    public void commandQuitTest()
    {
        System.out.println("<-------- commandQuitTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("QUIT");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("QUIT");
    }

    @Test
    public void commandPasvTest()
    {
        System.out.println("<-------- commandPasvTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("PASV");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("PASV");
    }

    @Test
    public void commandStorTest()
    {
        System.out.println("<-------- commandStorTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("STOR myFile");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("STOR FileToStore");
    }

    @Test
    public void commandRetrTest()
    {
        System.out.println("<-------- commandRetrTest -------->");
        cmdControleur.setOkPassword(false);
        cmdControleur.executCommand("RETR myFile");

        cmdControleur.setOkPassword(true);
        cmdControleur.executCommand("RETR FileToGet");
    }


    private class MockCmds extends Cmds
    {
        /**
         * @param ftpRequest
         * @param cmdControleur
         */
        public MockCmds(MockFtpRequest ftpRequest, MockCmdControleur cmdControleur) {
            super(ftpRequest, cmdControleur);
        }

        @Override
        public void userCmd(String cmd) {
            assertEquals("romain", cmd);
        }

        @Override
        public void processPass(String cmd) {
            assertEquals("sam2", cmd);
        }

        @Override
        public void processList() throws IOException {
            assertTrue(1 == 1);
        }

        @Override
        public void passMkdir(String cmd) {
            assertEquals("NewFolder", cmd);
        }

        @Override
        public void passSyst() {
            assertTrue(1 == 1);
        }

        @Override
        public void processPwd() {
            assertTrue(1 == 1);
        }

        @Override
        public void processCWD(String cmd) {
            assertEquals("Documents", cmd);
        }

        @Override
        public void processCdup(String cmd) {
            assertEquals("Bureau", cmd);
        }

        @Override
        public void processRNFR(String cmd) {
            assertEquals("file1", cmd);
        }

        @Override
        public void processRNTO(String cmd){
            assertEquals("filerename", cmd);
        }

        @Override
        public void processDELE(String cmd) {
            assertEquals("file_to_erase", cmd);
        }

        @Override
        public void processRMD(String cmd) {
            assertEquals("MyFolder", cmd);
        }

        @Override
        public void processPort(String cmd) {
            assertEquals("127,0,0,1,147,211",cmd);
        }

        @Override
        public void processExit() {
            assertTrue(1 == 1);
        }

        @Override
        public void processPasv() throws Exception {
            assertTrue(1 == 1);
        }

        @Override
        public void processSTOR(String cmd) throws Exception {
            assertEquals("FileToStore", cmd);
        }

        @Override
        public void processRETR(String cmd) throws Exception {
            assertEquals("FileToGet", cmd);
        }
    }

    private class MockFtpRequest extends car.tp1.ftp.FtpRequest {
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
        public void sendResponse(String ReplayMessage, int ReplayCode) {
            assertTrue(1 == 1);
        }

        public void setReplayMessageWaiting(String replayMessageWaiting) {
            this.ReplayMessageWaiting = replayMessageWaiting;
        }

        public void setReplayCodeWaiting(int replayCodeWaiting) {
            this.ReplayCodeWaiting = replayCodeWaiting;
        }
    }

    private class MockCmdControleur extends CmdControleur
    {

        /**
         * nouveau CmdControleur
         *
         * @param ftpRequest La FTPRequest courente.
         */
        public MockCmdControleur(FtpRequest ftpRequest) {
            super(ftpRequest);
        }

        @Override
        public void setCmdExecute() {
            this.cmdExecute = new MockCmds(ftp, cmdControleur);
        }
    }
}


























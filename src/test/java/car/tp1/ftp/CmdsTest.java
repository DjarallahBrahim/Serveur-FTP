package car.tp1.ftp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;


public class CmdsTest {

    static Param p = new Param();
    Cmds cmds;
    static Socket myServerSocket = null;
    static ServerSocket myServer;
    static Thread myThread;
    static Socket socketClient;
    static FtpRequest ftpRequest;
    CmdControleur cmdControleur;


    @BeforeClass
    public static void before()
    {
        System.out.println("@Before CMDSTEST");
        myThread = new Thread(){
            public void run()
            {
                try {
                    myServer = new ServerSocket(p.getPort()+3);
                    myServerSocket = myServer.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    @Test
    public void constructorTest()
    {
        System.out.println("Constructor Test CMDSTEST");
        synchronized (this) {
            try {
                socketClient = new Socket("127.0.0.1", p.getPort()+3);

                ftpRequest = new FtpRequest(socketClient);
                ftpRequest.start();
                cmdControleur = new CmdControleur(ftpRequest);
                this.cmds = new Cmds(ftpRequest, cmdControleur);

                assertNotNull(this.cmds);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

}

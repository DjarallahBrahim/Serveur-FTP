package car.tp1.ftp;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FtpRequestTest {

    static Param p = Param.getInstance();
    static Socket myServerSocket = null;
    static ServerSocket myServer;
    static Thread myThread;
    static Socket socketClient;
    static FtpRequest ftpRequest;

    @BeforeClass
    public static void before()
    {
        System.out.println("@Before");
        try {
            myServer = new ServerSocket(p.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Serveur : attend une connexion...");
        myThread = new Thread(){
            public void run()
            {
                System.out.println("Run Thread...");
                try {
                    myServerSocket = myServer.accept();
                    System.out.println("Serveur : connexion acceptée...");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

        System.out.println("Connexion d'un client");
        try {
            socketClient = new Socket("127.0.0.1", p.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ftpRequest = new FtpRequest(socketClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Start de ftpRequest");
        ftpRequest.start();
    }

    @Test
    public void isAuthenticatedTest()
    {
        System.out.println("isAuthenticatedTest");

        synchronized (this) {

                assertFalse(ftpRequest.isAuthenticated("romain"));
                ftpRequest.setUserName("romain");
                assertFalse(ftpRequest.isAuthenticated("sam1"));
                assertTrue(ftpRequest.isAuthenticated("sam2"));

//                ftpRequest.quit();
//                ftpRequest.interrupt();
        }

    }

//    @Test
//    public void runRequestTest()
//    {
//        System.out.println("runRequestTest");
//        synchronized (this){
//            try {
//                Socket socketClient = new Socket("127.0.0.1", p.getPort());
//                assertNotNull(socketClient);
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Test
//    public void sendResponseTest()
//    {
//        System.out.println("sendResponseTest");
//
//        assertNotNull(ftpRequest);
//        System.out.println("UUUUUUUUUUUUUUUUUUUUSER =========> " + ftpRequest.getUserName());
//
//        ftpRequest.sendResponse(p.getBienvenu(), ServerMessage._BIENVENU);
//
//        try {
//            final InputStream donnee = socketClient.getInputStream();
//            final byte[] buf = new byte[socketClient.getSendBufferSize()];
//
//            int content;
//
//            while ((content = donnee.read(buf)) >0) {
//                System.out.println(content);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }



    @Test
    public void test2()
    {
        assertNotNull(this.p);
    }

    @AfterClass
    public static void afterall()
    {
//        try {
//            myServerSocket.close();
//            myServer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

}


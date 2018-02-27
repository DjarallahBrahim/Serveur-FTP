package car.tp1.ftp;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import junit.framework.*;


public class UsersTest {

    private Users user1;
    private Users user2;

    @Before
    public void before()
    {
        this.user1 = new Users();
        this.user2 = new Users();
    }

    @Test
    public void chercherNameTest()
    {
        assertTrue(user1.chercheName("romain"));
        assertFalse(user1.chercheName("roman"));
    }

    @Test
    public void loginTest()
    {
        assertTrue(user1.login("romain", "sam2"));
        assertFalse(user1.login("romain", "sam1"));
    }
}

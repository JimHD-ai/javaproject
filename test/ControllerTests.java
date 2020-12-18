import static org.junit.Assert.*;

import core.Controller;
import core.Customer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import javax.naming.ldap.Control;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ControllerTests {
    static Controller c;
    public static void main(String[] args) {

    }
    @BeforeClass
    public static void init(){
        c = new Controller();
        c.customers.add(new Customer("petrakis", "oxi", "athina", "Student", "katialliws@vrwma.com"));
        c.customers.add(new Customer("liakouras", "kati", "athinamou", "Student", "katietsi@vrwmesoles.com"));
    }

    @Test
    public void testListCapacity(){
        assertEquals(4, c.customers.size(), 0);
    }
    @Test
    public void testInputIntegrity(){
        InputStream sysInBackup = System.in; // backup System.in to restore it later
        ByteArrayInputStream in =new ByteArrayInputStream("ff\nmyid\nathinara\n1\n1\n414".getBytes());
        System.out.println(in.toString());
        System.setIn(in);
        assertTrue(c.begin());
    }
    @Test
    public void testListIntegrity(){
        assertTrue(c.verifyListIntegrity());
        c.customers.add(new Customer("liakouras", "kati", "athinamou", "Student", "katietsi@vrwmesoles.com"));
        assertFalse(c.verifyListIntegrity());
    }

}

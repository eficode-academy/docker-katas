package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest 
public class HelloControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client; 
    //Test spins up an entire server and client to perform the test
    
    @Test
    public void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/hello/sofus"); 
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Hello sofus", body);
    }
    @Test
    public void testCombineName() {
        String name = "Sonny";
        HelloController sut = new HelloController();
        System.out.println("testing");
        assertEquals("Hello "+name, sut.combineName(name),"Name and greeting not properly combined");
        
        
    }

}
package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/hello")
public class HelloController{
    @Get("/{name}") 
    @Produces(MediaType.TEXT_PLAIN) 
    public String index(String name) {
        return combineName(name); 
    }
    
    //Example of a function that can be tested through normal unit frameworks
    public String combineName( String name) {
        return "Hello "+name;
    }
}

package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class RootController{
    @Get("/") 
    @Produces(MediaType.TEXT_HTML) 
    public String index() {
        return "<h1> Hello World</h1>"; 
    }
    
}

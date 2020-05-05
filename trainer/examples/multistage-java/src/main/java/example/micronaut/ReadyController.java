package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/status")
public class ReadyController{
    @Get("/") 
    @Produces(MediaType.TEXT_PLAIN) 
    public String index() {
        return "Up and running"; 
    }
}

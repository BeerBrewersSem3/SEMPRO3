package beerbrewers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class RouteController {

    @GetMapping("/")
    @ResponseBody
    public String hello() throws IOException {
        // Load the index.html file from the resources/public directory
        Resource resource = new ClassPathResource("public/login.html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Convert the bytes to a string using UTF-8 encoding
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @GetMapping("/login")
    @ResponseBody
    public String loginPage() throws IOException {
        // Load the index.html file from the resources/public directory
        Resource resource = new ClassPathResource("public/login.html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Convert the bytes to a string using UTF-8 encoding
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @GetMapping("/monitor")
    @ResponseBody
    public String monitorPage() throws IOException {
        Resource resource = new ClassPathResource("public/monitorPage.html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Convert the bytes to a string using UTF-8 encoding
        return new String(bytes, StandardCharsets.UTF_8);
    }
}


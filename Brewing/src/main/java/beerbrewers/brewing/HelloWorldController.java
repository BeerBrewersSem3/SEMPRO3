package beerbrewers.brewing;

/* Imports */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Controller
public class HelloWorldController {
    @Autowired
    private BrewingApplication brewingApplication;

    @GetMapping("/")
    @ResponseBody
    public String hello() throws IOException {
        // Load the index.html file from the resources/public directory
        Resource resource = new ClassPathResource("public/index.html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Convert the bytes to a string using UTF-8 encoding
        String html = new String(bytes, StandardCharsets.UTF_8);
        return html.replace("<!--Current-State-Value-->",brewingApplication.getCurrentState());
    }

    @PostMapping("/sendCommand")
    public ResponseEntity<String> sendData(@RequestBody Map<String, String> payload) {
        try {
            String number = payload.get("number");
            brewingApplication.sendCommand(Integer.parseInt(number));
            return ResponseEntity.ok("Number sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/executeCommand")
    public ResponseEntity<String> setBooleanTrue() {
        try {
            brewingApplication.executeCommand(true);
            return ResponseEntity.ok("Boolean value set successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

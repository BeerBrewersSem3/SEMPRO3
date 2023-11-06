package beerbrewers.brewing;

/* Imports */
import beerbrewers.BrewMaster9000.opcua.OpcUaClientCommand;
import beerbrewers.BrewMaster9000.opcua.OpcUaNodes;
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
public class RouteController {

    @Autowired
    private OpcUaClientCommand opcUaClientCommand;
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/")
    @ResponseBody
    public String hello() throws IOException {
        // Load the index.html file from the resources/public directory
        Resource resource = new ClassPathResource("public/index.html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Convert the bytes to a string using UTF-8 encoding
        String html = new String(bytes, StandardCharsets.UTF_8);
        html = html.replace("<!--Current-State-Value-->", dashboardService.getCurrentNodeValue(OpcUaNodes.STATE_CURRENT));
        return html.replace("<!--Cntrl-Cmd-Value-->", dashboardService.getCurrentNodeValue(OpcUaNodes.CNTRL_CMD));
    }

    // Mapping a handle for HTTP Post request sent with /sendCommand endpoint
    @PostMapping("/sendCommand")
    public ResponseEntity<String> sendData(@RequestBody Map<String, String> payload) {
        try {
            // Extracts the value from the payload (In string format, converts to int).
            int number = Integer.parseInt(payload.get("number"));
            // Validates the number
            if(number >= 0 && number <= 5) {
                // Send command to OPC UA Server if number is valid
                opcUaClientCommand.sendCommand(number);
                // Return a response (code 200)
                return ResponseEntity.ok("Number sent successfully");
            } else {
                // Returns a response upon invalid (code 400)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number must be between 0 and 5");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return a response upon error (code 500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/executeCommand")
    public ResponseEntity<String> setBooleanTrue() {
        try {
            opcUaClientCommand.executeCommand(true);
            return ResponseEntity.ok("Boolean value set successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

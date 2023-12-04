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
        return convertToString("login");
    }

    @GetMapping("/login")
    @ResponseBody
    public String loginPage() throws IOException {
        return convertToString("login");
    }

    @GetMapping("/monitor")
    @ResponseBody
    public String monitorPage() throws IOException {
        return convertToString("monitorPage");
    }

    @GetMapping("/history")
    @ResponseBody
    public String historyPage() throws IOException {
        return convertToString("historyPage");
    }

    public String convertToString(String htmlString) throws IOException {
        Resource resource = new ClassPathResource("public/" + htmlString + ".html");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}


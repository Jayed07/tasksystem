package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sirma.academy.tasksystem.service.DataService;

import java.io.IOException;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/upload")
    String upload() {
        return "upload";
    }
    @PostMapping(value = "/upload", params = "import=true")
    public String importCSV(@RequestParam MultipartFile csvFile) throws IOException {
        dataService.importSCV(csvFile.getInputStream());
        return "redirect:projects";
    }
}

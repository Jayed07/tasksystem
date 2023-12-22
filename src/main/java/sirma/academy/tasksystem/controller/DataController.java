package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sirma.academy.tasksystem.service.DataService;
import sirma.academy.tasksystem.service.EmployeeService;
import sirma.academy.tasksystem.service.ProjectService;

import java.io.IOException;

@Controller
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectService projectService; //TODO: Field injection VS Constructor injection

    @GetMapping("/upload")
    String upload() {
        return "upload";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@RequestParam("employeeId") String employeeIdString, Model model) {
        Long employeeId;
        try {
            employeeId = Long.parseLong(employeeIdString);
        } catch (NumberFormatException e) {
            model.addAttribute("employeeIdError", "Invalid Employee ID format.");
            return "upload";
        }

        if (employeeService.ifExists(employeeId)) {
            model.addAttribute("employeeIdError", "Employee already exists.");
            return "upload";
        }

        employeeService.hireEmployee(employeeId);
        return "redirect:/employees";
    }

    @PostMapping("/addProject")
    public String addProject(@RequestParam("projectId") String projectIdString, Model model) {
        Long projectId;
        try {
            projectId = Long.parseLong(projectIdString);
        } catch (NumberFormatException e) {
            model.addAttribute("projectIdError", "Invalid Project ID format.");
            return "upload";
        }

        if (projectService.ifExists(projectId)) {
            model.addAttribute("projectIdError", "Project already exists.");
            return "upload";
        }
        projectService.add(projectId);
        return "redirect:projects";
    }

    @PostMapping("/uploadCSV")
    public String importCSV(@RequestParam MultipartFile csvFile) throws IOException {
        dataService.importSCV(csvFile.getInputStream());
        return "redirect:projects";
    }
}

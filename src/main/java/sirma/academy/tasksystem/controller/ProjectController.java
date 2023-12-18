package sirma.academy.tasksystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {
    @GetMapping("/projects")
    public String getProjects() {
        return "projects";
    }
}

package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.service.ProjectCardService;

import java.util.*;

@Controller
public class ProjectController {

    @Autowired
    private ProjectCardService projectCardService;
    @GetMapping("/projects")
    public String getProjects(Model model) {
        Map<Long, Set<Long>> employeeIdsByProjectId = projectCardService.getAllEmployeeIdsByProjectId();
        model.addAttribute("employeeIdsByProjectId", employeeIdsByProjectId);
        return "projects";
    }

    @GetMapping("/projects/{id}")
    public String getEmployeeDetails(@PathVariable Long id, Model model) {
        Project project = projectCardService.getProjectById(id);
        List<Employee> employeeList = projectCardService.getAllEmployeesByProjectId(id);

        model.addAttribute("project", project);
        model.addAttribute("employees", employeeList);
        return "project-details";
    }
}

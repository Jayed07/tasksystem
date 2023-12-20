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
public class EmployeeController {

    @Autowired
    private ProjectCardService projectCardService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        Map<Long, Set<Long>> projectIdByEmployeeId = projectCardService.getAllProjectsByEmployee();
        model.addAttribute("projectIdByEmployeeId", projectIdByEmployeeId);
        return "employees";
    }

    @GetMapping("/employees/{id}")
    public String getEmployeeDetails(@PathVariable Long id, Model model) {
        Employee employee = projectCardService.getEmployeeById(id);
        List<Project> projectList = projectCardService.getAllProjectsByEmployeeId(id);

        model.addAttribute("employee", employee);
        model.addAttribute("projects", projectList);
        return "employee-details";
    }
}

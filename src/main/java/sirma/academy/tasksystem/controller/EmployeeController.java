package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sirma.academy.tasksystem.service.ProjectCardService;

import java.util.Map;
import java.util.Set;

@Controller
public class EmployeeController {

    @Autowired
    private ProjectCardService projectCardService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        Map<Long, Set<Long>> projectIdByEmployeeId = projectCardService.getAllProjectsByEmployeeId();
        model.addAttribute("projectIdByEmployeeId", projectIdByEmployeeId);
        return "employees";
    }
}

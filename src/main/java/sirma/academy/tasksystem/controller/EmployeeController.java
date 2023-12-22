package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;
import sirma.academy.tasksystem.service.EmployeeService;
import sirma.academy.tasksystem.service.ProjectCardService;
import sirma.academy.tasksystem.service.ProjectService;

import java.time.LocalDate;
import java.util.*;

@Controller
public class EmployeeController {

    @Autowired
    private ProjectCardService projectCardService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectService projectService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        Map<Long, Set<Long>> projectIdByEmployeeId = projectCardService.getAllProjectsByEmployee();
        model.addAttribute("projectIdByEmployeeId", projectIdByEmployeeId);

        Set<Employee> freeEmployees = employeeService.getFreeEmployees();
        model.addAttribute("freeEmployees", freeEmployees);
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

    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee (@PathVariable Long id) {

        List <ProjectCard> projectCardList = projectCardService.getAllProjectCardsByEmployeeId(id);
        if(!projectCardList.isEmpty()) {
            for (ProjectCard projectCard : projectCardList) {
                projectCardService.delete(projectCard);
            }
            employeeService.delete(id);
        }

        return "redirect:/employees";
    }

    @GetMapping("/editEmployee/{id}")
    public String getEmployeeEdit(@PathVariable Long id, Model model) {
        List<Project> allProjects = projectService.getAll();
        List<Project> allProjectsByEmployee = projectCardService.getAllProjectsByEmployeeId(id);
        List<Project> notBeingWorkedOnByCurrentEmployee = new ArrayList<>();
        for (Project project : allProjects) {
            if(!allProjectsByEmployee.contains(project)) {
                notBeingWorkedOnByCurrentEmployee.add(project);
            }
        }

        model.addAttribute("employeeId", id);
        model.addAttribute("availableProjects", notBeingWorkedOnByCurrentEmployee);
        return "edit-employee";
    }

    @PostMapping("/editEmployee/{id}")
    public String postEmployeeEdit(@PathVariable Long id, @RequestParam("projectId") Long projectId) {
        Employee employee = employeeService.getById(id);
        ProjectCard projectCard = new ProjectCard();
        Project project = projectService.getById(projectId);
        projectCard.setProject(project);
        projectCard.setEmployee(employee);
        projectCard.setDateFrom(LocalDate.now());
        projectCardService.add(projectCard);

        return "redirect:/employees";
    }
}

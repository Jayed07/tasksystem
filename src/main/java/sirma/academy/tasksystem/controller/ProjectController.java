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
public class ProjectController {

    @Autowired
    private ProjectCardService projectCardService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/projects")
    public String getProjects(Model model) {
        Map<Long, Set<Long>> employeeIdsByProjectId = projectCardService.getAllEmployeeIdsByProjectId();
        model.addAttribute("employeeIdsByProjectId", employeeIdsByProjectId);

        Set<Project> unassignedProjects = projectService.getUnassignedProjects();
        model.addAttribute("unassignedProjects", unassignedProjects);
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

    @PostMapping("/deleteProject/{id}")
    public String deleteProject (@PathVariable Long id) {

        List <ProjectCard> projectCardList = projectCardService.getAllProjectCardsByProjectId(id);
        if(!projectCardList.isEmpty()) {
            for (ProjectCard projectCard : projectCardList) {
                projectCardService.delete(projectCard);
            }
            projectService.delete(id);
        } else {
            projectService.delete(id);
        }

        return "redirect:/projects";
    }

    @GetMapping("/editProject/{id}")
    public String getProjectEdit(@PathVariable Long id, Model model) {
        List<Employee> allEmployees = employeeService.getAll();
        List<Employee> allEmployeesByProject = projectCardService.getAllEmployeesByProjectId(id);
        List<Employee> notWorkingOnCurrentProject = new ArrayList<>();
        for (Employee employee : allEmployees) {
            if(!allEmployeesByProject.contains(employee)) {
                notWorkingOnCurrentProject.add(employee);
            }
        }

        model.addAttribute("projectId", id);
        model.addAttribute("availableEmployees", notWorkingOnCurrentProject);
        return "edit-project";
    }

    @PostMapping("/editProject/{id}")
    public String postProjectEdit(@PathVariable Long id, @RequestParam("employeeId") Long employeeId) {
        Project project = projectService.getById(id);
        ProjectCard projectCard = new ProjectCard();
        Employee employee = employeeService.getById(employeeId);
        projectCard.setProject(project);
        projectCard.setEmployee(employee);
        projectCard.setDateFrom(LocalDate.now());
        projectCardService.add(projectCard);

        return "redirect:/projects";
    }
}

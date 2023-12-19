package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;
import sirma.academy.tasksystem.repository.ProjectCardRepository;
import sirma.academy.tasksystem.repository.ProjectRepository;

import java.util.*;
import java.time.LocalDate;
import java.util.Date;

@Service
public class ProjectCardService {

    @Autowired
    private ProjectCardRepository projectCardRepository;
    @Autowired
    private EmployeeService employeeService;

    public void add (ProjectCard projectCard) {
        projectCardRepository.save(projectCard);
    }

    public ProjectCard findByEmployeeAndProject (Employee employee, Project project) {
        return projectCardRepository.getByEmployeeAndProject(employee, project);
    }
}

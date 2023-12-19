package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;
import sirma.academy.tasksystem.repository.ProjectCardRepository;

import java.util.*;
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


    public Map<Long, Set<Long>> getAllEmployeeIdsByProjectId() {
        List<ProjectCard> projectCards = projectCardRepository.findAll();

        Map<Long, Set<Long>> employeeIdsByProjectId = new HashMap<>();

        for (ProjectCard projectCard : projectCards) {
            Long projectId = projectCard.getProject().getId();
            Long employeeId = projectCard.getEmployee().getId();

            // Check if the project ID exists in the map
            if (!employeeIdsByProjectId.containsKey(projectId)) {
                employeeIdsByProjectId.put(projectId, new HashSet<>());
            }

            employeeIdsByProjectId.get(projectId).add(employeeId);
        }

        return employeeIdsByProjectId;
    }
}

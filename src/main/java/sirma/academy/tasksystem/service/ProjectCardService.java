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
    @Autowired
    private ProjectService projectService;

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

    public Map<Long, Set<Long>> getAllProjectsByEmployee() {
        List<ProjectCard> projectCards = projectCardRepository.findAll();

        Map<Long, Set<Long>> projectsByEmployeeId = new HashMap<>();

        for (ProjectCard projectCard : projectCards) {
            Long projectId = projectCard.getProject().getId();
            Long employeeId = projectCard.getEmployee().getId();

            // Check if the employee ID exists in the map
            if (!projectsByEmployeeId.containsKey(employeeId)) {
                projectsByEmployeeId.put(employeeId, new HashSet<>());
            }

            projectsByEmployeeId.get(employeeId).add(projectId);
        }

        return projectsByEmployeeId;
    }

    public Employee getEmployeeById(Long id) {
        return employeeService.getById(id);
    }

    public List<Project> getAllProjectsByEmployeeId(Long id) {
        List<ProjectCard> projectCards = projectCardRepository.findAll();
        List<Project> projectList = new ArrayList<>();
        for (ProjectCard projectCard : projectCards) {
            if(projectCard.getEmployee() == employeeService.getById(id)) {
                projectList.add(projectCard.getProject());
            }
        }
        return projectList;
    }

    public Project getProjectById(Long id) {
        return projectService.getById(id);
    }

    public List<Employee> getAllEmployeesByProjectId(Long id) {
        List<ProjectCard> projectCards = projectCardRepository.findAll();
        List<Employee> employeesList = new ArrayList<>();
        for (ProjectCard projectCard : projectCards) {
            if(projectCard.getProject() == projectService.getById(id)) {
                employeesList.add(projectCard.getEmployee());
            }
        }
        return employeesList;
    }
}

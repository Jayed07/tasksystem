package sirma.academy.tasksystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;

import java.util.*;


public interface ProjectCardRepository extends JpaRepository<ProjectCard, Long> {
    ProjectCard getByEmployeeAndProject(Employee employee, Project projects);
    List<ProjectCard> getAllProjectCardsByProjectId(Long id);

    List<ProjectCard> getAllProjectCardsByEmployeeId(Long id);
}

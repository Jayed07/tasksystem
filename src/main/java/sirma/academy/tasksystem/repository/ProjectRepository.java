package sirma.academy.tasksystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sirma.academy.tasksystem.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}

package sirma.academy.tasksystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sirma.academy.tasksystem.model.ProjectCard;

public interface ProjectCardRepository extends JpaRepository<ProjectCard, Long> {
}

package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.repository.ProjectRepository;

import java.util.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void add (Long id) {
        Project project = new Project();
        project.setId(id);
        projectRepository.save(project);
    }

    public void delete (Long id) {
        projectRepository.findById(id).ifPresent(project -> projectRepository.delete(project));
    }

    public Project getById (Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> projects() {
        return projectRepository.findAll();
    }
}

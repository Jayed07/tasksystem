package sirma.academy.tasksystem.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="employees")
public class Employee {

    @Id
    @Column(nullable = false)
    private Long id;
//    @OneToMany
//    private List<Project> projects = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public List<Project> getProjects() {
//        return projects;
//    }
//
//    public void setProjects(List<Project> projects) {
//        this.projects = projects;
//    }
}

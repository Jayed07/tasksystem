package sirma.academy.tasksystem.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="projects")
public class Project {

    @Id
    @Column(nullable = false)
    private Long id;
//    @OneToMany
//    private List<Employee> employees = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public List<Employee> getEmployees() {
//        return employees;
//    }
//
//    public void setEmployees(List<Employee> employees) {
//        this.employees = employees;
//    }


}

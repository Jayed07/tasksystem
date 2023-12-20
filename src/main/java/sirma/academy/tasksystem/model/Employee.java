package sirma.academy.tasksystem.model;

import jakarta.persistence.*;

@Entity
@Table(name="employees")
public class Employee {

    @Id
    @Column(nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

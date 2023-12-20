package sirma.academy.tasksystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sirma.academy.tasksystem.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}

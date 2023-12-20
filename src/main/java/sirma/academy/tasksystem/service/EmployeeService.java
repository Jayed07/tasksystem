package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.repository.EmployeeRepository;

import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public void hireEmployee(Long id) {
            Employee employee = new Employee();
            employee.setId(id);
            employeeRepository.save(employee);
    }

    public void fireEmployee(Long id) {
        employeeRepository.findById(id).ifPresent(employee -> employeeRepository.delete(employee));
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> employees() {
        return employeeRepository.findAll();
    }

    public boolean ifExists(Long employeeId) {
        return employeeRepository.findById(employeeId).isPresent();
    }
}

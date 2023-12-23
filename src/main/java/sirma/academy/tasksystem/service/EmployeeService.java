package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.ProjectCard;
import sirma.academy.tasksystem.repository.EmployeeRepository;
import sirma.academy.tasksystem.repository.ProjectCardRepository;

import java.util.*;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProjectCardRepository projectCardRepository;

    public void hireEmployee(Long id) {
            Employee employee = new Employee();
            employee.setId(id);
            employeeRepository.save(employee);
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

    public Set<Employee> getFreeEmployees () {
        List<Employee> allEmployees = employeeRepository.findAll();
        List<ProjectCard> allProjectCards = projectCardRepository.findAll();
        Set<Employee> freeEmployees = new HashSet<>();

        for (Employee employee : allEmployees) {
            boolean hasProject = false;
            for (ProjectCard projectCard : allProjectCards) {
                if (projectCard.getEmployee() == employee) {
                    hasProject = true;
                }
            }
            if (!hasProject) {
                freeEmployees.add(employee);
            }
        }

        return freeEmployees;
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public void delete(Long id) {
        employeeRepository.findById(id).ifPresent(project -> employeeRepository.delete(project));
    }
}

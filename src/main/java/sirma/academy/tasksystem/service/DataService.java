package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    private EmployeeService employeeService;
    private ProjectService projectService;
    private ProjectCardService projectCardService;

    @Autowired
    public DataService(EmployeeService employeeService, ProjectService projectService, ProjectCardService projectCardService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
        this.projectCardService = projectCardService;
    }

    public void importSCV(InputStream csvFileStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvFileStream));
        try (reader) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                // Remove starting and ending double quotes, and trailing commas
                line = line.replaceAll("^\"|\"$|,{2,}$", "");

                // Split the cleaned line into values array
                String[] values = line.split(", ", -1);
                // -1 in split() method preserves trailing empty strings

                // Trim each value to remove spaces and potential quotes
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim().replaceAll("^\"|\"$", "");
                }

                if(values[0].equals("")) {
                    continue;
                }

                Long employeeId = Long.parseLong(values[0]);
                Long projectId = Long.parseLong(values[1]);
                LocalDate dateFrom = LocalDate.parse(values[2]);
                LocalDate dateTo;
                if (!values[3].equals("NULL")) {
                    dateTo= LocalDate.parse(values[3]);
                } else {
                    dateTo = null;
                }

                ProjectCard projectCard = new ProjectCard();
                // Employee
                if (employeeId > 0) {
                    if(employeeService.getById(employeeId) == null) {
                        Employee employee = new Employee();
                        employee.setId(employeeId);
                        employeeService.hireEmployee(employeeId);
                        projectCard.setEmployee(employee);
                    } else {
                        projectCard.setEmployee(employeeService.getById(employeeId));
                    }
                } else {
                    throw new IOException("Invalid employee ID.");
                }
                // Project
                if (projectId > 0) {
                    if (projectService.getById(projectId) == null) {
                        Project project = new Project();
                        project.setId(projectId);
                        projectService.add(projectId);
                        projectCard.setProject(project);
                    } else {
                        projectCard.setProject(projectService.getById(projectId));
                    }
                } else {
                    throw new IOException("Invalid project ID.");
                }
                //Date From
                if (dateFrom == null) {
                    throw new IOException("Starting date can not be NULL.");
                } else {
                    projectCard.setDateFrom(dateFrom);
                }
                //Date To
                projectCard.setDateTo(dateTo);

                //Save project card to repository
                projectCardService.add(projectCard);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }
}

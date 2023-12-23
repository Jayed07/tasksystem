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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

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
                try {
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

                    if (values[0].equals("")) {
                        continue;
                    }

                    Long employeeId = Long.parseLong(values[0]);
                    Long projectId = Long.parseLong(values[1]);
                    LocalDate dateFrom = parseDate(values[2]);

                    //Not necessary because of parse method
//                    if (dateFrom == null) {
//                        throw new IOException(String.format("Starting date cannot be NULL. Employee: %d; Project: %d", employeeId, projectId));
//                    }
//
                    LocalDate dateTo;
                    if (!values[3].equals("NULL")) {
                        dateTo = parseDate(values[3]);
                    } else {
                        dateTo = null;
                    }

                    if (dateTo == null) {
                        LocalDate dateToTest = LocalDate.now();
                        if (dateToTest.isBefore(dateFrom)) {
                            throw new IOException(String.format("End date cannot be before start date. Employee: %d; Project: %d", employeeId, projectId));
                        }
                    } else {
                        if (dateTo.isBefore(dateFrom)) {
                            throw new IOException(String.format("End date cannot be before start date. Employee: %d; Project: %d", employeeId, projectId));
                        }
                    }

                    ProjectCard projectCard = new ProjectCard();
                    // Employee
                    if (employeeId > 0) {
                        if (employeeService.getById(employeeId) == null) {
                            Employee employee = new Employee();
                            employee.setId(employeeId);
                            employeeService.hireEmployee(employeeId);
                            projectCard.setEmployee(employee);
                        } else {
                            projectCard.setEmployee(employeeService.getById(employeeId));
                        }
                    } else {
                        throw new IOException("Invalid employee ID: " + employeeId);
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
                        if ((projectCardService.findByEmployeeAndProject(employeeService.getById(employeeId), projectService.getById(projectId))) != null) {
                            throw new IOException(String.format("Project-Employee pairs cannot be duplicated. Project: %d; Employee: %d", projectId, employeeId));
                        }

                    } else {
                        throw new IOException("Invalid project ID: " + projectId);
                    }

                    //Date From
                    projectCard.setDateFrom(dateFrom);

                    //Date To
                    projectCard.setDateTo(dateTo);

                    //Save project card to repository
                    projectCardService.add(projectCard);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    public LocalDate parseDate(String date) {
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[yyyy-MM-dd]" + "[dd/MM/yyyy]" + "[MM/dd/yyyy]" +
                        "[dd MMM yyyy]" + "[yyyy/MM/dd]" + "[EEE, MMM dd, yyyy]" + "[yyyy.MM.dd]" +
                        "[MMM dd, yyyy]" + "[dd-MM-yyyy]" + "[yyyy/MMM/dd]" + "[d MMMM yyyy]" + "[EEE, dd MMM yyyy]"
                        + "[yy/MM/dd]" + "[MMMM dd, yyyy]" + "[yyMMdd]" + "[d MMM yy]" + "[dd/MMM/yyyy]" + "[yyyy/MMM/d]"
                        + "[dd-MMM-yyyy]" + "[EEE, yyyy/MM/dd]"));
        DateTimeFormatter dateTimeFormatter = dateTimeFormatterBuilder.toFormatter();
        return LocalDate.parse(date, dateTimeFormatter);
    }
}

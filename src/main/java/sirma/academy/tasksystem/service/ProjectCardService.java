package sirma.academy.tasksystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sirma.academy.tasksystem.data.Stats;
import sirma.academy.tasksystem.model.Employee;
import sirma.academy.tasksystem.model.Project;
import sirma.academy.tasksystem.model.ProjectCard;
import sirma.academy.tasksystem.repository.ProjectCardRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectCardService {

    @Autowired
    private ProjectCardRepository projectCardRepository;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectService projectService;

    public void add(ProjectCard projectCard) {
        projectCardRepository.save(projectCard);
    }

    public ProjectCard findByEmployeeAndProject(Employee employee, Project project) {
        return projectCardRepository.getByEmployeeAndProject(employee, project);
    }


    public Map<Long, Set<Long>> getAllEmployeeIdsByProjectId() {
        List<ProjectCard> projectCards = projectCardRepository.findAll();

        Map<Long, Set<Long>> employeeIdsByProjectId = new HashMap<>();

        for (ProjectCard projectCard : projectCards) {
            Long projectId = projectCard.getProject().getId();
            Long employeeId = projectCard.getEmployee().getId();

            if (!employeeIdsByProjectId.containsKey(projectId)) {
                employeeIdsByProjectId.put(projectId, new HashSet<>());
            }

            employeeIdsByProjectId.get(projectId).add(employeeId);
        }

        return employeeIdsByProjectId;
    }

    public Map<Long, Set<Long>> getAllProjectsByEmployee() {
        List<ProjectCard> projectCards = projectCardRepository.findAll();

        Map<Long, Set<Long>> projectsByEmployeeId = new HashMap<>();

        for (ProjectCard projectCard : projectCards) {
            Long projectId = projectCard.getProject().getId();
            Long employeeId = projectCard.getEmployee().getId();

            if (!projectsByEmployeeId.containsKey(employeeId)) {
                projectsByEmployeeId.put(employeeId, new HashSet<>());
            }

            projectsByEmployeeId.get(employeeId).add(projectId);
        }

        return projectsByEmployeeId;
    }

    public Employee getEmployeeById(Long id) {
        return employeeService.getById(id);
    }

    public List<Project> getAllProjectsByEmployeeId(Long id) {
        List<ProjectCard> projectCards = projectCardRepository.findAll();
        List<Project> projectList = new ArrayList<>();
        for (ProjectCard projectCard : projectCards) {
            if (projectCard.getEmployee() == employeeService.getById(id)) {
                projectList.add(projectCard.getProject());
            }
        }
        return projectList;
    }

    public Project getProjectById(Long id) {
        return projectService.getById(id);
    }

    public List<Employee> getAllEmployeesByProjectId(Long id) {
        List<ProjectCard> projectCards = projectCardRepository.findAll();
        List<Employee> employeesList = new ArrayList<>();
        for (ProjectCard projectCard : projectCards) {
            if (projectCard.getProject() == projectService.getById(id)) {
                employeesList.add(projectCard.getEmployee());
            }
        }
        return employeesList;
    }

    //Get pair with most time
    public Stats findPairWithMaxTimeTogether() {
        List<ProjectCard> projectCards = projectCardRepository.findAll();

        Map<Long, Set<Long>> projectsByEmployeeId = getAllProjectsByEmployee();

        Stats result = new Stats();
        long maxDaysWorked = 0;

        for (Map.Entry<Long, Set<Long>> entry : projectsByEmployeeId.entrySet()) {
            Long employeeIdA = entry.getKey(); //Gets first employee
            Set<Long> projectsA = entry.getValue(); // Get first employee's projects

            for (Map.Entry<Long, Set<Long>> innerEntry : projectsByEmployeeId.entrySet()) {
                Long employeeIdB = innerEntry.getKey(); //Gets second employee
                Set<Long> projectsB = innerEntry.getValue(); // Get second employee's projects

                if (employeeIdA.equals(employeeIdB)) {
                    continue; // Skip same employee pair comparison
                }

                // Find common projects between two employees
                Set<Long> commonProjects = new HashSet<>(projectsA);
                commonProjects.retainAll(projectsB);

                if (!commonProjects.isEmpty()) {
                    long totalDays = calculateTotalDaysWorkedTogether(employeeIdA, employeeIdB, commonProjects, projectCards);

                    if (totalDays > maxDaysWorked) {
                        maxDaysWorked = totalDays;

                        result.setEmployeeIds(new HashSet<>(Arrays.asList(employeeIdA, employeeIdB)));
                        result.setTotalDaysWorkedTogether(totalDays);
                        result.setProjectsAndDays(getProjectsAndDaysWorked(employeeIdA, employeeIdB, commonProjects, projectCards));
                    }
                }
            }
        }

        return result;
    }

    private long calculateTotalDaysWorkedTogether(Long employeeIdA, Long employeeIdB,
                                                  Set<Long> commonProjects, List<ProjectCard> projectCards) {

        Set<String> calculatedPairs = new HashSet<>(); // To track already calculated pairs

        long totalDays = 0;

        for (ProjectCard card : projectCards) {
            Long projectId = card.getProject().getId();

            if (commonProjects.contains(projectId)) {
                Long employeeId = card.getEmployee().getId();
                String pairKey = createPairKey(employeeIdA, employeeIdB, projectId);

                if ((employeeId.equals(employeeIdA) || employeeId.equals(employeeIdB)) && !calculatedPairs.contains(pairKey)) {
                    List<ProjectCard> allProjectCards = getAllProjectCardsByProjectId(projectId);

                    LocalDate fromDateA = null;
                    LocalDate toDateA = null;
                    LocalDate fromDateB = null;
                    LocalDate toDateB = null;
                    boolean foundA = false;
                    boolean foundB = false;

                    for (ProjectCard innerCard : allProjectCards) {
                        Long cardEmployeeId = innerCard.getEmployee().getId();

                        if (cardEmployeeId.equals(employeeIdA)) {
                            fromDateA = innerCard.getDateFrom();
                            toDateA = (innerCard.getDateTo() != null) ? innerCard.getDateTo() : LocalDate.now();
                            foundA = true;
                        } else if (cardEmployeeId.equals(employeeIdB)) {
                            fromDateB = innerCard.getDateFrom();
                            toDateB = (innerCard.getDateTo() != null) ? innerCard.getDateTo() : LocalDate.now();
                            foundB = true;
                        }

                        if (foundA && foundB) {
                            LocalDate latestStartDate = fromDateA.isAfter(fromDateB) ? fromDateA : fromDateB;
                            LocalDate earliestEndDate = toDateA.isBefore(toDateB) ? toDateA : toDateB;

                            long days = ChronoUnit.DAYS.between(latestStartDate, earliestEndDate);
                            totalDays += days;
                            calculatedPairs.add(pairKey);
                            break;
                        }
                    }
                }
            }
        }
        return totalDays;
    }

    //This method create unique key (EmployeeA_EmployeeB_ProjectId)
    private String createPairKey(Long employeeIdA, Long employeeIdB, Long projectId) {
        return Stream.of(employeeIdA, employeeIdB, projectId)
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining("_"));
    }

    private Map<Project, Long> getProjectsAndDaysWorked(
            Long employeeIdA, Long employeeIdB, Set<Long> commonProjects, List<ProjectCard> projectCards) {

        Map<Project, Long> projectsAndDays = new HashMap<>();
        Set<ProjectCard> processedCardProjects = new HashSet<>();

        LocalDate fromDateA = null;
        LocalDate toDateA = null;
        LocalDate fromDateB = null;
        LocalDate toDateB = null;
        long totalDaysPerProject = 0;
        boolean flagA = false;
        boolean flagB = false;

        for (ProjectCard card : projectCards) {
            Long projectId = card.getProject().getId();
            if (commonProjects.contains(projectId) && !processedCardProjects.contains(card)) {

                processedCardProjects.add(card);
                List<ProjectCard> allCards = getAllProjectCardsByProjectId(projectId);

                for (ProjectCard innerCard : allCards) {
                    Long cardEmployeeId = innerCard.getEmployee().getId();
                    processedCardProjects.add(innerCard);
                    if (cardEmployeeId.equals(employeeIdA)) {
                        fromDateA = innerCard.getDateFrom();
                        toDateA = (innerCard.getDateTo() != null) ? innerCard.getDateTo() : LocalDate.now();
                        flagA = true;
                    } else if (cardEmployeeId.equals(employeeIdB)) {
                        fromDateB = innerCard.getDateFrom();
                        toDateB = (innerCard.getDateTo() != null) ? innerCard.getDateTo() : LocalDate.now();
                        flagB = true;
                    }

                    if (flagA && flagB) {
                        Project project = card.getProject();

                        LocalDate latestStartDate = fromDateA.isAfter(fromDateB) ? fromDateA : fromDateB;
                        LocalDate earliestEndDate = toDateA.isBefore(toDateB) ? toDateA : toDateB;

                        totalDaysPerProject = ChronoUnit.DAYS.between(latestStartDate, earliestEndDate);

                        projectsAndDays.put(project, totalDaysPerProject);

                        flagA = false;
                        flagB = false;
                    }
                }
            }
        }

        return projectsAndDays;
    }

    public List<ProjectCard> getAllProjectCardsByProjectId(Long id) {
        return projectCardRepository.getAllProjectCardsByProjectId(id);
    }

    public void delete(ProjectCard projectCard) {
        projectCardRepository.delete(projectCard);
    }

    public List<ProjectCard> getAllProjectCardsByEmployeeId(Long id) {
        return projectCardRepository.getAllProjectCardsByEmployeeId(id);
    }
}


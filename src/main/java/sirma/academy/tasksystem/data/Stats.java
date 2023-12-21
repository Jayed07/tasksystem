package sirma.academy.tasksystem.data;

import sirma.academy.tasksystem.model.Project;

import java.util.*;

public class Stats {
    private Set<Long> employeeIds;
    private long totalDaysWorkedTogether;
    private Map<Project, Long> projectsAndDays;

    public Set<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(Set<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public long getTotalDaysWorkedTogether() {
        return totalDaysWorkedTogether;
    }

    public void setTotalDaysWorkedTogether(long totalDaysWorkedTogether) {
        this.totalDaysWorkedTogether = totalDaysWorkedTogether;
    }

    public Map<Project, Long> getProjectsAndDays() {
        return projectsAndDays;
    }

    public void setProjectsAndDays(Map<Project, Long> projectsAndDays) {
        this.projectsAndDays = projectsAndDays;
    }
}

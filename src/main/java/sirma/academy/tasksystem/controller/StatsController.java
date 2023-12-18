package sirma.academy.tasksystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {
    @GetMapping("/statistics")
    public String getStats() {
        return "statistics";
    }
}

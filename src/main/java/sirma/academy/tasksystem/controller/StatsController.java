package sirma.academy.tasksystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sirma.academy.tasksystem.data.Stats;
import sirma.academy.tasksystem.service.ProjectCardService;

@Controller
public class StatsController {
    @Autowired
    private ProjectCardService projectCardService;
    @GetMapping("/statistics")
    public String getStats(Model model) {
        Stats stats = projectCardService.findPairWithMaxTimeTogether();
        model.addAttribute("stats", stats);
        return "statistics";
    }
}

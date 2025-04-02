package com.example.demo.controller;

import com.example.demo.model.Top;
import com.example.demo.service.TopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TopController {

    @Autowired
    private TopService topService;

    @GetMapping("/top")
    public String showTop(Model model, 
                         @RequestParam(defaultValue = "rank") String sortField,
                         @RequestParam(defaultValue = "asc") String sortOrder) {
        model.addAttribute("tops", topService.getAllTops(sortField, sortOrder));
        model.addAttribute("currentSortField", sortField);
        model.addAttribute("currentSortOrder", sortOrder);
        return "top";
    }

    @PostMapping("/top/add")
    public String addTop(@RequestParam int rank,
                        @RequestParam String name,
                        @RequestParam String netWorth,
                        @RequestParam int age,
                        @RequestParam String country,
                        @RequestParam String industry,
                        @RequestParam String score) {
        
        Top top = new Top();
        top.setRank(rank);
        top.setName(name);
        top.setNetWorth(netWorth);
        top.setAge(age);
        top.setCountry(country);
        top.setIndustry(industry);
        top.setScore(score);
        
        topService.addTop(top);
        return "redirect:/top";
    }

    @PostMapping("/top/delete/{rank}")
    public String deleteTop(@PathVariable int rank) {
        topService.deleteTop(rank);
        return "redirect:/top";
    }

    @PostMapping("/top/edit/{rank}")
    public String editTop(@PathVariable int rank,
                         @RequestParam String name,
                         @RequestParam String netWorth,
                         @RequestParam int age,
                         @RequestParam String country,
                         @RequestParam String industry,
                         @RequestParam String score) {
        
        Top top = new Top();
        top.setRank(rank);
        top.setName(name);
        top.setNetWorth(netWorth);
        top.setAge(age);
        top.setCountry(country);
        top.setIndustry(industry);
        top.setScore(score);
        
        topService.updateTop(top);
        return "redirect:/top";
    }

    @GetMapping("/top/charts")
    public String showCharts(Model model) {
        model.addAttribute("tops", topService.getAllTops("rank", "asc"));
        return "top-charts";
    }

    @GetMapping("/top/tasks")
    public String showTasks(Model model) {
        model.addAttribute("tops", topService.getAllTops("rank", "asc"));
        model.addAttribute("task1Result", topService.getTask1Result());
        model.addAttribute("task2Result", topService.getTask2Result());
        return "top-tasks";
    }
} 
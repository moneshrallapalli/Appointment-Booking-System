package com.appointment.booking.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

 
public class DashboardController {
    /*
    @GetMapping("/professor-dashboard")
    public String professorDashboard() {
        return "professor-dashboard"; 
    } */

    @GetMapping("/student-dashboard")
    public String studentDashboard() {
        return "student-dashboard";
    }


    @GetMapping("/ta-dashboard")
    public String taDashboard() {
        return "ta-dashboard"; }

}

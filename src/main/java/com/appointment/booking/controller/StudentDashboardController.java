package com.appointment.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping  ;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.appointment.booking.repository.AppointmentGroupRepository;
import com.appointment.booking.model.AppointmentGroup;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class StudentDashboardController {

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;

    @GetMapping("/student-dashboard")
    public String showStudentDashboard(Model model) {
        List<AppointmentGroup> groups = appointmentGroupRepository.findByStatus(AppointmentGroup.Status.PUBLISHED);
        model.addAttribute("groups", groups);
        return "student-dashboard";


    }
    
    

    
}

package com.appointment.booking.controller;
import com.appointment.booking.model.AppointmentGroup;
import com.appointment.booking.repository.AppointmentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Date;
import java.util.List;




@Controller
@RequestMapping("/professor-dashboard")

public class ProfessorDashboardController {

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {
        List<AppointmentGroup> groups = appointmentGroupRepository.findByProfessorUsername(principal.getName());
        model.addAttribute("groups", groups);
        return "professor-dashboard";

    }
    
    @GetMapping("/create-group")
    public String showCreateGroupForm(Model model) {
        model.addAttribute("appointmentGroup", new AppointmentGroup());
        return "create-group";
    }

// have to figure out to avoid no entry submission. @notnull??

    @PostMapping("/create-group")
    public String createGroup(@ModelAttribute AppointmentGroup appointmentGroup, Principal principal) {
        appointmentGroup.setProfessorUsername(principal.getName());
        appointmentGroup.setCreatedAt(new Date());
        appointmentGroupRepository.save(appointmentGroup);
        return "redirect:/professor-dashboard";



        
    }
    
}




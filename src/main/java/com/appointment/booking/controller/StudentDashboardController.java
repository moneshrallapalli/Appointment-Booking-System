package com.appointment.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping  ;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.appointment.booking.repository.AppointmentGroupRepository;
import com.appointment.booking.repository.TimeSlotRepository;
import com.appointment.booking.model.AppointmentGroup;
import com.appointment.booking.model.TimeSlot;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class StudentDashboardController {

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository; // injection to call non-static instance variable to this controller


    @GetMapping("/student-dashboard")
    public String showStudentDashboard(Model model) {
        List<AppointmentGroup> groups = appointmentGroupRepository.findByStatus(AppointmentGroup.Status.PUBLISHED);
        model.addAttribute("groups", groups);
        return "student-dashboard";

    }

    @GetMapping("/student-dashboard/group/{id}/slots")
    public String viewGroupSlots(@PathVariable Long id, Model model) {
        AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);
        List <TimeSlot> openSlots = timeSlotRepository.findByAppointmentGroupIdAndIsBookedFalse(id);
        model.addAttribute("group", group);
        model.addAttribute("slots", openSlots);
        return "student-view-slots";
    }
    
    
    @PostMapping("/student-dashboard/group/{id}/slots/{slotId}/book")
    public String bookSlot(@PathVariable Long id, @PathVariable Long slotId, Principal principal) {
        TimeSlot slot = timeSlotRepository.findById(slotId).orElse(null);
        if (slot != null && !slot.isBooked()){
            slot.setBooked(true);
            slot.setBookedBy(principal.getName());
            timeSlotRepository.save(slot);
        }
        return "redirect:/student-dashboard/group/"+ id +"/slots";
        
        
    }
    

    
}

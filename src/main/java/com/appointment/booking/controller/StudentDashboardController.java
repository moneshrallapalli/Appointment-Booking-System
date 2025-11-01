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
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;



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
    public String bookSlot(@PathVariable Long id, @PathVariable Long slotId,@RequestParam(required = false) String emails ,Principal principal) {
        String studentEmail = principal.getName();
        Long alreadyBookedCount = timeSlotRepository.countBookingsByStudentInGroup(studentEmail,id);
        if (alreadyBookedCount > 0){
            return "redirect:/student-dashboard/group/" + id + "/slots?error=alreadyBooked";

        }

        TimeSlot slot = timeSlotRepository.findById(slotId).orElse(null);
        if (slot != null && !slot.isBooked()){
            List<String> studentList = new ArrayList<>();
            studentList.add(principal.getName());

            if (emails != null && !emails.trim().isEmpty()) {
                String[] additionalEmails = emails.split(",");
                for (String email : additionalEmails) {
                    String trimmedEmail = email.trim();
                    if (!trimmedEmail.isEmpty()) {
                        studentList.add(trimmedEmail);
                    }
                }
            }

            slot.setBooked(true);
            slot.setBookedStudentsEmails(studentList);
            //slot.setBookedBy(principal.getName());
            timeSlotRepository.save(slot);
        }
        return "redirect:/student-dashboard/group/"+ id +"/slots";
    }
    
    @GetMapping("/student-dashboard/my-bookings")
    public String viewMyBookings( Model model, Principal principal) {
        List<TimeSlot> bookings = timeSlotRepository.findByStudentEmail(principal.getName());
        model.addAttribute("bookings", bookings);
        return "student-bookings";
    }
}

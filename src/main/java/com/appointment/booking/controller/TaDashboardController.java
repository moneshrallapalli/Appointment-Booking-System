package com.appointment.booking.controller;

import org.springframework.web.bind.annotation.GetMapping  ;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/ta-dashboard")
public class TaDashboardController {

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @GetMapping
    public String taDashboard() {
        return "ta-dashboard";

    }
    
@GetMapping("/view-bookings")

public String viewBookings(Model model) {
    List<AppointmentGroup> groups = appointmentGroupRepository.findByStatus(AppointmentGroup.Status.PUBLISHED);
    List<TimeSlot> bookedSlots = new ArrayList<>();
    for (AppointmentGroup group : groups) {
        List<TimeSlot> slots = timeSlotRepository.findByAppointmentGroupId(group.getId());
        for (TimeSlot slot : slots) {
            if (slot.isBooked()){
                bookedSlots.add(slot);
            }
        }
    }
    model.addAttribute("bookedSlots", bookedSlots);
    return "ta-view-bookings";
}


    
}

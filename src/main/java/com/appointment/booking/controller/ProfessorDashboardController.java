package com.appointment.booking.controller;
import com.appointment.booking.model.AppointmentGroup;
import com.appointment.booking.repository.AppointmentGroupRepository;
import com.appointment.booking.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;

import com.appointment.booking.model.TimeSlot;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
@RequestMapping("/professor-dashboard")

public class ProfessorDashboardController {

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

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

    @GetMapping("/group/{id}/slots")
    public String showTimeSlots(@PathVariable Long id, Model model) {
        AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);
        model.addAttribute("group", group);
        model.addAttribute("slots", timeSlotRepository.findByAppointmentGroupId(id));
        model.addAttribute("timeSlot", new TimeSlot());
        return "add-slots";
    }
/* 
    @PostMapping("/group/{id}/slots")
    public String addSlotToGroup(@PathVariable Long id, @ModelAttribute TimeSlot timeSlot) {
        AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);
        if (group != null) {
            timeSlot.setAppointmentGroup(group);
            timeSlotRepository.save(timeSlot);
        }
        return "redirect:/professor-dashboard/group/" + id + "/slots";
    } */

    @PostMapping("/group/{id}/slots")
    public String addSlotToGroup(@PathVariable Long id, @ModelAttribute TimeSlot submittedSlot) {
    AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);
    if (group != null) {
        TimeSlot newSlot = new TimeSlot();
        newSlot.setStartTime(submittedSlot.getStartTime());
        newSlot.setEndTime(submittedSlot.getEndTime());
        newSlot.setAppointmentGroup(group);
        newSlot.setBooked(false);  
        timeSlotRepository.save(newSlot);
    }
    return "redirect:/professor-dashboard/group/" + id + "/slots";
    }

    @PostMapping("/group/{id}/publish")
    public String publishGroup(@PathVariable Long id){
        AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);
        if (group != null) {
            group.setStatus(AppointmentGroup.Status.PUBLISHED);
            appointmentGroupRepository.save(group); }

            return "redirect:/professor-dashboard";
        
        }

    @PostMapping("/group/{id}/slots/auto")
    public String autoGenerateSlots(@PathVariable Long id, @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                    @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                    @RequestParam("durationInMinutes") int durationInMinutes) {     
        AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);

        if (group != null) {
            List <TimeSlot> slots = new ArrayList<>();
            LocalDateTime current = startTime;
            int slotCount = 0;

            while (current.plusMinutes(durationInMinutes).isBefore(endTime) || current.plusMinutes(durationInMinutes).isEqual(endTime)) {
                TimeSlot slot = new TimeSlot();
                slot.setAppointmentGroup(group);
                slot.setStartTime(current);
                slot.setEndTime(current.plusMinutes(durationInMinutes));
                slot.setBooked(false);  
                slots.add(slot);
                slotCount++;
                current = current.plusMinutes(durationInMinutes);

                if (slotCount %2 == 0) {
                    current = current.plusMinutes(5);
                    
                }

            }
            timeSlotRepository.saveAll(slots);
        } 
        return "redirect:/professor-dashboard/group/" + id + "/slots";
    }
    
    @PostMapping("/group/{id}/slots/auto-multi-day")
    public String autoGenerateMultiDaySlots(
        @PathVariable Long id,
        @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
        @RequestParam("durationInMinutes") int durationInMinutes,
        @RequestParam("selectedDates") String selectedDates) {

            AppointmentGroup group = appointmentGroupRepository.findById(id).orElse(null);

            if(group != null){
                List<LocalDate> dates = new ArrayList<>();
                LocalDate startDate = startTime.toLocalDate();
                dates.add(startDate);

                String[] dateStrings = selectedDates.split(",");
                

                for (String dateStr : dateStrings) {
                    try {
                        LocalDate parsedDate = LocalDate.parse(dateStr.trim());
                        if (!dates.contains(parsedDate)){
                            dates.add(parsedDate);

                        }
                        //dates.add(LocalDate.parse(dateStr.trim()));

                    } catch (Exception e) {
                        continue;
                    }
                }

                for (LocalDate date : dates) {
                    List<TimeSlot> slots = new ArrayList<>();
//have to get time start and endtime
                    LocalDateTime slotStart = LocalDateTime.of(date, startTime.toLocalTime());
                    LocalDateTime slotEnd = LocalDateTime.of(date, endTime.toLocalTime()); 
                    LocalDateTime current = slotStart;
                    int slotCount = 0;
                    while (current.plusMinutes(durationInMinutes).isBefore(slotEnd) || current.plusMinutes(durationInMinutes).isEqual(slotEnd)) {
                        TimeSlot slot = new TimeSlot();
                        slot.setAppointmentGroup(group);
                        slot.setStartTime(current);
                        slot.setEndTime(current.plusMinutes(durationInMinutes));
                        slot.setSlotDate(date);
                        slot.setBooked(false);
                        slots.add(slot);
                        slotCount++;
                        current = current.plusMinutes(durationInMinutes);

                        if (slotCount % 2 == 0) {
                            current = current.plusMinutes(5);

                        }
                    }
                    timeSlotRepository.saveAll(slots);

                }
            }
            return "redirect:/professor-dashboard/group/" + id + "/slots";

        }
        
    
    
}
    




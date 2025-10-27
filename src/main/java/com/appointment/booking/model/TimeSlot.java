package com.appointment.booking.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity


public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isBooked = false; //setting this to false by default means this is not yet boooked.
    // private String bookedBy;  

    @ElementCollection
    @CollectionTable(name = "time_slot_bookings", joinColumns = @JoinColumn(name = "time_slot_id"))
    @Column(name = "student_email")
    private List<String> bookedStudentsEmails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "appointment_group_id")
    private AppointmentGroup appointmentGroup;

    public Long getId() {
        return id; }

    public void setId(Long id) {
        this.id = id;}

    public LocalDateTime getStartTime() {
        return startTime;}

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime; }

    public LocalDateTime getEndTime() {
        return endTime; }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime; }

    public boolean isBooked() {
        return isBooked; }

    public void setBooked(boolean booked) {
        isBooked = booked; }

    public AppointmentGroup getAppointmentGroup() {
        return appointmentGroup;}

    public void setAppointmentGroup(AppointmentGroup appointmentGroup) {
        this.appointmentGroup = appointmentGroup;}

    public List<String> getBookedStudentsEmails() {
        return bookedStudentsEmails;
    }
    public void setBookedStudentsEmails(List <String> bookedStudentsEmails) {
        this.bookedStudentsEmails = bookedStudentsEmails;
    }

    // public String getBookedBy(){
    //     return bookedBy; }
    // public void setBookedBy(String bookedBy){
    //     this.bookedBy = bookedBy;}




}

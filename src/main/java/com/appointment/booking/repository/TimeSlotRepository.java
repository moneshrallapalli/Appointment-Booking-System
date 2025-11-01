package com.appointment.booking.repository;
import com.appointment.booking.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByAppointmentGroupId(Long appointmentGroupId);
    List<TimeSlot> findByAppointmentGroupIdAndIsBookedFalse(Long groupid);

    @Query ("SELECT t FROM TimeSlot t WHERE :email MEMBER OF t.bookedStudentsEmails")
    List<TimeSlot> findByStudentEmail(@Param("email") String email);

    @Query ("SELECT COUNT(t) FROM TimeSlot t WHERE :email MEMBER OF t.bookedStudentsEmails AND t.appointmentGroup.id = :groupId")
    Long countBookingsByStudentInGroup(@Param("email") String email, @Param("groupId") Long groupId);
}


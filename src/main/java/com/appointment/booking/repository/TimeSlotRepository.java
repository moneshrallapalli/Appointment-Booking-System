package com.appointment.booking.repository;
import com.appointment.booking.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByAppointmentGroupId(Long appointmentGroupId);
    List<TimeSlot> findByAppointmentGroupIdAndIsBookedFalse(Long groupid);
}
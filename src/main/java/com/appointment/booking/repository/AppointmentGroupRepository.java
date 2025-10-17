package com.appointment.booking.repository;
import com.appointment.booking.model.AppointmentGroup;
import com.appointment.booking.model.AppointmentGroup.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AppointmentGroupRepository extends JpaRepository<AppointmentGroup, Long> {
    List<AppointmentGroup> findByProfessorUsername(String professorUsername);
    List<AppointmentGroup> findByStatus(Status status);
}






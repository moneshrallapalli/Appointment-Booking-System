package com.appointment.booking.repository;
import com.appointment.booking.model.AppointmentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface AppointmentGroupRepository extends JpaRepository<AppointmentGroup, Long> {
    List<AppointmentGroup> findByProfessorUsername(String professorUsername);
}




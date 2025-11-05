package com.appointment.booking.repository;

import com.appointment.booking.model.AppointmentGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import com.appointment.booking.model.TimeSlot;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TimeSlotRepositorySimpleTest {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private AppointmentGroupRepository appointmentGroupRepository;

    // @Autowired
    // private TestEntityManager entityManager;


    @Test
    void testFindAvailableSlots() {
        AppointmentGroup group = new AppointmentGroup();
        group.setTitle("meeting");
        group.setProfessorUsername("Professor@iu.edu");
        group.setStatus(AppointmentGroup.Status.PUBLISHED);
        group.setCreatedAt(new Date());
        group.setType("individual");
        // entityManager.persistAndFlush(group);
        AppointmentGroup savedGroup = appointmentGroupRepository.save(group);
        TimeSlot slot = new TimeSlot();
        slot.setStartTime(LocalDateTime.of(2025,11,04,10,0));
        slot.setEndTime(LocalDateTime.of(2025,11,04,11,0));
        slot.setBooked(false);
        slot.setSlotDate(LocalDate.of(2025,11,04));
        slot.setAppointmentGroup(savedGroup);
        // entityManager.persistAndFlush(slot);
        timeSlotRepository.save(slot);

        List<TimeSlot> availableSlots = timeSlotRepository.findByAppointmentGroupIdAndIsBookedFalse(savedGroup.getId());

        assertEquals(1, availableSlots.size());
        assertFalse(availableSlots.get(0).isBooked());


    }

    
}

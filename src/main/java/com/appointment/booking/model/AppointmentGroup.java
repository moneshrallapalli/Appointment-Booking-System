package com.appointment.booking.model;
import jakarta.persistence.*;
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
public class AppointmentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String type;
    private String professorUsername;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title =title;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getProfessorUsername() {return professorUsername;}
    public void setProfessorUsername(String professorUsername) {this.professorUsername = professorUsername;}

    public Date getCreatedAt() {return createdAt;}
    public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}

    
}

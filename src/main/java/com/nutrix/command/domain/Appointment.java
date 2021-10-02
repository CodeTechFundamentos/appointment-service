package com.nutrix.command.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "nutritionist_id", nullable = false)
    private Integer nutritionistId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Diet diet;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "appointment_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @Column(name = "nutritionist_notes", nullable = true)
    private String nutritionistNotes;

    public Appointment save(Appointment appointment) throws Exception{return null;}
    public void delete(Integer id) throws Exception{}
    protected List<Appointment> getAll() throws  Exception{return null;}
    public Optional<Appointment> getById(Integer id) throws Exception{return null;}
    public List<Appointment> findBetweenDates(Date date1, Date date2) throws Exception{return null;}
    public List<Appointment> findByPatient(Integer patient_id) throws Exception{return null;}
    public List<Appointment> findByNutritionist(Integer nutritionist_id) throws Exception{return null;}
}


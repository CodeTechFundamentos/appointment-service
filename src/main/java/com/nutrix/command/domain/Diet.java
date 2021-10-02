package com.nutrix.command.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "diet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "create_at",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    public Diet save(Diet diet) throws Exception{return null;}
    public void delete(Integer id) throws Exception{}
    protected List<Diet> getAll() throws  Exception{return null;}
    public Optional<Diet> getById(Integer id) throws Exception{return null;}
}
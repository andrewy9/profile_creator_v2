package com.andrew.profile_creator.models.profiles;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employment_id;
    private String name;
}

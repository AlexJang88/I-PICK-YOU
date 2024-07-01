package com.project.pickyou.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickID implements Serializable {
    @Id
    private String picker;
    @Id
    private String target;
}

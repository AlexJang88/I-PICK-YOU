package com.project.pickyou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenceID implements Serializable {
    private Long resumeId;
    private String name;
}

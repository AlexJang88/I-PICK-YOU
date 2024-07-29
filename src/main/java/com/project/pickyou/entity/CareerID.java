package com.project.pickyou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
/*@NoArgsConstructor
@AllArgsConstructor*/
public class CareerID implements Serializable {
    private Long resumeId;
    private String name;

    public CareerID() {}

    public CareerID(Long resumeId, String name) {
        this.resumeId = resumeId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CareerID careerID = (CareerID) o;
        return resumeId.equals(careerID.resumeId) && name.equals(careerID.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resumeId, name);
    }
}

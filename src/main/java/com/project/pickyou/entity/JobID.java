package com.project.pickyou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
/*@NoArgsConstructor
@AllArgsConstructor*/
public class JobID implements Serializable {
    private Long resumeId;
    private String name;

    public JobID() {}

    public JobID(Long resumeId, String name) {
        this.resumeId = resumeId;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobID JobID = (JobID) o;
        return resumeId.equals(JobID.resumeId) && name.equals(JobID.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resumeId, name);
    }
}

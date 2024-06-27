package com.project.pickyou.entity;

import com.project.pickyou.dto.QaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "qa")
public class QaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Column(name = "member_id")
    private String memberId;
    private int ref;
    private Date reg;

    @Builder
    public QaEntity(Long id, String title, String content, String memberId, int ref, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.ref = ref;
        this.reg = reg;
    }

    public QaDTO toQaDTO() {
        return QaDTO.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .memberId(this.memberId)
                .ref(this.ref)
                .reg(this.reg)
                .build();
    }
}

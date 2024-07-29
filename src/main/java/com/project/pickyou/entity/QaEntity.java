package com.project.pickyou.entity;

import com.project.pickyou.dto.QaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "qa")
@DynamicInsert
public class QaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int ref;
    private String pw;
    private int status;
    private Date reg;

    @Builder
    public QaEntity(Long id, String title, String content, String writer, int ref, String pw, int status, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.ref = ref;
        this.pw = pw;
        this.status = status;
        this.reg = reg;
    }

    public QaDTO toQaDTO() {
        return QaDTO.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .writer(this.writer)
                .ref(this.ref)
                .pw(this.pw)
                .status(this.status)
                .reg(this.reg)
                .build();
    }
}

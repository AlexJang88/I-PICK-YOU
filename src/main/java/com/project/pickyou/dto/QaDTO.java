package com.project.pickyou.dto;

import com.project.pickyou.entity.QaEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class QaDTO {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private int ref;
    private String pw;
    private int status;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date reg;

    @Builder
    public QaDTO(Long id, String title, String content, String writer, int ref, String pw, int status, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.ref = ref;
        this.pw = pw;
        this.status = status;
        this.reg = reg;
    }

    public QaEntity toQaEntity() {
        return QaEntity.builder()
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

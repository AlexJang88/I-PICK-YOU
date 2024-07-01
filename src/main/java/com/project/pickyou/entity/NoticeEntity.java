package com.project.pickyou.entity;

import com.project.pickyou.dto.NoticeDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Column(name = "member_id")
    private String memberId;
    @Column(name = "read_count")
    private int readCount;
    private Date reg;

    @Builder
    public NoticeEntity(Long id, String title, String content, String memberId,  int readCount, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.readCount = readCount;
        this.reg = reg;
    }

    public NoticeDTO toNoticeDTO() {
        return NoticeDTO.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .memberId(this.memberId)
                .readCount(this.readCount)
                .reg(this.reg)
                .build();
    }
}

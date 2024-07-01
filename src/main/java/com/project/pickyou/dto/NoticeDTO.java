package com.project.pickyou.dto;

import com.project.pickyou.entity.NoticeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class NoticeDTO {
    private Long id;
    private String title;
    private String content;
    private String memberId;
    private int readCount;
    @DateTimeFormat(pattern = "yy-MM-dd")
    private Date reg;

    @Builder
    public NoticeDTO(Long id, String title, String content, String memberId, int readCount, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.readCount = readCount;
        this.reg = reg;
    }

    public NoticeEntity toNoticeEntity() {
        return NoticeEntity.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .memberId(this.memberId)
                .readCount(this.readCount)
                .reg(this.reg)
                .build();
    }
}

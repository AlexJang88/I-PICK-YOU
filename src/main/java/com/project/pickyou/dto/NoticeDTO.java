package com.project.pickyou.dto;

import com.project.pickyou.entity.NoticeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class NoticeDTO {
    private Long id;
    private String title;
    private String content;
    private String memberId;
    private Long imageRegistrationId;
    private int readCount;
    private Date reg;

    @Builder
    public NoticeDTO(Long id, String title, String content, String memberId, Long imageRegistrationId, int readCount, Date reg) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.imageRegistrationId = imageRegistrationId;
        this.readCount = readCount;
        this.reg = reg;
    }

    public NoticeEntity toNoticeEntity() {
        return NoticeEntity.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .memberId(this.memberId)
                .imageRegistrationId(this.imageRegistrationId)
                .readCount(this.readCount)
                .reg(this.reg)
                .build();
    }
}

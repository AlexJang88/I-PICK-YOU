package com.project.pickyou.dto;

import com.project.pickyou.entity.SatisfactionEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SatisfactionDTO {
    private Long id;
    private int score;
    private String content;
    private String writer;
    private String target;

    @Builder
    public SatisfactionDTO(Long id, int score, String content, String writer, String target) {
        this.id = id;
        this.score = score;
        this.content = content;
        this.writer = writer;
        this.target = target;
    }

    public SatisfactionEntity toSatisfactionEntity() {
        return SatisfactionEntity.builder()
                .id(this.id)
                .score(this.score)
                .content(this.content)
                .writer(this.writer)
                .target(this.target)
                .build();
    }
}

package com.project.pickyou.entity;

import com.project.pickyou.dto.SatisfactionDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "satisfaction")
public class SatisfactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score;
    private String content;
    private String writer;
    private String target;

    @Builder
    public SatisfactionEntity(Long id, int score, String content, String writer, String target) {
        this.id = id;
        this.score = score;
        this.content = content;
        this.writer = writer;
        this.target = target;
    }

    public SatisfactionDTO toSatisfactionDTO() {
        return SatisfactionDTO.builder()
                .id(this.id)
                .score(this.score)
                .content(this.content)
                .writer(this.writer)
                .target(this.target)
                .build();
    }
}

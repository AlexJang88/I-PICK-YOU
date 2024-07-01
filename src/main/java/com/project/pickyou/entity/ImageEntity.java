package com.project.pickyou.entity;

import com.project.pickyou.dto.ImageDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.fasterxml.jackson.databind.util.ClassUtil.name;

@Data
@NoArgsConstructor
@Entity
@Table(name = "image")
@DynamicInsert
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name="board_type")
    private int boardType;
    @Column(name="board_num")
    private Long boardNum;

    @Builder
    public ImageEntity(Long id, String name, int boardType, Long boardNum) {
        super();
        this.id = id;
        this.name = name;
        this.boardType = boardType;
        this.boardNum = boardNum;
    }



    // Convert DTO to Entity
    public ImageDTO toImageDTO() {
        return ImageDTO.builder()

                .id(this.id)
                .name(this.name)
                .boardType(this.boardType)
                .boardNum(this.boardNum)
                 .build();
    }


}

package com.project.pickyou.entity;

import com.project.pickyou.dto.ImageRegistrationDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "image_registration")

public class ImageRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="board_type")
    private int boardType;
    @Column(name="board_num")
    private Long boardNum;


    @Builder
    public ImageRegistrationEntity(Long id, int boardType, Long boardNum) {
        super();
        this.id = id;
        this.boardType = boardType;
        this.boardNum = boardNum;
    }

    public ImageRegistrationDTO toImageRegistrationDTO() {
        return ImageRegistrationDTO.builder()

                .id(this.id)
                .boardType(this.boardType)
                .boardNum(this.boardNum)
                 .build();
    }

}

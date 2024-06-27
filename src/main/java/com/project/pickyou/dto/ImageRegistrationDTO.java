package com.project.pickyou.dto;

import com.project.pickyou.entity.ImageRegistrationEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageRegistrationDTO {
    private Long id;
    private int boardType;
    private Long boardNum;



    @Builder
    public ImageRegistrationDTO(Long id, int boardType, Long boardNum) {
        super();
        this.id = id;
        this.boardType = boardType;
        this.boardNum = boardNum;
    }

    public ImageRegistrationEntity toImageRegistrationEntity() {
        return ImageRegistrationEntity.builder()
                .id(this.id)
                .boardType(this.boardType)
                .boardNum(this.boardNum)
                 .build();
    }



}

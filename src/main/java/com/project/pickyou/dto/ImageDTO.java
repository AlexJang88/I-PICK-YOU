package com.project.pickyou.dto;

import com.project.pickyou.entity.ImageEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String name;
    private int boardType;
    private Long boardNum;


    @Builder
    public ImageDTO(Long id, String name, int boardType, Long boardNum) {
        super();
        this.id = id;
        this.name = name;
        this.boardType = boardType;
        this.boardNum = boardNum;
    }



    // Convert DTO to Entity
    public ImageEntity toImageEntity() {
        return ImageEntity.builder()
                .id(this.id)
                .name(this.name)
                .boardType(this.boardType)
                .boardNum(this.boardNum)
                 .build();
    }





}

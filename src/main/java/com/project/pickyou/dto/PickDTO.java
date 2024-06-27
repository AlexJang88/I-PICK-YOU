package com.project.pickyou.dto;

import com.project.pickyou.entity.PickEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PickDTO {

    private String picker;
    private String target;


    @Builder
    public PickDTO(String picker, String target) {
        super();
        this.picker = picker;
        this.target = target;
    }
        public PickEntity toPickEntity() {
            return PickEntity.builder()
                .picker(this.picker)
                .target(this.target)
                    .build();
        }


}

package com.project.pickyou.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDTO {
    private String id;
    private String name;
    private String birth;
    private String height;
    private String weight;
    private String health;
    private int gender;

    @Builder
    public MemberInfoDTO(String id, String name, String birth, String height, String weight, String health, int gender){
        super();
        this.id=id;
        this.name=name;
        this.birth=birth;
        this.height=height;
        this.weight=weight;
        this.health=health;
        this.gender=gender;
    }



}

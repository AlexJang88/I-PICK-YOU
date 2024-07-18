package com.project.pickyou.service;

import com.project.pickyou.dto.AgencyDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.entity.AgencyEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface AgencyService {


    public void agencyCount(Long agencynum); //조회수 증가

    public void agencymain(Model model, int pageNum);

    public void agencyDetails(Model model, Long agencynum);

    public AgencyEntity saveAgency(AgencyDTO agencyDTO);

    public void saveImage(ImageDTO imageDTO, MultipartFile[] files);

    public void deleteAgencyImg(Long agencynum); //소개소 이미지지우기

    public void deleteAgencyNum(Long agencynum);  //소개소 하나 지우기

    public void agencyUpdate(Long agencynum, AgencyDTO agencyDTO, MultipartFile[] files); //업데이트(수정)
}

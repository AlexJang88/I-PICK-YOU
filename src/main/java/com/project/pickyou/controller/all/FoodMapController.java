package com.project.pickyou.controller.all;

import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.entity.FoodMapEntity;
import com.project.pickyou.service.FoodMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/foodMap/*")
public class FoodMapController {

    private final FoodMapService foodMapService;

    // 푸드맵 리스트 가져오기, 페이징 처리
    @GetMapping("posts")
    public String list(Model model, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {
        foodMapService.AllPosts(model, pageNum);
        return "/foodMap/list";
    }

    // 글쓰기
    @GetMapping("posts/new")
    public String write() {
        return "/foodMap/write";
    }

    // 글쓰기 pro
    @PostMapping("posts")
    public String writePro(FoodMapDTO dto, ImageDTO imageDTO, Principal principal,
                           @RequestParam("files") MultipartFile[] files) {

        // 푸드맵 인서트
        dto.setMemberId(principal.getName());
        FoodMapEntity saveFoodMap = foodMapService.foodMapInsert(dto);

        // 위에서 이미지 파일에 등록된 훈련소 번호의 숫자를 가져옴
        Long foodMapId = saveFoodMap.getId();

        // 푸드맵 이미지 인서트
        imageDTO.setBoardNum(foodMapId);
        foodMapService.saveImage(imageDTO, files);

        return "redirect:/foodMap/posts";
    }

    // 푸드맵 상세정보
    @GetMapping("posts/{ref}")
    public String info(Model model, @PathVariable int ref) {
        foodMapService.foodMapInfo(ref, model);

        long longRef = (long) ref;
        foodMapService.foodMapImage(longRef, 4, model);
        return "/foodMap/info";
    }

    // 푸드맵 글 삭제
    @DeleteMapping("/posts/{boardNum}")
    public String delete(@PathVariable Long boardNum) {
        foodMapService.foodMapDelete(boardNum);
        return "redirect:/foodMap/posts";
    }

    // 푸드맵 글 수정
    @GetMapping("/posts/{boardNum}/edit")
    public String update(Model model, @PathVariable Long boardNum) {
        foodMapService.foodMapUpdate(model, boardNum);
        return "foodMap/update";
    }

    // 푸드맵 글 수정 pro
    @PutMapping("/posts/{id}")
    public String updatePro(@PathVariable Long id, ArrayList<MultipartFile> files, FoodMapDTO dto) {
        foodMapService.update(files, dto);
        return "redirect:/foodMap/posts/"+id;
    }

    // 댓글작성하기
    @PostMapping("/posts/{boardNum}/ref")
    public String ref(Model model, @PathVariable Long boardNum) {

        return "foodMap/update";
    }

}

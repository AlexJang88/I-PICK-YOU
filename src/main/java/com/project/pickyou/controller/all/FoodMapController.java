package com.project.pickyou.controller.all;

import com.project.pickyou.dto.FoodMapDTO;
import com.project.pickyou.dto.ImageDTO;
import com.project.pickyou.dto.NoticeDTO;
import com.project.pickyou.dto.PointDTO;
import com.project.pickyou.entity.FoodMapEntity;
import com.project.pickyou.service.FoodMapService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/foodMap/*")
public class FoodMapController {

    private final FoodMapService foodMapService;

    // 푸드맵 글번호
    int type = 4;

    // 푸드맵 리스트 가져오기, 페이징 처리
    @GetMapping("posts")
    public String list(Model model, Principal principal,
                       @RequestParam(value = "pageNum", defaultValue = "1") int pageNum) {

        // @@
        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        foodMapService.AllPosts(model, pageNum);
        return "/foodMap/list";
    }

    // 글쓰기
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @GetMapping("posts/new")
    public String write(Principal principal, Model model) {

        // @@
        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        return "/foodMap/write";
    }

    // 글쓰기 pro
    @PostMapping("posts")
    public String writePro(FoodMapDTO dto, ImageDTO imageDTO, Principal principal,
                           @RequestParam("files") MultipartFile[] files,
                           @RequestParam("address") String address) {

        // 푸드맵 인서트
        dto.setMemberId(principal.getName());
        dto.setMap(address);
        FoodMapEntity saveFoodMap = foodMapService.foodMapInsert(dto);

        // 위에서 이미지 파일에 등록된 푸드맵 번호의 숫자를 가져옴
        Long foodMapId = saveFoodMap.getId();

        // 푸드맵 이미지 인서트
        imageDTO.setBoardNum(foodMapId);
        foodMapService.saveImage(imageDTO, files);

        return "redirect:/foodMap/posts";
    }

    // 푸드맵 상세정보
    @GetMapping("posts/{ref}")
    public String info(Model model, @PathVariable int ref, Principal principal, HttpSession session, HttpServletRequest request) {
        long longRef = (long) ref;

        String sid = "";
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        if (principal != null) {  //로그인 되어잇을때 조회수 올리기
            sid = principal.getName();
            model.addAttribute("id", principal.getName());
            if (session.getAttribute(sid + "_" + type + "_" + ref) == null) {
                foodMapService.foodMapCnt(longRef, ref, model);
                session.setAttribute(sid + "_" + type + "_" + ref, "true");
            }
        } else {   //로그인안되어있을때 조회수 올리기
            sid = ip;
            if (session.getAttribute(sid + "_" + type + "_" + ref) == null) {
                foodMapService.foodMapCnt(longRef, ref, model);
                session.setAttribute(sid + "_" + type + "_" + ref, "true");
            }
        }
        model.addAttribute("sessionId", sid);

        foodMapService.foodMapInfo(ref, model);
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
    @PreAuthorize("hasAnyRole('ROLE_COMPANY','ROLE_USER')")
    @GetMapping("/posts/{boardNum}/edit")
    public String update(Model model, @PathVariable Long boardNum, Principal principal) {

        // @@
        String id = "";
        if (principal != null) {
            model.addAttribute("id", principal.getName());
        }
        // @@

        foodMapService.foodMapUpdate(model, boardNum);
        return "foodMap/update";
    }

    // 푸드맵 글 수정 pro
    @PutMapping("/posts/{id}")
    public String updatePro(@PathVariable Long id, ArrayList<MultipartFile> files, FoodMapDTO dto) {

        foodMapService.update(files, dto);

        return "redirect:/foodMap/posts/" + id;
    }

    // 푸드맵 댓글 작성하기
    @PostMapping("/posts/{boardNum}/ref")
    public String ref(ImageDTO imageDTO, FoodMapDTO fmDTO, Model model, @PathVariable int boardNum, Principal principal,
                      @RequestParam("files") MultipartFile[] files) {

        // foodMap 댓글 인서트
        fmDTO.setMemberId(principal.getName());
        FoodMapEntity saveFoodMap = foodMapService.refInsert(fmDTO, boardNum);


        // 파일이 비어있지 않은 경우에만 이미지 저장
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Long foodMapId = saveFoodMap.getId();
            imageDTO.setBoardNum(foodMapId);
            foodMapService.saveImage(imageDTO, files);
        }


        return "redirect:/foodMap/posts/{boardNum}";
    }

    // 푸드맵 대댓글 작성하기
    @PostMapping("/posts/{boardNum}/reply")
    public String reply(ImageDTO imageDTO, FoodMapDTO fmDTO, Principal principal, @PathVariable int boardNum,
                        @RequestParam("files") MultipartFile[] files,
                        @RequestParam("fullContent") String fullContent,
                        @RequestParam("foodMapNum") Long foodMapNum) {

        // int id = boardNum.intValue();
        // foodMap 대댓글 인서트
        fmDTO.setMemberId(principal.getName());
        fmDTO.setReply(boardNum);
        fmDTO.setContent(fullContent + fmDTO.getContent());
        FoodMapEntity saveFoodMap = foodMapService.replyInsert(fmDTO);

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Long foodMapId = saveFoodMap.getId();
            imageDTO.setBoardNum(foodMapId);
            foodMapService.saveImage(imageDTO, files);
        }

        return "redirect:/foodMap/posts/" + foodMapNum;
    }

    // 푸드맵 인증글 체크
    @PostMapping("/posts/{boardNum}/check")
    public String check(PointDTO Pdto, @PathVariable Long boardNum, FoodMapDTO FMdto) {
        foodMapService.foodMapPointInsert(Pdto, FMdto);
        return "redirect:/foodMap/posts";
    }

    // TEST
    @GetMapping("/test")
    public String test(Model model) {
        List<List<Map<String, Object>>> dataPointsList = new ArrayList<>();

        List<Map<String, Object>> dataPoints = new ArrayList<>();
        dataPoints.add(createDataPoint(1640995200000L, 100000));
        dataPoints.add(createDataPoint(1643673600000L, 300000));
        dataPoints.add(createDataPoint(1646092800000L, 223410));
        dataPoints.add(createDataPoint(1648771200000L, 432342));
        dataPoints.add(createDataPoint(1651363200000L, 300000));

        dataPointsList.add(dataPoints);

        model.addAttribute("dataPointsList", dataPointsList);
        return "foodMap/test";
    }

    private Map<String, Object> createDataPoint(long x, int y) {
        Map<String, Object> dataPoint = new HashMap<>();
        dataPoint.put("x", x);
        dataPoint.put("y", y);
        return dataPoint;
    }








}

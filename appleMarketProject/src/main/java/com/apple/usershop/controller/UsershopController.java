package com.apple.usershop.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apple.common.util.UsershopImgUtil;
import com.apple.product.domain.Product;
import com.apple.usershop.domain.ItemReview;
import com.apple.usershop.domain.Usershop;
import com.apple.usershop.service.UsershopService;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/usershop")
@RequiredArgsConstructor
public class UsershopController {

    @Setter(onMethod_ = @Autowired)
    private UsershopService usershopService;
    
    private final UsershopImgUtil fileUtil;  // 이미지 유틸리티 주입

    // 특정 userNo에 대한 상점 페이지를 가져옴
    @GetMapping("/{userNo}")
    public String usershop(@PathVariable Long userNo, Model model) {
        if (userNo != null) {
            // Usershop 정보 설정
            Usershop usershop = usershopService.findByUserNo(userNo);
            model.addAttribute("usershop", usershop);
            
            // 상품 목록 가져오기
            List<Product> usershopItemList = usershopService.usershopItemListByUserNo(userNo);
            model.addAttribute("usershopItemList", usershopItemList);

            // 상품 개수 가져오기
            Long productCount = usershopService.countProductsByUserNo(userNo);
            model.addAttribute("productCount", productCount);

            // 리뷰 목록 가져오기
            List<ItemReview> itemReviewList = usershopService.usershopReviewListByShopId(usershop.getShopId());
            model.addAttribute("itemReviewList", itemReviewList);
            
            // 평균 평점 계산 및 설정
            long averageStarRating = usershopService.calculateAverageStarRating(itemReviewList);
            model.addAttribute("averageStarRating", averageStarRating);
            
            // 만족도 퍼센트 계산 및 설정
            double satisfactionPercentage = usershopService.calculateSatisfactionPercentage(itemReviewList);
            model.addAttribute("satisfactionPercentage", satisfactionPercentage);
            
            // 가입일 가져오기
            model.addAttribute("formattedDate", new SimpleDateFormat("yyyy-MM-dd").format(usershop.getUser().getUserRegDate()));

            // 상점 방문 카운트 증가
            usershopService.shopVisitCount(usershop.getShopId());
            
            long sumSelectReview1 = usershopService.sumSelectReview1ByShopId(usershop.getShopId());
            long sumSelectReview2 = usershopService.sumSelectReview2ByShopId(usershop.getShopId());
            long sumSelectReview3 = usershopService.sumSelectReview3ByShopId(usershop.getShopId());
            long sumSelectReview4 = usershopService.sumSelectReview4ByShopId(usershop.getShopId());
            long sumSelectReview5 = usershopService.sumSelectReview5ByShopId(usershop.getShopId());

            model.addAttribute("sumSelectReview1", sumSelectReview1);
            model.addAttribute("sumSelectReview2", sumSelectReview2);
            model.addAttribute("sumSelectReview3", sumSelectReview3);
            model.addAttribute("sumSelectReview4", sumSelectReview4);
            model.addAttribute("sumSelectReview5", sumSelectReview5);
        }

        return "usershop/usershop";
    }

    // 상점 정보 업데이트
    @PostMapping("/usershopUpdate")
    public String userShopUpdate(@RequestParam("userNo") Long userNo, 
                                 @RequestParam("introText") String introText, 
                                 @RequestParam("newNickname") String newNickname) {

        Usershop usershop = usershopService.findByUserNo(userNo);

        if (usershop != null) {
            usershopService.updateShopIntroduce(usershop.getShopId(), introText);
            usershopService.updateUserNickname(userNo, newNickname);
            return "redirect:/usershop/" + userNo;  // 업데이트 후 다시 페이지로 리다이렉트
        } else {
            return "redirect:/errorPage";  // 에러 페이지로 리다이렉트
        }
    }

    // 상점 이미지 업데이트
    @PostMapping("/shopImgUpdate")
    public String shopImgUpdate(@RequestParam("file") MultipartFile file, @RequestParam("shopId") String shopId) {
        Usershop usershop = usershopService.findByShopId(shopId);

        if (usershop != null && !file.isEmpty()) {
            // 파일 저장 및 상점 이미지 업데이트
            String uploadFileName = fileUtil.saveFile(file);
            usershop.setShopImgName(uploadFileName);
            usershopService.updateUsershop(usershop);  // 변경 사항 저장
        }

        return "redirect:/usershop/" + usershop.getUser().getUserNo();
    }

    // 특정 상점에 대한 리뷰 및 기타 정보를 조회
    @GetMapping("/list{userNo}")
    public String usershopList(@PathVariable Long userNo, Model model) {
        if (userNo != null) {
        	// Usershop 정보 설정
            Usershop usershop = usershopService.findByUserNo(userNo);
            model.addAttribute("usershop", usershop);
            
            // 상품 목록 가져오기
            List<Product> usershopItemList = usershopService.usershopItemListByUserNo(userNo);
            model.addAttribute("usershopItemList", usershopItemList);

            // 상품 개수 가져오기
            Long productCount = usershopService.countProductsByUserNo(userNo);
            model.addAttribute("productCount", productCount);

            // 리뷰 목록 가져오기
            List<ItemReview> itemReviewList = usershopService.usershopReviewListByShopId(usershop.getShopId());
            model.addAttribute("itemReviewList", itemReviewList);
            
            // 평균 평점 계산 및 설정
            long averageStarRating = usershopService.calculateAverageStarRating(itemReviewList);
            model.addAttribute("averageStarRating", averageStarRating);
            
            // 만족도 퍼센트 계산 및 설정
            double satisfactionPercentage = usershopService.calculateSatisfactionPercentage(itemReviewList);
            model.addAttribute("satisfactionPercentage", satisfactionPercentage);

            // 가입일 가져오기
            model.addAttribute("formattedDate", new SimpleDateFormat("yyyy-MM-dd").format(usershop.getUser().getUserRegDate()));

            // 상점 방문 카운트 증가
            usershopService.shopVisitCount(usershop.getShopId());
            
            long sumSelectReview1 = usershopService.sumSelectReview1ByShopId(usershop.getShopId());
            long sumSelectReview2 = usershopService.sumSelectReview2ByShopId(usershop.getShopId());
            long sumSelectReview3 = usershopService.sumSelectReview3ByShopId(usershop.getShopId());
            long sumSelectReview4 = usershopService.sumSelectReview4ByShopId(usershop.getShopId());
            long sumSelectReview5 = usershopService.sumSelectReview5ByShopId(usershop.getShopId());

            model.addAttribute("sumSelectReview1", sumSelectReview1);
            model.addAttribute("sumSelectReview2", sumSelectReview2);
            model.addAttribute("sumSelectReview3", sumSelectReview3);
            model.addAttribute("sumSelectReview4", sumSelectReview4);
            model.addAttribute("sumSelectReview5", sumSelectReview5);
        }

        // "client/usershop/usershopList"를 반환하여 올바른 뷰를 렌더링
        return "usershop/usershopList";
    }

    
    // 이미지 파일을 보여줌
    @ResponseBody
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }
}

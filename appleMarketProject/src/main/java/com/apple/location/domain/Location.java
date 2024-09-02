package com.apple.location.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "location")
public class Location {
	
	@Id
	@Column(name="location_id")
	private long locationID;
	
	@Column(name="location_name", nullable = false)
	private String locationName;

	/*
	*
    @GetMapping("/user/location")
    public String getUserLocation(@AuthenticationPrincipal User user, Model model) {
        if (user != null && user.getLocation() != null) {
            String locationName = user.getLocation().getLocationName();
            model.addAttribute("locationName", locationName);
        } else {
            model.addAttribute("locationName", "알 수 없음");
        }
        return "user/locationView"; // 실제 뷰의 이름을 사용하세요.
    }
    * <a class="header-link" href="#">
    현재 지역(${locationName}) 으로 검색
</a>
@GetMapping("/user/location")
public String getUserLocation(@SessionAttribute("user") User user, Model model) {
    if (user != null && user.getLocation() != null) {
        String locationName = user.getLocation().getLocationName();
        model.addAttribute("locationName", locationName);
    } else {
        model.addAttribute("locationName", "알 수 없음");
    }
    return "user/locationView"; // 실제 뷰의 이름을 사용하세요.
}
*
* @GetMapping("/somePage")
public String somePage(Model model) {
    User currentUser = getCurrentUser(); // 세션 또는 SecurityContext에서 현재 유저를 가져옴

    if (currentUser != null && currentUser.getLocation() != null) {
        model.addAttribute("locationID", currentUser.getLocation().getLocationID());
        model.addAttribute("locationName", currentUser.getLocation().getLocationName());
    } else {
        model.addAttribute("locationID", null);
        model.addAttribute("locationName", "알 수 없음");
    }

    return "yourTemplate"; // 실제 템플릿 이름
}

	* */
	
}

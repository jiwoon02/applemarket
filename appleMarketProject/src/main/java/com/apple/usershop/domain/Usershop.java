package com.apple.usershop.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import com.apple.user.domain.User;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="usershop")
public class Usershop {
    
    @Id
    private String shopId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "user_no")
    private User user;
    
    @ColumnDefault(value = "0")
    private Long shopVisitCount;
    
    @Lob
    private String shopIntroduce;
    
    @Transient
    private MultipartFile file;
    
    @Column
    private String shopImgName;
}

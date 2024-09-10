package com.apple.usershop.domain;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import com.apple.user.domain.User;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@Entity
@Table(name="usershop")
@SequenceGenerator(name="usershop_generator", sequenceName="apple_usershop_seq", initialValue=1, allocationSize=1)
public class Usershop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="usershop_generator")
    private Long shopId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", referencedColumnName = "user_no", nullable = false)
    private User user;
    
    @ColumnDefault(value = "0")
    @Column(nullable = false)
    private Long shopVisitCount;
    
    @Lob
    private String shopIntroduce;
    
    @Transient
    private MultipartFile file;
    
    @Column
    private String shopImgName;
}

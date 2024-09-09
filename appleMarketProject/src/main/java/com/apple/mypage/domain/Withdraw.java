package com.apple.mypage.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "withdraw")
public class Withdraw {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "withdraw_seq_generator")
    @SequenceGenerator(name = "withdraw_seq_generator", sequenceName = "withdraw_seq", allocationSize = 1)
    @Column(name = "withdraw_no")
    private Long withdrawNo;
    
    @Column(name = "user_no", nullable = false)
    private Long userNo;
    
    @Column(name = "withdraw_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date withdrawDate;
    
    @Column(name = "reason")
    private String reason;

}

package com.apple.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.mypage.domain.Withdraw;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {

}

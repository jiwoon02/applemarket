package com.apple.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apple.admin.domain.CommunityReport;

@Repository
public interface CommunityReportRepository extends JpaRepository<CommunityReport, Long>{


	
	
	@Query("SELECT c.communityPost.communityPostID as communityPostID, COUNT(c) AS count FROM CommunityReport c GROUP BY c.communityPost.communityPostID")
	List<Object[]> ReportCount();
	
	
	@Modifying
    @Query("DELETE FROM CommunityReport cr WHERE cr.communityPost.communityPostID IN :communityPostids")
	void deleteByCommunityPostids(List<Long> communityPostids);
	
	
	

}

package com.example.RecomendationSystem.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.RecomendationSystem.Entity.User;
import com.example.RecomendationSystem.Entity.WatchedHistory;
import com.example.RecomendationSystem.Entity.Enum.CountStatus;
@Repository
public interface WatchedHistoryRepository extends JpaRepository<WatchedHistory, Long>{
	@Query("Select w from WatchedHistory w Join Fetch w.movie where w.user.id =: userId")
	public List<WatchedHistory> getByUserId(@Param("userId") long userid);
	@Modifying 
	@Query("DELETE FROM WatchedHistory w WHERE w.user.id = :userId")
	public void deleteAllByUserId(@Param("userId") long userid);
	
	public void deleteWatchedHistoryByUserIdAndMovieId(long userid, long movieId);
	@Query("Select w from WatchedHistory w Join Fetch w.movie where w.user.id =: userId AND w.status =: status")
	public List<WatchedHistory> getByUserIdAndStatus(@Param("userId") long userid,@Param("status") CountStatus status);
	
	
}

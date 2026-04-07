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
	@Query("Select w from WatchedHistory w Join Fetch w.movie where w.user.id = :userId")
	public List<WatchedHistory> getByUserId(@Param("userId") long userId);
	@Modifying 
	@Query("DELETE FROM WatchedHistory w WHERE w.user.id = :userId")
	public void deleteAllByUserId(@Param("userId") long userId);
	
	public void deleteWatchedHistoryByUserIdAndMovieId(long userId, long movieId);
	@Query("Select w from WatchedHistory w Join Fetch w.movie where w.user.id =:userId AND w.status IN :statuses")
	public List<WatchedHistory> getByUserIdAndStatus(@Param("userId") long userId,@Param("statuses") List<CountStatus>  status);
	
	
}

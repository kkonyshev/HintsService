package us.im360.crmdbdata.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import us.im360.crmdbdata.entities.DatabaseVersion;



/**
 * 
 * @author Aida
 * @since v1.0
 */

public interface DatabaseVersionRepository extends JpaRepository<DatabaseVersion, Integer> {
	
	@Query("SELECT d FROM DatabaseVersion d ORDER BY d.id DESC")
	List<DatabaseVersion> getLatestUpdate();
	
	@Query("SELECT d FROM DatabaseVersion d ORDER BY d.id")
	List<DatabaseVersion> getAllUpdates();
}

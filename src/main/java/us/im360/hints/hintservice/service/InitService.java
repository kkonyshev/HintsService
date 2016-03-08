package us.im360.hints.hintservice.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import us.im360.crmdbdata.entities.DatabaseVersion;
import us.im360.crmdbdata.repositories.DatabaseVersionRepository;

import us.im360.hints.hintservice.result.AbstractResult;
import us.im360.hints.hintservice.result.DatabaseVersionResult;
import us.im360.hints.hintservice.result.ResultCode;
import us.im360.hints.hintservice.result.ResultReason;

/**
 * 
 * @author Aida
 */

@Service
@Transactional
public class InitService {
		
	@Autowired
	DatabaseVersionRepository dbVersionRepository; 


	/**
	 * @return all the updates
	 */
	public DatabaseVersionResult getAllUpdates() {
		DatabaseVersionResult dbVersionResult = new DatabaseVersionResult();

		List<DatabaseVersion> list = dbVersionRepository.getAllUpdates();

		if (list.isEmpty()) {
			dbVersionResult.setResultCode(ResultCode.FAIL);
			dbVersionResult.setResultReason(ResultReason.UPDATE_NOT_FOUND);
		} else {
			for (DatabaseVersion dbVersion : list) {
				dbVersionResult.addToDBVersionList(dbVersion);
			}
			dbVersionResult.setResultCode(ResultCode.SUCCESS);
		}
		return dbVersionResult;
	}
	
	
	/**
	 * @return all the information regarding the last update
	 */
	public DatabaseVersionResult getLatestupdate() {
		DatabaseVersionResult dbVersionResult = new DatabaseVersionResult();

		List<DatabaseVersion> list = dbVersionRepository.getLatestUpdate();
		
		if (list.isEmpty()) {
			dbVersionResult.setResultCode(ResultCode.FAIL);
			dbVersionResult.setResultReason(ResultReason.UPDATE_NOT_FOUND);
		} else {
			dbVersionResult.addToDBVersionList(list.get(0));
			dbVersionResult.setResultCode(ResultCode.SUCCESS);
		}
		return dbVersionResult;
	}
	

}

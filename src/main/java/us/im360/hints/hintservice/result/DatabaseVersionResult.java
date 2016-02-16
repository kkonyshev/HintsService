package us.im360.hints.hintservice.result;

import java.util.ArrayList;
import java.util.List;

import us.im360.crmdbdata.entities.DatabaseVersion;

/**
 * 
 * @author Aida
 */
public class DatabaseVersionResult extends AbstractResult{
	
	private static final long serialVersionUID = 1L;

	public List<DatabaseVersion> dbVersionList = new ArrayList<DatabaseVersion>();
	
	public List<DatabaseVersion> getDbVersionList() {
		return dbVersionList;
	}
	public void setDbVersionList(List<DatabaseVersion> dbVersionList) {
		this.dbVersionList = dbVersionList;
	}
	
	/**
	 * adds a new databaseVersion to the list
	 * @param dbVersion
	 */
	public void addToDBVersionList(DatabaseVersion dbVersion) {
		this.dbVersionList.add(dbVersion);
	}

}

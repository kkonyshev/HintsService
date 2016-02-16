package us.im360.hints.hintservice;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;


/**
 * @author 	Aida
 * 
 */
public interface InitHandler {
	
	@GET
	Response connect();
	
	@GET 
	Response getAllUpdates();
		
	@GET
	Response getLatestUpdate();
	
	
}
 	
package us.im360.hints.hintservice.handlerImpl;


import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.im360.hints.hintservice.InitHandler;

import us.im360.hints.hintservice.result.AbstractResult;
import us.im360.hints.hintservice.service.InitService;
import us.im360.hints.hintservice.util.HintsUtils;

/**
 * Init API Handler, for debug and test only
 * @author Aida
 * @version v1.0
 * @since v1.0
 */

@Component
@Path("init")
public class InitHandlerImpl implements InitHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(InitHandler.class);	
	
	
	@Autowired
	InitService initService;
		
	@Autowired
	private ObjectMapper mapper;
	
	@GET
	@Path("connect")
	@Override
	public Response connect() {
	   	
		return Response.ok().build();
	}
	

	
	@GET
	@Path("databaseversion/allupdates")
	@Override
	public Response getAllUpdates() {
		String result = "{}";
		
		try {
			AbstractResult abstractResult = initService.getAllUpdates();
			result = mapper.writeValueAsString(abstractResult);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException", e);
			return Response.serverError().build();
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException", e);
			return Response.serverError().build();
		} catch (IOException e) {
			logger.error("IOException", e);
			return Response.serverError().build();
		}
		
		return Response.status(200).entity(result)
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	
	@GET
	@Path("databaseversion/latestupdate")
	@Override
	public Response getLatestUpdate() {
		String result = "{}";
		
		try {
			AbstractResult abstractResult = initService.getLatestupdate();
			result = mapper.writeValueAsString(abstractResult);
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException", e);
			return Response.serverError().build();
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException", e);
			return Response.serverError().build();
		} catch (IOException e) {
			logger.error("IOException", e);
			return Response.serverError().build();
		}
		
		return Response.status(200).entity(result)
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	
}








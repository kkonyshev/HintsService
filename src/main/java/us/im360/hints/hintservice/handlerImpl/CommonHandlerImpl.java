package us.im360.hints.hintservice.handlerImpl;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.CommonHandler;
import us.im360.hints.hintservice.InitHandler;
import us.im360.hints.hintservice.result.AbstractResult;
import us.im360.hints.hintservice.service.InitService;
import us.im360.hints.hintservice.service.TicketService;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 *
 */
@Component
@Path("details")
public class CommonHandlerImpl extends AbstractHandlerImpl implements CommonHandler {

	private static final Logger logger = LoggerFactory.getLogger(InitHandler.class);

	@Autowired
	private TicketService ticketService;

	@GET
	@Path("ticket/userId/{userId}/restaurantId/{restaurantId}/ticketVisibleId/{ticketVisibleId}")
	@Override
	public Response getTicketDetail(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("ticketVisibleId") Integer ticketVisibleId)
	{
		logger.debug("userId: {}, restaurantId: {}, ticketVisibleId: {}", userId, restaurantId, ticketVisibleId);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = ticketService.getTicketDetails(userId, restaurantId, ticketVisibleId);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

}








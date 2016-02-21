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
import us.im360.hints.hintservice.service.ProductService;
import us.im360.hints.hintservice.service.TicketService;
import us.im360.hints.hintservice.service.UserService;
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
@SuppressWarnings("unused")
@Component
@Path("/")
public class CommonHandlerImpl extends AbstractHandlerImpl implements CommonHandler {

	private static final Logger logger = LoggerFactory.getLogger(InitHandler.class);

	@Autowired
	private TicketService ticketService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;


	@GET
	@Path("details/ticket/userId/{userId}/restaurantId/{restaurantId}/ticketVisibleId/{ticketVisibleId}")
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
			responseBuilder.success().withArray(PRODUCTS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("user/list/userId/{userId}/restaurantId/{restaurantId}/groupId/{groupId}/active/{active}")
	@Override
	public Response getUsers(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("groupId") Integer groupId,
			@PathParam("active") Integer active
	) {
		logger.debug("userId: {}, restaurantId: {}, groupId: {}, active: {}", userId, restaurantId, active);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = userService.getUsers(restaurantId, groupId, active);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(USERS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("product/stock/userId/{userId}/restaurantId/{restaurantId}/productId/{productId}")
	@Override
	public Response getProductStock(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("productId") String productId)
	{
		logger.debug("restaurantId: {}, userId: {}, productId: {}", restaurantId, userId, productId);

		JsonNode stock = productService.getProductStock(productId);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (stock!=null) {
			responseBuilder.success().withPlainNode(stock);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}
}








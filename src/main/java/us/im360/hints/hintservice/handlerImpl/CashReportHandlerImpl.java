package us.im360.hints.hintservice.handlerImpl;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.CashReportHandler;
import us.im360.hints.hintservice.InitHandler;
import us.im360.hints.hintservice.service.CashReportService;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Cash report service handler implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Component
@Path("cash/report")
public class CashReportHandlerImpl implements CashReportHandler {

	private static final Logger logger = LoggerFactory.getLogger(InitHandler.class);
		
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CashReportService cashReportService;

	private static final String FIELD_NAME = "details";

	/**
	 * Cash report on date
	 *
	 * @param restaurantId terminal identifier
	 * @param closeDate report date
	 * @return
	 */
	@GET
	@Path("/{restaurantId}/{closeDate}")
	@Override
	public Response getCloseReportOnDate(
			@PathParam("restaurantId") Integer restaurantId, @PathParam("closeDate") String closeDate
	) {
		logger.debug("restaurantId: {}, closeDate: {}" + restaurantId, closeDate);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = cashReportService.getCloseReport(restaurantId, closeDate);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	/**
	 * Helper method for building response object
	 *
	 * @param responseBuilder response success/fail builder instance
	 * @return
     */
	private Response buildResponse(ResponseBuilder responseBuilder) {
		try {
			String resultStr = objectMapper.writeValueAsString(responseBuilder.build());
			return Response.status(200).entity(resultStr).type(MediaType.APPLICATION_JSON).build();
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException", e);
			return Response.serverError().build();
		} catch (JsonGenerationException e) {
			logger.error("JsonGenerationException", e);
			return Response.serverError().build();
		} catch (IOException e) {
			logger.error("IOException", e);
			return Response.serverError().build();
		} catch (Exception e) {
			logger.error("Exception", e);
			return Response.serverError().build();
		}
	}
}







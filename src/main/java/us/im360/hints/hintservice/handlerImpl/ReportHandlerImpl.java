package us.im360.hints.hintservice.handlerImpl;


import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.InitHandler;
import us.im360.hints.hintservice.service.CashReportService;
import us.im360.hints.hintservice.service.ProductService;
import us.im360.hints.hintservice.service.ProfitReportService;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Cash report service handler implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Component
@Path("report")
public class ReportHandlerImpl implements ReportHandler {

	private static final Logger logger = LoggerFactory.getLogger(ReportHandler.class);
		
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CashReportService cashReportService;

	@Autowired
	private ProfitReportService profitReportService;

	@Autowired
	private ProductService productService;

	private static final String DETAILS_FIELD_NAME = "details";

	/**
	 * Cash report on date
	 *
	 * @param restaurantId terminal identifier
	 * @param closeDate report date
	 * @return
	 */
	@GET
	@Path("cash/restaurantId/{restaurantId}/closeDate/{closeDate}")
	@Override
	public Response getCloseReportOnDate(
			@PathParam("restaurantId") Integer restaurantId, @PathParam("closeDate") String closeDate
	) {
		logger.debug("restaurantId: {}, closeDate: {}" + restaurantId, closeDate);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = cashReportService.getCloseReport(restaurantId, closeDate);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("profit/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}/strains/{strainListComaSeparated}/tiers/{tierListComaSeparated}")
	@Override
	public Response getProfitReport(
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate,
			@PathParam("strainListComaSeparated") String strainListComaSeparated,
			@PathParam("tierListComaSeparated") String tierListComaSeparated)
	{
		logger.debug("restaurantId: {}, startDate: {}, endDate: {}, strainListComaSeparated: {}, tierListComaSeparated: {}", restaurantId, startDate, endDate, strainListComaSeparated, tierListComaSeparated);

		Collection<String> strains = StringUtils.isEmpty(strainListComaSeparated) ? Collections.<String>emptyList() : Arrays.asList(strainListComaSeparated.split(","));
		Collection<String> tiers = StringUtils.isEmpty(tierListComaSeparated) ? Collections.<String>emptyList() : Arrays.asList(tierListComaSeparated.split(","));

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		List<JsonNode> resultList = profitReportService.getProfitReport(restaurantId, startDate, endDate, strains, tiers);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}


	@GET
	@Path("stock/restaurantId/{restaurantId}/userId/{userId}/productId/{productId}")
	@Override
	public Response getStockReport(
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("userId") Integer userId,
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








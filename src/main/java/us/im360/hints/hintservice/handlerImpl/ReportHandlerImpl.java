package us.im360.hints.hintservice.handlerImpl;


import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.model.AuditInfo;
import us.im360.hints.hintservice.service.*;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cash report service handler implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Component
@Path("report")
public class ReportHandlerImpl extends AbstractHandlerImpl implements ReportHandler {

	private static final Logger logger = LoggerFactory.getLogger(ReportHandler.class);

	@Autowired
	private AuditService auditService;

	@Autowired
	private CashReportService cashReportService;

	@Autowired
	private ProfitReportService profitReportService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ProductService productService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private LossReportService lossReportService;

	@Autowired
	private SalesReportService salesReportService;

	@Autowired
	private UnitsService unitsService;

	@Autowired
	private BagsService bagsService;

	@Autowired
	private InventoryService inventoryService;

	@GET
	@Path("cash/userId/{userId}/restaurantId/{restaurantId}/closeDate/{closeDate}")
	@Override
	public Response getCloseReportOnDate(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("closeDate") String closeDate
	) {
		logger.debug("userId: {}, restaurantId: {}, closeDate: {}", userId, restaurantId, closeDate);
		auditService.log(userId, new AuditInfo());

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
	@Path("profit/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getProfitReport(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		logger.debug("userId: {}, restaurantId: {}, startDate: {}, endDate: {}", userId, restaurantId, startDate, endDate);
		auditService.log(userId, new AuditInfo());

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		List<JsonNode> resultList = profitReportService.getProfitReport(restaurantId, startDate, endDate);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("loss/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getLossReport(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		logger.debug("userId: {}, restaurantId: {}, startDate: {}, endDate: {}, strainListComaSeparated: {}, tierListComaSeparated: {}", userId, restaurantId, startDate, endDate);
		auditService.log(userId, new AuditInfo());

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		List<JsonNode> resultList = lossReportService.getProfitReport(restaurantId, startDate, endDate);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("payment/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getPaymentReport(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate
	)
	{
		logger.debug("userId: {}, restaurantId: {}, productId: {}, startDate: {}, endDate: {}", userId, restaurantId, startDate, endDate);
		auditService.log(userId, new AuditInfo());

		List<JsonNode> resultList = new ArrayList<>(2);
		JsonNode cashRow = paymentService.getPaymentReport("CASH", startDate, endDate);
		JsonNode cashlessRow = paymentService.getPaymentReport("CASHLESS_ATM", startDate, endDate);

		if (cashRow!=null) {
			resultList.add(cashRow);
		}
		if (cashlessRow!=null) {
			resultList.add(cashlessRow);
		}

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("tickets/userId/{userId}/restaurantId/{restaurantId}/date/{date}/timeStart/{timeStart}/timeEnd/{timeEnd}/userIdList/{userIdList}")
	@Override
	public Response getTickets(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("date") String date,
			@PathParam("timeStart") String timeStart,
			@PathParam("timeEnd") String timeEnd,
			@PathParam("userIdList") String userIdList
	) {
		logger.debug("userId: {}, restaurantId: {}, date: {}, timeStart: {}, timeEnd: {}, userIdList: {}", userId, restaurantId, date, timeStart, timeEnd, userIdList);
		auditService.log(userId, new AuditInfo());

		List<String> userIds = Arrays.asList(userIdList.split(","));

		List<JsonNode> resultList = ticketService.getTicketList(restaurantId, date, timeStart, timeEnd, userIds);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}


	@GET
	@Path("units/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getUnits(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId
	) {
		logger.debug("userId: {}, restaurantId: {}", userId, restaurantId);
		auditService.log(userId, new AuditInfo());

		List<JsonNode> resultList = unitsService.getUnits(restaurantId);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("sales/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getSalesReport(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		logger.debug("restaurantId: {}, startDate: {}, endDate: {}, strainListComaSeparated: {}, tierListComaSeparated: {}", restaurantId, startDate, endDate);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		List<JsonNode> resultList = salesReportService.getSalesReport(restaurantId, startDate, endDate);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}


	@GET
	@Path("bags/userId/{userId}/restaurantId/{restaurantId}/status/{status}/attr1/{attr1}")
	@Override
	public Response getBags(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status,
			@PathParam("attr1") String attr1
	) {
		logger.debug(" userId: {}, restaurantId: {}, status: {}, attr1: {}", restaurantId, userId, status, attr1);
		auditService.log(userId, new AuditInfo());

		List<JsonNode> resultList = bagsService.getBags(restaurantId, status, attr1);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(BAGS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("bags/userId/{userId}/restaurantId/{restaurantId}/status/{status}")
	@Override
	public Response getBagsNullStatus(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status
	) {
		return getBags(userId, restaurantId, status, null);
	}

	@GET
	@Path("stock/userId/{userId}/restaurantId/{restaurantId}/date/{date}")
	@Override
	public Response getStockReport(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("date") String date
	) {
		logger.debug("restaurantId: {}, date: {}" + restaurantId, date);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = productService.getStockReport(restaurantId, date);

		if (resultList!=null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("extracts/userId/{userId}/restaurantId/{restaurantId}/status/{status}/attr1/{attr1}")
	@Override
	public Response getExtracts(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status,
			@PathParam("attr1") String attr1
	) {
		logger.debug("userId: {}, restaurantId: {}, status: {}, attr1: {}", userId, restaurantId, status, attr1);
		auditService.log(userId, new AuditInfo());

		List<JsonNode> resultList = bagsService.getExtracts(restaurantId, status, attr1);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(EXTRACTS_BAGS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("extracts/userId/{userId}/restaurantId/{restaurantId}/status/{status}")
	@Override
	public Response getExtractsNullStatus(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status
	) {
		return getExtracts(userId, restaurantId, status, null);
	}

	@GET
	@Path("inventory/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getInventory(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId
	) {
		logger.debug("userId: {}, restaurantId: {}", userId, restaurantId);
		auditService.log(userId, new AuditInfo());

		List<JsonNode> resultList = inventoryService.getInventory(restaurantId);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(INVENTORY_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}
}








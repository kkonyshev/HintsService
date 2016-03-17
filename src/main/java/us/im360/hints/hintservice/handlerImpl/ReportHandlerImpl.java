package us.im360.hints.hintservice.handlerImpl;


import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.service.*;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Cash report service handler implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Component
@Path("report")
public class ReportHandlerImpl extends AbstractHandlerImpl implements ReportHandler {

	private static final Logger logger = LoggerFactory.getLogger(ReportHandler.class);

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
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("closeDate") String closeDate)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = cashReportService.getCloseReport(restaurantId, closeDate);
			if (resultList!=null && !resultList.isEmpty()) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("cash/close/userId/{userId}/restaurantId/{restaurantId}/cashRegisterId/{cashRegisterId}")
	@Override
	public Response getCashClose(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("cashRegisterId") String cashRegisterId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			ObjectNode resultList = cashReportService.getCashClose(restaurantId, cashRegisterId);
			if (resultList != null) {
				responseBuilder.success().withPlainNode(resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("profit/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getProfitReport(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = profitReportService.getProfitReport(restaurantId, startDate, endDate);
			if (resultList != null && !resultList.isEmpty()) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("loss/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getLossReport(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = lossReportService.getProfitReport(restaurantId, startDate, endDate);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("payment/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getPaymentReport(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<JsonNode> resultList = new ArrayList<>(2);
		try {
			List<JsonNode> cashRowSet = paymentService.getPaymentReport("CASH", startDate, endDate);
			if (CollectionUtils.isNotEmpty(cashRowSet)) {
				JsonNode cashJsonNode = cashRowSet.iterator().next();
				if (cashJsonNode.get("cash")!=null) {
					resultList.add(cashJsonNode);
				}
			}

			List<JsonNode> cashlessRowSet = paymentService.getPaymentReport("CASHLESS_ATM", startDate, endDate);
			if (CollectionUtils.isNotEmpty(cashlessRowSet)) {
				JsonNode cashlessJsonNode = cashlessRowSet.iterator().next();
				if (cashlessJsonNode.get("cash") != null) {
					resultList.add(cashlessJsonNode);
				}
			}

			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("tickets/userId/{userId}/restaurantId/{restaurantId}/date/{date}/timeStart/{timeStart}/timeEnd/{timeEnd}/userIdList/{userIdList}")
	@Override
	public Response getTickets(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("date") String date,
			@PathParam("timeStart") String timeStart,
			@PathParam("timeEnd") String timeEnd,
			@PathParam("userIdList") String userIdList)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		List<String> userIds = Arrays.asList(userIdList.split(","));
		try {
			List<JsonNode> resultList = ticketService.getTicketList(restaurantId, date, timeStart, timeEnd, userIds);
			if (CollectionUtils.isNotEmpty(resultList)) {
				for (JsonNode node: resultList) {
					ObjectNode nodeObj = (ObjectNode) node;

					ObjectNode paymentCashNode = objectMapper.createObjectNode();
					paymentCashNode.put("type", "CASH");
					paymentCashNode.put("amount", node.get("cash"));
					nodeObj.remove("cash");

					ObjectNode paymentCashlessNode = objectMapper.createObjectNode();
					paymentCashlessNode.put("type", "CASHLESS_ATM");
					paymentCashlessNode.put("amount", node.get("cashless_atm"));
					nodeObj.remove("cashless_atm");

					ArrayNode arr = new ArrayNode(objectMapper.getNodeFactory());
					arr.add(paymentCashNode);
					arr.add(paymentCashlessNode);

					nodeObj.put("payments", arr);
				}
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}


	@GET
	@Path("units/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getUnits(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = unitsService.getUnits(restaurantId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("sales/userId/{userId}/restaurantId/{restaurantId}/startDate/{startDate}/endDate/{endDate}")
	@Override
	public Response getSalesReport(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("startDate") String startDate,
			@PathParam("endDate") String endDate)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = salesReportService.getSalesReport(restaurantId, startDate, endDate);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}


	@GET
	@Path("bags/userId/{userId}/restaurantId/{restaurantId}/status/{status}/attr1/{attr1}")
	@Override
	public Response getBags(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status,
			@PathParam("attr1") String attr1)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = bagsService.getBags(restaurantId, status, attr1);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(BAGS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("bags/userId/{userId}/restaurantId/{restaurantId}/status/{status}")
	@Override
	public Response getBagsNullStatus(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status)
	{
		audit(userId);
		return getBags(userId, restaurantId, status, null);
	}

	@GET
	@Path("stock/userId/{userId}/restaurantId/{restaurantId}/date/{date}")
	@Override
	public Response getStockReport(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("date") String date)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = productService.getStockReport(restaurantId, date);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}

	@GET
	@Path("extracts/userId/{userId}/restaurantId/{restaurantId}/status/{status}/attr1/{attr1}")
	@Override
	public Response getExtracts(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status,
			@PathParam("attr1") String attr1)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = bagsService.getExtracts(restaurantId, status, attr1);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(EXTRACTS_BAGS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("extracts/userId/{userId}/restaurantId/{restaurantId}/status/{status}")
	@Override
	public Response getExtractsNullStatus(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") Integer status)
	{
		audit(userId);
		return getExtracts(userId, restaurantId, status, null);
	}

	@GET
	@Path("inventory/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getInventory(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> rowResult = inventoryService.getInventory(restaurantId);
			if (CollectionUtils.isNotEmpty(rowResult)) {
				Map<String, ArrayNode> strainIdToDetailMap = new HashMap<>(rowResult.size() * 2);
				Set<JsonNode> uniqueStrainSet = new HashSet<>(rowResult.size());
				for (JsonNode node : rowResult) {
					ObjectNode nodeObj = (ObjectNode) node;

					ObjectNode detailItem = objectMapper.createObjectNode();
					detailItem.put("grams_per_jar", nodeObj.remove("grams_per_jar"));
					detailItem.put("quantity", nodeObj.remove("quantity"));

					String strainId = nodeObj.get("id").getTextValue();
					ArrayNode innerList = strainIdToDetailMap.get(strainId);
					if (innerList == null) {
						innerList = new ArrayNode(objectMapper.getNodeFactory());
						strainIdToDetailMap.put(strainId, innerList);
					}
					innerList.add(detailItem);
					uniqueStrainSet.add(node);
				}
				for (JsonNode strainNode : uniqueStrainSet) {
					ObjectNode nodeObj = (ObjectNode) strainNode;
					ArrayNode detailsArr = strainIdToDetailMap.remove(strainNode.get("id").getTextValue());
					nodeObj.put("details", detailsArr);
				}
				responseBuilder.success().withArray(INVENTORY_FIELD_NAME, rowResult);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("inventory/list/userId/{userId}/restaurantId/{restaurantId}/attr1/{attr1}")
	@Override
	public Response getInventoryList(
			@PathParam("userId") String userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("attr1") String attr1)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = inventoryService.getInventoryList(restaurantId, attr1);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(INVENTORY_LIST_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Throwable e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}
}








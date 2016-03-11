package us.im360.hints.hintservice.handlerImpl;


import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import us.im360.hints.hintservice.CommonHandler;
import us.im360.hints.hintservice.InitHandler;
import us.im360.hints.hintservice.dto.FlowerInventoryReqDto;
import us.im360.hints.hintservice.service.*;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("UnusedDeclaration")
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

	@Autowired
	private StrainService strainService;

	@Autowired
	private AttributeService attributeService;

	@Autowired
	private OptionService optionService;

	@Autowired
	private PrintService printService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private ShipmentService shipmentService;

	@Autowired
	private CashService cashService;

	@Autowired
	private FlowerService flowerService;

	@GET
	@Path("user/list/userId/{userId}/restaurantId/{restaurantId}/groupId/{groupId}/active/{active}")
	@Override
	public Response getUsers(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("groupId") Integer groupId,
			@PathParam("active") Integer active)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = userService.getUsers(restaurantId, groupId, active);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(USERS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
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
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> stockList = productService.getProductStock(productId);
			if (CollectionUtils.isNotEmpty(stockList)) {
				JsonNode stock = stockList.iterator().next();
				responseBuilder.success().withPlainNode(stock);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("strain/list/userId/{userId}/restaurantId/{restaurantId}/active/{active}")
	@Override
	public Response getStrains(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("active") Integer active)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = strainService.getStrains(restaurantId, active);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(STRAINS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("attribute/list/userId/{userId}/restaurantId/{restaurantId}/active/{active}")
	@Override
	public Response getAttributes(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("active") Integer active)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = attributeService.getAttributes(restaurantId, active);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(ATTRIBUTES_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("option/list/userId/{userId}/restaurantId/{restaurantId}/active/{active}")
	@Override
	public Response getOptions(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("active") Integer active)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = optionService.getOptions(restaurantId, active);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(OPTIONS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("tier/costs/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getTiersCost(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = strainService.getTiersCost(restaurantId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(TIERS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("extract/costs/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getExtractsCost(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = strainService.getExtractsCost(restaurantId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(TIERS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("details/ticket/userId/{userId}/restaurantId/{restaurantId}/ticketVisibleId/{ticketVisibleId}")
	@Override
	public Response getTicketDetail(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("ticketVisibleId") Integer ticketVisibleId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = ticketService.getTicketDetails(userId, restaurantId, ticketVisibleId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(PRODUCTS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("print/information/userId/{userId}/restaurantId/{restaurantId}/categoryId/{categoryId}/tier/{tier}")
	@Override
	public Response getPrintInformation(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("categoryId") String categoryId,
			@PathParam("tier") Integer tier)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = printService.getPrintInformation(restaurantId, categoryId, tier);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("menu/flowers/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getFlowersMenu(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = menuService.getFlowersMenu(restaurantId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				ArrayList<JsonNode> flowersList = menuService.buildFlowersMenuOut(resultList);
				responseBuilder.success().withArray(ITEMS_FIELD_NAME, flowersList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("menu/extracts/userId/{userId}/restaurantId/{restaurantId}")
	@Override
	public Response getExtractsMenu(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = menuService.getExtractsMenu(restaurantId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(ITEMS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("shipments/flowers/userId/{userId}/restaurantId/{restaurantId}/dateStart/{dateStart}/dateEnd/{dateEnd}")
	@Override
	public Response getShipmentsFlowers(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("dateStart") String dateStart,
			@PathParam("dateEnd") String dateEnd)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = shipmentService.getFlowersShipments(restaurantId, dateStart, dateEnd);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(SHIPMENTS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("shipment/details/flowers/userId/{userId}/restaurantId/{restaurantId}/shipmentId/{shipmentId}")
	@Override
	public Response getShipmentFlowersDetails(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("shipmentId") String shipmentId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = shipmentService.getFlowersShipmentDetails(restaurantId, shipmentId);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("shipments/extracts/userId/{userId}/restaurantId/{restaurantId}/dateStart/{dateStart}/dateEnd/{dateEnd}")
	@Override
	public Response getShipmentsExtracts(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("dateStart") String dateStart,
			@PathParam("dateEnd") String dateEnd)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = shipmentService.getExtractsShipments(restaurantId, dateStart, dateEnd);
			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(SHIPMENTS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@GET
	@Path("shipment/details/extracts/userId/{userId}/restaurantId/{restaurantId}/shipmentId/{shipmentId}")
	@Override
	public Response getShipmentExtractsDetails(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("shipmentId") String shipmentId)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			List<JsonNode> resultList = shipmentService.getExtractsShipmentDetails(restaurantId, shipmentId);

			if (CollectionUtils.isNotEmpty(resultList)) {
				responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@POST
	@Path("strain/update/userId/{userId}/restaurantId/{restaurantId}/status/{status}/prevStrain/{prevStrain}/strain/{strain}/attribute/{attribute}")
	@Override
	public Response updateStrain(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("status") String status,
			@PathParam("prevStrain") String prevStrain,
			@PathParam("strain") String strain,
			@PathParam("attribute") String attribute)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			int rows = strainService.updateStrain(restaurantId, status, prevStrain, strain, attribute);
			responseBuilder.success();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@POST
	@Path("strain/attribute/update/userId/{userId}/restaurantId/{restaurantId}/status/{status}/prevStrain/{prevStrain}/attribute/{attribute}")
	@Override
	public Response updateStrainAttribute(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("prevStrain") String prevStrain,
			@PathParam("status") String status,
			@PathParam("attribute") String attribute)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			int rowCount = strainService.updateStrainAttribute(restaurantId, status, prevStrain, attribute);
			if (rowCount > 1) {
				responseBuilder.success();
			} else {
				throw new IllegalArgumentException("undefined locationId: " + restaurantId);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}
		return buildResponse(responseBuilder);
	}

	@POST
	@Path("cash/drop/insert/userId/{userId}/restaurantId/{restaurantId}/terminal/{terminal}/cashRegisterId/{cashRegisterId}/cashCount/{cashCount}")
	@Override
	public Response insertCashDrop(
			@PathParam("userId") Integer userId,
			@PathParam("restaurantId") Integer restaurantId,
			@PathParam("terminal") String terminal,
			@PathParam("cashRegisterId") String cashRegisterId,
			@PathParam("cashCount") Double cashCount)
	{
		audit(userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			int rowUpdated = cashService.insertCashDrop(terminal, cashRegisterId, cashCount, userId, restaurantId);
			if (rowUpdated<1) {
				responseBuilder.success();
			} else {
				throw new IllegalStateException(EMPTY_RESULT_EXCEPTION_MSG);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}

	@POST
	@Path("inventory/flower")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response addInventoryFlower(FlowerInventoryReqDto flowerInventory)
	{
		audit(flowerInventory.userId);
		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);
		try {
			flowerService.addInventoryFlower(flowerInventory);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			responseBuilder.fail(e.getMessage());
		}

		return buildResponse(responseBuilder);
	}
}




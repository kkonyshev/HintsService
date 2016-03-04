package us.im360.hints.hintservice.handlerImpl;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.im360.hints.hintservice.CommonHandler;
import us.im360.hints.hintservice.InitHandler;
import us.im360.hints.hintservice.service.*;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
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
		List<JsonNode> resultList = userService.getUsers(restaurantId, groupId, active);

		if (resultList != null && !resultList.isEmpty()) {
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
		audit(userId);

		JsonNode stock = productService.getProductStock(productId);

		ResponseBuilder responseBuilder = ResponseBuilder.create(objectMapper);

		if (stock != null) {
			responseBuilder.success().withPlainNode(stock);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = strainService.getStrains(restaurantId, active);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(STRAINS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = attributeService.getAttributes(restaurantId, active);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(ATTRIBUTES_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = optionService.getOptions(restaurantId, active);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(OPTIONS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = strainService.getTiersCost(restaurantId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(TIERS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = strainService.getExtractsCost(restaurantId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(TIERS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = ticketService.getTicketDetails(userId, restaurantId, ticketVisibleId);

		if (resultList != null && !resultList.isEmpty()) {
			responseBuilder.success().withArray(PRODUCTS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = printService.getPrintInformation(restaurantId, categoryId, tier);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = menuService.getFlowersMenu(restaurantId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(ITEMS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = menuService.getExtractsMenu(restaurantId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(ITEMS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = shipmentService.getFlowersShipments(restaurantId, dateStart, dateEnd);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(SHIPMENTS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = shipmentService.getFlowersShipmentDetails(restaurantId, shipmentId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = shipmentService.getExtractsShipments(restaurantId, dateStart, dateEnd);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(SHIPMENTS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		List<JsonNode> resultList = shipmentService.getExtractsShipmentDetails(restaurantId, shipmentId);

		if (CollectionUtils.isNotEmpty(resultList)) {
			responseBuilder.success().withArray(DETAILS_FIELD_NAME, resultList);
		} else {
			responseBuilder.fail();
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
		boolean result = strainService.updateStrain(restaurantId, status, prevStrain, strain, attribute);

		if (BooleanUtils.isTrue(result)) {
			responseBuilder.success();
		} else {
			responseBuilder.fail();
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
		boolean result = strainService.updateStrainAttribute(restaurantId, status, prevStrain, attribute);

		if (BooleanUtils.isTrue(result)) {
			responseBuilder.success();
		} else {
			responseBuilder.fail();
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
		boolean result = cashService.cashDropInsert(terminal, cashRegisterId, cashCount, userId, restaurantId);

		if (BooleanUtils.isTrue(result)) {
			responseBuilder.success();
		} else {
			responseBuilder.fail();
		}

		return buildResponse(responseBuilder);
	}

}




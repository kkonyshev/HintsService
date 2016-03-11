package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.dto.FlowerInventoryJarReqDto;
import us.im360.hints.hintservice.dto.FlowerInventoryReqDto;
import us.im360.hints.hintservice.util.HintsUtils;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Flower service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 11/03/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class FlowerService {

	private static final Logger logger = LoggerFactory.getLogger(FlowerService.class);

	@Autowired
	@Qualifier("flowersQueryStore")
	private Properties flowersQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> addInventoryFlower(FlowerInventoryReqDto req) {
		logger.info("calling FlowerService");

		//1
		String parentProsperCategoryId = getProsperCategoryIdByUnitDetailId(req);
		logger.info("Found prosperCategoryId: {}", parentProsperCategoryId);

		//2
		String inventoryUUID = UUID.randomUUID().toString();
		insertStrainInventory(inventoryUUID, parentProsperCategoryId, req);

		//3
		updateStrainUnitDetails(req);

		//3
		insertStrainUnitStockDairy(inventoryUUID, parentProsperCategoryId, req);

		//4 cycle
		for (FlowerInventoryJarReqDto jar: req.jars) {
			logger.info("Processing jar grams/quantity: {}/{}", jar.grams, jar.quantity);

			//4.1
			String jarParentProductId = getJarParentProductId(parentProsperCategoryId, req.restaurantId, jar);
			logger.info("Found jarParentProductId: {}", jarParentProductId);

			String jarDetailsUUID = UUID.randomUUID().toString();
			//4.2
			insertJarStrainDetails(inventoryUUID, jarDetailsUUID, req.restaurantId, jarParentProductId, req.costPerGram, jar);

			String jarStockDairyMD5id = HintsUtils.md5Gen();
			//4.3
			insertProsperStockDairy(jarStockDairyMD5id, jarParentProductId, jarDetailsUUID, jar, req.restaurantId);

			//4.4
			insertJarAudit(jarStockDairyMD5id, req.userId);
		}

		return null;
	}

	private String getProsperCategoryIdByUnitDetailId(FlowerInventoryReqDto req) {
		String queryGetProsperCategory = flowersQueryStore.getProperty("getParentProsperCategoryIdByUnit");
		logger.info("QUERY TO EXECUTE: {}", queryGetProsperCategory);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("unitDetailId", req.unitDetailId)
				.addValue("restaurantId", req.restaurantId)
				;
		logger.info("QUERY PARAMS. unitDetailId: {}, restaurantId: {}", req.unitDetailId, req.restaurantId);

		List<JsonNode> queryGetProsperCategoryResultList =
				namedParameterJdbcTemplate.query(
					queryGetProsperCategory,
						params,
					new JsonNodeRowMapper(objectMapper)
				);

		JsonNode singleResult = queryGetProsperCategoryResultList.iterator().next();
		if (singleResult==null) {
			throw new IllegalArgumentException("No prosper category found for unitDetailId " + req.unitDetailId);
		}

		return singleResult.get("parentProsperCategoryId").asText();
	}

	private void insertStrainInventory(String inventoryUUID, String parentProsperCategoryId, FlowerInventoryReqDto req) {
		String queryInsertStrainInventory = flowersQueryStore.getProperty("insertStrainInventory");
		logger.info("QUERY TO EXECUTE: {}", queryInsertStrainInventory);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("inventoryUUID", inventoryUUID)
				.addValue("restaurantId", req.restaurantId)
				.addValue("unitDetailId", req.unitDetailId)
				.addValue("cost", req.cost)
				.addValue("totalWeight", req.totalWeight)
				.addValue("userId", req.userId)
				.addValue("weighTech", req.weighTech)
				.addValue("date", req.date)
				.addValue("totalWeight", req.totalWeight)
				.addValue("bagWeight", req.bagWeight)
				.addValue("startWeight", req.startWeight)
				.addValue("endWeight", req.endWeight)
				.addValue("loss", req.loss)
				.addValue("parentProsperCategoryId", parentProsperCategoryId);
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(queryInsertStrainInventory, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_inventory: " + rowsCount);
		}
	}

	private void updateStrainUnitDetails(FlowerInventoryReqDto req) {
		String queryUpdateStrainUnitDetails = flowersQueryStore.getProperty("updateStrainUnitDetails");
		logger.info("QUERY TO EXECUTE: {}", queryUpdateStrainUnitDetails);

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("unitDetailId", req.unitDetailId);
		logger.info("QUERY PARAMS: {}", req.unitDetailId);

		int rowsCount = namedParameterJdbcTemplate.update(queryUpdateStrainUnitDetails, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows updated on im_strain_units_details: " + rowsCount);
		}
	}

	private void insertStrainUnitStockDairy(String inventoryUUID, String parentProsperCategoryId, FlowerInventoryReqDto req) {
		String queryInsertStrainUnitStockDiary = flowersQueryStore.getProperty("insertStrainUnitStockDiary");
		logger.info("QUERY TO EXECUTE: {}", queryInsertStrainUnitStockDiary);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("inventoryUUID", inventoryUUID)
				.addValue("restaurantId", req.restaurantId)
				.addValue("unitDetailId", req.unitDetailId)
				.addValue("parentProsperCategoryId", parentProsperCategoryId);
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(queryInsertStrainUnitStockDiary, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_inventory: " + rowsCount);
		}
	}


	private String getJarParentProductId(String parentProsperCategoryId, String restaurantId, FlowerInventoryJarReqDto jar) {
		String queryGetParentProductId = flowersQueryStore.getProperty("cycle-00-getParentProductId");
		logger.info("QUERY TO EXECUTE: {}", queryGetParentProductId);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("parentProsperCategoryId", parentProsperCategoryId)
				.addValue("grams", jar.grams)
				.addValue("restaurantId", restaurantId)
				;
		logger.info("QUERY PARAMS. parentProsperCategoryId: {}, grams: {}, restaurantId: {}", parentProsperCategoryId, jar.grams, restaurantId);

		List<JsonNode> queryGetParentProductIdList =
				namedParameterJdbcTemplate.query(
						queryGetParentProductId,
						params,
						new JsonNodeRowMapper(objectMapper)
				);

		JsonNode singleResult = queryGetParentProductIdList.iterator().next();
		if (singleResult==null) {
			throw new IllegalArgumentException("No parent category found for jar grams/quantity/restaurantId" + jar.grams + "/" + jar.quantity + "/" + restaurantId);
		}

		return singleResult.get("jarProductId").asText();
	}

	private void insertJarStrainDetails(String inventoryUUID, String jarDetailsUUID, String restaurantId, String jarProductId, Double costPerGram, FlowerInventoryJarReqDto jar) {
		String queryInsertJarStrainDetails = flowersQueryStore.getProperty("cycle-01-insertJarStrainDetails");
		logger.info("QUERY TO EXECUTE: {}", queryInsertJarStrainDetails);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("jarDetailsUUID", jarDetailsUUID)
				.addValue("restaurantId", restaurantId)
				.addValue("jarProductId", jarProductId)
				.addValue("quantity", jar.quantity)
				.addValue("costPerGram", costPerGram)
				.addValue("inventoryUUID", inventoryUUID);
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(queryInsertJarStrainDetails, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows updated on im_strain_detail: " + rowsCount);
		}
	}

	private void insertProsperStockDairy(String jarStockDairyMD5id, String jarProductId, String jarDetailsUUID, FlowerInventoryJarReqDto jar, String restaurantId) {
		String queryInsertJarStockDairy = flowersQueryStore.getProperty("cycle-01-insertJarStockDairy");
		logger.info("QUERY TO EXECUTE: {}", queryInsertJarStockDairy);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("jarStockDairyMD5id", jarStockDairyMD5id)
				.addValue("quantity", jar.quantity)
				.addValue("jarProductId", jarProductId)
				.addValue("jarDetailsUUID", jarDetailsUUID)
				.addValue("restaurantId", restaurantId);
		logger.info("QUERY PARAMS. jarStockDairyMD5id: {}, jarDetailsUUID: {}, jarProductId: {}", jarStockDairyMD5id, jarDetailsUUID, jarProductId);

		int rowsCount = namedParameterJdbcTemplate.update(queryInsertJarStockDairy, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into posper_stockdiary: " + rowsCount);
		}
	}

	private void insertJarAudit(String jarStockDairyMD5id, Integer userId) {
		String queryInsertJarAudit = flowersQueryStore.getProperty("cycle-01-jarAudit");
		logger.info("QUERY TO EXECUTE: {}", queryInsertJarAudit);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("jarStockDairyMD5id", jarStockDairyMD5id)
				.addValue("userId", userId);
		logger.info("QUERY PARAMS. jarStockDairyMD5id: {}, userId: {}", jarStockDairyMD5id, userId);

		int rowsCount = namedParameterJdbcTemplate.update(queryInsertJarAudit, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into portal_audit: " + rowsCount);
		}
	}

	private void updateProsperProduct(String jarProductId, FlowerInventoryJarReqDto jar) {
		String queryUpdateProsperProduct = flowersQueryStore.getProperty("cycle-01-prosperProduct");
		logger.info("QUERY TO EXECUTE: {}", queryUpdateProsperProduct);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("quantity", jar.quantity)
				.addValue("jarProductId", jarProductId);
		logger.info("QUERY PARAMS. quantity: {}, jarProductId: {}", jar.quantity, jarProductId);

		int rowsCount = namedParameterJdbcTemplate.update(queryUpdateProsperProduct, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount!=1) {
			throw new IllegalArgumentException("Wrong number of rows updated in posper_product: " + rowsCount);
		}
	}
}

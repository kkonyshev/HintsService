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
import us.im360.hints.hintservice.dto.FlowerInventoryReqDto;
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
}

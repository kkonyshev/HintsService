package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Strain service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
@Transactional
public class StrainService {

	private static final Logger logger = LoggerFactory.getLogger(StrainService.class);

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	@Qualifier("editorQueryStore")
	private Properties editorQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getStrains(Integer restaurantId, Integer active) {
		try {
			String profitReportQuery = commonQueryStore.getProperty("getStrains");
			logger.trace("QUERY TO EXECUTE: " + profitReportQuery);

			return namedParameterJdbcTemplate.query(
					profitReportQuery,
					new MapSqlParameterSource()
							.addValue("active", active),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	public List<JsonNode> getTiersCost(Integer restaurantId) {
		try {
			String query = commonQueryStore.getProperty("getTiersCost");
			logger.trace("QUERY TO EXECUTE: " + query);

			return namedParameterJdbcTemplate.query(
					query,
					new MapSqlParameterSource()
							.addValue("restaurantId", restaurantId),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return Collections.emptyList();
		}
	}

	public List<JsonNode> getExtractsCost(Integer restaurantId) {
		try {
			String query = commonQueryStore.getProperty("getExtractsCost");
			logger.trace("QUERY TO EXECUTE: " + query);

			return namedParameterJdbcTemplate.query(
					query,
					new MapSqlParameterSource()
							.addValue("restaurantId", restaurantId),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return Collections.emptyList();
		}
	}


	public boolean updateStrain(Integer restaurantId, String status, String prevStrain, String strain, String attribute) {
		try {

			String queryUpdateType = editorQueryStore.getProperty("updateStrainStatus");
			logger.trace("QUERY TO EXECUTE: " + queryUpdateType);
			int updateTypeResult = namedParameterJdbcTemplate.update(
					queryUpdateType,
					new MapSqlParameterSource()
							.addValue("restaurantId", restaurantId)
							.addValue("status", status)
							.addValue("prevStrain", prevStrain)
							.addValue("attribute", attribute)
			);

			logger.debug("ROWS UPDATED [updateStrainType]: " + updateTypeResult);

			if (updateTypeResult<1) {
				throw new IllegalArgumentException("undefined locationId: " + restaurantId);
			}

			SqlParameterSource[] batchParamsGrams = new SqlParameterSource[4];
			MapSqlParameterSource params = new MapSqlParameterSource()
					.addValue("strain", strain)
					.addValue("prevStrain", prevStrain);

			String[] grams = new String[]{"7g", "3.5g", "1.75g", "1g"};
			for (int i=0;i<grams.length; i++) {
				batchParamsGrams[i]= new MapSqlParameterSource()
						.addValue("strain", strain)
						.addValue("prevStrain", prevStrain)
						.addValue("grams", grams[i]);
			}

			String updateStrainProsperProduct = editorQueryStore.getProperty("updateStrainProsperProduct");
			logger.trace("QUERY TO EXECUTE: " + updateStrainProsperProduct);
			int[] updateStrainResultList = namedParameterJdbcTemplate.batchUpdate(
					updateStrainProsperProduct,
					batchParamsGrams
			);
			logger.debug("ROWS UPDATED [updateStrainProsperProduct]: " + updateStrainResultList);


			String updateStrainProsperTicketLine = editorQueryStore.getProperty("updateStrainProsperTicketLine");
			logger.trace("QUERY TO EXECUTE: " + updateStrainProsperTicketLine);
			int[] updateStrainProsperTicketLineList = namedParameterJdbcTemplate.batchUpdate(
					updateStrainProsperTicketLine,
					batchParamsGrams
			);
			logger.debug("ROWS UPDATED [updateStrainProsperTicketLine]: " + updateStrainProsperTicketLineList);


			String updateStrainType = editorQueryStore.getProperty("updateStrainType");
			logger.trace("QUERY TO EXECUTE: " + updateStrainType);
			int updateStrainTypeList = namedParameterJdbcTemplate.update(
					updateStrainType,
					params
			);
			logger.debug("ROWS UPDATED [updateStrainType]: " + updateStrainTypeList);


			String updateStrainExtractsType = editorQueryStore.getProperty("updateStrainExtractsType");
			logger.trace("QUERY TO EXECUTE: " + updateStrainExtractsType);
			int updateStrainExtractsTypeList = namedParameterJdbcTemplate.update(
					updateStrainExtractsType,
					params
			);
			logger.debug("ROWS UPDATED [updateStrainExtractsType]: " + updateStrainExtractsTypeList);


			String updateStrainProsperCategoryLike = editorQueryStore.getProperty("updateStrainProsperCategoryLike");
			logger.trace("QUERY TO EXECUTE: " + updateStrainProsperCategoryLike);
			int updateStrainProsperCategoryLikeList = namedParameterJdbcTemplate.update(
					updateStrainProsperCategoryLike,
					params
			);
			logger.debug("ROWS UPDATED [updateStrainProsperCategoryLike]: " + updateStrainProsperCategoryLikeList);


			String updateStrainProsperCategory = editorQueryStore.getProperty("updateStrainProsperCategory");
			logger.trace("QUERY TO EXECUTE: " + updateStrainProsperCategory);
			int updateStrainProsperCategoryList = namedParameterJdbcTemplate.update(
					updateStrainProsperCategory,
					params
			);
			logger.debug("ROWS UPDATED [updateStrainProsperCategory]: " + updateStrainProsperCategoryList);

			return true;

		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.debug(e.getMessage(), e);
			return false;
		}
	}

	public boolean updateStrainAttribute(Integer restaurantId, String status, String prevAttribute, String attribute) {
		try {

			String updateStrainAttribute = editorQueryStore.getProperty("updateStrainAttribute");
			logger.trace("QUERY TO EXECUTE: " + updateStrainAttribute);
			int updateStrainProsperTicketLineList = namedParameterJdbcTemplate.update(
					updateStrainAttribute,
					new MapSqlParameterSource()
							.addValue("restaurantId", restaurantId)
							.addValue("status", status)
							.addValue("attribute", attribute)
							.addValue("prevAttribute", prevAttribute)
			);
			logger.debug("ROWS UPDATED [updateStrainProsperTicketLineList]: " + updateStrainProsperTicketLineList);

			if (updateStrainProsperTicketLineList<1) {
				throw new IllegalArgumentException("undefined locationId: " + restaurantId);
			}

			return true;

		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.debug(e.getMessage(), e);
			return false;
		}
	}
}

package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.dto.MiscInventoryJarReqDto;
import us.im360.hints.hintservice.dto.MiscInventoryReqDto;
import us.im360.hints.hintservice.util.HintsUtils;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class MiscService {

	private static final Logger logger = LoggerFactory.getLogger(MiscService.class);

	@Autowired
	@Qualifier("miscQueryStore")
	private Properties miscQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public void addInventoryMisc(MiscInventoryReqDto req) {
		logger.info("calling MiscService");

		for (MiscInventoryJarReqDto jar : req.jars) {
			//1
			processJar(req, jar);

			//2
			String strainDetailUUID = UUID.randomUUID().toString();
			insertStrainDetail(strainDetailUUID, jar.product, jar.quantity);

			//3
			String prosperStockDairyMD5Id = HintsUtils.md5Gen();
			insertProsperStockDairy(strainDetailUUID, prosperStockDairyMD5Id,jar.product, jar.quantity, req.restaurantId);

			//4
			insertStockDairyAudit(prosperStockDairyMD5Id, req.userId);

			//5
			updateProsperProduct(jar.product, jar.quantity);

			//6
			processJar2(req, jar);

			//7
			String prosperStockDairyMD5Id2 = HintsUtils.md5Gen();
			insertProsperStockDairy2(prosperStockDairyMD5Id2, jar.product, jar.loss, jar.quantity, req.restaurantId);

			//8
			insertStockDairyAudit2(prosperStockDairyMD5Id2, req.userId);

			//9
			updateProsperProduct2(jar.product, jar.quantity, jar.loss);
		}
	}

	private void processJar(MiscInventoryReqDto req, MiscInventoryJarReqDto jar) {
		logger.info("Processing jar product/quantity/loss: {}/{}/{}", jar.product, jar.quantity, jar.loss);

		BigDecimal count = new BigDecimal(jar.loss).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal loss = BigDecimal.ZERO;
		BigDecimal initial = BigDecimal.ZERO;

		while (count.compareTo(BigDecimal.ZERO)>0) {

            JsonNode innerResult = getStrainMiscData(jar.product, req.restaurantId);
            BigDecimal totalGrams = innerResult.get("total_grams").getDecimalValue().setScale(2, RoundingMode.HALF_UP);
            BigDecimal gramsPackaged = innerResult.get("grams_packaged").getDecimalValue().setScale(2, RoundingMode.HALF_UP);
            String strainInventoryId = innerResult.get("im_strain_inventory_id").getTextValue();

            logger.info("totalGrams/gramsPackaged/strainInventoryId: {}/{}/{}", totalGrams, gramsPackaged, strainInventoryId);

            initial = gramsPackaged.add(count);
            boolean lossStrategy = totalGrams.compareTo(initial) > 0;
            logger.info("lossStrategy, is negative count only: {}", lossStrategy);
            if (lossStrategy) {
                loss = count.negate();
            } else {
                loss = totalGrams.subtract(gramsPackaged).negate().setScale(2, RoundingMode.HALF_UP);
            }

            String strainMiscUUID = UUID.randomUUID().toString();
            logger.info("loss/strainMiscUUID: {}/{}", loss, strainMiscUUID);
            insertMiscStrain(strainMiscUUID, jar.product, loss, strainInventoryId, req.restaurantId);

            //end loop
            initial = initial.subtract(count).setScale(2, BigDecimal.ROUND_HALF_UP);
            count = count.add(loss).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
	}

	private JsonNode getStrainMiscData(String product, String restaurantId) {
		String query = miscQueryStore.getProperty("getStrainMiscData");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("restaurantId", restaurantId);
		logger.info("QUERY PARAMS. product: {}, restaurantId: {}", product, restaurantId);

		List<JsonNode> queryResult =
				namedParameterJdbcTemplate.query(
						query,
						params,
						new JsonNodeRowMapper(objectMapper)
				);

		JsonNode singleResult = queryResult.iterator().next();
		if (singleResult == null) {
			throw new IllegalArgumentException("No strain misc found for product: " + product);
		}

		return singleResult;
	}

	private void insertMiscStrain(String miscStrainUUID, String product, BigDecimal loss, String strainInventoryId, String restaurantId) {
		String query = miscQueryStore.getProperty("insertStrainInventory");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("miscStrainUUID", miscStrainUUID)
				.addValue("product", product)
				.addValue("loss", loss)
				.addValue("strainInventoryId", strainInventoryId)
				.addValue("restaurantId", restaurantId)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_misc: " + rowsCount);
		}
	}

	private void insertStrainDetail(String strainDetailUUID, String product, Double quantity) {
		String query = miscQueryStore.getProperty("insertStrainDetail");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("strainDetailUUID", strainDetailUUID)
				.addValue("product", product)
				.addValue("quantity", quantity)
				;
		logger.info("QUERY PARAMS. strainDetailUUID/product/quantity: {}/{}/{}", strainDetailUUID, product, quantity);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_detail: " + rowsCount);
		}
	}

	private void insertProsperStockDairy(String strainDetailUUID, String prosperStockDairyMD5Id, String product, Double quantity, String restaurantId) {
		String query = miscQueryStore.getProperty("insertProsperStockDairy");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("prosperStockDairyMD5Id", prosperStockDairyMD5Id)
				.addValue("quantity", quantity)
				.addValue("product", product)
				.addValue("strainDetailUUID", strainDetailUUID)
				.addValue("restaurantId", restaurantId)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into posper_stockdiary: " + rowsCount);
		}
	}

	private void insertStockDairyAudit(String prosperStockDairyMD5Id, Integer userId) {
		String query = miscQueryStore.getProperty("insertPortalAuditMisc");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("prosperStockDairyMD5Id", prosperStockDairyMD5Id)
				.addValue("userId", userId)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into portal_audit: " + rowsCount);
		}
	}

	private void updateProsperProduct(String product, Double quantity) {
		String query = miscQueryStore.getProperty("updateProsperProductMisc");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("quantity", quantity)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows updated on posper_product: " + rowsCount);
		}
	}


	private void processJar2(MiscInventoryReqDto req, MiscInventoryJarReqDto jar) {
		logger.info("Processing jar product/quantity/loss: {}/{}/{}", jar.product, jar.quantity, jar.loss);

		BigDecimal count = new BigDecimal(jar.quantity).multiply(BigDecimal.valueOf(3.5)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal loss = BigDecimal.ZERO;
		BigDecimal initial = BigDecimal.ZERO;

		while (count.compareTo(BigDecimal.ZERO)>0) {

			JsonNode innerResult2 = getStrainMiscData2(jar.product, req.restaurantId);
			BigDecimal nullAmount = innerResult2.get("null_amount").getDecimalValue().setScale(2, RoundingMode.HALF_UP);
			logger.info("nullAmount/count: {}/{}", nullAmount, count);

			JsonNode innerResult3 = getStrainMiscData3(jar.product, nullAmount.doubleValue());
			String strainInventoryId = innerResult3.get("im_strain_inventory_id").getTextValue();
			BigDecimal gramsPackaged = innerResult3.get("grams_packaged").getDecimalValue().setScale(2, RoundingMode.HALF_UP);
			BigDecimal totalGrams = innerResult3.get("total_grams").getDecimalValue().setScale(2, RoundingMode.HALF_UP);

			logger.info("strainInventoryId/gramsPackaged/totalGrams: {}/{}/{}", strainInventoryId, gramsPackaged, totalGrams);

			BigDecimal gramsPackagesForUpdate = BigDecimal.ZERO;
			if (strainInventoryId==null) {
				if (gramsPackaged.add(nullAmount).compareTo(totalGrams)<=0) {
					initial = gramsPackaged;
					if (gramsPackaged.add(nullAmount).compareTo(initial.add(count)) > 0) {
						gramsPackagesForUpdate = gramsPackaged.add(count);
					} else {
						gramsPackagesForUpdate = gramsPackaged.add(nullAmount);
					}
				} else {
					initial = gramsPackaged;
					if (totalGrams.compareTo(gramsPackaged.add(count))>0) {
						gramsPackagesForUpdate = gramsPackaged.add(count);
					} else {
						gramsPackagesForUpdate = totalGrams;
					}
				}
			} else {
				initial = gramsPackaged;
				if (totalGrams.compareTo(initial.add(count))>0) {
					gramsPackagesForUpdate = gramsPackaged.add(count);
				} else {
					gramsPackagesForUpdate = totalGrams;
				}
			}
			gramsPackagesForUpdate = gramsPackagesForUpdate.setScale(2, RoundingMode.HALF_UP);
			loss = new BigDecimal(gramsPackagesForUpdate.doubleValue());
			logger.info("gramsPackagesForUpdate/initial/loss: {}/{}/{}", gramsPackagesForUpdate, initial, loss);
			updateStrainMisc2(jar.product, nullAmount.doubleValue(), gramsPackagesForUpdate.doubleValue());

			//end loop
			logger.info("count/loss/initial: {}/{}/{}", count, loss, initial);

			count = count.subtract(loss.subtract(initial)).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
	}

	private JsonNode getStrainMiscData2(String product, String restaurantId) {
		String query = miscQueryStore.getProperty("getStrainMiscData2");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("restaurantId", restaurantId);
		logger.info("QUERY PARAMS. product: {}, restaurantId: {}", product, restaurantId);

		List<JsonNode> queryResult =
				namedParameterJdbcTemplate.query(
						query,
						params,
						new JsonNodeRowMapper(objectMapper)
				);

		JsonNode singleResult = queryResult.iterator().next();
		if (singleResult == null) {
			throw new IllegalArgumentException("No strain misc found for product: " + product);
		}

		return singleResult;
	}

	private JsonNode getStrainMiscData3(String product, Double nullAmount) {
		String query = miscQueryStore.getProperty("getStrainMiscData3");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("nullAmount", nullAmount);
		logger.info("QUERY PARAMS. product/nullAmount: {}/{}", product, nullAmount);

		List<JsonNode> queryResult =
				namedParameterJdbcTemplate.query(
						query,
						params,
						new JsonNodeRowMapper(objectMapper)
				);

		JsonNode singleResult = queryResult.iterator().next();
		if (singleResult == null) {
			throw new IllegalArgumentException("No strain misc found for product: " + product);
		}

		return singleResult;
	}

	private void updateStrainMisc2(String product, Double nullAmount, Double gramsPackaged) {
		String query = miscQueryStore.getProperty("updateStrainMisc2");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("nullAmount", nullAmount)
				.addValue("gramsPackaged", gramsPackaged)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows updated on im_strain_misc: " + rowsCount);
		}
	}

	private void insertProsperStockDairy2(String prosperStockDairyMD5Id2, String product, Double loss, Double quantity, String restaurantId) {
		String query = miscQueryStore.getProperty("insertProsperStockDairy2");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("prosperStockDairyMD5Id2", prosperStockDairyMD5Id2)
				.addValue("loss", loss)
				.addValue("quantity", quantity)
				.addValue("product", product)
				.addValue("restaurantId", restaurantId)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into posper_stockdiary: " + rowsCount);
		}
	}

	private void insertStockDairyAudit2(String prosperStockDairyMD5Id2, Integer userId) {
		String query = miscQueryStore.getProperty("insertPortalAuditMisc2");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("prosperStockDairyMD5Id2", prosperStockDairyMD5Id2)
				.addValue("userId", userId)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows inserted into portal_audit: " + rowsCount);
		}
	}

	private void updateProsperProduct2(String product, Double quantity, Double loss) {
		String query = miscQueryStore.getProperty("updateProsperProductMisc2");
		logger.info("QUERY TO EXECUTE: {}", query);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("product", product)
				.addValue("quantity", quantity)
				.addValue("loss", loss)
				;
		logger.info("QUERY PARAMS: {}", params);

		int rowsCount = namedParameterJdbcTemplate.update(query, params);
		logger.info("rowsCount: {}", rowsCount);

		if (rowsCount != 1) {
			throw new IllegalArgumentException("Wrong number of rows updated on posper_product: " + rowsCount);
		}
	}
}
package us.im360.hints.hintservice.service;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.dto.InventoryExtractJarReqDto;
import us.im360.hints.hintservice.dto.InventoryExtractReqDto;
import us.im360.hints.hintservice.util.HintsUtils;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Inventory service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    @Qualifier("reportQueryStore")
    private Properties reportQueryStore;

    @Autowired
    @Qualifier("invQueryStore")
    private Properties invQueryStore;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<JsonNode> getInventory(Integer restaurantId) {
        String query = reportQueryStore.getProperty("getInventory");
        logger.trace("QUERY TO EXECUTE: " + query);
        List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
                query,
                new MapSqlParameterSource().addValue("restaurantId", restaurantId),
                new JsonNodeRowMapper(objectMapper)
        );
        logger.trace("result set: {}", rowResult);
        return  rowResult;
    }

    public List<JsonNode> getInventoryList(Integer restaurantId, String attr1) {
        String query = reportQueryStore.getProperty("getInventoryList");
        logger.trace("QUERY TO EXECUTE: " + query);
        List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
                query,
                new MapSqlParameterSource()
                        .addValue("restaurantId", restaurantId)
                        .addValue("attr1", attr1),
                new JsonNodeRowMapper(objectMapper));
        logger.debug("result set: {}", rowResult);
        return rowResult;
    }

    /**
     *
     * @param req
     */
    public void addInventoryExtract(InventoryExtractReqDto req) {
        String parentId = getParentId(req.extractDetailsId);

        String extractUUID = UUID.randomUUID().toString();
        insertStrainExtract(extractUUID, req);

        updateStrainExtractsDetails(req.extractDetailsId,req.gramsRemaining);

        String strainStockDairyUUID = UUID.randomUUID().toString();
        insertStrainUnitStockDairy(strainStockDairyUUID, extractUUID, parentId, req);

        for (InventoryExtractJarReqDto jar: req.jars) {
            String productId = getProductId(req.extractDetailsId, jar.grams);

            String detailUUID = UUID.randomUUID().toString();
            insertStrainDetails(detailUUID, req.restaurantId, extractUUID, req.extractDetailsId, req.pricePerGram, jar);

            String prosperStockDairyMD5Id = HintsUtils.md5Gen();
            insertProsperStockDairy(prosperStockDairyMD5Id, req.restaurantId, jar.quantity, productId, detailUUID);

            updateProsperProduct(jar.quantity, productId);
        }

        insertAuditPortal(req.userId, extractUUID);
    }

    private String getParentId(String extractDetailsId) {
        String query = invQueryStore.getProperty("getParentId");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("extractDetailsId", extractDetailsId);
        logger.info("QUERY PARAMS. extractDetailsId: {}", extractDetailsId);

        List<JsonNode> queryResult =
                namedParameterJdbcTemplate.query(
                        query,
                        params,
                        new JsonNodeRowMapper(objectMapper)
                );

        if (CollectionUtils.isEmpty(queryResult)) {
            throw new IllegalArgumentException("No parentId found for extractDetailsId: " + extractDetailsId);
        }

        return queryResult.iterator().next().get("parentId").asText();
    }

    private void insertStrainExtract(String extractUUID, InventoryExtractReqDto req) {
        String query = invQueryStore.getProperty("insertStrainExtract");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("extractUUID", extractUUID)
                .addValue("restaurantId", req.restaurantId)
                .addValue("cost", req.cost)
                .addValue("gramsTotal", req.gramsTotal)
                .addValue("endingWeight", req.endingWeight)
                .addValue("lossWeight", req.lossWeight)
                .addValue("date", req.date)
                .addValue("userId", req.userId)
                .addValue("weighTech", req.weighTech)
                .addValue("extractDetailsId", req.extractDetailsId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_extracts: " + rowsCount);
        }
    }

    private void updateStrainExtractsDetails(String extractDetailsId, Double gramsRemaining) {
        String query = invQueryStore.getProperty("updateStrainExtractsDetails");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("extractDetailsId", extractDetailsId)
                .addValue("gramsRemaining", gramsRemaining)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows updated on im_strain_extract_details: " + rowsCount);
        }
    }

    private void insertStrainUnitStockDairy(String strainStockDairyUUID, String extractUUID, String parentId, InventoryExtractReqDto req) {
        String query = invQueryStore.getProperty("insertStrainUnitStockDairy");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("strainStockDairyUUID", strainStockDairyUUID)
                .addValue("extractUUID", extractUUID)
                .addValue("parentId", parentId)
                .addValue("gramsTotal", req.gramsTotal)
                .addValue("extractDetailsId", req.extractDetailsId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_units_stockdiary: " + rowsCount);
        }
    }

    //cycle starts here

    private String getProductId(String extractDetailsId, Double grams) {
        String query = invQueryStore.getProperty("getProductId");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("extractDetailsId", extractDetailsId)
                .addValue("grams", grams);
        logger.info("QUERY PARAMS. extractDetailsId: {}", extractDetailsId);

        List<JsonNode> queryResult =
                namedParameterJdbcTemplate.query(
                        query,
                        params,
                        new JsonNodeRowMapper(objectMapper)
                );

        if (CollectionUtils.isEmpty(queryResult)) {
            throw new IllegalArgumentException("No productId found for im_strain_extract_details: " + extractDetailsId);
        }

        return queryResult.iterator().next().get("productId").asText();
    }

    private void insertStrainDetails(String detailUUID, String restaurantId, String extractUUID, String extractDetailsId, Double pricePerGram, InventoryExtractJarReqDto jar) {
        String query = invQueryStore.getProperty("insertStrainDetails");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("detailUUID", detailUUID)
                .addValue("restaurantId", restaurantId)
                .addValue("grams", jar.grams)
                .addValue("quantity", jar.quantity)
                .addValue("pricePerGram", pricePerGram)
                .addValue("extractUUID", extractUUID)
                .addValue("extractDetailsId", extractDetailsId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows inserted into im_strain_detail: " + rowsCount);
        }
    }

    private void insertProsperStockDairy(String prosperStockDairyMD5Id, String restaurantId, Double quantity, String productId, String detailUUID) {
        String query = invQueryStore.getProperty("insertProsperStockDairy");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("prosperStockDairyMD5Id", prosperStockDairyMD5Id)
                .addValue("quantity", quantity)
                .addValue("productId", productId)
                .addValue("detailUUID", detailUUID)
                .addValue("restaurantId", restaurantId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows inserted into posper_stockdiary: " + rowsCount);
        }
    }

    private void updateProsperProduct(Double quantity, String productId) {
        String query = invQueryStore.getProperty("updateProsperProduct");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantity", quantity)
                .addValue("productId", productId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows updated on posper_product: " + rowsCount);
        }
    }

    //cycle ends here

    private void insertAuditPortal(String userId, String extractUUID) {
        String query = invQueryStore.getProperty("insertAuditPortal");
        logger.info("QUERY TO EXECUTE: {}", query);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("extractUUID", extractUUID)
                .addValue("userId", userId)
                ;
        logger.info("QUERY PARAMS: {}", params);

        int rowsCount = namedParameterJdbcTemplate.update(query, params);
        logger.info("rowsCount: {}", rowsCount);

        if (rowsCount != 1) {
            throw new IllegalArgumentException("Wrong number of rows inserted into portal_audit: " + rowsCount);
        }
    }
}

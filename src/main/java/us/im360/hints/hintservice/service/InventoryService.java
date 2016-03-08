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
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.List;
import java.util.Properties;

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
}

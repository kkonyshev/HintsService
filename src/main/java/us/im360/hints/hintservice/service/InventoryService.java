package us.im360.hints.hintservice.service;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Inventory service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
@Transactional
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
        try {
            String query = reportQueryStore.getProperty("getInventory");
            logger.trace("QUERY TO EXECUTE: " + query);

            List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
                    query,
                    new MapSqlParameterSource()
                            .addValue("restaurantId", restaurantId),
                    new JsonNodeRowMapper(objectMapper));

            logger.debug("result set: {}", rowResult);

            if (CollectionUtils.isEmpty(rowResult)) {
                return Collections.emptyList();
            } else {
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

                return rowResult;
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    public List<JsonNode> getInventoryList(Integer restaurantId, String attr1) {
        try {
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

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }
}

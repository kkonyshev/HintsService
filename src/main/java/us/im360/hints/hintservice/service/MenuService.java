package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Menu service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 24/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class MenuService {

	private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

	private static final String DELIMITER = "g_";

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getExtractsMenu(Integer restaurantId) {
		jdbcTemplate.execute("SELECT @rate := (rate + 1) FROM posper_tax WHERE id = 1;");
		String query = commonQueryStore.getProperty("getExtractsMenu");
		logger.trace("QUERY TO EXECUTE: " + query);
		List<JsonNode> resultList = namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource().addValue("restaurantId", restaurantId),
				new JsonNodeRowMapper(objectMapper)
		);
		return resultList;
	}

	public List<JsonNode> getFlowersMenu(Integer restaurantId) {
		jdbcTemplate.execute("SELECT @rate := (rate + 1) FROM posper_tax WHERE id = 1;");
		String query = commonQueryStore.getProperty("getFlowersMenu");
		logger.trace("QUERY TO EXECUTE: " + query);
		List<JsonNode> resultList = namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource().addValue("restaurantId", restaurantId),
				new JsonNodeRowMapper(objectMapper)
		);
		return resultList;
	}

	/**
	 * helper method
	 * TODO
	 * @param inputList
	 * @return
     */
	public ArrayList<JsonNode> buildFlowersMenuOut(List<JsonNode> inputList) {
		for (JsonNode node : inputList) {
			ObjectNode nodeObj = (ObjectNode) node;

			ArrayNode details = new ArrayNode(objectMapper.getNodeFactory());
			for (String size : new String[]{"1", "7", "35", "175"}) {
				details.add(buildSizeNode(size, nodeObj));
			}
			nodeObj.put("details", details);
		}
		return new ArrayList<>(inputList);
	}

	protected JsonNode buildSizeNode(String size, ObjectNode row) {
		ObjectNode sizeNode = objectMapper.createObjectNode();
		sizeNode.put("size", Integer.valueOf(size));

		String suffix_units = "units";
		JsonNode unitsNode = row.remove(size + DELIMITER + suffix_units);
		sizeNode.put(suffix_units, unitsNode==null ? null : unitsNode.asInt());

		String suffix_price = "price";
		JsonNode priceNode = row.remove(size + DELIMITER + suffix_price);
		sizeNode.put(suffix_price, priceNode==null ? "" : priceNode.asText());

		String suffix_sup = "sup";
		JsonNode supNode = row.remove(size + DELIMITER + suffix_sup);
		sizeNode.put(suffix_sup, supNode==null ? "" : supNode.asText());

		return sizeNode;
	}
}

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
 * Shipment service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 24/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class ShipmentService {

	private static final Logger logger = LoggerFactory.getLogger(ShipmentService.class);

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getFlowersShipments(Integer restaurantId, String dateStart, String dateEnd) {
		String query = commonQueryStore.getProperty("getFlowersShipments");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("dateStart", dateStart)
						.addValue("dateEnd", dateEnd),
				new JsonNodeRowMapper(objectMapper)
		);
	}

	public List<JsonNode> getFlowersShipmentDetails(Integer restaurantId, String shipmentId) {
		String query = commonQueryStore.getProperty("getFlowersShipmentDetails");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("shipmentId", shipmentId),
				new JsonNodeRowMapper(objectMapper)
		);
	}

	public List<JsonNode> getExtractsShipments(Integer restaurantId, String dateStart, String dateEnd) {
		String query = commonQueryStore.getProperty("getExtractsShipments");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("dateStart", dateStart)
						.addValue("dateEnd", dateEnd),
				new JsonNodeRowMapper(objectMapper)
		);
	}

	public List<JsonNode> getExtractsShipmentDetails(Integer restaurantId, String shipmentId) {
		String query = commonQueryStore.getProperty("getExtractsShipmentDetails");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("shipmentId", shipmentId),
				new JsonNodeRowMapper(objectMapper)
		);
	}
}

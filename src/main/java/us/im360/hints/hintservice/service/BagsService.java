package us.im360.hints.hintservice.service;

import org.apache.commons.lang.StringUtils;
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
 * Bags service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class BagsService {

	private static final Logger logger = LoggerFactory.getLogger(BagsService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getBags(Integer restaurantId, Integer status, String attr1) {
		String query = reportQueryStore.getProperty("getBags");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("status", status)
						.addValue("attr1", StringUtils.isEmpty(attr1) ? null : attr1)
						.addValue("restaurantId", restaurantId),
				new JsonNodeRowMapper(objectMapper));
	}

	public List<JsonNode> getExtracts(Integer restaurantId, Integer status, String attr1) {
		String query = reportQueryStore.getProperty("getExtracts");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("status", status)
						.addValue("attr1", StringUtils.isEmpty(attr1) ? null : attr1),
				new JsonNodeRowMapper(objectMapper));
	}

}

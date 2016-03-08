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
 * Option service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class OptionService {

	private static final Logger logger = LoggerFactory.getLogger(OptionService.class);

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getOptions(Integer restaurantId, Integer active) {
		String profitReportQuery = commonQueryStore.getProperty("getOptions");
		logger.trace("QUERY TO EXECUTE: " + profitReportQuery);
		return namedParameterJdbcTemplate.query(
				profitReportQuery,
				new MapSqlParameterSource()
						.addValue("active", active),
				new JsonNodeRowMapper(objectMapper)
		);
	}
	

}

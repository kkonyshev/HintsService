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
 * Print service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 24/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class PrintService {

	private static final Logger logger = LoggerFactory.getLogger(PrintService.class);

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getPrintInformation(Integer restaurantId, String categoryId, Integer tier) {
		String query = commonQueryStore.getProperty("getPrintInfo");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				new MapSqlParameterSource()
						.addValue("restaurantId", restaurantId)
						.addValue("categoryId", categoryId)
						.addValue("tier", tier),
				new JsonNodeRowMapper(objectMapper)
		);
	}
	

}

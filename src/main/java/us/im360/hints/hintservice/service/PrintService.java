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
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Print service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 24/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class PrintService {

	private static final Logger logger = LoggerFactory.getLogger(PrintService.class);

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getPrintInformation(Integer restaurantId, String categoryId, Integer tier)
	{
		try {
			String query = commonQueryStore.getProperty("getPrintInfo");
			logger.debug("QUERY TO EXECUTE: " + query);

			return namedParameterJdbcTemplate.query(
					query,
					new MapSqlParameterSource()
							.addValue("restaurantId", restaurantId)
							.addValue("categoryId", categoryId)
							.addValue("tier", tier),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}

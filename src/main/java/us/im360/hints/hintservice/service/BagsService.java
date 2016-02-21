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
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Bags service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class BagsService {

	private static final Logger logger = LoggerFactory.getLogger(BagsService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getBags(Integer restaurantId, Integer status, String attr1)
	{
		logger.debug("restaurantId: {}", restaurantId);

		try {
			String profitReportQuery = reportQueryStore.getProperty("getBags");
			logger.debug("QUERY TO EXECUTE: " + profitReportQuery);

			return namedParameterJdbcTemplate.query(
					profitReportQuery,
					new MapSqlParameterSource()
							.addValue("status", status)
							.addValue("attr1", StringUtils.isEmpty(attr1) ? null : attr1),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}
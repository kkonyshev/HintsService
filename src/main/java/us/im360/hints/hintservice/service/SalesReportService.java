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
 * Sales report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 19/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class SalesReportService {

	private static final Logger logger = LoggerFactory.getLogger(SalesReportService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getSalesReport(Integer restaurantId, String startDate, String endDate) {
		String profitReportQuery = reportQueryStore.getProperty("salesReport");
		logger.trace("QUERY TO EXECUTE: " + profitReportQuery);
		return namedParameterJdbcTemplate.query(profitReportQuery,
				new MapSqlParameterSource()
						.addValue("startDate", startDate)
						.addValue("endDate", endDate),
				new JsonNodeRowMapper(objectMapper)
		);
	}
	

}

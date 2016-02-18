package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Profit report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class ProfitReportService {

	private static final Logger logger = LoggerFactory.getLogger(ProfitReportService.class);

	@Autowired
	@Qualifier("queryStore")
	private Properties queryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getProfitReport(Integer restaurantId, String startDate, String endDate)
	{
		logger.debug("restaurantId: {}, startDate: {}, endDate: {}", restaurantId, startDate, endDate);

		try {
			String profitReportQuery = queryStore.getProperty("profitReport");
			logger.debug("QUERY TO EXECUTE: " + profitReportQuery);

			return namedParameterJdbcTemplate.query(profitReportQuery,
					new MapSqlParameterSource()
							.addValue("startDate", startDate)
							.addValue("endDate", endDate),
							new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}

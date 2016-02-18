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
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class CashReportService {

	private static final Logger logger = LoggerFactory.getLogger(CashReportService.class);

	@Autowired
	@Qualifier("queryStore")
	private Properties queryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getCloseReport(Integer restaurantId, String closeDate) {

		logger.debug("restaurantId: {}, closeDate: {}", restaurantId, closeDate);

		try {
			String cashCloseReportQuery = queryStore.getProperty("cashCloseReport");
			logger.debug("QUERY TO EXECUTE: " + cashCloseReportQuery);

			jdbcTemplate.update("set @runtot1:=0;");
			jdbcTemplate.update("set @runtot2:=0;");
			return namedParameterJdbcTemplate.query(cashCloseReportQuery,
					new MapSqlParameterSource()
							.addValue("closeDate", closeDate),
					new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}

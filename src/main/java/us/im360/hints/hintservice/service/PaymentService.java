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
 * Product service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class PaymentService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getPaymentReport(String type, String startDate, String endDate) {
		String paymentReportQuery = reportQueryStore.getProperty("paymentReport");
		logger.trace("QUERY TO EXECUTE: " + paymentReportQuery);
		List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
				paymentReportQuery,
				new MapSqlParameterSource()
						.addValue("type", type)
						.addValue("startDate", startDate)
						.addValue("endDate", endDate),
				new JsonNodeRowMapper(objectMapper));
		logger.trace("RESULT: {}", rowResult);
		return rowResult;
	}
}

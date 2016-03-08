package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Product service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	@Qualifier("commonQueryStore")
	private Properties commonQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getProductStock(String productId) {
		String query = commonQueryStore.getProperty("productStock");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				Collections.singletonMap("productId", productId),
				new JsonNodeRowMapper(objectMapper)
		);
	}

	public List<JsonNode> getStockReport(Integer restaurantId, String date) {
		String query = reportQueryStore.getProperty("stockReport");
		logger.trace("QUERY TO EXECUTE: " + query);
		return namedParameterJdbcTemplate.query(
				query,
				Collections.singletonMap("date", date),
				new JsonNodeRowMapper(objectMapper)
		);
	}
	

}

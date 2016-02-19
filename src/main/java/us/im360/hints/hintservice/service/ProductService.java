package us.im360.hints.hintservice.service;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Product service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public JsonNode getProductStock(String productId)
	{
		logger.debug("productId: {}", productId);

		try {
			String productStockGetByProductIdQuery = reportQueryStore.getProperty("productStockGetByProductId");
			logger.debug("QUERY TO EXECUTE: " + productStockGetByProductIdQuery);

			List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
					productStockGetByProductIdQuery,
					Collections.singletonMap("productId", productId),
					new JsonNodeRowMapper(objectMapper));

			if (CollectionUtils.isEmpty(rowResult)) {
				return null;
			} else {
				return rowResult.iterator().next();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
	}
	

}

package us.im360.hints.hintservice.service;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Ticket service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class TicketService {

	private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

	@Autowired
	@Qualifier("reportQueryStore")
	private Properties reportQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode>  getTicketList(
			Integer userId,
			Integer restaurantId,
			String date,
			String timeStart,
			String timeEnd,
			Collection<String> userIds)
	{
		logger.debug("productId: {}", userId);

		try {
			String ticketListQuery = reportQueryStore.getProperty("ticketList");
			logger.debug("QUERY TO EXECUTE: " + ticketListQuery);

			List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
					ticketListQuery,
					new MapSqlParameterSource()
							.addValue("date", date)
							.addValue("timeStart", timeStart)
							.addValue("timeEnd", timeEnd)
							.addValue("userIds", userIds),
					new JsonNodeRowMapper(objectMapper));

			logger.debug("result set: {}", rowResult);

			if (CollectionUtils.isEmpty(rowResult)) {
				return Collections.emptyList();
			} else {
				Iterator<JsonNode> it = rowResult.iterator();
				while (it.hasNext()) {
					JsonNode node = it.next();
					ObjectNode nodeObj = (ObjectNode) node;

					ObjectNode paymentCashNode = objectMapper.createObjectNode();
					paymentCashNode.put("type", "CASH");
					paymentCashNode.put("amount", node.get("cash"));
					nodeObj.remove("cash");

					ObjectNode paymentCashlessNode = objectMapper.createObjectNode();
					paymentCashlessNode.put("type", "CASHLESS_ATM");
					paymentCashlessNode.put("amount", node.get("cashless_atm"));
					nodeObj.remove("cashless_atm");

					ArrayNode arr = new ArrayNode(objectMapper.getNodeFactory());
					arr.add(paymentCashNode);
					arr.add(paymentCashlessNode);

					nodeObj.put("payments", arr);
				}

				return rowResult;
			}

		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
	}
	

}

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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Ticket service implementation
 * <p>
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

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

    public List<JsonNode> getTicketList(Integer restaurantId, String date, String timeStart, String timeEnd, Collection<String> userIds) {
        String query = reportQueryStore.getProperty("ticketList");
        logger.trace("QUERY TO EXECUTE: " + query);
        List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
                query,
                new MapSqlParameterSource()
                        .addValue("date", date)
                        .addValue("timeStart", timeStart)
                        .addValue("timeEnd", timeEnd)
                        .addValue("userIds", userIds),
                new JsonNodeRowMapper(objectMapper));

        logger.debug("result set: {}", rowResult);
        return rowResult;
    }

    public List<JsonNode> getTicketDetails(String userId, Integer restaurantId, Integer ticketVisibleId) {
        String query = commonQueryStore.getProperty("ticketDetails");
        logger.trace("QUERY TO EXECUTE: " + query);
        List<JsonNode> rowResult = namedParameterJdbcTemplate.query(
                query,
                new MapSqlParameterSource()
                        .addValue("ticketVisibleId", ticketVisibleId),
                new JsonNodeRowMapper(objectMapper));
        logger.debug("result set: {}", rowResult);
        return rowResult;
    }
}

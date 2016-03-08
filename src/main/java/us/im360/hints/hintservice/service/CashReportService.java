package us.im360.hints.hintservice.service;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.List;
import java.util.Properties;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
public class CashReportService {

    private static final Logger logger = LoggerFactory.getLogger(CashReportService.class);

    @Autowired
    @Qualifier("reportQueryStore")
    private Properties reportQueryStore;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<JsonNode> getCloseReport(Integer restaurantId, String closeDate) {
        String cashCloseReportQuery = reportQueryStore.getProperty("cashCloseReport");
        logger.trace("QUERY TO EXECUTE: " + cashCloseReportQuery);
        return namedParameterJdbcTemplate.query(
                cashCloseReportQuery,
                new MapSqlParameterSource()
                        .addValue("closeDate", closeDate)
                        .addValue("restaurantId", restaurantId),
                new JsonNodeRowMapper(objectMapper));
    }

    public ObjectNode getCashClose(Integer restaurantId, String cashRegisterId) {
        ObjectNode resultRow = objectMapper.createObjectNode();

        //cash register dates and host
        JsonNode getCashRegisterInfoRow =
                fetchSingleRowSelect(
                        "getCashRegisterInfo",
                        new MapSqlParameterSource().addValue("cashRegisterId", cashRegisterId)
                );

        String dateStart = getCashRegisterInfoRow.get("dateStart").asText();
        String dateEnd = getCashRegisterInfoRow.get("dateEnd").asText();
        String host = getCashRegisterInfoRow.get("host").asText();

        // cashless atm info
        JsonNode getCashlessInfoRow =
                fetchSingleRowSelect(
                        "getCashlessInfo",
                        new MapSqlParameterSource()
                                .addValue("restaurantId", restaurantId)
                                .addValue("dateStart", dateStart)
                                .addValue("dateEnd", dateEnd)
                                .addValue("host", host)
                );

        // cashier name
        JsonNode getUserNameByCashRegisterRow = fetchSingleRowSelect(
                "getUserNameByCashRegister",
                new MapSqlParameterSource()
                        .addValue("restaurantId", restaurantId)
                        .addValue("dateStart", dateStart)
                        .addValue("dateEnd", dateEnd)
                        .addValue("host", host)
        );

        // cash drop
        JsonNode getCashDropRow = fetchSingleRowSelect(
                "getCashDrop",
                new MapSqlParameterSource()
                        .addValue("restaurantId", restaurantId)
                        .addValue("host", host)
        );

        // cashless count
        JsonNode getCashlessCountRow = fetchSingleRowSelect(
                "getCashlessCount",
                new MapSqlParameterSource()
                        .addValue("restaurantId", restaurantId)
                        .addValue("dateEnd", dateEnd)
                        .addValue("host", host)
        );

        // compose result
        resultRow.put("total",              getCashlessInfoRow.get("total"));
        resultRow.put("cash",               getCashlessInfoRow.get("cash"));
        resultRow.put("cashless_atm",       getCashlessInfoRow.get("cashless_atm"));
        resultRow.put("cash_tax",           getCashlessInfoRow.get("cash_tax"));
        resultRow.put("cashless_atm_tax",   getCashlessInfoRow.get("cashless_atm_tax"));
        resultRow.put("name",               getUserNameByCashRegisterRow.get("name"));
        resultRow.put("cash_drop",          getCashDropRow.get("cash_drop"));
        resultRow.put("cashless_atm_count", getCashlessCountRow.get("cashless_atm_count"));

        return resultRow;
    }

    private JsonNode fetchSingleRowSelect(String queryName, SqlParameterSource queryParams) {
        JsonNode resultRow;

        String query = reportQueryStore.getProperty(queryName);
        logger.trace("QUERY TO EXECUTE: " + query);

        List<JsonNode> queryResultList = namedParameterJdbcTemplate.query(
                query,
                queryParams,
                new JsonNodeRowMapper(objectMapper));

        logger.trace("queryResultList: " + queryResultList);

        if (CollectionUtils.isEmpty(queryResultList)) {
            throw new IllegalArgumentException(queryName + " result is empty");
        } else {
            resultRow = queryResultList.iterator().next();
        }

        return resultRow;
    }

}

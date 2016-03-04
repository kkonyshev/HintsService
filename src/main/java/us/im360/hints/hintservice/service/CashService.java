package us.im360.hints.hintservice.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * Cash service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
@Transactional
public class CashService {

    private static final Logger logger = LoggerFactory.getLogger(CashService.class);

    @Autowired
    @Qualifier("editorQueryStore")
    private Properties editorQueryStore;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    private ObjectMapper objectMapper;

    public boolean cashDropInsert(String terminal, String cashRegisterId, Double cashCount, Integer userId, Integer restaurantId) {
        try {
            String query = editorQueryStore.getProperty("cashDropInsert");
            logger.trace("QUERY TO EXECUTE: " + query);

            int rows = namedParameterJdbcTemplate.update(
                    query,
                    new MapSqlParameterSource()
                            .addValue("terminal", terminal)
                            .addValue("cashRegisterId", cashRegisterId)
                            .addValue("cashCount", cashCount)
                            .addValue("userId", userId)
                            .addValue("restaurantId", restaurantId)
            );

            if (rows>0) {
                return true;
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }


}

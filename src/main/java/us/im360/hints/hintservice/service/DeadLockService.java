package us.im360.hints.hintservice.service;

import com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.List;
import java.util.Properties;

/**
 * Strain service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 21/02/16.
 */
@SuppressWarnings("UnusedDeclaration")
@Service
@Transactional(isolation = Isolation.READ_COMMITTED, timeout = 3000)
public class DeadLockService {

	private static final Logger logger = LoggerFactory.getLogger(DeadLockService.class);

	@Autowired
	@Qualifier("editorQueryStore")
	private Properties editorQueryStore;

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	//@Retryable(MySQLTransactionRollbackException.class)
	public void doDL(Integer id1, String name1, Integer id2, String name2) throws InterruptedException {

		String query = editorQueryStore.getProperty("deadLock");

		printDbState();
		logger.info("id: {}, name: {}", id1, name1);
		int updated = namedParameterJdbcTemplate.update(
				query,
				new MapSqlParameterSource()
						.addValue("id", id1)
						.addValue("name", name1)
		);
		printDbState();
		Thread.sleep(3000);
		printDbState();
		logger.info("id: {}, name: {}", id2, name2);
		int updated1 = namedParameterJdbcTemplate.update(
				query,
				new MapSqlParameterSource()
						.addValue("id", id2)
						.addValue("name", name2)
		);
		printDbState();
		Thread.sleep(5000);
		printDbState();
		logger.info("done");
	}

	private void printDbState() {
		List<JsonNode> res = namedParameterJdbcTemplate.query(
				"select id, name from DL_EXAMPLE",
				new EmptySqlParameterSource(),
				new JsonNodeRowMapper(objectMapper));
		logger.info("DB sate: {}", res);
	}

	@Recover
	public void recover(MySQLTransactionRollbackException e) {
		logger.error("RECOVERY: " + e.getMessage(), e);
	}
}

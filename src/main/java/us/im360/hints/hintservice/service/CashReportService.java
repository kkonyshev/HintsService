package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.Collections;
import java.util.List;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class CashReportService {

	private static String CASH_REPORT_CLOSE = "" +
			"select cc.cashregister_id AS register " +
			"     , cc.terminal AS terminal " +
			"     , if(date(cc.date_open) = ? , time_format(cc.date_open,'%h:%i %p'), '09:00 AM') AS time_open " +
			"     , time_format(cc.date_close,'%h:%i %p') AS time_closed " +
			"     , cc.cash_expected AS expected_cash " +
			"     , cc.cash_actual AS actual_cash " +
			"     , cc.cash_difference AS cash_difference " +
			"     , cc.cashless_atm_expected AS expected_debit " +
			"     , cc.cashless_atm_actual AS actual_debit " +
			"     , if(cc.terminal like '%1', (SELECT SUM(x.cashless_atm_actual) " +
			"                                    FROM im_close_cash x " +
			"                                   WHERE x.terminal like '%1' " +
			"                                     and date(x.date_close) = date(cc.date_close) " +
			"                                     and x.location_id = cc.location_id " +
			"                                     and x.id <= cc.id) " +
			"                               , (SELECT SUM(x.cashless_atm_actual) " +
			"                                    FROM im_close_cash x " +
			"                                   WHERE x.terminal not like '%1' " +
			"                                     and date(x.date_close) = date(cc.date_close) " +
			"                                     and x.location_id = cc.location_id " +
			"                                     and x.id <= cc.id) " +
			"     ) AS running_debit_sum " +
			"     , cc.cashless_atm_difference AS debit_difference " +
			"     , cc.total_expected AS expected_total " +
			"     , cc.total_actual AS actual_total " +
			"     , cc.total_difference AS total_difference " +
			"     , upper(pu.name) AS cashier " +
			"     , upper(concat(us.first_name, ' ', us.last_name)) AS manager " +
			"  from im_close_cash cc " +
			"  left join tb_users us " +
			"         on cc.user_id = us.id " +
			"  left join posper_user pu " +
			"         on cc.mostsales_userid = pu.id " +
			"  left join (select cashregister_id, sum(cash_drop) AS cash_drop from im_cash_drop group by cashregister_id) t1 " +
			"         on t1.cashregister_id = cc.cashregister_id " +
			" where date(cc.date_close) = ? " +
			"   and cc.location_id = ?;";

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getCloseReport(Integer restaurantId, String closeDate) {

		try {
			return jdbcTemplate.query(
					CASH_REPORT_CLOSE,
					new Object[]{closeDate, closeDate, restaurantId},
					new JsonNodeRowMapper(objectMapper)
			);

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}

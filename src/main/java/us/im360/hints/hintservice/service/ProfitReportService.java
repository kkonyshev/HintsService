package us.im360.hints.hintservice.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.util.JsonNodeRowMapper;

import java.util.*;

/**
 * Profit report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class ProfitReportService {

	private static final Logger logger = LoggerFactory.getLogger(ProfitReportService.class);


	private static String PROFIT_REPORT = "" +
			"select t1.name AS stain " +
			"     , t1.tier_name AS tier " +
			"     , t2.cost as cost " +
			"     , t2.grams as total_grams_sold " +
			"     , t1.profit as gross_receipts " +
			"     , t1.revenue as gross_revenue " +
			"     , if((t1.profit-t2.cost)/t2.grams <= 0, 0, (t1.profit-t2.cost)/t2.grams) as profit_gram " +
			"     , if(t1.revenue-t2.cost <= 0, 0, t1.revenue-t2.cost) as net_profit " +
			"  from (select sum(tl.priceSell * tl.amount * (1+tl.taxRate)) as profit " +
			"             , sum(tl.priceSell * tl.amount) as revenue " +
			"             , ty.name as name " +
			"             , op.name as option_name " +
			"             , tier.tier_name " +
			"             , ca2.id " +
			"          from posper_ticketline tl " +
			"             , posper_ticket ti " +
			"             , posper_product pr " +
			"             , posper_category ca1 " +
			"             , posper_category ca2 " +
			"             , im_strain_type ty " +
			"             , im_strain_tier tier " +
			"             , im_strain_category_option co " +
			"          left join im_strain_option op " +
			"                 on op.id = co.option_id " +
			"         where co.category_id = ca2.id " +
			"           and ca1.parent_id = ca2.id " +
			"           and tl.ticket_ticketline = ti.id " +
			"           and tl.product_id = pr.id " +
			"           and pr.category_id = ca1.id " +
			"           and ty.posper_category_id = ca1.id " +
			"           and ty.im_strain_tier_id = tier.id " +
			"           and date(ti.dateClose) >= :startDate " +
			"           and date(ti.dateClose) <= :endDate " +
			"         group by ty.name, tier.tier_name) t1 " +
			"          left join (select ty.name " +
			"                          , ti.tier_name " +
			"                          , sum(-1 * st.units * dt.cost_per_gram * dt.grams_per_jar) as cost " +
			"                          , sum(-1 * st.units * dt.grams_per_jar) as grams " +
			"                       from im_strain_detail dt " +
			"                          , posper_stockdiary st " +
			"                          , posper_product pr " +
			"                          , posper_category ca " +
			"                          , im_strain_type ty " +
			"                          , im_strain_tier ti " +
			"                      where dt.id = st.im_strain_detail_id " +
			"                        and st.product_id = pr.id " +
			"                        and pr.category_id = ca.id " +
			"                        and ty.posper_category_id = ca.id " +
			"                        and ty.im_strain_tier_id = ti.id " +
			"                        and st.reason in (-6, 6, -8, 8) " +
			"                        and date(st.date) >= :startDate " +
			"                        and date(st.date) <= :endDate " +
			"                      group by ty.name, ti.tier_name) t2 " +
			"                 on t1.name = t2.name " +
			"                and t1.tier_name = t2.tier_name " +
			"              where t1.id in(:strains) " +
			"                and t1.tier_name in(:tiers)";

	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public List<JsonNode> getProfitReport(Integer restaurantId, String startDate, String endDate, Collection<String> strains, Collection<String> tiers)
	{
		logger.debug("restaurantId: {}, startDate: {}, endDate: {}, strainListComaSeparated: {}, tierListComaSeparated: {}", restaurantId, startDate, endDate, strains, tiers);

		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			paramMap.put("strains", strains);
			paramMap.put("tiers", tiers);

			logger.debug("paramMap: {}" + paramMap);

			return namedParameterJdbcTemplate.query(PROFIT_REPORT,
							paramMap,
							new JsonNodeRowMapper(objectMapper));

		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	

}

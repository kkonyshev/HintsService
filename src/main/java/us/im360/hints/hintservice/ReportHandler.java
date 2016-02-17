package us.im360.hints.hintservice;

import javax.ws.rs.core.Response;

/**
 * Report service interface
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
public interface ReportHandler {

    /**
     * Cash report on date
     *
     * @param restaurantId
     * @param closeDate
     * @return
     */
    Response getCloseReportOnDate(Integer restaurantId, String closeDate);

    Response getProfitReport(
            Integer restaurantId,
            String startDate,
            String endDate,
            String strainListComaSeparated,
            String tierListComaSeparated);
}

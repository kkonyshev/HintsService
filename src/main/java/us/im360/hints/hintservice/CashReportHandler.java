package us.im360.hints.hintservice;

import javax.ws.rs.core.Response;

/**
 * Cash report service interface
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 16/02/16.
 */
public interface CashReportHandler {

    /**
     * Cash report on date
     *
     * @param restaurantId
     * @param closeDate
     * @return
     */
    Response getCloseReportOnDate(Integer restaurantId, String closeDate);
}

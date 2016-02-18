package us.im360.hints.hintservice;

import javax.ws.rs.PathParam;
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
    Response getCloseReportOnDate(
            Integer userId,
            Integer restaurantId,
            String closeDate
    );

    /**
     * Profit report
     *
     * @param restaurantId
     * @param startDate
     * @param endDate
     * @return
     */
    Response getProfitReport(
            Integer userId,
            Integer restaurantId,
            String startDate,
            String endDate
    );

    /**
     * Loss report
     *
     * @param userId
     * @param restaurantId
     * @param startDate
     * @param endDate
     * @return
     */
    Response getLossReport(
            Integer userId,
            Integer restaurantId,
            String startDate,
            String endDate);

    /**
     * Product stock report
     *
     * @param userId
     * @param restaurantId
     * @param productId
     * @return
     */
    Response getStockReport(
            Integer userId,
            Integer restaurantId,
            String productId
    );

    /**
     * Payment report
     *
     * @param userId
     * @param restaurantId
     * @param startDate
     * @param endDate
     * @return
     */
    Response getPaymentReport(
            Integer userId,
            Integer restaurantId,
            String startDate,
            String endDate
    );
}

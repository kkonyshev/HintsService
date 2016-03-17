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
    Response getCloseReportOnDate(
            String userId,
            Integer restaurantId,
            String closeDate
    );

    /**
     * Cash close report
     *
     * @param userId
     * @param restaurantId
     * @param cashRegisterId
     * @return
     */
    Response getCashClose(
            String userId,
            Integer restaurantId,
            String cashRegisterId);

    /**
     * Profit report
     *
     * @param restaurantId
     * @param startDate
     * @param endDate
     * @return
     */
    Response getProfitReport(
            String userId,
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
            String userId,
            Integer restaurantId,
            String startDate,
            String endDate);

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
            String userId,
            Integer restaurantId,
            String startDate,
            String endDate
    );

    /**
     * Ticket list
     *
     * @param userId
     * @param restaurantId
     * @param date
     * @param timeStart
     * @param timeEnd
     * @param userIdList
     * @return
     */
    Response getTickets(
            String userId,
            Integer restaurantId,
            String date,
            String timeStart,
            String timeEnd,
            String userIdList
    );

    /**
     * Sales report
     *
     * @param userId
     * @param restaurantId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Response getSalesReport(
            String userId,
            Integer restaurantId,
            String dateStart,
            String dateEnd
    );

    /**
     * Unit list
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getUnits(
            String userId,
            Integer restaurantId
    );

    /**
     * List of bags
     *
     * @param userId
     * @param restaurantId
     * @param status
     * @param attr1
     * @return
     */
    Response getBags(
            String userId,
            Integer restaurantId,
            Integer status,
            String attr1
    );

    /**
     * List of bags with null attr1
     *
     * @param userId
     * @param restaurantId
     * @param status
     * @return
     */
    Response getBagsNullStatus(
            String userId,
            Integer restaurantId,
            Integer status
    );

    /**
     * Stock report
     *
     * @param userId
     * @param restaurantId
     * @param date
     * @return
     */
    Response getStockReport(
            String userId,
            Integer restaurantId,
            String date
    );

    /**
     * Extracts list
     *
     * @param userId
     * @param restaurantId
     * @param status
     * @param attr1
     * @return
     */
    Response getExtracts(
            String userId,
            Integer restaurantId,
            Integer status,
            String attr1
    );

    /**
     * Extracts list with null attr1
     * @param userId
     * @param restaurantId
     * @param status
     * @return
     */
    Response getExtractsNullStatus(
            String userId,
            Integer restaurantId,
            Integer status
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getInventory(
            String userId,
            Integer restaurantId
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @param attr1 status
     * @return
     */
    Response getInventoryList(
            String userId,
            Integer restaurantId,
            String attr1
    );
}

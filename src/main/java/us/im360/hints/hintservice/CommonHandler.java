package us.im360.hints.hintservice;

import javax.ws.rs.core.Response;

/**
 * Common service interface
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 19/02/16.
 */
public interface CommonHandler {

    /**
     *
     * @param userId
     * @param restaurantId
     * @param ticketVisibleId
     * @return
     */
    Response getTicketDetail(
            Integer userId,
            Integer restaurantId,
            Integer ticketVisibleId
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @param groupId
     * @param active
     * @return
     */
    Response getUsers(
            Integer userId,
            Integer restaurantId,
            Integer groupId,
            Integer active
    );

    /**
     * Product stock
     *
     * @param userId
     * @param restaurantId
     * @param productId
     * @return
     */
    Response getProductStock(
            Integer userId,
            Integer restaurantId,
            String productId
    );

    /**
     * Strains list
     *
     * @param userId
     * @param restaurantId
     * @param active
     * @return
     */
    Response getStrains(
            Integer userId,
            Integer restaurantId,
            Integer active
    );

    /**
     * List of attributes
     *
     * @param userId
     * @param restaurantId
     * @param active
     * @return
     */
    Response getAttributes(
            Integer userId,
            Integer restaurantId,
            Integer active
    );

    /**
     * Options list
     * @param userId
     * @param restaurantId
     * @param active
     * @return
     */
    Response getOptions(
            Integer userId,
            Integer restaurantId,
            Integer active
    );

    /**
     * Tiers costs
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getTiersCost(
            Integer userId,
            Integer restaurantId
    );


    /**
     * Extracts costs
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getExtractsCost(
            Integer userId,
            Integer restaurantId
    );

    /**
     * Print information
     *
     * @param userId
     * @param restaurantId
     * @param categoryId
     * @param tier
     * @return
     */
    Response getPrintInformation(
            Integer userId,
            Integer restaurantId,
            String categoryId,
            Integer tier
    );
}

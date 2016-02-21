package us.im360.hints.hintservice;

import javax.ws.rs.PathParam;
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
}

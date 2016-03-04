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

    /**
     * Flowers menu
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getFlowersMenu(
            Integer userId,
            Integer restaurantId
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getExtractsMenu(
            Integer userId,
            Integer restaurantId
    );

    /**
     * Flowers Shipments
     *
     * @param userId
     * @param restaurantId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Response getShipmentsFlowers(
            Integer userId,
            Integer restaurantId,
            String dateStart,
            String dateEnd
    );

    /**
     * Flowers shipment details
     *
     * @param userId
     * @param restaurantId
     * @param shipmentId
     * @return
     */
    Response getShipmentFlowersDetails(
            Integer userId,
            Integer restaurantId,
            String shipmentId
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    Response getShipmentsExtracts(
            Integer userId,
            Integer restaurantId,
            String dateStart,
            String dateEnd
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @param shipmentId
     * @return
     */
    Response getShipmentExtractsDetails(
            Integer userId,
            Integer restaurantId,
            String shipmentId
    );

    /**
     * Update strain method
     *
     * @param userId
     * @param restaurantId
     * @param prevStrain
     * @param strain
     * @param attribute
     * @param status
     * @return
     */
    Response updateStrain(
            Integer userId,
            Integer restaurantId,
            String prevStrain,
            String strain,
            String attribute,
            String status
    );

    /**
     * Update strain attribute
     *
     * @param userId
     * @param restaurantId
     * @param prevStrain
     * @param status
     * @param attribute
     * @return
     */
    Response updateStrainAttribute(
            Integer userId,
            Integer restaurantId,
            String prevStrain,
            String status,
            String attribute
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @param terminal
     * @param cashRegisterId
     * @param cashCount
     * @return
     */
    Response insertCashDrop(
            Integer userId,
            Integer restaurantId,
            String terminal,
            String cashRegisterId,
            Double cashCount);
}

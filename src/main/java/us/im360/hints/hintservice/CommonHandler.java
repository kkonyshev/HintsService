package us.im360.hints.hintservice;

import us.im360.hints.hintservice.dto.AdjustMiscReqDto;
import us.im360.hints.hintservice.dto.FlowerInventoryReqDto;
import us.im360.hints.hintservice.dto.InventoryExtractReqDto;
import us.im360.hints.hintservice.dto.MiscInventoryReqDto;

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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
            Integer restaurantId
    );

    /**
     *
     * @param userId
     * @param restaurantId
     * @return
     */
    Response getExtractsMenu(
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
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
            String userId,
            Integer restaurantId,
            String terminal,
            String cashRegisterId,
            Double cashCount);

    /**
     * Add flower inventory
     *
     * @param flowerInventory
     * @return
     */
    Response addInventoryFlower(FlowerInventoryReqDto flowerInventory);

    /**
     * Add misc inventory
     *
     * @param miscInventory
     * @return
     */
    Response addInventoryMisc(MiscInventoryReqDto miscInventory);

    /**
     * Adjust misc stock
     *
     * @param reqDto
     * @return
     */
    Response stockMistAdjust(AdjustMiscReqDto reqDto);

    /**
     * Add inventory extract
     *
     * @param reqDto
     * @return
     */
    Response inventoryExtractAdd(InventoryExtractReqDto reqDto);
}

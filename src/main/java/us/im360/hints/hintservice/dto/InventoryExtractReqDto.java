package us.im360.hints.hintservice.dto;

import java.util.List;

/**
 * Inventory extract req dto
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 15/03/16.
 */
public class InventoryExtractReqDto {
    public String userId;
    public String restaurantId;
    public String extractDetailsId;
    public Double cost;
    public Double pricePerGram;
    public Double gramsTotal;
    public Double gramsRemaining;
    public Double endingWeight;
    public Double lossWeight;
    public List<InventoryExtractJarReqDto> jars;
    public String date;
    public Double weighTech;
}

package us.im360.hints.hintservice.dto;

import java.util.List;

/**
 * Flower service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 11/03/16.
 */
public class FlowerInventoryReqDto {
    //TODO convert all userIds to String
    public Integer userId;
    public String restaurantId;

    public String unitDetailId;

    public Double cost;
    public String weighTech;

    public String date;
    public Double totalWeight;

    public Double bagWeight;
    public Double startWeight;
    public Double endWeight;
    public Double loss;

    public List<FlowerInventoryJarReqDto> jars;
    public Double costPerGram;

    public Double shake;
    public Double crumb;
}

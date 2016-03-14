package us.im360.hints.hintservice.dto;

import java.util.List;

/**
 * Flower service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 14/03/16.
 */
public class AdjustMiscReqDto {
    //TODO convert all userIds to String
    public Integer userId;
    public String restaurantId;
    public List<AdjustMiscProductReqDto> products;
}

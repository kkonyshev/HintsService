package us.im360.hints.hintservice.dto;

import java.util.List;

/**
 * Adjust misc req dto
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 14/03/16.
 */
public class AdjustMiscReqDto {
    public String userId;
    public String restaurantId;
    public List<AdjustMiscProductReqDto> products;
}

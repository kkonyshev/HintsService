package us.im360.hints.hintservice.dto;

import java.util.List;

/**
 * Misc inventory req dto
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 13/03/16.
 */
public class MiscInventoryReqDto {
    public String userId;
    public String restaurantId;
    public List<MiscInventoryJarReqDto> jars;
}

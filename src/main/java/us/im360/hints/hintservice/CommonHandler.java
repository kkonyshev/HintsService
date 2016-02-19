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
     * @return
     */
    Response get(
            Integer userId,
            Integer restaurantId
    );
}

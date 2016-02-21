package us.im360.hints.hintservice.handlerImpl;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import us.im360.hints.hintservice.ReportHandler;
import us.im360.hints.hintservice.util.ResponseBuilder;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com>
 */
public class AbstractHandlerImpl  {

    public static final String DETAILS_FIELD_NAME = "details";

    private static final Logger logger = LoggerFactory.getLogger(AbstractHandlerImpl.class);

    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * Helper method for building response object
     *
     * @param responseBuilder response success/fail builder instance
     */
    protected Response buildResponse(ResponseBuilder responseBuilder) {
        try {
            String resultStr = objectMapper.writeValueAsString(responseBuilder.build());
            return Response.status(200).entity(resultStr).type(MediaType.APPLICATION_JSON).build();
        } catch (JsonMappingException e) {
            logger.error("JsonMappingException", e);
            return Response.serverError().build();
        } catch (JsonGenerationException e) {
            logger.error("JsonGenerationException", e);
            return Response.serverError().build();
        } catch (IOException e) {
            logger.error("IOException", e);
            return Response.serverError().build();
        } catch (Exception e) {
            logger.error("Exception", e);
            return Response.serverError().build();
        }
    }
}

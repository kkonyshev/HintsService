package us.im360.hints.hintservice.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rene
 */
public class SystemErrorException extends WebApplicationException {
	
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LoggerFactory.getLogger(SystemErrorException.class);
	
    public static final String SYSTEM_ERROR = "System Error";
    public static final String VALIDATION_ERROR = "Message failed validation";

    
    public SystemErrorException() {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(SYSTEM_ERROR).type(MediaType.TEXT_PLAIN).build());
    }
    
    public SystemErrorException(Exception message){
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
        log.error(SYSTEM_ERROR, message);
    }
    
}

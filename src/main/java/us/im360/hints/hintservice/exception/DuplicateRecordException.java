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
public class DuplicateRecordException extends WebApplicationException{
	
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(DuplicateRecordException.class);
        
    public static final String DUPLICATE_RECORD = "Duplicate Record Exception";

    
    public DuplicateRecordException() {
         super(Response.status(Response.Status.FORBIDDEN)
             .entity(DUPLICATE_RECORD).type(MediaType.TEXT_PLAIN).build());
     }
    
    public DuplicateRecordException(Exception message) {
         super(Response.status(Response.Status.FORBIDDEN)
             .entity(message.getMessage()).type(MediaType.TEXT_PLAIN).build());
        log.error(DUPLICATE_RECORD, message);
     }
    
}

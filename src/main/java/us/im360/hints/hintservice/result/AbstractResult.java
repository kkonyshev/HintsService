package us.im360.hints.hintservice.result;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author 	Yen
 * @version v1.0
 * @since  	v1.0
 * 
 */
@XmlRootElement
public class AbstractResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private ResultCode resultCode;
    private ResultReason resultReason;
    
	public ResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(ResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public ResultReason getResultReason() {
		return resultReason;
	}

	public void setResultReason(ResultReason resultReason) {
		this.resultReason = resultReason;
	}
    
}

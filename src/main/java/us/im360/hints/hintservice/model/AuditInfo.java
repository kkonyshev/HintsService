package us.im360.hints.hintservice.model;

import javax.servlet.http.HttpServletRequest;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
public class AuditInfo {
    public final String methodName;
    public final Integer userId;
    public final String sid;
    public AuditInfo(HttpServletRequest req, Integer userId) {
        this.methodName = new Exception().getStackTrace()[1].getMethodName();
        this.userId = userId;
        this.sid = req.getSession().getId();
    }
    @Override
    public String toString() {
        return String.format("method(%s); userId(%s); SID(%s)", this.methodName, userId, sid);
    }
}

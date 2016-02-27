package us.im360.hints.hintservice.model;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
public class AuditInfo {
    public final Integer userId;
    public final String sid;
    public final String rid;
    public final String url;
    public AuditInfo(HttpServletRequest req, Integer userId) {
        this.userId = userId;
        this.sid = req.getSession().getId();
        this.url = req.getRequestURL().toString();
        this.rid = UUID.randomUUID().toString().substring(0, 5);
    }
    @Override
    public String toString() {
        return String.format("userId(%s); SID(%s); RID(%s); url(%s)", userId, sid, rid, url);
    }
}

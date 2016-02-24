package us.im360.hints.hintservice.model;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
public class AuditInfo {
    private final String methodName;
    public AuditInfo() {
        this.methodName = new Exception().getStackTrace()[1].getMethodName();
    }
    @Override
    public String toString() {
        return "AuditInfo. method: " + this.methodName;
    }
}

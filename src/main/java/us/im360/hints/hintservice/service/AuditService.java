package us.im360.hints.hintservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.im360.hints.hintservice.model.AuditInfo;

/**
 * Cash report service implementation
 *
 * Created by Konstantin Konyshev <konyshev.konstantin@gmail.com> on 18/02/16.
 */
@SuppressWarnings("unused")
@Service
@Transactional
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    public void log(Integer userId, AuditInfo auditInfo) {
        logger.info("AUDIT| usedId: {}, auditInfo: {}", userId, auditInfo);
    }
}

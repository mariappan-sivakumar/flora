package com.mari.flora.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final ApplicationEventPublisher eventPublisher;
    private final CurrentUserProvider currentUserProvider;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer paramDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        // Business method runs first. If it throws, we skip audit entirely —
        // we only ever log operations that actually succeeded.
        Object result = joinPoint.proceed();

        try {
            recordAudit(joinPoint, auditable, result);
        } catch (Exception ex) {
            // AOP-side audit failure must never break the caller's response.
            log.error("Audit aspect failed for method {}", joinPoint.getSignature(), ex);
        }

        return result;
    }

    private void recordAudit(ProceedingJoinPoint joinPoint, Auditable auditable, Object result) {
        StandardEvaluationContext context = buildEvalContext(joinPoint, result);

        // ✅ NEW: skip entirely if condition is set and evaluates false
        if (!auditable.condition().isBlank()) {
            Expression conditionExpr = parser.parseExpression(auditable.condition());
            Boolean shouldAudit = conditionExpr.getValue(context, Boolean.class);
            if (Boolean.FALSE.equals(shouldAudit)) {
                log.debug("Audit skipped — condition false for method {}", joinPoint.getSignature());
                return;
            }
        }

        CurrentUserProvider.CurrentUserContext user = currentUserProvider.resolve();
        context.setVariable("username", user.username());

        Long entityId = resolveEntityId(auditable, context, result);
        String message = resolveMessage(auditable, context, entityId, user.username());

        eventPublisher.publishEvent(new AuditEvent(
                this, user.userId(), user.username(),
                auditable.action(), auditable.entityType(), entityId, message
        ));
    }

    private StandardEvaluationContext buildEvalContext(ProceedingJoinPoint joinPoint, Object result) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = paramDiscoverer.getParameterNames(method);
        Object[] args = joinPoint.getArgs();

        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        if (result != null) {
            context.setVariable("result", result);
        }
        return context;
    }

    private Long resolveEntityId(Auditable auditable, StandardEvaluationContext context, Object result) {
        // Priority 1: explicit expression on the annotation (needed for DELETE, e.g. "#id")
        if (!auditable.entityIdExpression().isBlank()) {
            Expression expr = parser.parseExpression(auditable.entityIdExpression());
            Object value = expr.getValue(context);
            return value != null ? Long.valueOf(value.toString()) : null;
        }
        // Priority 2: try to read "id" off the result via reflection (covers CREATE/UPDATE
        // for Garland/Category/Company, which all expose getId())
        if (result != null) {
            try {
                Method getId = result.getClass().getMethod("getId");
                Object id = getId.invoke(result);
                return id != null ? Long.valueOf(id.toString()) : null;
            } catch (Exception ignored) {
                // no getId() on result — fall through
            }
        }
        return null;
    }

    private String resolveMessage(Auditable auditable, StandardEvaluationContext context,
                                  Long entityId, String username) {
        if (!auditable.message().isBlank()) {
            Expression expr = parser.parseExpression(auditable.message(), new org.springframework.expression.common.TemplateParserContext());
            return expr.getValue(context, String.class);
        }
        // Fallback generic message so nothing is ever missing a readable line in the UI
        return String.format("%s performed %s on %s (ID: %s)",
                username, auditable.action(), auditable.entityType(),
                entityId != null ? entityId : "N/A");
    }
}
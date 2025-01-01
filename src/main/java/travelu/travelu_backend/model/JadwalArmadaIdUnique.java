package travelu.travelu_backend.model;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.springframework.web.servlet.HandlerMapping;
import travelu.travelu_backend.service.JadwalService;


/**
 * Validate that the id value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = JadwalArmadaIdUnique.JadwalArmadaIdUniqueValidator.class
)
public @interface JadwalArmadaIdUnique {

    String message() default "{Exists.jadwal.armadaId}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class JadwalArmadaIdUniqueValidator implements ConstraintValidator<JadwalArmadaIdUnique, Long> {

        private final JadwalService jadwalService;
        private final HttpServletRequest request;

        public JadwalArmadaIdUniqueValidator(final JadwalService jadwalService,
                final HttpServletRequest request) {
            this.jadwalService = jadwalService;
            this.request = request;
        }

        @Override
        public boolean isValid(final Long value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(jadwalService.get(Long.parseLong(currentId)).getArmadaId())) {
                // value hasn't changed
                return true;
            }
            return !jadwalService.armadaIdExists(value);
        }

    }

}

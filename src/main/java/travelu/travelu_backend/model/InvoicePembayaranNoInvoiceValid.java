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
import travelu.travelu_backend.service.InvoicePembayaranService;


/**
 * Check that noInvoice is present and available when a new InvoicePembayaran is created.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = InvoicePembayaranNoInvoiceValid.InvoicePembayaranNoInvoiceValidValidator.class
)
public @interface InvoicePembayaranNoInvoiceValid {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class InvoicePembayaranNoInvoiceValidValidator implements ConstraintValidator<InvoicePembayaranNoInvoiceValid, String> {

        private final InvoicePembayaranService invoicePembayaranService;
        private final HttpServletRequest request;

        public InvoicePembayaranNoInvoiceValidValidator(
                final InvoicePembayaranService invoicePembayaranService,
                final HttpServletRequest request) {
            this.invoicePembayaranService = invoicePembayaranService;
            this.request = request;
        }

        @Override
        public boolean isValid(final String value, final ConstraintValidatorContext cvContext) {
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("noInvoice");
            if (currentId != null) {
                // only relevant for new objects
                return true;
            }
            String error = null;
            if (value == null) {
                // missing input
                error = "NotNull";
            } else if (invoicePembayaranService.noInvoiceExists(value)) {
                error = "Exists.invoicePembayaran.noInvoice";
            }
            if (error != null) {
                cvContext.disableDefaultConstraintViolation();
                cvContext.buildConstraintViolationWithTemplate("{" + error + "}")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }

    }

}

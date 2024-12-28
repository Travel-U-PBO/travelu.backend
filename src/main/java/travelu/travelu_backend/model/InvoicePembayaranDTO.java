package travelu.travelu_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvoicePembayaranDTO {

    @Size(max = 255)
    @InvoicePembayaranNoInvoiceValid
    private String noInvoice;

    @NotNull
    private StatusPembayaran status;

    @NotNull
    private Double harga;

}

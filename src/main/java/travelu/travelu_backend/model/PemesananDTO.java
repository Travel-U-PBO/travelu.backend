package travelu.travelu_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PemesananDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String namaCustomer;

    @NotNull
    @Size(max = 255)
    private String noTempatduduk;

    @NotNull
    private StatusPembayaran statusPembayaran;

    @NotNull
    private Long pelangganId;

    private Long pembayaranId;

    @Size(max = 255)
    private String noInvoice;

    private List<Long> listDiskon;

    @NotNull
    private Long jadwalId;

}

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
    private StatusPembayaran statusPembayaran;

    @Size(max = 255)
    private String panggilan;

    @Size(max = 255)
    private String noTelp;

    @Size(max = 255)
    private String alamat;

    @NotNull
    private Long pelangganId;

    private Long pembayaranId;

    private List<Long> listDiskon;

    @NotNull
    private Long jadwalId;

}

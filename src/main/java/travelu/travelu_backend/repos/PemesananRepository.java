package travelu.travelu_backend.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import travelu.travelu_backend.domain.Diskon;
import travelu.travelu_backend.domain.Jadwal;
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pembayaran;
import travelu.travelu_backend.domain.Pemesanan;


public interface PemesananRepository extends JpaRepository<Pemesanan, Long> {

    Pemesanan findFirstByPelangganId(Pelanggan pelanggan);

    Pemesanan findFirstByPembayaranId(Pembayaran pembayaran);

    Pemesanan findFirstByListDiskon(Diskon diskon);

    Pemesanan findFirstByJadwalId(Jadwal jadwal);

    List<Pemesanan> findAllByListDiskon(Diskon diskon);

}

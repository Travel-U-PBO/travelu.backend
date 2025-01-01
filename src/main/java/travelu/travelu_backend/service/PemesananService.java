package travelu.travelu_backend.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.Csticket;
import travelu.travelu_backend.domain.Diskon;
import travelu.travelu_backend.domain.Jadwal;
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pembayaran;
import travelu.travelu_backend.domain.Pemesanan;
import travelu.travelu_backend.model.PemesananDTO;
import travelu.travelu_backend.repos.CsticketRepository;
import travelu.travelu_backend.repos.DiskonRepository;
import travelu.travelu_backend.repos.JadwalRepository;
import travelu.travelu_backend.repos.PelangganRepository;
import travelu.travelu_backend.repos.PembayaranRepository;
import travelu.travelu_backend.repos.PemesananRepository;
import travelu.travelu_backend.util.NotFoundException;
import travelu.travelu_backend.util.ReferencedWarning;


@Service
@Transactional
public class PemesananService {

    private final PemesananRepository pemesananRepository;
    private final PelangganRepository pelangganRepository;
    private final PembayaranRepository pembayaranRepository;
    private final DiskonRepository diskonRepository;
    private final JadwalRepository jadwalRepository;
    private final CsticketRepository csticketRepository;

    public PemesananService(final PemesananRepository pemesananRepository,
            final PelangganRepository pelangganRepository,
            final PembayaranRepository pembayaranRepository,
            final DiskonRepository diskonRepository, final JadwalRepository jadwalRepository,
            final CsticketRepository csticketRepository) {
        this.pemesananRepository = pemesananRepository;
        this.pelangganRepository = pelangganRepository;
        this.pembayaranRepository = pembayaranRepository;
        this.diskonRepository = diskonRepository;
        this.jadwalRepository = jadwalRepository;
        this.csticketRepository = csticketRepository;
    }

    public List<PemesananDTO> findAll() {
        final List<Pemesanan> pemesanans = pemesananRepository.findAll(Sort.by("id"));
        return pemesanans.stream()
                .map(pemesanan -> mapToDTO(pemesanan, new PemesananDTO()))
                .toList();
    }

    public PemesananDTO get(final Long id) {
        return pemesananRepository.findById(id)
                .map(pemesanan -> mapToDTO(pemesanan, new PemesananDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PemesananDTO pemesananDTO) {
        final Pemesanan pemesanan = new Pemesanan();
        mapToEntity(pemesananDTO, pemesanan);
        return pemesananRepository.save(pemesanan).getId();
    }

    public void update(final Long id, final PemesananDTO pemesananDTO) {
        final Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pemesananDTO, pemesanan);
        pemesananRepository.save(pemesanan);
    }

    public void delete(final Long id) {
        pemesananRepository.deleteById(id);
    }

    private PemesananDTO mapToDTO(final Pemesanan pemesanan, final PemesananDTO pemesananDTO) {
        pemesananDTO.setId(pemesanan.getId());
        pemesananDTO.setNamaCustomer(pemesanan.getNamaCustomer());
        pemesananDTO.setPanggilan(pemesanan.getPanggilan());
        pemesananDTO.setNoTelp(pemesanan.getNoTelp());
        pemesananDTO.setAlamat(pemesanan.getAlamat());
        pemesananDTO.setPelangganId(pemesanan.getPelangganId() == null ? null : pemesanan.getPelangganId().getId());
        pemesananDTO.setPembayaranId(pemesanan.getPembayaranId() == null ? null : pemesanan.getPembayaranId().getId());
        pemesananDTO.setListDiskon(pemesanan.getListDiskon().stream()
                .map(diskon -> diskon.getId())
                .toList());
        pemesananDTO.setJadwalId(pemesanan.getJadwalId() == null ? null : pemesanan.getJadwalId().getId());
        return pemesananDTO;
    }

    private Pemesanan mapToEntity(final PemesananDTO pemesananDTO, final Pemesanan pemesanan) {
        pemesanan.setNamaCustomer(pemesananDTO.getNamaCustomer());
        pemesanan.setPanggilan(pemesananDTO.getPanggilan());
        pemesanan.setNoTelp(pemesananDTO.getNoTelp());
        pemesanan.setAlamat(pemesananDTO.getAlamat());
        final Pelanggan pelangganId = pemesananDTO.getPelangganId() == null ? null : pelangganRepository.findById(pemesananDTO.getPelangganId())
                .orElseThrow(() -> new NotFoundException("pelangganId not found"));
        pemesanan.setPelangganId(pelangganId);
        final Pembayaran pembayaranId = pemesananDTO.getPembayaranId() == null ? null : pembayaranRepository.findById(pemesananDTO.getPembayaranId())
                .orElseThrow(() -> new NotFoundException("pembayaranId not found"));
        pemesanan.setPembayaranId(pembayaranId);
        final List<Diskon> listDiskon = diskonRepository.findAllById(
                pemesananDTO.getListDiskon() == null ? Collections.emptyList() : pemesananDTO.getListDiskon());
        if (listDiskon.size() != (pemesananDTO.getListDiskon() == null ? 0 : pemesananDTO.getListDiskon().size())) {
            throw new NotFoundException("one of listDiskon not found");
        }
        pemesanan.setListDiskon(new HashSet<>(listDiskon));
        final Jadwal jadwalId = pemesananDTO.getJadwalId() == null ? null : jadwalRepository.findById(pemesananDTO.getJadwalId())
                .orElseThrow(() -> new NotFoundException("jadwalId not found"));
        pemesanan.setJadwalId(jadwalId);
        return pemesanan;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pemesanan pemesanan = pemesananRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Csticket pemesananIdCsticket = csticketRepository.findFirstByPemesananId(pemesanan);
        if (pemesananIdCsticket != null) {
            referencedWarning.setKey("pemesanan.csticket.pemesananId.referenced");
            referencedWarning.addParam(pemesananIdCsticket.getId());
            return referencedWarning;
        }
        return null;
    }

}

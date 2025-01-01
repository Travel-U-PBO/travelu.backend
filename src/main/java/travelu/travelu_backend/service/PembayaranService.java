package travelu.travelu_backend.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.Pembayaran;
import travelu.travelu_backend.domain.Pemesanan;
import travelu.travelu_backend.model.PembayaranDTO;
import travelu.travelu_backend.repos.PembayaranRepository;
import travelu.travelu_backend.repos.PemesananRepository;
import travelu.travelu_backend.util.NotFoundException;
import travelu.travelu_backend.util.ReferencedWarning;


@Service
public class PembayaranService {

    private final PembayaranRepository pembayaranRepository;
    private final PemesananRepository pemesananRepository;

    public PembayaranService(final PembayaranRepository pembayaranRepository,
            final PemesananRepository pemesananRepository) {
        this.pembayaranRepository = pembayaranRepository;
        this.pemesananRepository = pemesananRepository;
    }

    public List<PembayaranDTO> findAll() {
        final List<Pembayaran> pembayarans = pembayaranRepository.findAll(Sort.by("id"));
        return pembayarans.stream()
                .map(pembayaran -> mapToDTO(pembayaran, new PembayaranDTO()))
                .toList();
    }

    public PembayaranDTO get(final Long id) {
        return pembayaranRepository.findById(id)
                .map(pembayaran -> mapToDTO(pembayaran, new PembayaranDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PembayaranDTO pembayaranDTO) {
        final Pembayaran pembayaran = new Pembayaran();
        mapToEntity(pembayaranDTO, pembayaran);
        return pembayaranRepository.save(pembayaran).getId();
    }

    public void update(final Long id, final PembayaranDTO pembayaranDTO) {
        final Pembayaran pembayaran = pembayaranRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pembayaranDTO, pembayaran);
        pembayaranRepository.save(pembayaran);
    }

    public void delete(final Long id) {
        pembayaranRepository.deleteById(id);
    }

    private PembayaranDTO mapToDTO(final Pembayaran pembayaran, final PembayaranDTO pembayaranDTO) {
        pembayaranDTO.setId(pembayaran.getId());
        pembayaranDTO.setMetode(pembayaran.getMetode());
        pembayaranDTO.setHarga(pembayaran.getHarga());
        pembayaranDTO.setNoInvoice(pembayaran.getNoInvoice());
        pembayaranDTO.setName(pembayaran.getName());
        pembayaranDTO.setNoTelp(pembayaran.getNoTelp());
        pembayaranDTO.setEmail(pembayaran.getEmail());
        pembayaranDTO.setAddress(pembayaran.getAddress());
        pembayaranDTO.setInvoice(pembayaran.getInvoice());
        pembayaranDTO.setVAnumber(pembayaran.getVAnumber());
        pembayaranDTO.setExpiredDate(pembayaran.getExpiredDate());
        pembayaranDTO.setStatus(pembayaran.getStatus());
        return pembayaranDTO;
    }

    private Pembayaran mapToEntity(final PembayaranDTO pembayaranDTO, final Pembayaran pembayaran) {
        pembayaran.setMetode(pembayaranDTO.getMetode());
        pembayaran.setHarga(pembayaranDTO.getHarga());
        pembayaran.setNoInvoice(pembayaranDTO.getNoInvoice());
        pembayaran.setName(pembayaranDTO.getName());
        pembayaran.setNoTelp(pembayaranDTO.getNoTelp());
        pembayaran.setEmail(pembayaranDTO.getEmail());
        pembayaran.setAddress(pembayaranDTO.getAddress());
        pembayaran.setInvoice(pembayaranDTO.getInvoice());
        pembayaran.setVAnumber(pembayaranDTO.getVAnumber());
        pembayaran.setExpiredDate(pembayaranDTO.getExpiredDate());
        pembayaran.setStatus(pembayaranDTO.getStatus());
        return pembayaran;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pembayaran pembayaran = pembayaranRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pemesanan pembayaranIdPemesanan = pemesananRepository.findFirstByPembayaranId(pembayaran);
        if (pembayaranIdPemesanan != null) {
            referencedWarning.setKey("pembayaran.pemesanan.pembayaranId.referenced");
            referencedWarning.addParam(pembayaranIdPemesanan.getId());
            return referencedWarning;
        }
        return null;
    }

}

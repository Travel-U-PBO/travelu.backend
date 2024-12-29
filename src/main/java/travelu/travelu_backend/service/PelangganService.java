package travelu.travelu_backend.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.Csticket;
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pemesanan;
import travelu.travelu_backend.model.PelangganDTO;
import travelu.travelu_backend.repos.CsticketRepository;
import travelu.travelu_backend.repos.PelangganRepository;
import travelu.travelu_backend.repos.PemesananRepository;
import travelu.travelu_backend.util.NotFoundException;
import travelu.travelu_backend.util.ReferencedWarning;


@Service
public class PelangganService {

    private final PelangganRepository pelangganRepository;
    private final PemesananRepository pemesananRepository;
    private final CsticketRepository csticketRepository;

    public PelangganService(final PelangganRepository pelangganRepository,
            final PemesananRepository pemesananRepository,
            final CsticketRepository csticketRepository) {
        this.pelangganRepository = pelangganRepository;
        this.pemesananRepository = pemesananRepository;
        this.csticketRepository = csticketRepository;
    }

    public List<PelangganDTO> findAll() {
        final List<Pelanggan> pelanggans = pelangganRepository.findAll(Sort.by("id"));
        return pelanggans.stream()
                .map(pelanggan -> mapToDTO(pelanggan, new PelangganDTO()))
                .toList();
    }

    public PelangganDTO get(final Long id) {
        return pelangganRepository.findById(id)
                .map(pelanggan -> mapToDTO(pelanggan, new PelangganDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PelangganDTO pelangganDTO) {
        final Pelanggan pelanggan = new Pelanggan();
        mapToEntity(pelangganDTO, pelanggan);
        return pelangganRepository.save(pelanggan).getId();
    }

    public void update(final Long id, final PelangganDTO pelangganDTO) {
        final Pelanggan pelanggan = pelangganRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pelangganDTO, pelanggan);
        pelangganRepository.save(pelanggan);
    }

    public void delete(final Long id) {
        pelangganRepository.deleteById(id);
    }

    private PelangganDTO mapToDTO(final Pelanggan pelanggan, final PelangganDTO pelangganDTO) {
        pelangganDTO.setId(pelanggan.getId());
        pelangganDTO.setName(pelanggan.getName());
        pelangganDTO.setEmail(pelanggan.getEmail());
        pelangganDTO.setPassword(pelanggan.getPassword());
        pelangganDTO.setRole(pelanggan.getRole());
        pelangganDTO.setNoTelp(pelanggan.getNoTelp());
        pelangganDTO.setImg(pelanggan.getImg());
        return pelangganDTO;
    }

    private Pelanggan mapToEntity(final PelangganDTO pelangganDTO, final Pelanggan pelanggan) {
        pelanggan.setName(pelangganDTO.getName());
        pelanggan.setEmail(pelangganDTO.getEmail());
        pelanggan.setPassword(pelangganDTO.getPassword());
        pelanggan.setRole(pelangganDTO.getRole());
        pelanggan.setNoTelp(pelangganDTO.getNoTelp());
        pelanggan.setImg(pelangganDTO.getImg());
        return pelanggan;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pelanggan pelanggan = pelangganRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pemesanan pelangganIdPemesanan = pemesananRepository.findFirstByPelangganId(pelanggan);
        if (pelangganIdPemesanan != null) {
            referencedWarning.setKey("pelanggan.pemesanan.pelangganId.referenced");
            referencedWarning.addParam(pelangganIdPemesanan.getId());
            return referencedWarning;
        }
        final Csticket pelangganIdCsticket = csticketRepository.findFirstByPelangganId(pelanggan);
        if (pelangganIdCsticket != null) {
            referencedWarning.setKey("pelanggan.csticket.pelangganId.referenced");
            referencedWarning.addParam(pelangganIdCsticket.getId());
            return referencedWarning;
        }
        return null;
    }

}

package travelu.travelu_backend.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.Cabang;
import travelu.travelu_backend.domain.DestinasiItinerari;
import travelu.travelu_backend.domain.Jadwal;
import travelu.travelu_backend.domain.Kota;
import travelu.travelu_backend.model.CabangDTO;
import travelu.travelu_backend.repos.CabangRepository;
import travelu.travelu_backend.repos.DestinasiItinerariRepository;
import travelu.travelu_backend.repos.JadwalRepository;
import travelu.travelu_backend.repos.KotaRepository;
import travelu.travelu_backend.util.NotFoundException;
import travelu.travelu_backend.util.ReferencedWarning;


@Service
public class CabangService {

    private final CabangRepository cabangRepository;
    private final KotaRepository kotaRepository;
    private final JadwalRepository jadwalRepository;
    private final DestinasiItinerariRepository destinasiItinerariRepository;

    public CabangService(final CabangRepository cabangRepository,
            final KotaRepository kotaRepository, final JadwalRepository jadwalRepository,
            final DestinasiItinerariRepository destinasiItinerariRepository) {
        this.cabangRepository = cabangRepository;
        this.kotaRepository = kotaRepository;
        this.jadwalRepository = jadwalRepository;
        this.destinasiItinerariRepository = destinasiItinerariRepository;
    }

    public List<CabangDTO> findAll() {
        final List<Cabang> cabangs = cabangRepository.findAll(Sort.by("id"));
        return cabangs.stream()
                .map(cabang -> mapToDTO(cabang, new CabangDTO()))
                .toList();
    }

    public CabangDTO get(final Long id) {
        return cabangRepository.findById(id)
                .map(cabang -> mapToDTO(cabang, new CabangDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CabangDTO cabangDTO) {
        final Cabang cabang = new Cabang();
        mapToEntity(cabangDTO, cabang);
        return cabangRepository.save(cabang).getId();
    }

    public void update(final Long id, final CabangDTO cabangDTO) {
        final Cabang cabang = cabangRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cabangDTO, cabang);
        cabangRepository.save(cabang);
    }

    public void delete(final Long id) {
        cabangRepository.deleteById(id);
    }

    private CabangDTO mapToDTO(final Cabang cabang, final CabangDTO cabangDTO) {
        cabangDTO.setId(cabang.getId());
        cabangDTO.setName(cabang.getName());
        cabangDTO.setKotaId(cabang.getKotaId() == null ? null : cabang.getKotaId().getId());
        return cabangDTO;
    }

    private Cabang mapToEntity(final CabangDTO cabangDTO, final Cabang cabang) {
        cabang.setName(cabangDTO.getName());
        final Kota kotaId = cabangDTO.getKotaId() == null ? null : kotaRepository.findById(cabangDTO.getKotaId())
                .orElseThrow(() -> new NotFoundException("kotaId not found"));
        cabang.setKotaId(kotaId);
        return cabang;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Cabang cabang = cabangRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Jadwal asalCabangIdJadwal = jadwalRepository.findFirstByAsalCabangId(cabang);
        if (asalCabangIdJadwal != null) {
            referencedWarning.setKey("cabang.jadwal.asalCabangId.referenced");
            referencedWarning.addParam(asalCabangIdJadwal.getId());
            return referencedWarning;
        }
        final Jadwal destinasiCabangIdJadwal = jadwalRepository.findFirstByDestinasiCabangId(cabang);
        if (destinasiCabangIdJadwal != null) {
            referencedWarning.setKey("cabang.jadwal.destinasiCabangId.referenced");
            referencedWarning.addParam(destinasiCabangIdJadwal.getId());
            return referencedWarning;
        }
        final DestinasiItinerari cabangIdDestinasiItinerari = destinasiItinerariRepository.findFirstByCabangId(cabang);
        if (cabangIdDestinasiItinerari != null) {
            referencedWarning.setKey("cabang.destinasiItinerari.cabangId.referenced");
            referencedWarning.addParam(cabangIdDestinasiItinerari.getId());
            return referencedWarning;
        }
        return null;
    }

}

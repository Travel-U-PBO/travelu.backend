package travelu.travelu_backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import travelu.travelu_backend.domain.Kota;
import travelu.travelu_backend.model.CabangDTO;
import travelu.travelu_backend.repos.KotaRepository;
import travelu.travelu_backend.service.CabangService;
import travelu.travelu_backend.util.CustomCollectors;
import travelu.travelu_backend.util.ReferencedWarning;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/cabangs")
public class CabangController {

    private final CabangService cabangService;
    private final KotaRepository kotaRepository;

    public CabangController(final CabangService cabangService,
            final KotaRepository kotaRepository) {
        this.cabangService = cabangService;
        this.kotaRepository = kotaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("kotaIdValues", kotaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Kota::getId, Kota::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cabangs", cabangService.findAll());
        return "cabang/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cabang") final CabangDTO cabangDTO) {
        return "cabang/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cabang") @Valid final CabangDTO cabangDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cabang/add";
        }
        cabangService.create(cabangDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cabang.create.success"));
        return "redirect:/cabangs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("cabang", cabangService.get(id));
        return "cabang/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("cabang") @Valid final CabangDTO cabangDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "cabang/edit";
        }
        cabangService.update(id, cabangDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cabang.update.success"));
        return "redirect:/cabangs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = cabangService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            cabangService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cabang.delete.success"));
        }
        return "redirect:/cabangs";
    }

}

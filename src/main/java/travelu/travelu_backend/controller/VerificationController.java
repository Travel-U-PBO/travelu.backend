package travelu.travelu_backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import travelu.travelu_backend.model.KotaDTO;
import travelu.travelu_backend.service.KotaService;
import travelu.travelu_backend.service.PelangganService;
import travelu.travelu_backend.service.VerificationService;
import travelu.travelu_backend.util.ReferencedWarning;
import travelu.travelu_backend.util.WebUtils;

import java.io.IOException;

@RestController
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private PelangganService pelangganService;
//
//    @GetMapping("/verify-email/{email}/{code}")
//    public ResponseEntity<String> verifyEmail(@PathVariable String email, @PathVariable String code, HttpServletResponse response) throws IOException {
//        if (verificationService.verifyCode(email, code)) {
//            pelangganService.updateStatus(email, "VERIFIED");
//            response.sendRedirect("http://localhost:9000/verified");
//            return ResponseEntity.ok("Email verified successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
//        }
//    }
}

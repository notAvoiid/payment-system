package com.abreu.payment_system.controller;

import com.abreu.payment_system.model.dto.pix.PixChargeRequestDTO;
import com.abreu.payment_system.service.PixService;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pix")
public class PixController {

    private final PixService pixService;

    public PixController(PixService pixService) {
        this.pixService = pixService;
    }

    @GetMapping("/{txid}")
    public ResponseEntity pixDetailCharge(@PathVariable String txid) {

        JSONObject response = this.pixService.pixDetailCharge(txid);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @GetMapping("/list")
    public ResponseEntity pixListCharges() {

        List<JSONObject> response = this.pixService.pixListCharges();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @GetMapping
    public ResponseEntity pixCreateEVP() {

        JSONObject response = this.pixService.pixCreateEVP();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

    @PostMapping
    public ResponseEntity pixCreateCharge(@RequestBody PixChargeRequestDTO pixChargeRequest) {
        JSONObject response = this.pixService.pixCreateCharge(pixChargeRequest);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }
}

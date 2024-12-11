package com.example.GateInGateOut.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.GateInGateOut.Entity.GateOut;
import com.example.GateInGateOut.Service.ServiceGateOut;

@RestController
@RequestMapping("/gateout")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class GateOutController {

    @Autowired
    private ServiceGateOut serviceGateOut;

    // Endpoint to get all GateOut records
    @GetMapping("/")
    public List<GateOut> findAll(GateOut gateOut) {
        return serviceGateOut.findAll(gateOut);
    }

    // Get a single GateOut record by ID
    @GetMapping("/{gateout}")
    public GateOut getSingleData(@PathVariable("gateout") long gateout) {
        return serviceGateOut.findById(gateout);
    }

    // Endpoint to insert a new GateOut record
    @PostMapping("/")
    public GateOut insertData(@RequestBody GateOut gateOut) {
        return serviceGateOut.saveGateOut(gateOut);
    }

    // Update the data without specifying an ID
    @PutMapping("/")
    public GateOut updateData(@RequestBody GateOut gateOut) {
        return serviceGateOut.updateGateOut(gateOut);
    }

    // Update the data by ID
    @PutMapping("/{gateout}")
    public GateOut updateSingleData(@PathVariable("gateout") long gateout, @RequestBody GateOut gateOut) {
        GateOut existingGateOut = serviceGateOut.findById(gateout);
        existingGateOut.setEnteredby(gateOut.getEnteredby());
        existingGateOut.setEntrydate(gateOut.getEntrydate());
        existingGateOut.setEntrytime(gateOut.getEntrytime());
        existingGateOut.setInvoice(gateOut.getInvoice());
        existingGateOut.setInvoicedate(gateOut.getInvoicedate());
        existingGateOut.setPlant(gateOut.getPlant());
        existingGateOut.setPotype(gateOut.getPotype());
        existingGateOut.setPurchaseorder(gateOut.getPurchaseorder());
        existingGateOut.setRemarks(gateOut.getRemarks());
        existingGateOut.setSuppliercode(gateOut.getSuppliercode());
        existingGateOut.setSuppliername(gateOut.getSuppliername());
        existingGateOut.setVehiclenumber(gateOut.getVehiclenumber());
        existingGateOut.setVehicletime(gateOut.getVehicletime());
        return serviceGateOut.saveGateOut(existingGateOut);
    }

    // Delete a single GateOut record by ID
    @DeleteMapping("/{gateout}")
    public void deleteSingleData(@PathVariable("gateout") long gateout) {
        GateOut gateOutToDelete = serviceGateOut.findById(gateout);
        serviceGateOut.deleteGateOut(gateOutToDelete);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Replace this with your authentication logic
        if ("ProductMaster".equals(username) && "ProductMaster@1234567890".equals(password)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

package com.example.GateInGateOut.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.GateInGateOut.Entity.GateIn;
import com.example.GateInGateOut.Service.ServiceGateIn;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/gatein")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class GateInController {

    @Autowired
    private ServiceGateIn serviceGateIn;
    
    private static final String SAP_API_URL = "https://my418696-api.s4hana.cloud.sap/sap/opu/odata4/sap/api_purchaseorder_2/srvd_a2x/sap/purchaseorder/0001/PurchaseOrder";
    private static final String SAP_USERNAME = "ProductMaster";
    private static final String SAP_PASSWORD = "ProductMaster@1234567890";

    // Endpoint to get all GateIn records
    @GetMapping("/")
    public List<GateIn> findAll(GateIn gateIn) {
        return serviceGateIn.findAll(gateIn);
    }
    // get the single gatein record
    @GetMapping("/{gatein}")
    public GateIn getSingleData(@PathVariable("gatein") long gatein) {
        return serviceGateIn.findById(gatein);
    }

    // Endpoint to insert a new GateIn record
    @PostMapping("/")
    public GateIn insertData(@RequestBody GateIn gateIn) {
        return serviceGateIn.saveGateIn(gateIn);
    }
    
    // Update the data
    @PutMapping("/")
    public GateIn updateData(@RequestBody GateIn gateIn)
    {
    	return serviceGateIn.updateGateIn(gateIn);
    }
    
 // Update the data through id
    @PutMapping("/{gatein}")
    public GateIn updateSingleData(@PathVariable("gatein") long gatein, @RequestBody GateIn gateIn)
    {
    	GateIn id = serviceGateIn.findById(gatein);
    	id.setEnteredby(gateIn.getEnteredby());
    	id.setEntrydate(gateIn.getEntrydate());
    	id.setEntrytime(gateIn.getEntrytime());
    	id.setInvoice(gateIn.getInvoice());
    	id.setInvoicedate(gateIn.getInvoicedate());
    	id.setPlant(gateIn.getPlant());
    	id.setPotype(gateIn.getPotype());
    	id.setPurchaseorder(gateIn.getPurchaseorder());
    	id.setRemarks(gateIn.getRemarks());
    	id.setSuppliercode(gateIn.getSuppliercode());
    	id.setSuppliername(gateIn.getSuppliername());
    	id.setVehiclenumber(gateIn.getVehiclenumber());
    	id.setVehicletime(gateIn.getVehicletime());
    	GateIn allupdate = serviceGateIn.updateGateIn(id);
    	return allupdate;
    }
    
    
    // delete the row
    @DeleteMapping("/{gatein}")
    public void deleteSingleData(@PathVariable("gatein") long gatein)
    {
    	GateIn id = serviceGateIn.findById(gatein);
    	serviceGateIn.deleteGateIn(id);
    }
    
    // SAP API Integration: Get Purchase Orders
    @GetMapping("/PurchaseOrders")
    public ResponseEntity<String> getSAPData() {
        RestTemplate restTemplate = new RestTemplate();

        // Set headers with Basic Auth and CSRF Token (if required by SAP)
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SAP_USERNAME, SAP_PASSWORD); // Set Basic Auth for SAP
        headers.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the GET request to SAP API
        ResponseEntity<String> response = restTemplate.exchange(SAP_API_URL, HttpMethod.GET, entity, String.class);

        return response;
    }
   
} 

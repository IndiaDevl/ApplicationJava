package com.example.GateInGateOut.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    
    @GetMapping("/PurchaseOrders/{poNumber}")
    public ResponseEntity<String> getSpecificPurchaseOrder(@PathVariable String poNumber) {
        String url = SAP_API_URL + "/" + poNumber; // Append the PO number to the API URL
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        headers.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the GET request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response;
    }
    
    @DeleteMapping("/PurchaseOrders/{poNumber}")
    public ResponseEntity<String> deletePurchaseOrder(@PathVariable String poNumber) {
        String url = SAP_API_URL + "/" + poNumber;
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Fetch CSRF Token
        HttpHeaders fetchHeaders = new HttpHeaders();
        fetchHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        fetchHeaders.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> fetchEntity = new HttpEntity<>(fetchHeaders);
        ResponseEntity<String> fetchResponse;
        try {
            fetchResponse = restTemplate.exchange(url, HttpMethod.GET, fetchEntity, String.class);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch CSRF Token: PurchaseOrderNumber " + poNumber +", " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Extract CSRF Token and Cookies
        String csrfToken = fetchResponse.getHeaders().getFirst("X-CSRF-Token");
        if (csrfToken == null || csrfToken.isEmpty()) {
            return new ResponseEntity<>("CSRF Token not found in the response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders deleteHeaders = new HttpHeaders();
        deleteHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        deleteHeaders.set("X-CSRF-Token", csrfToken);
        deleteHeaders.put(HttpHeaders.COOKIE, fetchResponse.getHeaders().get(HttpHeaders.SET_COOKIE));

        // Step 2: Perform DELETE Operation
        HttpEntity<String> deleteEntity = new HttpEntity<>(deleteHeaders);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, deleteEntity, String.class);
            return new ResponseEntity<>("Purchase Order deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting Purchase Order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/PurchaseOrders")
    public ResponseEntity<String> createPurchaseOrder(@RequestBody String purchaseOrderPayload) {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Fetch CSRF Token
        HttpHeaders fetchHeaders = new HttpHeaders();
        fetchHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        fetchHeaders.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> fetchEntity = new HttpEntity<>(fetchHeaders);
        ResponseEntity<String> fetchResponse;
        try {
            fetchResponse = restTemplate.exchange(SAP_API_URL, HttpMethod.GET, fetchEntity, String.class);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch CSRF Token: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Extract CSRF Token and Cookies
        String csrfToken = fetchResponse.getHeaders().getFirst("X-CSRF-Token");
        if (csrfToken == null || csrfToken.isEmpty()) {
            return new ResponseEntity<>("CSRF Token not found in the response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        postHeaders.set("X-CSRF-Token", csrfToken);
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.put(HttpHeaders.COOKIE, fetchResponse.getHeaders().get(HttpHeaders.SET_COOKIE));

        // Step 2: Create PO (POST Request)
        HttpEntity<String> postEntity = new HttpEntity<>(purchaseOrderPayload, postHeaders);
        try {
            ResponseEntity<String> postResponse = restTemplate.exchange(SAP_API_URL, HttpMethod.POST, postEntity, String.class);
            return new ResponseEntity<>("Purchase Order created successfully. Response: " + postResponse.getBody(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating Purchase Order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
// updating purchase order header only
    @PutMapping("/PurchaseOrders/{poNumber}")
    public ResponseEntity<String> updatePurchaseOrder(@PathVariable String poNumber, @RequestBody String updatePayload) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SAP_API_URL + "/" + poNumber; // Update the specific Purchase Order URL

        // Step 1: Fetch CSRF Token
        HttpHeaders fetchHeaders = new HttpHeaders();
        fetchHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        fetchHeaders.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> fetchEntity = new HttpEntity<>(fetchHeaders);
        ResponseEntity<String> fetchResponse;
        try {
            fetchResponse = restTemplate.exchange(url, HttpMethod.GET, fetchEntity, String.class);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch CSRF Token: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Extract CSRF Token and Cookies
        String csrfToken = fetchResponse.getHeaders().getFirst("X-CSRF-Token");
        if (csrfToken == null || csrfToken.isEmpty()) {
            return new ResponseEntity<>("CSRF Token not found in the response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders putHeaders = new HttpHeaders();
        putHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        putHeaders.set("X-CSRF-Token", csrfToken);
        putHeaders.setContentType(MediaType.APPLICATION_JSON);
        putHeaders.put(HttpHeaders.COOKIE, fetchResponse.getHeaders().get(HttpHeaders.SET_COOKIE));

        // Step 2: Perform PUT Operation (without _PurchaseOrderItem)
        HttpEntity<String> putEntity = new HttpEntity<>(updatePayload, putHeaders);
        try {
            ResponseEntity<String> putResponse = restTemplate.exchange(url, HttpMethod.PUT, putEntity, String.class);
            return new ResponseEntity<>("Purchase Order updated successfully. Response: " + putResponse.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Purchase Order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // purchase order line item updating separately 
    
    @PutMapping("/PurchaseOrderItems/{poNumber}/{itemNumber}")
    public ResponseEntity<String> updatePurchaseOrderItem(
        @PathVariable String poNumber,
        @PathVariable String itemNumber,
        @RequestBody String itemPayload
    ) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SAP_API_URL + "/" + poNumber + "/_PurchaseOrderItem/" + itemNumber;

        // Fetch CSRF Token (similar to above)
        HttpHeaders fetchHeaders = new HttpHeaders();
        fetchHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        fetchHeaders.add("X-CSRF-Token", "Fetch");

        HttpEntity<String> fetchEntity = new HttpEntity<>(fetchHeaders);
        ResponseEntity<String> fetchResponse;
        try {
            fetchResponse = restTemplate.exchange(url, HttpMethod.GET, fetchEntity, String.class);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to fetch CSRF Token: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Extract CSRF Token and Cookies
        String csrfToken = fetchResponse.getHeaders().getFirst("X-CSRF-Token");
        if (csrfToken == null || csrfToken.isEmpty()) {
            return new ResponseEntity<>("CSRF Token not found in the response", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders putHeaders = new HttpHeaders();
        putHeaders.setBasicAuth(SAP_USERNAME, SAP_PASSWORD);
        putHeaders.set("X-CSRF-Token", csrfToken);
        putHeaders.setContentType(MediaType.APPLICATION_JSON);
        putHeaders.put(HttpHeaders.COOKIE, fetchResponse.getHeaders().get(HttpHeaders.SET_COOKIE));

        // Perform PUT Operation
        HttpEntity<String> putEntity = new HttpEntity<>(itemPayload, putHeaders);
        try {
            ResponseEntity<String> putResponse = restTemplate.exchange(url, HttpMethod.PUT, putEntity, String.class);
            return new ResponseEntity<>("Purchase Order Item updated successfully. Response: " + putResponse.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating Purchase Order Item: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


} 

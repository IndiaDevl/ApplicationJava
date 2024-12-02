package com.example.GateInGateOut.Entity;

import java.sql.Date;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class GateOut {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gateout_seq")
	@SequenceGenerator(name = "gateout_seq", sequenceName = "gateout_seq", allocationSize = 1, initialValue = 1000000001)
	private long gateout;

	@Temporal(TemporalType.DATE)
	private Date Entrydate;
	private LocalTime entrytime;
	private String plant;
	private String vehiclenumber;
	private LocalTime vehicletime;
	private String suppliername;
	private long suppliercode;
	private long purchaseorder;
	private char potype;
	private long invoice;
	private Date invoicedate;
	private String enteredby;
	private String remarks;
	public long getGateout() {
		return gateout;
	}
	public void setGateout(long gateout) {
		this.gateout = gateout;
	}
	public Date getEntrydate() {
		return Entrydate;
	}
	public void setEntrydate(Date entrydate) {
		Entrydate = entrydate;
	}
	public LocalTime getEntrytime() {
		return entrytime;
	}
	public void setEntrytime(LocalTime entrytime) {
		this.entrytime = entrytime;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getVehiclenumber() {
		return vehiclenumber;
	}
	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}
	public LocalTime getVehicletime() {
		return vehicletime;
	}
	public void setVehicletime(LocalTime vehicletime) {
		this.vehicletime = vehicletime;
	}
	public String getSuppliername() {
		return suppliername;
	}
	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}
	public long getSuppliercode() {
		return suppliercode;
	}
	public void setSuppliercode(long suppliercode) {
		this.suppliercode = suppliercode;
	}
	public long getPurchaseorder() {
		return purchaseorder;
	}
	public void setPurchaseorder(long purchaseorder) {
		this.purchaseorder = purchaseorder;
	}
	public char getPotype() {
		return potype;
	}
	public void setPotype(char potype) {
		this.potype = potype;
	}
	public long getInvoice() {
		return invoice;
	}
	public void setInvoice(long invoice) {
		this.invoice = invoice;
	}
	public Date getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getEnteredby() {
		return enteredby;
	}
	public void setEnteredby(String enteredby) {
		this.enteredby = enteredby;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}

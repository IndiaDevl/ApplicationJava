package com.example.GateInGateOut.Service;

import java.util.List;

import com.example.GateInGateOut.Entity.GateIn;

public interface ServiceGateIn  {

	public GateIn saveGateIn(GateIn gateIn);
	public GateIn updateGateIn(GateIn gateIn);
	public void deleteGateIn(GateIn gateIn);
	public GateIn findById(Long gatein);
	public List<GateIn> findAll(GateIn gateIn);

}

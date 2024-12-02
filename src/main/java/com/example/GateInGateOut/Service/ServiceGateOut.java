package com.example.GateInGateOut.Service;

import java.util.List;


import com.example.GateInGateOut.Entity.GateOut;

public interface ServiceGateOut {

	public GateOut saveGateOut(GateOut gateOut);
	public GateOut updateGateOut(GateOut gateOut);
	public void deleteGateOut(GateOut gateOut);
	public GateOut findById(Long gateout);
	public List<GateOut> findAll(GateOut gateOut);

}

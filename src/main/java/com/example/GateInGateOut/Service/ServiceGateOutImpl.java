package com.example.GateInGateOut.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GateInGateOut.Entity.GateOut;
import com.example.GateInGateOut.Repository.RepositoryGateOut;
@Service
public class ServiceGateOutImpl implements ServiceGateOut {

	@Autowired
	private RepositoryGateOut repositoryGateOut;
	@Override
	public GateOut saveGateOut(GateOut gateOut) {
		return repositoryGateOut.save(gateOut);
	}

	@Override
	public GateOut updateGateOut(GateOut gateOut) {
		return repositoryGateOut.save(gateOut);
	}

	@Override
	public void deleteGateOut(GateOut gateOut) {
		repositoryGateOut.delete(gateOut);

	}

	@Override
	public GateOut findById(Long gateout) {
		return repositoryGateOut.findById(gateout).get();
	}

	@Override
	public List<GateOut> findAll(GateOut gateOut) {
		return repositoryGateOut.findAll();
	}

}

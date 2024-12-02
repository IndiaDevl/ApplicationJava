package com.example.GateInGateOut.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GateInGateOut.Entity.GateIn;
import com.example.GateInGateOut.Repository.RepositoryGateIn;

@Service
public class ServiceGateInImpl implements ServiceGateIn {

	@Autowired
	private RepositoryGateIn repositoryGateIn;
	@Override
	public GateIn saveGateIn(GateIn gateIn) {
		return repositoryGateIn.save(gateIn);
	}

	@Override
	public GateIn updateGateIn(GateIn gateIn) {
		return repositoryGateIn.save(gateIn);
	}

	@Override
	public void deleteGateIn(GateIn gateIn) {
		repositoryGateIn.delete(gateIn);
	}

	@Override
	public GateIn findById(Long gatein) {
		return repositoryGateIn.findById(gatein).get();
	}

	@Override
	public List<GateIn> findAll(GateIn gateIn) {
		return repositoryGateIn.findAll();
	}

}

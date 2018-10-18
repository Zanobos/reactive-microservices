package it.zano.microservices.controller.soa;

import it.zano.microservices.util.JsonUtils;

import java.io.Serializable;

public class BaseSoaRequestPayload implements Serializable{

	private static final long serialVersionUID = 42L;

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}

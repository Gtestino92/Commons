package com.ontiveroapi.handlers;

public abstract class ApiReqHandler<T, Y> {

	protected abstract Y execute(T request);

	public Y runService(T body) {
		try {
			Y result = execute(body);
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
}

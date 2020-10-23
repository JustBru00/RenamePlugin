package com.gmail.justbru00.epic.rename.utils.v3;

public class XpResponse {

	private String errorMessage;
	private Integer xpAmount;
	private boolean transactionSuccess;
	
	public XpResponse(String _errorMessage, boolean _transactionSuccess, Integer _xpAmount) {
		super();
		transactionSuccess = _transactionSuccess;
		errorMessage = _errorMessage;
		xpAmount = _xpAmount;
	}
	
	public XpResponse() {
		super();
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Integer getXpAmount() {
		return xpAmount;
	}

	public boolean isTransactionSuccess() {
		return transactionSuccess;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setXpAmount(Integer xpAmount) {
		this.xpAmount = xpAmount;
	}

	public void setTransactionSuccess(boolean transactionSuccess) {
		this.transactionSuccess = transactionSuccess;
	}

}

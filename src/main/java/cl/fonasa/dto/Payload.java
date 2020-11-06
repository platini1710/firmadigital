package cl.fonasa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payload {
private String run;
private String entity;
private String purpose;
private String expiration;
private String keySecret;

public Payload (String run, String entity, String purpose,  String expiration,String keySecret) {
	this.run=run;
	this.entity=entity;	
	this.purpose=purpose;		
	this.expiration=expiration;	
	this.keySecret=keySecret;
}
public String getRun() {
	
	return run;
}
public void setRun(String run) {
	this.run = run;
}
public String getEntity() {
	return entity;
}
public void setEntity(String entity) {
	this.entity = entity;
}
public String getPurpose() {
	return purpose;
}
public void setPurpose(String purpose) {
	this.purpose = purpose;
}
public String getExpiration() {
	return expiration;
}
public void setExpiration(String expiration) {
	this.expiration = expiration;
}
public String getKeySecret() {
	return keySecret;
}
public void setKeySecret(String keySecret) {
	this.keySecret = keySecret;
}



}

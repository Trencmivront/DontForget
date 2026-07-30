package main.java.inter;

import org.springframework.http.ResponseEntity;
// Interface for PUT (UPDATE) and DELETE services
public interface Command <I>{
//	Return a reasonable respond.
	public ResponseEntity<String> execute(I i);
}
package main.io.github.trencmivront.dontforget.inter;

import org.springframework.http.ResponseEntity;
// Interface for PUT (UPDATE) and DELETE services
public interface Command <I>{
//	Return a reasonable respond.
	public ResponseEntity<String> execute(I i);
}
package main.io.github.trencmivront.dontforget.inter;

import org.springframework.http.ResponseEntity;
// Interface for GET services
public interface Query <I, O>{
//	Return a list of objects or a single object
	public ResponseEntity<O> execute(I i);
}

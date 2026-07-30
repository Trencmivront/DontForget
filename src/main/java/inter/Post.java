package main.java.inter;

import org.springframework.http.ResponseEntity;
// Interface for POST (CREATE) services
public interface Post <I>{
//	return the id of the created object
	public ResponseEntity<Long> execute(I i);
}

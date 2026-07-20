package main.java.api;

public class HeadUrl {

	private final String head = "http://localhost:8080";
	
	public String getHead(){
		return head;
	}
	
	public String generateApi(String end) {
		return head + end;
	}
	
	public String  generateApi(String end1, Long end2) {
		return head + end1 + end2;
	}
	
}

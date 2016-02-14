package Model;

public class PersonVM {

	public String id;
	public String name;
	public Long mobileNumber;
	public String location;
	public String emailID;
	public String status;
	public String accessLevel;
	public String type;
	
	public PersonVM(){
		
	}
	
	public PersonVM(String string, String name2, Long mobileNumber2, String location2, String emailID, String status, String accessLevel, String type) {
		this.id = string;
		this.name = name2;
		this.mobileNumber = mobileNumber2;
		this.location = location2;
		this.emailID = emailID;
		this.accessLevel = accessLevel;
		this.status = status;
		this.type = type;
	}
	
	public PersonVM(String string, String name2, Long mobileNumber2, String location2, String emailID, String type) {
		this.id = string;
		this.name = name2;
		this.mobileNumber = mobileNumber2;
		this.location = location2;
		this.emailID = emailID;
		this.type = type;
	}

	
}

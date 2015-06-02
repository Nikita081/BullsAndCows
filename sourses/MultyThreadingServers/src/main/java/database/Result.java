package database;

import javax.persistence.*;

@Entity
@Table(name = "bullsandcows.Results")

public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idResults")
	private int idResults;
	
	@Column(name = "idUser",unique = true)
	private int idUser;
	
	@Column(name = "numberAttempts")
	private int numberAttempts;
	
	@Column(name = "winOrLose")
	private int winOrLose;
	
	public Result() {

	}
	
	public Result(int idUser, int numberAttempts, int winOrLose) {
		this.idUser = idUser;
		this.numberAttempts = numberAttempts;
		this.winOrLose = winOrLose;
	}
	
	public int getIdResults(){
		return idResults;
	}
	
	public int getIdUser(){
		return this.idUser;
	}
	
	public int getNumberAttempts(){
		return this.numberAttempts;
	}
	
	public int getWinOrLose(){
		return this.winOrLose;
	}
	
	public void setIdUser(int idUser){
		this.idUser = idUser;
	}
	
	public void setNumberAttempts(int attempts){
		this.numberAttempts = attempts;
	}
	
	public void setWinOrLose(int winLose){
		this.winOrLose = winLose;
	}
	
	public void setIdResults(int id){
		this.idResults= id;
	}
}

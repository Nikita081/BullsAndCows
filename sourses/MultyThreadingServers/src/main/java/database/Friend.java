package database;

import javax.persistence.*;

@Entity
@Table(name = "bullsandcows.Friends")

public class Friend {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idFriendship")
	private int idFriendship;
	
	@Column(name = "idFirst",unique = true)
	private int idFirst;
	
	@Column(name = "idSecond",unique = true)
	private int idSecond;
	
	public Friend() {

	}
	
	public Friend(int idFirst, int idSecond) {
		this.idFirst = idFirst;
		this.idSecond = idSecond;
	}


	public int getIdFriendship() {
		return idFriendship;
	}

	public int getIdFirst() {
		return this.idFirst;
	}

	public int getIdSecond() {
		return this.idSecond;
	}

	public void setIdFirst(int token){
		this.idFirst = token;
	}
	public void setIdSecond(int token){
		this.idSecond = token;
	}
	
	public void setIdFriendship(int id) {
		this.idFriendship = id;
	}
}


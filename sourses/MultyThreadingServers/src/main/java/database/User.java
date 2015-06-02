package database;

import javax.persistence.*;

@Entity
@Table(name = "bullsandcows.Users")

public class User {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "idUser")
		private int idUser;
		
		@Column(name = "userToken",unique = true)
		private int userToken;
		
		@Column(name = "userNik")
		private String userNik;
		
		public User() {

		}
		
		public User(int userToken, String userNik) {
			this.userToken = userToken;
			this.userNik = userNik;
		}


		public int getIdUser() {
			return idUser;
		}

		public int getUserToken() {
			return this.userToken;
		}

		public String getUserNik() {
			return this.userNik;
		}

		public void setUserToken(int token){
			this.userToken = token;
		}
		public void setUserNik(String nik){
			this.userNik = nik;
		}
		
		public void setIdUser(int id) {
			this.idUser = id;
		}
}

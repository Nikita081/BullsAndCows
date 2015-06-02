package Query;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import database.Friend;
import database.Result;
import database.User;

public class Query {

	public int addUser(User user) {
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(user);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM USERS");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int updateUser(User user) {
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(user);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public User getUser(int idUser) {
		User user;
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			user = (User) session.load(User.class, idUser);
			Hibernate.initialize(user.getUserToken());
		} catch (Exception e) {
			session.getTransaction().rollback();
			return null;
		}
		session.getTransaction().commit();
		return user;
	}

	@SuppressWarnings("unchecked")
	public int getUserByToken(int token) {
		int id = 0;
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			List<User> list = session.createQuery("from User").list();
			for (User a : list) {
				Hibernate.initialize(a.getUserToken());
				if (a.getUserToken() == token) {
					Hibernate.initialize(a.getIdUser());
					id = a.getIdUser();
					break;
				}
			}
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
 			return -1;
		}
		session.getTransaction().commit();

		return id;
	}

	public int addFriend(Friend friends) {
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(friends);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM FRIENDS");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	public int updateFriend(Friend friends) {
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(friends);
		} catch (Exception e) {
			session.getTransaction().rollback();
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<User> getFriendsOf(int id) {
		User user = getUser(id);
		if (user == null)
			return null;
		else {
			List<Friend> in;
			ArrayList<User> result = new ArrayList<User>();
			Session session = InitSession.getSessionFactory()
					.getCurrentSession();
			session.beginTransaction();
			try {
				in = session.createQuery("from Friend").list();
				for (Friend fr : in) {
					Hibernate.initialize(fr.getIdFirst());

					if (fr.getIdFirst() == user.getIdUser()) {
						User u = (User) session.load(User.class,
								fr.getIdSecond());
						Hibernate.initialize(u.getUserToken());
						result.add(u);
					}

					if (fr.getIdSecond() == user.getIdUser()) {
						User u = (User) session.load(User.class,
								fr.getIdFirst());
						Hibernate.initialize(u.getUserToken());
						result.add(u);
					}
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				return null;
			}
			session.getTransaction().commit();
			return result;

		}
	}
	
	public int addResult(Result result){
		Session session = InitSession.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.save(result);
		} catch (Exception e) {
			session.getTransaction().rollback();
			System.out.println("HELLO I AM A PROBLEM FROM RESULT");
			return -1;
		}
		session.getTransaction().commit();
		return 0;
	}

	@SuppressWarnings("unchecked")
	public int getNumberGamesOfUser(int id){
		User user = getUser(id);
		if(user==null){
			return -1;
		}
		else{
				List<Result> results;
				int numberGames = 0;
				
				Session session = InitSession.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				try{
					results = session.createQuery("from Result").list();
					for(Result res : results){
					
						Hibernate.initialize(res.getIdUser());
						if(res.getIdUser()==user.getIdUser())
							numberGames++;
					}
					
				} catch (Exception e) {
					session.getTransaction().rollback();
					return 1;
				}
				session.getTransaction().commit();
				return numberGames;
		}
	}
	
	@SuppressWarnings("unchecked")
	public int getNumberWinsOfUser(int id){
		User user = getUser(id);
		if(user==null){
			return -1;
		}
		else{
				List<Result> results;
				int numberWins = 0;
				
				Session session = InitSession.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				try{
					results = session.createQuery("from Result").list();
					for(Result res : results){
					
						Hibernate.initialize(res.getIdUser());
						Hibernate.initialize(res.getWinOrLose());
						
						if((res.getIdUser()==user.getIdUser())&& res.getWinOrLose()==1)
							numberWins++;
					}
					
				} catch (Exception e) {
					session.getTransaction().rollback();
					return 1;
				}
				session.getTransaction().commit();
				return numberWins;
		}
	}
	@SuppressWarnings("unchecked")
	public int getNumberDrawOfUser(int id){
		User user = getUser(id);
		if(user==null){
			return -1;
		}
		else{
				List<Result> results;
				int numberDraws = 0;
				
				Session session = InitSession.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				try{
					results = session.createQuery("from Result").list();
					for(Result res : results){
					
						Hibernate.initialize(res.getIdUser());
						Hibernate.initialize(res.getWinOrLose());
						
						if((res.getIdUser()==user.getIdUser())&& res.getWinOrLose()==2)
							numberDraws++;
					}
					
				} catch (Exception e) {
					session.getTransaction().rollback();
					return 1;
				}
				session.getTransaction().commit();
				return numberDraws;
		}
	}
	
	@SuppressWarnings("unchecked")
	public int [] getAttemptsInfoOfUser(int id){
		int [] attemptsInfo = new int [3];
		
		User user = getUser(id);
		if(user==null){
			for(int i=0;i<3;i++){
				attemptsInfo[i] = 666;
			}
			return attemptsInfo;
		}
		else{
				List<Result> results;
				for(int i=0;i<3;i++){
					attemptsInfo[i] = 0;
				}
				Session session = InitSession.getSessionFactory().getCurrentSession();
				session.beginTransaction();
				
				try{
					results = session.createQuery("from Result").list();
					
					
					for(Result res : results){
					
						Hibernate.initialize(res.getIdUser());
						Hibernate.initialize(res.getNumberAttempts());
						
						if((res.getIdUser()==user.getIdUser())){
							if(attemptsInfo[1] != 0){
								if(res.getNumberAttempts()>attemptsInfo[1])
									attemptsInfo[1] = res.getNumberAttempts();
								System.out.println("max"+attemptsInfo[1]);
							}
							if(attemptsInfo[1] == 0){
								attemptsInfo[1] = res.getNumberAttempts();
								System.out.println("max!"+attemptsInfo[1]);
							}
							if(attemptsInfo[2] != 0){
								if(attemptsInfo[2]>res.getNumberAttempts())
									attemptsInfo[2] = res.getNumberAttempts();
								System.out.println("min"+attemptsInfo[2]);
							}
						
							if(attemptsInfo[2] == 0){
								attemptsInfo[2] = res.getNumberAttempts();
								System.out.println("min!"+attemptsInfo[2]);
							}
						
							attemptsInfo[0]+=res.getNumberAttempts();
					
						}
					}	
					
				} catch (Exception e) {
					System.out.println("attempts exeption:"+e);
					session.getTransaction().rollback();
					return null;
				}
				session.getTransaction().commit();
				return attemptsInfo;
		}
	}
}

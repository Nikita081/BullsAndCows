package com.fupm.skeb.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import database.Result;
import database.User;
import Query.Query;

public class ClientThread extends Thread {

	private static final String RIDDLE = "riddle";
	private static final String OWN_ATTEMPT = "own_attempt";
	private static final String ENEMY_ATTEMPT = "enemy_attempt";
	private static final String EQUALS = "=";
	private static final String DELIMETR = "@";
	private static final String END = "end";
	private static final String NEW_GAME = "newgame";
	private static final String STATISTIC = "statistic";
	private static final String CHAT = "chat";
	private static final String GAME = "game";
	private static final String ERROR = "error";
	private static final String WIN = "win";
	private static final String DRAW = "draw";
	private static final String LOSE = "lose";
	private static final String ATTEMPT = "attempt";
	private static final String ENEMY_TOKEN = "enemytoken";
	private static final String RENEW = "renew";
	private static final String OWN_RIDDLE = "ownriddle";
	private volatile int enemy_number_attempts;
	private int win_flag;
	private int port;
	private volatile int number_attempts;
	private int own_token = 0;
	private volatile int enemy_token = 0;
	private int max_clients;
	private volatile int own_riddle = 0;
	private volatile int enemy_riddle = 0;
	private int[] res_attempt = new int[2];
	private volatile boolean enemy_end_flag = false;
	private volatile boolean own_end_flag = false;
	private volatile boolean enemy_renew_flag = false;
	private boolean ready_to_play = true;
	private Socket clientSocket = null;
	private BufferedReader in = null;
	private PrintStream out = null;
	private final ClientThread[] threads;
	private final GameContent[] content;
	private ClientThread enemy_thread = null;
	private PrintStream enemy_output_stream = null;
	private String clientMessage;
	private volatile String logs_for_enemy = null;
	private Query new_query = new Query();

	public ClientThread(Socket clientSocket, ClientThread[] threads,
			GameContent[] content) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		this.content = content;
		max_clients = threads.length;
	}

	public void run() {

		try {
			/* open input stream */
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			/* open output stream */
			out = new PrintStream(clientSocket.getOutputStream());

			System.out.println("checking trololo" + port + "\n");

			int flag = waitNewGame();
			if (flag == -1) {
				return;
			}
			

			else if (flag == 0) {
				if (enemy_token == 0) {
					exchangeInformation();
				} else {
					System.out.println(this.getName()
							+ ": already searched enemy");
				}

				waitRiddle();

				if (enemy_riddle == 0) {
					exchangeRiddle();
				}
				out.println(ENEMY_TOKEN + EQUALS + enemy_token);
				out.println("find enemy");
				
				System.out.println(this.getName() + "exhcange riddele"
						+ enemy_riddle + " :: " + own_riddle);
			} else if(flag==2){
				System.out.println("flag is renew " + flag);
			}
			
			
			/* create game handler */
			BodyGame body = new BodyGame(enemy_riddle);

			/* now we have got all for starting our game. Let's start */

			while (true) {

				clientMessage = in.readLine();

				if (clientMessage != null && (enemy_riddle != 0)
						&& clientMessage.startsWith(GAME)) {

					System.out.println(this.getName() + ": got own_attempt");

					String own_attempt_array[] = clientMessage.split(EQUALS);

					if (!enemy_renew_flag && logs_for_enemy == null) {
						synchronized (this) {
							enemy_number_attempts++;
							enemy_output_stream.println(GAME + EQUALS
									+ ENEMY_ATTEMPT + EQUALS
									+ enemy_number_attempts + ". "
									+ own_attempt_array[2] + "\n");
						}
						System.out.println(this.getName()
								+ "we are in normal enemy log");
					}

					if (enemy_renew_flag) {
						enemy_number_attempts++;
						logs_for_enemy += GAME + EQUALS + ENEMY_ATTEMPT
								+ EQUALS + enemy_number_attempts + ". "
								+ own_attempt_array[2] + DELIMETR;
						System.out.println(this.getName()
								+ ": we are in save enemy log");
					}

					if (!enemy_renew_flag && logs_for_enemy != null) {
						String[] logs_array = logs_for_enemy.split(DELIMETR);
						for (String each : logs_array) {
							if (each.startsWith(GAME))
								enemy_output_stream.println(each + "\n");
						}
						logs_for_enemy = null;
						System.out.println(this.getName()
								+ ": we are in set enemy log");
					}

					res_attempt = body.countAll(Integer
							.parseInt(own_attempt_array[2]));

					if (res_attempt[0] == 4) {
						number_attempts++;
						own_end_flag = true;

						out.println(GAME + EQUALS + OWN_ATTEMPT + EQUALS
								+ number_attempts + ". " + own_attempt_array[2]
								+ " " + res_attempt[0] + " Bulls" + " 0 Cows");

						System.out.println(this.getName() + ": 4 bulls YESSSS");

						enemy_thread.enemy_end_flag = true;

						while (true) {
							if (enemy_end_flag && own_end_flag) {

								if (number_attempts > enemy_thread.number_attempts)
									win_flag = 0;
								else if (number_attempts < enemy_thread.number_attempts)
									win_flag = 1;
								else
									win_flag = 2;

								out.println(GAME + END);
								System.out.println(this.getName()
										+ ": we send end");
								break;
							}
						}

						break;

					} else {

						number_attempts++;

						out.println(GAME + EQUALS + OWN_ATTEMPT + EQUALS
								+ number_attempts + ". " + own_attempt_array[2]
								+ " " + res_attempt[0] + " Bulls; "
								+ res_attempt[1] + " Cows");
					}

				} else if (clientMessage != null && (enemy_riddle != 0)
						&& clientMessage.startsWith(CHAT)) {

					enemy_output_stream.println(clientMessage);

					System.out.println("chat mesage send to enemy::: "
							+ clientMessage);
				} else if (clientMessage != null && (enemy_riddle != 0)
						&& clientMessage.startsWith(RENEW)) {

					enemy_thread.enemy_renew_flag = true;

					synchronized (this) {
						for (int i = 0; i < content.length; i++) {
							if (content[i] == null) {
								content[i] = new GameContent(logs_for_enemy,
										enemy_renew_flag, own_token,
										number_attempts, own_riddle,
										enemy_riddle,enemy_number_attempts);
							}
						}
					}
					System.out.println(this.getName()
							+ ": win in save thread information to GameContent");

					freeThread();
					try {
						in.close();
						out.close();
						clientSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;

				}

			}

			/* write result to the database */
			Result new_result = new Result(new_query.getUserByToken(own_token),
					number_attempts, win_flag);
			new_query.addResult(new_result);

			freeThread();

		} catch (IOException e) {
			System.out.println(e);
		}
		try {
			in.close();
			out.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* waiting message about new game and add new user if need */
	private int waitNewGame() {
		while (true) {
			try {
				clientMessage = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (clientMessage.startsWith(NEW_GAME)) {
				String[] token_array = clientMessage.split(EQUALS);
				own_token = Integer.parseInt(token_array[1]);
				System.out.println(" cli message!!!!!" + clientMessage);
				User user = new User(own_token, " ");
				new_query.addUser(user);

				System.out.println(this.getName() + ": got token : "
						+ own_token);
				return 0;
			} else if (clientMessage.startsWith(STATISTIC)) {
				String[] token_array = clientMessage.split(EQUALS);
				System.out.println("token_array:" + clientMessage);
				int[] statistic = new int[5];
				statistic = getStatistic(Integer.parseInt(token_array[1]));
				if (statistic != null) {
					out.println(STATISTIC + "\n" + GAME + EQUALS + statistic[0]
							+ "\n" + WIN + EQUALS + statistic[1] + "\n"
							+ ATTEMPT + EQUALS + statistic[2] + "\n" + DRAW
							+ EQUALS + statistic[3] + "\n" + LOSE + EQUALS
							+ statistic[4]);
					out.println("end" + STATISTIC);
				} else {
					out.println(ERROR);
					
				}
				System.out.println(STATISTIC + "\n" + GAME + EQUALS
						+ statistic[0] + "\n" + WIN + EQUALS + statistic[1]
						+ "\n" + ATTEMPT + EQUALS + statistic[2] + "\n" + DRAW
						+ EQUALS + statistic[3]);
				freeThread();
				try {
					in.close();
					out.close();
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return -1;
			} else if (clientMessage.startsWith(RENEW)) {
				String[] renew_array = clientMessage.split(EQUALS);
				System.out.println("renew_array:" + clientMessage);
				own_token = Integer.parseInt(renew_array[1]);
				own_riddle = Integer.parseInt(renew_array[3]);
				enemy_token = Integer.parseInt(renew_array[5]);
				enemy_renew_flag = true;
				System.out.println(this.getName()
						+ ": we start exchange content");
				for (int i = 0; i < content.length; i++) {
					if (content[i] != null
							&& (content[i].own_riddle == own_riddle)) {
						number_attempts = content[i].number_attempts;
						enemy_number_attempts = content[i].enemy_number_attempts;
						logs_for_enemy = content[i].logs_for_enemy;
						System.out.println(this.getName()
								+ ": we print enemy logs from Context"+logs_for_enemy);
						enemy_riddle = content[i].enemy_riddle;
						break;
						
					}
				}
				System.out.println(this.getName()
						+ ": we end exchange content");
				
				if (enemy_renew_flag) {
					synchronized (this) {
						while (true) {
							for (int i = 0; i < threads.length; i++) {
								if (threads[i] != null
										&& (threads[i].own_token == enemy_token)
										&& (threads[i].own_riddle == enemy_riddle)) {
									threads[i].enemy_renew_flag = false;
									enemy_renew_flag = false;
									enemy_thread = threads[i];
									threads[i].enemy_thread = this;
									enemy_output_stream = threads[i].out;
									threads[i].enemy_output_stream = out;
									threads[i].ready_to_play = false;
									ready_to_play = false;
									break;
								}
							}
							if (enemy_output_stream != null) {
								break;
							}
						}
					}

				}
				
				System.out.println(this.getName()
						+ ": we and exchenge other info");
				return 1;
			}
		}
	}

	/*
	 * search for enemy among other threads and connect them exchanging thread's
	 * information
	 */
	private void exchangeInformation() {
		synchronized (this) {
			while (true) {
				for (int i = 0; i < max_clients; i++) {
					if (threads[i] != null && threads[i] != this
							&& threads[i].ready_to_play
							&& threads[i].own_token != 0) {

						enemy_thread = threads[i];
						threads[i].enemy_thread = this;

						threads[i].enemy_token = own_token;
						enemy_token = threads[i].own_token;

						enemy_output_stream = threads[i].out;
						threads[i].enemy_output_stream = out;

						threads[i].ready_to_play = false;
						ready_to_play = false;
						
						System.out.println(this.getName() + ": searched enemy");
						break;
					}
				}
				if (enemy_token != 0) {
					break;
				}
			}
		}
	}

	/*
	 * waiting riddle from client to send it to the enemy has chosen earlier
	 */
	private void waitRiddle() {
		while (true) {
			try {
				clientMessage = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (clientMessage.startsWith(RIDDLE)) {
				String[] riddle_array = clientMessage.split(EQUALS);
				own_riddle = Integer.parseInt(riddle_array[1]);

				System.out.println(this.getName() + ": got riddle");
				break;
			}
		}
	}

	/* exchange riddles */
	private void exchangeRiddle() {
		enemy_thread.enemy_riddle = own_riddle;
		while (true) {
			if (enemy_thread.own_riddle != 0) {
				enemy_riddle = enemy_thread.own_riddle;
				break;
			}
		}
	}

	private int[] getStatistic(int token) {
		int id = new_query.getUserByToken(token);
		System.out.println("id:" + id + ";  token:" + token);
		if (id < 0) {
			return null;
		} else {
			int[] statistic = new int[5];

			statistic[0] = new_query.getNumberGamesOfUser(id);
			statistic[1] = new_query.getNumberWinsOfUser(id);
			statistic[3] = new_query.getNumberDrawOfUser(id);
			statistic[4] = statistic[0] - statistic[1];
			int[] attempts = new int[3];

			attempts = new_query.getAttemptsInfoOfUser(id);
			System.out.println(attempts[0] + ":" + attempts[1] + ":"
					+ attempts[2]);
			statistic[2] = attempts[2];

			return statistic;
		}
	}

	private void freeThread() {
		synchronized (this) {
			for (int i = 0; i < max_clients; i++) {
				if (threads[i] == this) {
					threads[i] = null;
				}
			}
			System.out.println("free thread!!!");
		}
	}
}
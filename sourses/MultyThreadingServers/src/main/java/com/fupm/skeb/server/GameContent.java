package com.fupm.skeb.server;

public class GameContent {
	public String logs_for_enemy;
	public boolean renew_flag;
	public int own_token;
	public int number_attempts;
	public int own_riddle;
	public int enemy_riddle;
	public int enemy_number_attempts;
	GameContent(String logs_for_enemy,boolean renew_flag,int own_token,int number_attempts,int own_riddle,int enemy_riddle,int enemy_number_attempts){
		this.logs_for_enemy = logs_for_enemy;
		this.renew_flag = renew_flag;
		this.own_token = own_token;
		this.number_attempts = number_attempts;
		this.own_riddle = own_riddle;
		this.enemy_riddle = enemy_riddle;
		this.enemy_number_attempts = enemy_number_attempts;
	}
}

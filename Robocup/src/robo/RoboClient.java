package robo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RoboClient {

	public static void main(String[] args) throws Exception {
		final int AnzahlSpieler = 4;
		final int AnzahlGegner =4;
		String Team1="Java";
		String Team2="Bully";

		//Team 1
		Robo Spieler[];
		Spieler= new Robo [AnzahlSpieler];
		for(int i=0; i<AnzahlSpieler; i++){
			Spieler[i]= new Robo("127.0.0.1", 6000, Team1);

			if( Spieler[i].playerTypes[i].equals("keeper") ){
				Spieler[i].init(Team1, 15, true);
			}else{
				Spieler[i].init(Team1, 15, false);
			}
		}
		
		//Team 2
		Robo Gegner[];
		Gegner = new Robo [AnzahlGegner];
		for(int i=0;i<AnzahlGegner;i++){
			Gegner[i]= new Robo("127.0.0.1",6000, Team2);

			if( Gegner[i].playerTypes[i].equals("keeper") ){	
				Gegner[i].init(Team2, 15, true);
			}else{
				Gegner[i].init(Team2, 15, false);
			}
		}
		for(int i=0; i<AnzahlGegner;i++){
			Gegner[i].turn(180);
			Thread.sleep(100);
		}
		
		Thread.sleep(500);
		boolean running = true;
		String input = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(running)
		{
			try {
				System.out.print("> ");
				input = reader.readLine();
				if(input.equals("exit"))
					running = false;
				else{
					for(int i=0; i<AnzahlSpieler; i++){
						Spieler[i].send(input);
					}
					//Gegner
					for(int i=0; i<AnzahlGegner; i++){
						Gegner[i].send(input);
					}//*/
				}
				input = "";
			}
			catch(IOException e) {
				e.printStackTrace();
			}			
		}
		System.out.println("Tsch�!");

		for(int i=0; i<AnzahlSpieler; i++){
			Spieler[i].bye();
		}
		//Gegner
		for(int i=0; i<AnzahlGegner; i++){
			Gegner[i].bye();
		}//*/

	}
}

package old_versions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RoboClient_v1 {

	public static void main(String[] args) throws Exception {
		final int AnzahlSpieler=11;
		final int AnzahlGegner=11;
		String Team1="Patrick";
		String Team2="Lars";

		//Team 1
		Robo Spieler[], Gegner[];
		Spieler= new Robo [11];
		Gegner = new Robo[11];
		

		for(int i=0; i<AnzahlSpieler; i++){
			Spieler[i]= new Robo("192.168.0.7", 6000, Team1);
			Spieler[i].init(Team1, 15);
		}
		for(int i=0;i<AnzahlGegner;i++){
			Gegner[i]= new Robo("192.168.0.7",6000, Team2);
			Gegner[i].init(Team2, 15);
		}



		Thread.sleep(1000);

		for(int i=0; i<AnzahlSpieler;i++){
			Spieler[i].turn(180);
		}
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
				else
					for(int i=0; i<11; i++){
						Spieler[i].send(input);
						Gegner[i].send(input);
					}
				input = "";
			}
			catch(IOException e) {
				e.printStackTrace();
			}			
		}
		System.out.println("Tsch�!");
		for(int i=0; i<11; i++){
			Spieler[i].bye();
			Gegner[i].bye();
		}

	}
}

package robo;

import udp.UDPServer;
import java.lang.Math;

public class Robo extends UDPServer{

	//Eigene Mannschaft - Information
	//Durch �ndern der Arrays k�nnen kick_off-Positionen und Spielertypen angegeben werden.
	public String [] playerTypes={"keeper", "defense_l", "defense_r", "striker", "back", "back", "back", "back", "back", "back", "striker"};
	public int playerPosition [][]={ {-40,0},{-30,+10},{-30,-10},{-10,0},{-20,-8},{-20,0},{-20,+8},{-20,+16},{-20,+24},{-20,+32},{-10,0}};

	//Spiel-Informationen
	public int goals_me=10;						//Eigene Tore
	public int goals_them=0;					//Tore Gegner
	public boolean play;						//Spielmodus
	public String myName;						//Teamname
	public String Side;							//Spielfeld-Seite

	//Gegner-Informationen
	public String otherPlayerName[];			//Name der Gegner, die der Player sieht.
	public double otherPlayerValues[][];		//Werte der Gegner

	public String myPlayerName[];
	public double myPlayerValues[][];

	//Offset f�r den Torschuss, damit m�glichst nicht der Pfosten getroffen wird. Abh�ngig von der Spielseite.
	public int kick_offset;


	//Eigener Player-Informationen
	public double Stamina;						//Ausdauer
	public double x;							//Absolute X-Position
	public double y;							//Absolute Y-Position
	public double lastX = 0.0;					// Vorherige X-Position
	public double lastY = 0.0;					// Vorherige Y-Position 
	public double worldAngle=0;					//Weltwinkel, f�r Objektpositionen wichtig
	public int NumberPlayer;					//Spieler-Nummer, wird vom Server vergeben.

	//closest-Flag Variablen
	public String closestFlagName;				//Name der n�chsten Flagge	
	public double closestFlagDist;				//Abstand des Spielers zur Flagge
	public double closestFlagAngle;				//Winkel, in dem der Spieler die Flagge sieht
	public double closestFlagX;					//Abs. X-Koordinate der Flagge
	public double closestFlagY;					//Abs. Y-Koordinate der Flagge

	//Line-Variablen
	public String lineName;						//Name der Linie, die der Spieler sieht
	public double lineDist;						//Abstand zur Linie
	public double lineAngle;					//Sichtwinkel zur Linie

	//Ball-Variablen
	public boolean seeBall;						//Sicht auf den Ball m�glich?
	public double ballDist;						//Abstand zum Ball	
	public double ballAngle;					//Sichtwinkel zum Ball
	public double ballSpeed;					//Ball-Geschwindigkeit
	public double ballX;						//Abs. X-Koordinate des Balls
	public double ballY;						//Abs. Y-Koordinate des Balls

	//Tor-Informationen
	public String goalName;						//Flaggenname, die direkt im Tor ist - im Weiteren Tor genannt
	public boolean seeGoal;						//Sicht auf das Tor m�glich?
	public double goalAngle;					//Sichtwinkel zum Tor
	public double goalDist;						//Abstand zum Tor
	//Pfosten-Informationen	
	public String cornerGoalLeft;				//Flaggenname am linken Pfosten
	public boolean seeCornerGoalLeft;			//Sicht auf linken Pfosten m�glich?
	public double cornerGoalLeftDist;			//Abstand zum linken Pfosten
	public double cornerGoalLeftAngle;			//Sichtwinkel auf linken Pfosten

	public String cornerGoalRight;				//Flaggenname am rechten Pfosten
	public boolean seeCornerGoalRight;			//Sicht auf rechten Pfosten m�glich?
	public double cornerGoalRightDist;			//Abstand zum rechten Pfosten
	public double cornerGoalRightAngle;			//Sichtwinkel zum rechten Pfosten

	//Parser-Informationen aus dem See-String
	public String [] NameFlags;					//Name der geparsten Objekte
	public double  [][] ValueFlags;				//Werte dieser Objekte

	//H�r-Informationen
	String Message;
	int playerNumberMessage;
	boolean getMessage;

	//Name der Flagen auf dem Spielfeld
	private String AllFlagNames[]={
			"(f t l 50)","(f t l 40)","(f t l 30)","(f t l 20)","(f t l 10)","(f t 0)",
			"(f t r 10)","(f t r 20)","(f t r 20)","(f t r 40)","(f t r 50)",
			"(f b l 50)","(f b l 40)","(f t l 30)","(f b l 20)","(f b l 10)","(f b 0)",
			"(f b r 10)","(f b r 20)","(f b r 20)","(f b r 40)","(f b r 50)",

			"(f l t 30)","(f l t 20)","(f l t 10)","(f l 0)","(f l b 10)","(f l b 20)","(f l b 30)",
			"(f r t 30)","(f r t 20)","(f r t 10)","(f r 0)","(f r b 10)","(f r b 20)","(f r b 30)",

			"(f l t)","(f c t)","(f r t)","(f l b)","(f c b)","(f r b)",

			"(f p l t)","(f p l c)","(f p l b)","(f g l t)","(g l)","(f g l b)",
			"(f p r t)","(f p r c)","(f p r b)","(f g r t)","(g r)","(f g r b)",
			"(f c)"
	};
	//Zugeh�rige Koordinaten der Flaggen
	private double AllFlagPositions[][]={
			{-50,-57.5}, {-40,-57.5}, {-30,-57.5}, {-20,-57.5}, {-10,-57.5}, {0,-57.5},
			{10,-57.5},  {20,-57.5},  {30,-57.5},  {40,-57.5},  {50,-57.5},
			{-50,57.5}, {-40,57.5}, {-30,57.5}, {-20,57.5}, {-10,57.5}, {0,57.5},
			{10,57.5},  {20,57.5},  {30,57.5},  {40,57.5},  {50,57.5},

			{-57.5,-30}, {-57.5,-20}, {-57.5,-10}, {-57.5,0}, {-57.5,10}, {-57.5,20}, {-57.5,30},
			{57.5,-30},  {57.5,-20},  {57.5,-10},  {57.5,0},  {57.5,10},  {57.5,20},  {57.5,30},

			{-52.5,-34}, {0,-34}, {52.5,-34}, {-52.5,34}, {0,34}, {52.5,34},

			{-36,-20}, {-36,0}, {-36,20}, {-52.5,-7}, {-52.5,0}, {-52.5,7},
			 {36,-20},  {36,0},  {36,20},  {52.5,-7},  {52.5,0},  {52.5,7},
			{0,0}
	};


	// Variablen f�r den Torwart 
	public	boolean	ballCatched = 		false;	// false = Torwart ist (von Anfang an) nicht im Ballbesitz
	private	double	catchable_area_l =	2.0;	// Wert aus der server.conf
	private	double	catchable_area_w =	1.0;	// Wert aus der server.conf
	private	int		goalie_max_moves =	2;		// Wert aus der server.conf
	public	int		goalie_used_moves = 0;		// wird inkrementiert, wenn der Torwart "moved", nachdem er den Ball gefangen hat (bis goalie_max_moves)
	public	double	free_kick_y	=		0.0;		// Der Torwart f�hrt einen Absto� immer von der Position [ -34.0 | free_kick_y ] aus.

	public	String	penaltyCenter;				// Flaggenname des Mittelpunkts der Strafraumlinie
	public	boolean	seePenaltyCenter;			// Sicht auf Mittelpunkt der Strafraumlinie m�glich?
	public	double	penaltyCenterDist;			// Abstand zum Mittelpunkt der Strafraumlinie
	public	double	penaltyCenterAngle;			// Sichtwinkel zum Mittelpunkt der Strafraumlinie

	public	String	goalCenter;					// Flaggenname des Mittelpunkts der Torlinie
	public	boolean	seeGoalCenter;				// Sicht auf Mittelpunkt der Torlinie m�glich?
	public	double	goalCenterDist;				// Abstand zum Mittelpunkt der Torlinie
	public	double	goalCenterAngle;			// Sichtwinkel zum Mittelpunkt der Torraumlinie

	public	int		stepToPenalty =		0;		// gibt an, in welchem Abschnitt sich der Torwart Richtung Elfmeterpunkt befindet
	public	int		stepToGoal =		0;		// gibt an, in welchem Abschnitt sich der Torwart Richtung Startposition am Tor befindet
	public	int		stepToBlockBall =	0;		// gibt an, in welchem Abschnitt sich der Torwart befindet, um den Ball zu blockieren
	public	int		stepToFreeKick =	0;		// gibt an, in welchem Abschnitt sich der Torwart f�r einen Absto� befindet

	public	double	lastBallX =			0.0;	// X-Koordinate, wo der Ball zuvor war
	public	double	lastBallY =			0.0;	// Y-Koordinate, wo der Ball zuvor war
	public	double	desiredX =			0.0;	// X-Koordinate, wo der Torwart hinl�uft, um den Ball zu blockieren
	public	double	desiredY =			0.0;	// Y-Koordinate, wo der Torwart hinl�uft, um den Ball zu blockieren
	public	double	lastDesiredX =		0.0;	// X-Koordinate, wo der Torwart zuletzt hin lief
	public	double	lastDesiredY =		0.0;	// Y-Koordinate, wo der Torwart zuletzt hin lief
	private	double	desiredOffset =		1.0;	// gibt an, wie weit eine neue 'desired'-Koordinate von einer Alten sein darf
	
	public	double	targetDist =		0.0;	// gibt den Abstand zu einer Position an
	public	double	startX =			0.0;	// X-Koordinate der Startposition
	public	double	startY =			0.0;	// Y-Koordinate der Startposition
	private boolean	keeper_info =		false;	// 'true' l�sst alle TW-Infos auf der Konsole ausgaben


	public Robo(String ip, int port, String MyName) throws Exception
	{	
		super(ip, port);
		this.myName = MyName;
	}

	// initialisert einen Spieler f�r das Team 'name' mit Versionsnummer
	public void init(String name, int version, boolean keeper) throws Exception	
	{
		if( keeper ){
			send("(init " + name +" (version "+version+") (goalie))");
		}else{
			send("(init " + name +" (version "+version+"))");
		}
		//if(myName.equals("Bully"))keeper_info = true;
	}

	public void bye() throws Exception
	{	
		send("(bye)");
		close();
	}



	public double diffTwoDouble(double d1, double d2){
		/*
		 * Funktion, die eine absolute Differenz zwischen zwei double-Werten liefert.
		 */

		//Reihenfolge richtig stellen.
		if(d1>d2){
			double save=d1;
			d1=d2;
			d2=save;
		}
		//Berechnen der Differenz
		return Math.abs(d2-d1);
	}

	public void calculate_player_position(double lineAngle, double lineDistance){
		/*
		 * Berechnen der Player-Position und aktualisieren der Koordinaten x und y.
		 * Da die Variablen lineAngle und lineDistance ver�ndert werden, hier als �bergabeparameter. 
		 */

		//�berpr�fen, auf welche Line geguckt wird, anpassen des Winkels
		if(lineName.equals("(l t)")){
			if(lineAngle<0){
				lineAngle+=180;
			}
		}
		else if(lineName.equals("(l r)")){
			if (lineAngle>0){
				lineAngle-=90;
			}
			else {
				lineAngle+=90;
			}
		}
		else if(lineName.equals("(l b)")){
			if(lineAngle>0){
				lineAngle-=180;
			}
		}
		else if(lineName.equals("(l l)")){
			if(lineAngle>0){
				lineAngle+=90;
			}else{
				lineAngle-=90;
			}
		}

		//Um die Koordinaten von beliebigen Objekten, welche gesehen werden zu berechnen
		//wird die globale Variable worldAngle gesetzt.
		worldAngle=lineAngle;

		//Winkel zwischen Line und closest Flag berechnen
		double angle=lineAngle-closestFlagAngle;
		//Winkelanpassung
		if(angle>180){
			angle-=360;
		}
		if(angle<-180){
			angle+=360;
		}

		//Koordinaten berechnen, an denen der Player steht und Variablen setzen.
		x=  (closestFlagX-Math.cos(Math.toRadians(angle))*closestFlagDist);
		y=  (closestFlagY+Math.sin(Math.toRadians(angle))*closestFlagDist);	
	}

	public double [] calculate_object_position(double objectDistance, double objectAngle){
		/*
		 * Berechnen der Position von beliebigen Objekten, die vom Spieler gesehen werden.
		 * Da die globale Variable worldAngle benutzt wird, muss zuerst die Funktion
		 * calculate_player_position aufgerufen werden.
		 * Die Koordinaten werden als Array zur�ckgegeben.
		 */
		double objectPosition[]={0,0};
		//Differenz zu worldAngle bilden
		double angle=worldAngle-objectAngle;
		//Winkelanpassung
		if (angle>180){
			angle-=360;
		}
		if(angle<-180){
			angle+=360;
		}
		//Berechnen der Position
		objectPosition[0]=(x+Math.cos(Math.toRadians(angle))*objectDistance);
		objectPosition[1]=(y-Math.sin(Math.toRadians(angle))*objectDistance);
		//R�ckgabe
		return objectPosition;
	}

	public double GetKickAngle(double minAngle, double maxAngle, double minAbstand,double distance, boolean debug){
		/* 
		 * Diese Funktion hilft bei der Findung des Schusswinkels.
		 * Es Analysiert im Weg stehende Spieler und findet dann automatisch den richtigen
		 * Schusswinkel heraus.
		 * boolean debug steuert die Konsolenausgabe zur Kontrolle
		 * 
		 * minAngle: 	minimal gew�nschter Schusswinkel
		 * maxAngle: 	maximal gew�nschter Schusswinkel
		 * minAbstand: 	Grad-Abstand, den der Gegner mindestens entfernt sein soll
		 * 				Bsp: minAbstand=5,  Gegner -8�, Schusswinkel bis -13 und ab -3
		 * distance: 	Distanz, die ein Gegner h�chstens haben darf, damit er noch hier ber�cksichtigt wird.
		 * 				Also: distance =35, Gegner ist 40 weit weg -> Funktion tut so, als ob er nicht existiert.
		 * 
		 */

		//Sicherheit gegen vertauschte Variablen. minAngle muss die kleinere Variable sein.
		if(minAngle>maxAngle){
			double swap=minAngle;
			minAngle=maxAngle;
			maxAngle=swap;
		}

		//Bestimmen der Anzahl, wieviele Gegner in der angegebenen Zone (zwischen minAngle und maxAngle, n�her als distance) sind.
		int zaehl=0;
		for(int i=0;i<otherPlayerName.length;i++){
			if((otherPlayerValues[i][1]<maxAngle+10&&otherPlayerValues[i][1]>minAngle-10)&&otherPlayerValues[i][0]<distance){
				zaehl++;	
			}
		}
		//Erzeugen eines Arrays, in der die Winkel der Gegner gespeichert werden. Gr��e ist durch zaehl bekannt.
		double angle [] = new double[zaehl];
		//Speichern der Werte in das Array
		for(int i=0;i<angle.length;i++){
			if((otherPlayerValues[i][1]<maxAngle+10&&otherPlayerValues[i][1]>minAngle-10)&&otherPlayerValues[i][0]<distance){
				angle[i]=otherPlayerValues[i][1];
			}
		}

		//Bestimmen des Schusswinkels
		boolean help=false;
		//Anf�nglicher Schusswinkel ist immer =0�
		double kickAngle=0;
		//Ausgabe von minAngle und maxAngle falls gew�nscht.
		if(debug){System.out.println("minAngle:"+minAngle+"  maxAngle:"+maxAngle);}

		//Durchgehen der Gegner, ob der vorgeschlagene Schusswinkel (kickAngle) mit einem dieser Kollidiert.
		for(int i=0;i<angle.length;i++){
			//Ausgabe der Aktuellen angle und kickAngle Werte, falls gew�nscht
			if(debug){System.out.println("Angle:"+angle[i]+"Kick-Angle: "+kickAngle);}


			if(diffTwoDouble(kickAngle,angle[i])<minAbstand){
				//Schusswinkel (kickAngle) und Winkel des Gegners (angle[i]) sind n�her als minAbstand aneinander.
				//Addieren von minAbstand/2 auf den Schusswinkel. Zur�cksetzen der Laufvariablen auf 0 bzw, 
				//Da diese am Ende erst inkrementiert wird auf -1. -> Die Schleife beginnt von neuem und kontrolliert 
				//wieder alle Gegner, ob diese mit dem neuen Schusswinkel kollidieren.
				kickAngle+=minAbstand/2;
				i=-1;
				//Ausgabe, falls gew�nscht
				if(debug){System.out.println("kickAngle("+kickAngle+") meets Player within minAbstand("+minAbstand+").");}
			}

			if(kickAngle>maxAngle){
				//Auf kickAngle wurde nun so oft  minAbstand/2 addiert, dass kickAnlge gr��er als maxAngle wurde. 

				//Darum wird nun die "andere Seite" getestet. kickAngle wird auf minAngle gesetzt. Damit dies nicht in 
				//einer Endlosschleife sich wiederholt, wird die Variable help von false auf true gesetzt.
				kickAngle=minAngle;

				if(help){
					//Sollte das Programm schonmal die Variable auf minAngle-1 gesetzt haben, wird hier die schleife unterbrochen.
					//kickAngle wird auf -180� gesetzt, damit der Spieler den Ball direkt nach hinten spielt.
					kickAngle=-180;

					if(debug){System.out.println("Sequence reaches end of maxAngle("+maxAngle+") second time.\n Exiting with kickAngle("+kickAngle+").");}
					break;
				}
				if(debug){System.out.println("Sequence reaches end of maxAngle ("+maxAngle+"). Automatically reset to minAngle ("+minAngle+").");}
				//Beim erstmaligen Eintreten in die if-Bedinung setzen der Variablen help=true.
				help=true;
			}
		}
		//R�ckgabe des Schusswinkels. 
		return kickAngle;	
	}

	public void tactic_striker () throws Exception{
		/*
		 * Diese Funktion enth�lt die Taktik f�r einen St�rmer.
		 * Der St�rmer sucht den Ball, findet ihn, l�uft zum Tor und schie�t dann auf einen freien
		 * Bereich im Tor.
		 */


		//Zuerst: Sehe ich den Ball? Wenn nicht, dann drehe ich mich um 30�
		if(!seeBall){
			turn(70);
		}

		//Wenn ich den Ball nun sehe, schaue ich, ob die Entfernung gr��er als 0.5 ist.
		else if(ballDist>0.5){

			//Die Entfernung ist gr��er als 0.5. Somit muss ich zuerst auf den Ball zulaufen.
			//Schaue ich auf den Ball? 
			if(Math.abs(ballAngle)>4){
				//Der Winkel, mit dem ich auf den Ball schaue ist gr��er als |2| grad.
				//Also muss ich mich um denselben betrag drehen.
				turn(ballAngle);
			}
			//Wenn nun der Winkel nicht gr��er als |2| grad ist, kann ich loslaufen.
			else{
				dash(100);
			}
		}
		else{
			//Nun ist die Distanz kleiner als 0.5, somit bin ich direkt am Ball. 
			//Jetzt suche ich mir einen Weg zum Tor.
			if(seeGoal){
				//Ich sehe das Tor.
				if(goalDist>30){
					//Das Tor ist noch weiter weg als 30
					if(goalAngle>2||goalAngle<-2){
						//Ich gucke nicht direkt aufs tor. Ich muss mich drehen.
						turn(goalAngle);
					}
					else{
						//Ich bin noch nicht nah genug am Tor. Ich schaue aber auf das Tor.
						//Welchen Schusswinkel brauch ich, um nicht direkt einen Gegner zu treffen?

						double kickAngle = GetKickAngle(-30,30,10,25,false);
						//System.out.println("Schusswinkel(Weg): "+kickAngle);
						kick(30,kickAngle);
					}
				}
				else{
					//Das Tor ist n�her als 27
					if(goalAngle>2||goalAngle<-2){
						//Ich gucke nicht direkt aufs Tor. Ich muss mich drehen.
						turn(goalAngle);
					}
					else{
						//Wenn ich beide Pfosten sehe, kann ich den richtigen Winkel f�r ein Torschuss berechnen.
						if(seeCornerGoalLeft&&seeCornerGoalRight){	
							double kickAngle= GetKickAngle(cornerGoalLeftAngle+kick_offset, cornerGoalRightAngle-kick_offset,9,100,false);
							//System.out.println("KickAngle Tor:"+kickAngle);
							kick(100,kickAngle);
						}
					}
				}
			}

			else if(seeCornerGoalRight){
				//Das Tor wird nicht direkt gesehen. Aber der rechte Pfosten.
				if(cornerGoalRightAngle>2||cornerGoalRightAngle<-2){
					//Zum Pfosten drehen
					turn(cornerGoalRightAngle);
				}
				else{
					//Es wird der richtige Schusswinkel berechnet, ohne einen Gegner direkt zu treffen.
					//Dann wird der Ball nach vorne geschossen.
					double kickAngle=GetKickAngle(-30,30,10,25,false);
					System.out.println("Schusswinkel(Weg, CornerGoalRight): "+kickAngle);
					kick(30,kickAngle);
				}

			}
			else if(seeCornerGoalLeft){						
				//Das Tor wird nicht gesehen, der rechte Pfosten auch nicht, aber der linke Pfosten.
				if(cornerGoalLeftAngle>2||cornerGoalLeftAngle<-2){
					//Zum Pfosten drehen
					turn(cornerGoalLeftAngle);
				}
				else{
					//Es wird der richtige Schusswinkel berechnet, ohne einen Gegner direkt zu treffen.
					//Dann wird der Ball nach vorne geschossen.
					double kickAngle=GetKickAngle(-30,30,10,25,false);
					System.out.println("Schusswinkel(Weg, CornerGoalLeft): "+kickAngle);
					kick(30,kickAngle);
				}
			}
			else{
				//Es wird weder das Tor, noch ein Pfosten gesehen. Drehen um 45�.
				turn(70);
			}

		}
	}



	public void resetSteps(int except) throws Exception{
		/*
		 * Setzt alle Schritt-Variablen bis auf eine gew�hlte Ausnahme zur�ck.
		 */
		if( except != 1 ) stepToPenalty = 0;
		if( except != 2 ) stepToGoal = 0;
		if( except != 3 ) stepToBlockBall = 0;
		if( except != 4 ) stepToFreeKick = 0;
	}
	
	public boolean runToStaticPosition(double distTarget, boolean precision) throws Exception{
		/*
		 * Diese Funkion l�sst einen Spieler zu einer statischen Position laufen.
		 * Der Parameter 'precision' gibt an, ob diese Position (relativ) genau oder
		 * so schnell es geht erreicht werden soll.
		 */
		
		if( precision )
		{			
			if( 2 <= distTarget ){
				dash(100);
			}else if( 0 <= distTarget && distTarget < 2){
				dash(50);
			}else{
				return false;
			}
		}else
		{
			if( 0 <= distTarget ){
				dash(100);
			}else{
				return false;
			}
		}
		
		if(keeper_info)System.out.println("TW: l�uft");
		
		return true;
	}

	public void runToBall(int power, double distance) throws Exception{
		if(!seeBall){						// Wenn der Ball nicht gesehen wird...
			turn(30);						// ...soll sich der Spieler um 30� Grad drehen
		}else if(ballDist > distance){ 		// Wenn der Ball gesehen wird und noch weiter als 'distance' entfernt ist... 
			if(Math.abs(ballAngle) > 2){	//    ...muss �berpr�ft werden, ob der Spieler noch genau genug auf den Ball zul�uft...
				turn(ballAngle);			//    ...wenn nicht, muss der Spieler sich zum Ball drehen.
			}else{							//    Wenn der Spieler genau genug auf den Ball zul�uft...
				dash( power );				//    ... soll er mit vorgegebener 'power' auf den Ball zulaufen
			}
		}
	}

	public double getAngle(double lookingX, double lookingY, double targetX, double targetY) throws Exception{
		/*
		 * Diese Funktion errechnet Drehwinkel und -richtung f�r die Funktion 'turn'.
		 * Drehpunkt ist dabei immer der Spieler selbst (x, y).
		 * Dazu werden folgende Variablen benutzt:
		 *          x:	X-Koordinate des Spielers
		 *          y:	Y-Koordinate des Spielers
		 *   lookingX:	X-Koordinate des Objekts, auf das der Spieler momentan schaut
		 *   lookingY:	Y-Koordinate des Objekts, auf das der Spieler momentan schaut
		 *    targetX:	X-Koordinate des Objekts, auf das der Spieler schauen soll
		 * 	  targetY:	Y-Koordinate des Objekts, auf das der Spieler schauen soll
		 * turn_angle:	Drehwinkel und -richtung, um auf das gew�nschte 'target'-Objekt zu schauen	 
		 */
		double myDistLookingX =	x - lookingX;	// X-Abstand zwischen dem Spieler und dem Objekt, was er gerade anschaut
		double myDistLookingY =	y - lookingY;	// Y-Abstand zwischen dem Spieler und dem Objekt, was er gerade anschaut
		double myDistTargetX =	x - targetX;	// X-Abstand zwischen dem Spieler und dem Objekt, was er anschauen soll
		double myDistTargetY =	y - targetY;	// Y-Abstand zwischen dem Spieler und dem Objekt, was er anschauen soll
		double alpha =			Math.toDegrees( Math.atan( myDistLookingY / myDistLookingX ) );
		double beta =			Math.toDegrees( Math.atan( myDistTargetY / myDistTargetX ) );
		double turn_angle =		180 - Math.abs(alpha) - Math.abs(beta);
		if(keeper_info)System.out.println("ANGLE: x: " + x + " / y: " + y);
		if(keeper_info)System.out.println("ANGLE: myDistLookingY: " + myDistLookingY);
		if(keeper_info)System.out.println("ANGLE: myDistLookingX: " + myDistLookingX);
		if(keeper_info)System.out.println("ANGLE: alpha: " + alpha);
		if(keeper_info)System.out.println("ANGLE: myDistTargetY: " + myDistTargetY);
		if(keeper_info)System.out.println("ANGLE: myDistTargetX: " + myDistTargetX);
		if(keeper_info)System.out.println("ANGLE: beta: " + beta);

		if( (alpha/beta) < 0 ){		// Wenn ALPHA ein anderes Vorzeichen hat als BETA...
			// ...muss TURN_ANGLE das gleiche Vorzeichen wie ALPHA haben
			if( alpha < 0){
				if(keeper_info)System.out.println("ANGLE: turn_angle:" + -turn_angle);
				return -turn_angle;
			}else{
				if(keeper_info)System.out.println("ANGLE: turn_angle:" + turn_angle);
				return turn_angle;
			}
		}else{						// Wenn ALPHA und BETA das gleiche Vorzeichen haben...
			// ...muss ALPHA von von BETA subtrahiert werden
			if(keeper_info)System.out.println("ANGLE: turn_angle:" + (beta - alpha));
			return (beta - alpha);

		}
	}
	
	public double getDistance(double x1, double y1, double x2, double y2) throws Exception{
		return Math.sqrt( Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2));
	}
	
	public void keeper_catchBall() throws Exception{
		resetSteps(100);	// Alle Schritt-Variable (ohne Ausnahme!) zur�cksetzen
		if( Math.abs(x) > 37 && Math.abs(y) < 19){ // Nur wenn der TW sich im Strafraum befindet, darf er den Ball fangen!
			catch_ball(ballAngle);
			if(keeper_info)System.out.println("TW: versuche den Ball zu fangen");
		}
	}

	public void keeper_runToPenaltyPoint() throws Exception{
		/*
		 * Diese Funktion tritt in Kraft, wenn der Ball in der gegnerischen H�lfte ist.
		 * Sie l�sst den Torwart in Schritten
		 */
		resetSteps(1);								// zur�cksetzen der Schritt-Variablen
		int penaltyX = -41, penaltyCenterX = -36;	// X-Werte der verwendeten Flags

		switch (stepToPenalty)
		{
		case 0:		// Der Torwart sucht sich den Mittelpunkt der Strafraumlinie
			if( !seePenaltyCenter ){
				turn(30);
			}else{
				turn(penaltyCenterAngle);
				stepToPenalty = 1;
				if(keeper_info)System.out.println(myName + ": G2G: schaut Richtung PC");
				if(keeper_info)System.out.println(myName + ":   X: " + x + " / Y: " + y + " PC-Angle:" + penaltyCenterAngle);
			}
			break;

		case 1:		// Der Torwart steht nun in Richtung des Strafraumlinienmittelpunktes und muss sich noch Richtung Elfmeterpunkt drehen
			turn( getAngle(penaltyCenterX, 0, penaltyX, 0) );
			stepToPenalty = 2;
			if(keeper_info)System.out.println(myName + ": schaut zum Elfmeterpunkt");
			break;

		case 2:		// Der steht nun in Richtung des Elfmeterpunktes und muss nun solange laufen, bis er da ist
			// Abstand zum Ziel berechnen
			targetDist = getDistance(x, y, penaltyX, 0);
			startX = x;
			startY = y;				
			stepToPenalty = 3;
			if(keeper_info)System.out.println( myName + ": Start der Abstandsberechnung");
			break;
			
		case 3:
			double startDist = getDistance(x, y, startX, startY);
			if(keeper_info)System.out.println(myName + ": G2P: targetDist: " + targetDist + " / startDist: " + startDist);
			
			// Wenn der Torwart noch nicht den gew�nschten Abstand gelaufen ist
			if( startDist <= targetDist){
				if(keeper_info)System.out.println(myName + ": G2P: laufe los   Abstand zum Ziel: " + (targetDist-startDist));
				if( Math.abs(x) > 35 && Math.abs(y) < 19){
					runToStaticPosition( (targetDist-startDist), false);
				}
			}else{
				if(keeper_info)System.out.println(myName + ": G2P: am Elfmeterpunkt angekommen");
				stepToPenalty = 4;
			}
			break;
			
		case 4:		// Der Torwart steht nun auf dem Elfmeterpunkt und schaut in Richtung Ball
			if( !seeBall ){
				turn(30);
				if(keeper_info)System.out.println("TW: G2P: sieht den Ball nicht und dreht sich");
			}else{
				turn(ballAngle);
				if(keeper_info)System.out.println("TW: G2P: schaut in Richtung Ball");
			}
			break;
		}
	}

	public void keeper_runToStartPosition() throws Exception{
		/*
		 * Diese Funktion tritt in Kraft, wenn der Ball in der gegnerischen H�lfte ist.
		 * Sie l�sst den Torwart in Schritten
		 */
		resetSteps(2);				// zur�cksetzen der Schritt-Variablen
		double goalCenterX = -52.5;	// X-Wert des verwendeten Flags

		switch (stepToGoal)
		{
		case 0:		// Der Torwart sucht sich den Mittelpunkt der Torlinie
			if( !seeGoalCenter ){
				turn(30);
			}else{
				turn(goalCenterAngle);
				stepToGoal = 1;
			}
			break;

		case 1:		// Der Torwart steht nun in Richtung des Torlinienmittelpunktes und muss sich noch Richtung Startposition drehen
			double direction = getAngle(goalCenterX, 0, playerPosition[0][0], 0);
			turn( direction );
			stepToGoal = 2;
			if(keeper_info)System.out.println("TW: G2G: schaut Richtung SP");
			if(keeper_info)System.out.println("  direction: " + direction + " / ");
			break;

		case 2:		// Der steht nun in Richtung des Startpunktes und muss nun solange laufen, bis er da ist 
			// Abstand zum Ziel berechnen
			targetDist = getDistance(x, y, playerPosition[0][0], 0);
			startX = x;
			startY = y;				
			stepToGoal = 3;
			break;
			
		case 3:
			double startDist = getDistance(x, y, startX, startY);
			
			// Wenn der Torwart noch nicht den gew�nschten Abstand gelaufen ist
			if( startDist <= targetDist){
				runToStaticPosition( (targetDist-startDist), true);
			}else{
				stepToGoal = 4;
			}
			break;
			
		case 4:		// Der Torwart steht nun auf dem Startpunkt und schaut in Richtung Ball
			if( !seeBall ){
				turn(30);
				if(keeper_info)System.out.println("TW: G2G: sieht den Ball nicht und dreht sich");
			}else{
				turn(ballAngle);
				if(keeper_info)System.out.println("TW: G2G: schaut in Richtung Ball");
			}
			break;
		}
	}
	
	

	public void keeper_blockBall() throws Exception{
		resetSteps(3);		// zur�cksetzen der Schritt-Variablen

		desiredX = playerPosition[0][0];
		if( (ballX - lastBallX) == 0){
			desiredY = 0;
		}else{
			desiredY = lastBallY + (ballY - lastBallY) / (ballX - lastBallX) * (desiredX - lastBallX);
		}
		
		desiredY = ballY / 2;
		
		if( desiredY < -10){
			desiredY = -10;
		}else if( desiredY > 10){
			desiredY = 10;
		}

		if(keeper_info)System.out.println("TW: BLOCK: START");
		if(keeper_info)System.out.println("  desiredX: " + desiredX + " / desiredY: " + desiredY);
		
		// Hat sich das Ziel "deutlich" ge�ndert...
		if( Math.abs(desiredX - lastDesiredX) > desiredOffset ||
				Math.abs(desiredY - lastDesiredY) > desiredOffset )
		{	// ...muss das Ziel aktualisiert werden
			lastDesiredX = desiredX;
			lastDesiredY = desiredY;
			stepToBlockBall = 0;
			if(keeper_info)System.out.println(" ZIEL HAT SICH VER�NDERT");
		}
		
		if(keeper_info)System.out.println(" HIER BIN ICH: OFFSET: X:" + Math.abs(desiredX - lastDesiredX) + " / Y: " + Math.abs(desiredY - lastDesiredY));
		
		// Hat sich das Ziel allerdings nur wenig bis gar nicht ge�ndert..
		if( Math.abs(desiredX - lastDesiredX) <= desiredOffset &&
				Math.abs(desiredY - lastDesiredY) <= desiredOffset )
		{
			if(keeper_info)System.out.println("  Ziel hat sich nur wenig ge�ndert! STEP: " + stepToBlockBall);
			
		 	// ... soll der Torwart auf die 'lastDesired'-Koordinaten zulaufen und von dauert
			switch (stepToBlockBall)
			{
			case 0:	// Torwart soll (wenn er den Ball sieht) sich Richtung Ball drehen
				if( !seeBall ){
					turn(30);
					if(keeper_info)System.out.println("  TW sieht den Ball nicht");
				}else{					
					turn(ballAngle);
					if(keeper_info)System.out.println("  TW sieht den Ball und hat sich zu ihm gedreht");
					stepToBlockBall = 1;
				}				
				break;
				
			case 1:	// Torwart soll sich von Sicht Richtung Ball zu den gew�nschten Koordinaten drehen
				double direction = getAngle(lastBallX, lastBallY, lastDesiredX, lastDesiredY);	// 'lastBall', weil schon ein weiterer Schritt vergangen ist!
				turn( direction );
				if(keeper_info)System.out.println("  TW hat sich zum Zielpunkt gedreht! DIR: " + direction);
				stepToBlockBall = 2;
				break;
				
			case 2:	// Abstand zum Ziel berechnen
				targetDist = getDistance(x, y, lastDesiredX, lastDesiredY);
				startX = x;
				startY = y;				
				stepToBlockBall = 3;
				break;
				
			case 3:	// Torwart soll zu den gew�nschten Koordinaten laufen bis er da ist
				double startDist = getDistance(x, y, startX, startY);
				
				// Wenn der Torwart noch nicht den gew�nschten Abstand gelaufen ist
				if( startDist < targetDist){
					runToStaticPosition( (targetDist-startDist), false);
				}else{
					stepToBlockBall = 4;
				}
				break;
				
			case 4:	// Torwart soll nun von den gew�nschten Koordinaten aus (wo er gerade steht) zum Ball schauen
				if( !seeBall ){
					turn(30);
					if(keeper_info)System.out.println("  TW sieht den Ball nicht");
				}else{					
					turn(ballAngle);
					if(keeper_info)System.out.println("  TW sieht den Ball und hat sich zu ihm gedreht");
					stepToBlockBall = 1;
				}
				// ZUSATZ:	wenn zB eine inkrementiere Laufvariable einen bestimmten Werte erreicht hat,
				//				kann der Torwart (der noch auf den nicht ge�nderten gew�nschten Koordinaten steht)
				//				auf den Ball zulaufen
				break;
			}
		}
	}
	
	public void v0_keeper_blockBall(double desired_X) throws Exception{
		/*
		 * Durch diese Funktion versucht der Torwart sich genau zwischen Ball und Tor zu positionieren.
		 * Es ist bekannt, wo der Ball zuvor war und wo der Ball jetzt gerade ist.
		 * Daraus l�sst sich ein Punkt vor dem Tor bestimmen, durch den der Ball ins Tor rollen w�rde.
		 * Der Torwart versucht nun, sich auf diesen Punkt zwischen Ball und der Torlinie zu stellen, damit
		 * (falls der Ball tats�chlich zu ihm rollt) er den Ball fangen kann, wenn dieser nah genug ist.
		 * 
		 * Noch nicht umgesetzter Zusatz:
		 * Der Torwart soll auf den Ball zulaufen, wenn entweder sich der Ball nicht mehr bewegt
		 * (ballSpeed < 2) oder der Ball schon "l�nger" seine Laufbahn nicht ver�ndert hat. 
		 */
		resetSteps(3);							// zur�cksetzen der Schritt-Variablen

		// Punkt finden, durch den der Ball am Torwart vorbeirollen w�rde, falls der Ball Richtung Tor genauso weiterrollt wie bisher
		double desired_Y = lastBallY + (ballY - lastBallY) / (ballX - lastBallX) * (desired_X - lastBallX);

		if(keeper_info)System.out.println(" DESIRED: Y = " + desired_Y);
		if(keeper_info)System.out.println(" DESIRED: X = " + desired_X);

		// Falls der Ball am Tor vorbei rollen w�rde, soll der Torwart nicht zu weit vom Tor weg laufen
		if( desired_Y < -8){
			desired_Y = -8;
		}else if( desired_Y > 8){
			desired_Y = 8;
		}

		if(keeper_info)System.out.println(" DESIRED: Y = " + desired_Y + " (angepasst falls doof)");
		if(keeper_info)System.out.println(" DESIRED: X = " + desired_X + " (angepasst falls doof)");

		// Wenn der Ball fast nicht mehr rollt...
		if( ballSpeed < 0 ){
			// ...soll der Torwart auf den Ball zulaufen
			runToBall(100, catchable_area_l/2);
		}else{	// Wenn der Ball noch rollt, soll der Torwart zu den 'desired'-Koordinaten laufen und dann zum Ball schauen
			switch (stepToBlockBall)
			{
			case 0:		// Der Torwart sucht den Ball
				if( !seeBall ){
					turn(30);
					if(keeper_info)System.out.println("TW: BLOCK: IST BLIND");
				}else{
					turn(ballAngle);
					stepToBlockBall = 1;
					if(keeper_info)System.out.println("TW: BLOCK: DREHUNG ZUM BALL " + ballAngle);
				}
				break;

			case 1:		// Der Torwart steht nun in Richtung des Ball und muss sich noch in Richtung der gew�nschten Koordinaten drehen
				double direction = Math.atan( diffTwoDouble(x,lastBallX) / diffTwoDouble(y,lastBallY) );
				turn( direction );
				stepToBlockBall = 2;
				if(keeper_info)System.out.println("TW: BLOCK: schaut zu den 'desired'-Koordinaten  DIRECTION: " + direction);
				break;

			case 2:		// Der Torwart steht nun in Richtung der gew�nschten Koordinaten und muss nun solange laufen, bis er da ist
				//m�gliches Problem: wenn der Torwart die eine Koordinate schon erreicht hat, die andere aber nicht, l�uft er zu weit!
				if( diffTwoDouble(x, desired_X) > 0.5 && diffTwoDouble(y, desired_Y) > 0.5 ){
					dash(50);
					if(keeper_info)System.out.println("TW: BLOCK: l�uft zu DESIRED");
				}else{
					stepToBlockBall = 3;
					if(keeper_info)System.out.println("TW: BLOCK: bei DESIRED angekommen");
				}
				break;

			case 3:		// Der Torwart steht nun auf den gew�nschten und schaut in Richtung Ball
				if( !seeBall ){
					turn(30);
					if(keeper_info)System.out.println("TW: BLOCK: sieht den Ball nicht und dreht sich");
				}else{
					// Wenn der Ball noch rollt, aber ungef�hr dahin, wo er bisher hingerollt ist...
					if( (ballSpeed > 1) && (Math.abs(desired_Y) < Math.abs(lastDesiredY)+catchable_area_w) ){	
						runToBall(100, catchable_area_l/2);	// ...soll der Torwart auf den Ball zulaufen						
					}else{
						turn(ballAngle);
						if(keeper_info)System.out.println("TW: BLOCK: schaut in Richtung Ball");
					}
				}
				break;
			}	
		}

		lastDesiredX = desired_X;
		lastDesiredY = desired_Y;
	}


	public void keeper_free_kick() throws Exception{
		resetSteps(4);		// zur�cksetzen der Schritt-Variablen
		
		if(keeper_info)System.out.println("TW: BALL CATCHED step:" + stepToFreeKick);
		
		switch (stepToFreeKick) {
		case 0:
			if( goalie_used_moves < goalie_max_moves){
				move( -38.0, free_kick_y);
				goalie_used_moves++;
			}else{
				stepToFreeKick = 1;
			}
			break;

		case 1:
			if( !seePenaltyCenter ){
				turn(30);
			}else{
				turn(penaltyCenterAngle);
				stepToFreeKick = 2;
			}
			break;

		case 2:
			kick(100, 0);
			goalie_used_moves = 0;
			stepToFreeKick = 0;
			break;
		}
	}

	public void tactic_keeper () throws Exception{
		/*
		 * Diese Funktion enth�lt die Taktik f�r den Torwart.
		 * Der Torwart �berpr�ft, ob er den Ball hat.
		 * Wenn er den Ball nicht hat, sucht er ihn und versucht ihn zu fangen. Dazu muss der Torwart den Ball gut einsch�tzen.
		 * Wenn er den Ball hat, schie�t er den Ball in einem bestimmten Winkel weg, oder "moved" sich zuvor noch mit dem Ball an eine bessere Position.
		 */
		
		if(Side.equals("right")){
			x = -x;
			y = -y;
			
			if( seeBall){
				ballX = -ballX;
				ballY = -ballY;
			}
		}
		
		if( !ballCatched )	// Wenn der Ball nicht gefangen wurde
		{
			// Wenn sich der Ball nahe genug und im Sichtfeld vom Torwart befindet und dieser im Strafraum ist...
			if( seeBall && (ballDist < catchable_area_l * 0.8) && (Math.abs(x) > 35) && (Math.abs(y) < 19) ){
				keeper_catchBall();					// ...versucht der Torwart den Ball zu fangen --> bei Erfolg: ballCatched = true (--> parse_hear)
			}
			else if(ballX > 0){						// Wenn der Ball sich in der gegnerischen H�lfte (rechts) befindet
				keeper_runToPenaltyPoint();
			}
			else{									// Wenn der Ball in der eigenen H�lfte ist
				if(ballX > -20){					// Wenn der Ball in den ersten 20 Metern der eigenen H�lfte ist
					keeper_runToStartPosition();
				}else if(ballX <= -20){				// Wenn sich der Ball in den letzten 30 Metern vor dem eigenen Tor befindet

					// Wenn der Ball sich fast nicht mehr bewegt und im Strafraum ist...
					if( ballSpeed < 10 && (Math.abs(ballX) > 35) && (Math.abs(ballY) < 19) )
					{
						runToBall(100, catchable_area_l * 0.8);	// ...soll der TW zum Ball laufen
					}else{
						keeper_blockBall();						// ...ansonsten soll er sich zwischen Ball und Tor stellen
					}
				}
			}
		}

		// Dieses IF muss alleine stehen, damit falls oben der Ball gefangen wurde reagiert werden kann!
		if( ballCatched ){						// Wenn der Ball gefangen wurde...
			keeper_free_kick();					// ...f�hrt der Torwart einen Absto� aus --> danach: ballCatched = false
		}
	}

	public void tactic_defense() throws Exception{
		if(Side.equals("right"))
		{	
		ballX = -ballX;	
		ballY = -ballY;
		}
		
		//Ball im Bereich
		if(!seeBall)
		{
			turn(30);
		}else
		{

			if(ballX < 0)
			{	
				if(playerTypes[NumberPlayer-1].equals("defense_l") && ballY > 0 || playerTypes[NumberPlayer-1].equals("defense_r") && ballY < 0)
				{
					//				System.out.print("Spieler: "+NumberPlayer+" "+(int)ballX+" "+(int)ballY+" "+"l: "+left+" "+"r: "+right+" "+"t: "+top+" "+"b: "+bottom+" "+"Ball drin! \r\n");

					if(ballDist>0.5)
					{
						if(Math.abs(ballAngle)>2){
							turn(ballAngle);
						}
						else{
							dash(100);
						}
					}else{
						if(seeGoal)
						{
							kick(100, goalAngle);
						}else{
							if(seeGoalCenter && goalCenterDist < 40)
								kick(100,180);
							else
								turn(40);
						}
					}
				}else
					turn(ballAngle);
			}else
			{	turn(ballAngle);
			}
		}
	}

	public String cutOut(String msg, String startPattern, String endPattern)	// schneidet aus 'msg' zwischen zwei 'pattern' einen String aus
	{
		return msg.substring(msg.indexOf(startPattern)+startPattern.length(), msg.indexOf(endPattern, msg.indexOf(startPattern)+startPattern.length()));
	}

	public void parse_init(String msg){
		/*
		 * Diese Funktion f�ngt die Initial-Nachricht vom Server ab. Hier gibt der Server die Seite,
		 * auf der gespielt wird bekannt sowieso den Spielmodus.
		 */
		//Ausschneiden und splitten der Nachricht.
		String Init=cutOut(msg,"(init ",")");
		String[] params=Init.split(" ");
		/* params
		 * [0] = "l" oder "r"
		 * [1] = Nummer des Spielers
		 * [2] = "before_kick_off" oder "play_on"
		 */

		if(params[0].equals("l")){
			//Team spielt auf der linken Seite. Setzen der Variablen
			Side="left";
			goalName="(g r)";
			cornerGoalLeft="(f g r t)";
			cornerGoalRight="(f g r b)";
			kick_offset=4;
				penaltyCenter = "(f p l c)";
				goalCenter = "(g l)";			//eigenes Tor
		}
		else{
			//Team spielt auf der rechten Seite. Setzen der Variablen
			Side="right";
			goalName="(g l)";
			cornerGoalLeft="(f g l t)";
			cornerGoalRight="(f g l b)";
			kick_offset=-6;
				penaltyCenter = "(f p r c)";
				goalCenter = "(g r)";			//eigenes Tor

		}
		//Speichern der Spielernummer
		NumberPlayer=Integer.parseInt(params[1]);

		//Setzen des Spielmodus
		if(params[2].contains("before_kick_off")){
			play=false;

		}
		if(params[2].contains("play_on")){
			play=true;
		}
	}

	public void parse_hear(String msg) throws Exception{ 
		/*
		 * Diese Funktion parst den hear-String vom Server. Sie achtet dabei 
		 * auf Nachrichten vom Referee.
		 */

		//Auf Nachrichten vom Referee h�ren
		if(msg.contains("our")&&!getMessage){
			String ref=cutOut(msg,"our ",")" );
			//Message
			Message=cutOut(ref,"\"","\"");
			String sender[]=ref.split(" ");
			playerNumberMessage=Integer.parseInt(sender[0]);
			getMessage=true;	
		}

		if(msg.contains("referee")){
			String ref = cutOut(msg,"referee ",")");


			//Spielmodus before_kick_off 
			if(ref.equals("before_kick_off")){
				play=false;
			}
			//Spielmodus play_on
			if(ref.equals("play_on")){
				play=true;
			}
			//Spielmodus kick_off_r oder l
			if( (ref.equals("kick_off_r") && Side.equals("right")) || (ref.equals("kick_off_l") && Side.equals("left")) )
				play=true;

			//Tor gefallen
			if(ref.contains("goal")){
				//Damit die Stamina nicht zu schnell zu wenig wird, wechseln die Player nach jedem Tor ihre Taktik. 
				//Positionen Tauschen;
				if(playerTypes[NumberPlayer-1].contains("striker")){
					playerTypes[NumberPlayer-1]="defense_l";
					playerPosition[NumberPlayer-1][0]=-30;
					playerPosition[NumberPlayer-1][1]=+10;
				}else if(playerTypes[NumberPlayer-1].contains("defense_l")){
					playerTypes[NumberPlayer-1]="striker";
					playerPosition[NumberPlayer-1][0]=-10;
					playerPosition[NumberPlayer-1][1]=0;
				}

				if( (ref.contains("r") && Side.equals("right")) || (ref.contains("l") && Side.equals("left")) ){
					goals_me++;
				}
				else{
					goals_them++;
				}
				play=false;
			}

			 	//Ball gefangen
			if( ref.contains("goalie_catch_ball") || ref.contains("free_kick") ){
				if( (ref.contains("l") && Side.equals("left")) || (ref.contains("r") && Side.equals("right")) ){
					ballCatched = true;
				}
				play=true;	// damit die tactic_keeper und damit der keeper_free_kick ausgef�hrt wird
			}else{
				ballCatched = false;
			}
		}
	}

	public void parse_sense(String msg){	// setzt nur Stamina auf den in "msg" stehenden Wert
		Stamina = Double.parseDouble(cutOut(msg,"(stamina "," "));
	}

	public void parse_see(String msg){
		/*
		 * Mit dieser Funktion kann der See-String geparst werden. Die Funktion 
		 * ermittelt aus den Objekten automatisch die n�chste Flagge (closestFlag), 
		 * die angegebene Line und den Ball. 
		 * Au�erdem setzt sie die Variablen entsprechend.
		 */

		//Ausschneiden der Flags aus dem See-String
		if(msg.contains("((")&&msg.endsWith("))")){		
			//Bei vielen Spielern kann es vorkommen, dass der String abgeschnitten wird. Dann funktioniert
			//cutOut nicht. Deswegen wird nur geparst, wenn der String vollst�ndig ist.

			String See=cutOut(msg,"((","))")+"))";
			//Splitten des See-Strings so, dass jedes Objekt eine einzelne Array-Stelle erh�lt
			String Flags[]=	See.split("\\(\\(");

			//Speichern der Objekte in zwei Arrays (Name und Werte). Es kommen max. 6 Werte vor.
			NameFlags=new String [Flags.length];
			ValueFlags= new double [NameFlags.length][6];


			for (int i=0; i<Flags.length;i++){
				//Ausschneiden des Objektnamens und speichern im NameFlags-Array
				NameFlags[i] = "(" + cutOut(Flags[i], "", ")") + ")";
				//Ausschneiden der Objekt-Werte und zwischenspeichern in einem String
				String Value = cutOut(Flags[i], ") ", ")");
				//Splitten des Strings an den Leerzeichen
				String ValueArray[] = Value.split(" ");

				//Parsen der einzelnen Zahlenstrings in Double.
				for(int j=0; j < ValueArray.length; j++){
					if(!ValueArray[j].contains("k")){
						//Es kann vorkommen, ein Spieler am Ende ein k enth�lt, dieses k darf nicht geparst werden.
						ValueFlags[i][j]=Double.parseDouble(ValueArray[j]);
					}
				}
			}

			//Alle See-Objekte liegen jetzt im Array NameFlags[] und ValueFlags[][].
			//Es kann nun die "closestFlag" bestimmt werden.

			//Variablen zum Zwischenspeichern von Entfernung und Flaggennummer
			//und Variable zur Kontrolle, ob �berhaupt eine Flagge gesehen wurde.
			double length=200; 
			int flagge=0;
			boolean flag_exist=false;

			for(int i=0; i < NameFlags.length; i++){
				if(NameFlags[i].contains("(f")){
					//Nur Flagge, die ein kleines f am Anfang haben ber�cksichtigen.
					flag_exist = true;
					if(length > ValueFlags[i][0]){
						//neue closestFlag gefunden
						flagge=i;
						length=ValueFlags[i][0];
					}
				}
			}


			if(flag_exist){
				//Es wurde eine closestFlag gefunden.
				//Speichern der Werte in den globalen Variablen
				closestFlagName=NameFlags[flagge];
				closestFlagDist=ValueFlags[flagge][0];
				closestFlagAngle=ValueFlags[flagge][1];

				//Herraussuchen der X- und Y- Koordinaten von closestFlag aus dem Array
				//Suchen der Flag mit dem gleichen Namen

				for(int i=0; i<AllFlagNames.length;i++){
					if(AllFlagNames[i].equals(closestFlagName)){
						closestFlagX=AllFlagPositions[i][0];
						closestFlagY=AllFlagPositions[i][1];
					}
				}
			}
			//Die Variablen der closestFlag sind jetzt gesetzt. Zur Positionsberechnung fehlt noch
			//die Linie, die der Spieler sieht.

			for(int i=0; i<NameFlags.length;i++){
				//Finden des Line-Eintrags
				if(NameFlags[i].contains("(L")||NameFlags[i].contains("(l")){
					//Setzen der Line-Variablen
					lineName=NameFlags[i];
					lineDist=ValueFlags[i][0];
					lineAngle=ValueFlags[i][1];

					//Nun kann die Spielerposition berechnet werden.
					calculate_player_position(lineAngle, lineDist);
				}
			}


			//Zur Schussberechnung ist es notwendig, alle Gegner zu erkennen. Dazu wird das See-Array
			//nach Playern durchsucht, die nicht den Namen der eigenen Mannschaft haben.
			//Zuerst Feststellen, wieviele Gegner gesehen werden.
			int AnzahlGegner=0;
			int AnzahlSpieler=0;
			for(int i=0; i<NameFlags.length; i++){
				if( (NameFlags[i].contains("(p") || NameFlags[i].contains("(P")) && (!NameFlags[i].contains("(p)") || !NameFlags[i].contains("(P)"))){
					if(!NameFlags[i].contains(myName)){
						AnzahlGegner++;
					}
					else{
						AnzahlSpieler++;

					}
				}
			}

			//Jetzt kann durch die bekannte Anzahl die passende Array-Gr��e erzeugt werden
			otherPlayerName= new String[AnzahlGegner];
			otherPlayerValues= new double[AnzahlGegner][6];
			myPlayerName= new String[AnzahlSpieler];
			myPlayerValues = new double [AnzahlSpieler][6];

			//Da durch die geringere Anzahl nicht die Laufvariable i benutzt werden kann, erzeugen eines eigenen Inkrementierenden
			//Array-Index
			int array_index_1=0;
			int array_index_2=0;
			for(int i=0;i<NameFlags.length;i++){
				//Das Array durchgehen, und nach Playern suchen. Es z�hlen nur Player, die mit Namen erkannt werden k�nnen.
				if((NameFlags[i].contains("(p")||NameFlags[i].contains("(P"))&&(!NameFlags[i].contains("(p)")||!NameFlags[i].contains("(p)"))){
					if(!NameFlags[i].contains(myName)){
						//Wenn der Name nicht meinen Teamnamen enth�lt, muss es der Gegner sein.
						//Speichern des Namen
						otherPlayerName[array_index_1]=NameFlags[i];
						//Speichern der Werte
						for(int j=0; j<otherPlayerValues[array_index_1].length; j++){
							otherPlayerValues[array_index_1][j]=ValueFlags[i][j];
						}
						//Inkrementieren von array_index
						array_index_1++;
					}else{
						myPlayerName[array_index_2]=NameFlags[i];
						for(int j=0;j<myPlayerValues[array_index_2].length;j++){
							myPlayerValues[array_index_2][j]=ValueFlags[i][j];
						}
					}
				}
			}

			//Filtern des Balls und des Tors aus den See-Objekten.

			//Boolean Variablen, um Abfragen zu k�nnen, ob das Objekt wirklich gesehen wird.
			//Wird das Objekt nicht gesehen, sind die Distanzen und Winkel alt. 
			seeBall				= false;
			seeGoal				= false;
			seeCornerGoalRight	= false;
			seeCornerGoalLeft	= false;
				seePenaltyCenter 	= false;
				seeGoalCenter	 	= false;

			//Im NameFlags-Array nach den Objekten suchen.
			for(int i=0; i<NameFlags.length;i++){

				//Nach dem Tor (Flag in der Mitte des Tores) suchen
				if(NameFlags[i].contains(goalName)){
					seeGoal=true;
					goalDist=ValueFlags[i][0];
					goalAngle=ValueFlags[i][1];
				}

				//Nach dem linken Pfosten suchen
				if(NameFlags[i].contains(cornerGoalLeft)){
					cornerGoalLeftDist=ValueFlags[i][0];
					cornerGoalLeftAngle=ValueFlags[i][1];
					seeCornerGoalLeft=true;
				}

				//Nach dem rechten Pfosten suchen
				if(NameFlags[i].contains(cornerGoalRight)){
					cornerGoalRightDist=ValueFlags[i][0];
					cornerGoalRightAngle=ValueFlags[i][1];
					seeCornerGoalRight=true;
				}

				//Nach dem Ball suchen
				if(NameFlags[i].equals("(B)")||NameFlags[i].equals("(b)")){
					//Sehen des Balls
					seeBall=true;
					//Der Ball wird gesehen. Setzen der globalen Variablen
					ballDist=ValueFlags[i][0];
					ballAngle=ValueFlags[i][1];
					ballSpeed=ValueFlags[i][2];

					//Berechnen der Absoluten Ball-Position
					double ballPosition[];
					ballPosition=calculate_object_position(ballDist, ballAngle);
					//Speichern in den globalen Variablen
					ballX=ballPosition[0];
					ballY=ballPosition[1];

				}

						//Nach dem Strafraumlinienmittelpunkt suchen
				if(NameFlags[i].equals(penaltyCenter)){
					seePenaltyCenter	= true;
					penaltyCenterDist	= ValueFlags[i][0];
					penaltyCenterAngle	= ValueFlags[i][1];
				}

						//Nach dem eigenen Tormittelpunkt suchen
				if(NameFlags[i].equals(goalCenter)){
					seeGoalCenter	= true;
					goalCenterDist	= ValueFlags[i][0];
					goalCenterAngle	= ValueFlags[i][1];
				}
			}
		}
	}

	public void parse(String msg) throws Exception{

		if(msg.contains("(init")){
			parse_init(msg);
		}

		if(msg.contains("(hear")){
			parse_hear(msg);
		}

		if(msg.contains("(sense_body "))
		{
			parse_sense(msg);	
		}

		if(msg.contains("(see "))
		{	
			parse_see(msg);

			if(play)
			{
				if(playerTypes[NumberPlayer-1].contains("striker")){
					tactic_striker();

				}else if(playerTypes[NumberPlayer-1].contains("keeper")){
					tactic_keeper();
				}
				if(playerTypes[NumberPlayer-1].contains("defense")){
					tactic_defense();
				}

			}else
			{
				//Startposition aus dem Array 
				move( playerPosition[NumberPlayer-1][0], playerPosition[NumberPlayer-1][1] );
			}
		}


		
		// Aktualisieren der "alten" Ballkoordinaten
		lastBallX = ballX;
		lastBallY = ballY;

		
		// Aktualisieren der "alten" Spieler-Koordinaten
		lastX = x;
		lastY = y;

		//System.out.println(msg);
	}




	public void turn(double angle) throws Exception{
		send("(turn "+angle+")");
		Thread.sleep(100);
	}
	public void dash(double power) throws Exception{
		send("(dash "+power+")");
		Thread.sleep(100);
	}
	public void move(double x, double y) throws Exception{
		send("(move "+x+" "+y+")");
		Thread.sleep(100);
	}
	public void kick(double power, double angle) throws Exception{
		send("(kick "+power+" "+angle+")");
		Thread.sleep(100);
	}
	public void catch_ball(double angle) throws Exception{ 
		send("(catch "+angle+")");
		Thread.sleep(100);
	}
	public void say_message(String msg)throws Exception{
		send("(say "+msg+")");	
	}



}





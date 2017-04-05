import java.util.Scanner;

public class Main {	
  private static String HELP = "-help";
  private static String RESET = "-reset";	
	
  private static String WELCOME = "Welcome to Checkers!";
  private static String INTRO = "Starting new Game, type " + HELP
  	+ " for a list of commands";
  private static String TO_PLAY = " to play:";
  private static String HELP_DISPLAY = 
    HELP + ": display list of commands\n" + 
    RESET + ": resets the board, and starts a new game\n" + 
    "To make a move, input the square of the piece you want to move,\n" + 
    "then the square you want to move it to. For example:\n" + 
    "D6 E5\n" +
    "To Jump, enter the sequence of spaces you want to jump through:\n" + 
    "D6 F4 D2\n";
  private static String RESET_DISPLAY = "Resetting!";

		  
  
	
  public static void main(String[] args){
	  System.out.println(WELCOME);
	  System.out.println(INTRO);
	  
	  
	  Board b = new Board();
	  Scanner sc = new Scanner(System.in);
	  
	  while(true){
		  System.out.println(b.toString());
		  System.out.println(b.player() + TO_PLAY);
		  String cmd = sc.nextLine();
		  if (cmd.toLowerCase().contains(HELP.toLowerCase())){
			  System.out.printf(HELP_DISPLAY);
		  } else if (cmd.toLowerCase().contains(RESET.toLowerCase())){
			  System.out.println(RESET_DISPLAY);
			  b.reset();
		  } else {
			  try {
			    String[] spaces = cmd.split("\\s+");
			    int[] si = new int[spaces.length * 2];
			    for(int i = 0; i < spaces.length; i++){
				  String s = spaces[i].toLowerCase();
				  si[(i*2)] = (int) (s.charAt(0) - 1);
				  si[(i*2) + 1] = ((int) s.charAt(1)) - 1;
			    }
			    System.out.println(si.toString());
			    if (spaces.length < 2){
			    	System.out.println("Input too short!");
			    }
			    if (spaces.length == 2){
			    	if (b.isValidMove(si)){
			    		b.move(si);
			    	} else {
			    		System.out.println("invalid move!");
			    	}
			    } else {
			    	if (b.isValidJump(si)){
			    		b.jump(si);
			    	} else {
			    		System.out.println("invalid jump!");
			    	}
			    }
			  } catch (IndexOutOfBoundsException e){
				System.out.println("Couldn't parse command. try again.");
			  }
		  }
	  }

  }
}

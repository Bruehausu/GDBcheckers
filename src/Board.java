
//Board class. contains majority of logic.
public class Board {
  private static int SIDE_LEN = 8;
  private static int SIZE = SIDE_LEN * SIDE_LEN;
  
  //starting places for Men for white and red respectively
  private int[][] wStart ={
    {0, 0}, {2, 0}, {4, 0}, {6, 0},
    {1, 1}, {3, 1}, {5, 1}, {7, 1},
    {2, 0}, {2, 2}, {4, 2}, {6, 2}
  };
  private int[][] rStart ={
    {1, 5}, {3, 5}, {5, 5}, {7, 5},
	{0, 6}, {2, 6}, {4, 6}, {6, 6},
	{1, 7}, {3, 7}, {5, 7}, {7, 7}
  };
	
  private Piece[] board;
  private Color curr_player;
  
  //sets a space on the board
  private void set(Piece p, int x, int y){
	  if (x < 0 || x >= SIDE_LEN || y < 0 || y >= SIDE_LEN){
		  throw new ArrayIndexOutOfBoundsException(
				   "tried to set x: " + x + " y: " + y);
	  }
	  
	  int i = x + (SIDE_LEN * y);
	  
	  this.board[i] = p;
	  
  }
  
  //gets a piece on the board. returns null if empty 
  private Piece get(int x, int y){
	 return this.board[x + (SIDE_LEN * y)];
  }
  
  //constructor: creates an empty board.
  public Board(){
	  board = new Piece[SIZE];
	  curr_player = Color.R;
	  this.reset();
  }
  
  //creates text representation of board. used in main
  public String toString(){
	  StringBuilder sb = new StringBuilder();
	  for(int i = 0; i < SIDE_LEN; i++){
		  sb.append((i+1) + " ");
		  for(int j = SIDE_LEN-1; j >=0 ; j--){
			  Piece p = this.get(j, i);
			  if (p == null) {
				  sb.append("  ");
			  } else {
				  sb.append(p.toString() + " ");
			  }
		  }
		  sb.append("\n");
	  }
	  sb.append("  ");
	  for(int j = SIDE_LEN-1; j >=0 ; j--){
		  sb.append((char) (((int) 'A') + j));
		  sb.append("  ");
	  }
	  sb.append("\n");
	  return sb.toString();
  }
  
  //resets the board to the game's starting state
  public void reset(){
	  for(int i = 0; i < SIZE; i++){
		  this.board[i] = null;
	  }
	  for(int i = 0; i < wStart.length; i++){
		  this.set(new Man(Color.W), wStart[i][0], wStart[i][1]);
		  this.set(new Man(Color.R), rStart[i][0], rStart[i][1]);
	  }
	  this.curr_player = Color.R;
	  
  }
  
  //string representation of current player color
  public String player(){
	  return (curr_player == Color.R)?"RED":"WHITE";
  }
  
  private boolean isValidSquare(int x, int y){
	  return (x >= 0 && x < SIDE_LEN && y >= 0 && y < SIDE_LEN);
  }
  
  //changes the current player
  private void nextTurn(){
	  curr_player = (curr_player == Color.R)?Color.W:Color.R;
  }
  
  /*
   * commands are represented in-code as a sequence of x-y locations on
   * the board in an array i.e. [x1, y1, x2, y2 ...]
   */
  
  //checks if a piece of color c could jump from (x,y) to (xx,yy)
  public boolean canJump(Color c, int x, int y, int xx, int yy){
	  int off = (curr_player==Color.R)?-2:2;
	  
	  //check movement is correct
	  if (yy != y + off) return false;
      if (! ((x == xx + 2 ) || (x == xx - 2 ))) return false;
      
      //check (xx,yy) is empty and that the jumped space contains
      //an enemy piece
      Piece a = this.get(xx, yy);
      Piece b = this.get((x + xx)/2, (y + yy)/2);
      return (a == null && b != null && b.get_color() != c);
  }
  
  //sees if a piece can jump either way from (x,y)
  public boolean canJumpFrom(Color c, int x, int y){
	  int off = (c==Color.R)?-2:2;
	  return (canJump(c, x, y, x + 2, y + off) ||
			  canJump(c, x, y, x - 2, y + off));
  }
  
  //checks if a jump command is valid
  public boolean isValidJump(int[] si){
	  if (si.length < 4 || si.length % 2 != 0 ||
			  !(isValidSquare(si[0],si[1]))) return false;
	  Piece a = this.get(si[0], si[1]);
	  if (a == null || a.get_color() != curr_player) return false;
	  
	  int last_x = si[0]; 
	  int last_y = si[1];
	  for(int i = 2; i < si.length; i++){
		  int x = si[i];
		  i++;
		  int y = si[i];
		  if (! this.canJump(curr_player, last_x, last_y, x, y)) return false;
		  if (! isValidSquare(x, y)) return false;
		  last_x = x;
		  last_y = y;
	  } 
	  return !(canJumpFrom(curr_player, last_x, last_y));
  }
  
  //executes a jump command
  public void jump(int[] si){
	  Piece a = this.get(si[0], si[1]);
	  this.set(null, si[0], si[1]);
	  
	  int last_x = si[0];
	  int last_y = si[1];
	  for(int i = 2; i < si.length; i++){
		  int x = si[i];
		  i++;
		  int y = si[i];
		  this.set(null, (x + last_x)/2, (y + last_y)/2);
		  last_x = x;
		  last_y = y;
	  } 
	  this.set(a, last_x, last_y);
	  this.nextTurn();
  }
  
  private boolean anyCanJump(Color c){
	  for(int i = 0; i < SIDE_LEN; i++){
		  for(int j = 0; j < SIDE_LEN; i++){
			  Piece p = get(i,j);
			  if (p != null && p.get_color() == c &&
					  canJumpFrom(c,i,j)) return true;
		  }
	  }
	  return false;
  }
  
  //checks if a move is valid
  public boolean isValidMove(int[] si){
	  if (anyCanJump(curr_player) || (si.length != 4)||
		  !(isValidSquare(si[0], si[1]))) return false;
	  
	  //check movement is correct
	  if (! ((si[2] == si[0] + 1 )|| (si[2] == si[0] - 1 ))) return false;
	  int off = (curr_player==Color.R)?-1:1;
	  if ((si[3]) != si[1] + off) return false;
	  
	  //checks spaces are correct
	  Piece a = this.get(si[0], si[1]);
	  Piece b = this.get(si[2], si[3]);
	  return (a != null && a.get_color() == curr_player && b == null);
  }
  
  //executes a move
  public void move(int[] si){
	  Piece a = this.get(si[0], si[1]);
	  this.set(a, si[2], si[3]);
	  this.set(null, si[0], si[1]);
	  this.nextTurn();
  }
	
}

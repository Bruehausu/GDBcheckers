
//basic checkers pieces
//possibly step checking logic should be in here?
public class Man implements Piece{
  private Color m_c;
  
  //constructor
  public Man(Color c){
	  this.m_c = c;
  }
	
  //piece color
  public Color get_color(){
	  return m_c;
  }
  
  //to string
  public String toString(){
	  return (m_c == Color.W)?"W ":"R ";
  }
  
	
}

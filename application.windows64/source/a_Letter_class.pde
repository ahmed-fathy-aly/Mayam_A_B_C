class Letter
{
  // the character it represents
  String character;
  boolean is_hidden;
  boolean bounce_more;
  
  // stuff we need to draw it
  float x_pos;
  float y_pos;
  float size;
  Color colour;
  
  // stuff to make it shaky
  boolean is_shaky;
  float angle;
  float angular_vel;
  float min_angle;
  float max_angle;
  
  // stuff to make it bouncy
  boolean is_bouncy;
  float min_hop;
  float bounce_vel;
  float bounce_acc;
  float bounce_rebound_vel;
  
  // stuff to make it travel
  boolean is_travelling ;
  float speed_coeff;
  float x_dest;
  float y_dest;
  
  // stuff to make it expand
  boolean is_changing_size;
  float dest_size;
  
  // stuff to make it change its color
  boolean is_changing_color;  
  Color destination_color;
  
  // stuff to make it return to its original form if the mouse is over then not
  boolean returning_to_original;
  
  // stuff for sound
  boolean played_sound;
  
  // default constructor
  Letter()
  {
  }
  
  
  
  // constructor
  Letter(String chr, float x, float y, float s, Color c)
  {
    character = chr;
    is_hidden = false;
    x_pos = x;
    y_pos = y;
    size = s;
    colour = c;
    
    // stuff to make it shaky
    is_shaky = true;
    angle = 0;
    angular_vel = PI / 150;
    min_angle =  - PI / 12;
    max_angle =  PI / 12;
    
    // stuff to make it bouncy
    is_bouncy = true;
    min_hop = y_pos + HEIGHT / 100;
    bounce_vel = 0;
    bounce_acc = HEIGHT / 2000;
    bounce_rebound_vel = - HEIGHT / 120;
    bounce_more = true;
    
    // stuff to make it expand
    boolean is_changing_size = false;
    float dest_size = 0;

    // stuff to make it travel
    is_travelling = false;
    speed_coeff = 0.05;  
    x_dest = x;
    y_dest = x;  
        
    is_changing_color = false;
    
    returning_to_original = false;
    
    // stuff for sound
    played_sound = false;
    
    
    
  }
  
  
  
  // draws the character in its position
  void draw()
  {
    textAlign(CENTER, CENTER);
    
    pushMatrix();
    translate(x_pos, y_pos);
    rotate(angle);
    // draw a frame for the text
    textSize(size+15);
    fill( color(0, 200) - colour.c);
    if (!is_hidden)
      text(character, 0, 0);
    else
      text('?', 0, 0);    
    // draw the letter
    textSize(size);
    fill(colour.c);
    if(! is_hidden)
      text(character, 0, 0);    
    else
      text('?', 0, 0);          
    popMatrix();
  
  }
  
  
  // updates the position and angle of the character
  void update()
  {
    // update the angular stuff
    if (is_shaky)
    {
      angle += angular_vel;
      if (angle > max_angle)
        angular_vel = -angular_vel;
      else if (angle < min_angle)
        angular_vel = -angular_vel;
    }
      
    // update the stuff that bounces it
    if(is_bouncy && !is_travelling)
    {
      bounce_vel += bounce_acc;
      y_pos += bounce_vel;
      // when it reaches the ground
      if (y_pos >= min_hop)
        bounce_vel =   bounce_rebound_vel; 
    }
   
   // update the stuff the makes it expand
   if (is_changing_size)
    {
      float increase = (dest_size - size) / 20;
      size += increase;
      
      // check if it nearly reached destination size
//      if(abs(dest_size - size) < dest_size / 20)
//        is_changing_size = false;
    } 
  
    // update the stuff to make it travel
    if (is_travelling)
    {
      // advance in directionof destination
      x_pos += (x_dest - x_pos) * speed_coeff;
      y_pos += (y_dest - y_pos) * speed_coeff;
      
      // check if reached
      if (dist(x_pos, y_pos, x_dest, y_dest) < 5)
      {
        is_travelling = false;
        x_pos = x_dest;
        y_pos = y_dest;
        min_hop = y_pos + HEIGHT / 100;
      }
    }
  
    // update the stuff that makes it change its color
    if(is_changing_color)
    {
      colour.change_to(destination_color, 0.02);
    }  
  }
  
  
  // when called, the letter keep exapnding or shrinking to that size
  void change_size_to(float s)
  {
    is_changing_size = true;
    dest_size = s;
  }
  
  
  // changes the color to another color
  void change_color_to(Color c2)
  {
    destination_color = new Color(c2.r, c2.g, c2.b, c2.opacity);
    is_changing_color = true;
  }
  
  // returns true if the mouse is over on the letter
  boolean is_mouse_over()
  {
 
    float x_dist = abs(mouseX - x_pos) ;
    float y_dist = abs(mouseY - y_pos) ;
    
    return (x_dist < size * character.length() * 0.7 / 2 && y_dist < size * 1.1 / 2);
  }
  
  // manges stuff done when mouse is over
  // change size to a destination size and make the letter more bouncy
  void handle_mouse_over(float original_size, float dest_size)
  {
    if(is_mouse_over())
    {
      if (sound_on && played_sound == false && is_hidden == false && wait_sound == 0)
      {
        alphapet[int(char(character.charAt(0))) - 65].play();
        played_sound = true;
        wait_sound = 40;
      }
      change_size_to(dest_size);
      if(bounce_more)
        bounce_rebound_vel = - HEIGHT / 80;
      returning_to_original = true;
        
    }
    else
    {
        if(played_sound == true)
          played_sound = false;
       if(returning_to_original)
      {
        change_size_to(original_size);
        bounce_rebound_vel =  - HEIGHT / 120;
        returning_to_original = false;
      }
    }    
  }
  
  void travel_to(float x, float y)
  {
    is_travelling = true;
    x_dest = x;
    y_dest = y;

  }
  
}

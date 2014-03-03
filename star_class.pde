/*
* A view that pops up once you get some letters correclty in a row
*/

class Star
{
  PImage star;  
  // stuff we need to draw it
  float x_pos;
  float y_pos;
  
  // stuff to make it shaky
  boolean is_shaky;
  float angle;
  float angular_vel;
  float min_angle;
  float max_angle;
  

  
  // stuff to make it travel
  boolean is_travelling ;
  float speed_coeff;
  float x_dest;
  float y_dest;
  


  
  // default constructor
  Star()
  {
  }
  
  
  
  // constructor
  Star(PImage star_image, float x, float y)
  {
    star = star_image;
    x_pos = x;
    y_pos = y;
    
    // stuff to make it shaky
    is_shaky = true;
    angle = 0;
    angular_vel = PI / 100;
    min_angle =  - PI * 10;
    max_angle =  PI * 10;
    

    // stuff to make it travel
    is_travelling = false;
    speed_coeff = 0.1;  
    x_dest = x;
    y_dest = x;  
        

    
    
  }
  
  
  
  // draws the character in its position
  void draw()
  {
    textAlign(CENTER, CENTER);
    
    pushMatrix();
    translate(x_pos, y_pos);
    rotate(angle);
    image(star, 0, 0);        
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

      }
    }
  
  }
  

  void travel_to(float x, float y)
  {
        if(sound_on)
          star_sound.play();
    is_travelling = true;
    x_dest = x;
    y_dest = y;

  }
  
}

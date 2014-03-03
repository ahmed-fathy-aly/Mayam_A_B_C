// represents a scroll bar which returns a value between 0 - 1

class Scroll_levels
{
  // value between 0 - 1
  float value;
  int current_level;
  int number_of_levels;
  
  // stuff needed to draw it
  float x1;
  float x2;
  float y1;
  float y2;
  float bar_width;
  color point_color;
  color ball_secondary_color;
  color bar_color;
  
  // stuff to adjust it
  boolean adjusting;
  
  
  Scroll_levels()
  {
  }
  
  
  Scroll_levels(int n_levels, float x_pos1, float y_pos1, float x_pos2, float y_pos2,  color colour_of_point, color mouse_over_color, color color_of_bar)
  {
    
    // stuff to draw it
    x1 = x_pos1;
    y1 = y_pos1;
    x2 = x_pos2;
    y2 = y_pos2;    
    point_color = colour_of_point;
    ball_secondary_color = mouse_over_color;
    bar_color = color_of_bar;
    
    bar_width = map(dist(x1, y1, x2, y2), 0, 1000, 1, 20);
    
    // stuff to adjust it
    adjusting = false;
    
    // stuff for its value
    number_of_levels = n_levels;
    current_level = int(number_of_levels / 2);
  }
  
  
  // draws the bar as a line with a point 
  void draw()
  {
    // draw the bar
    strokeWeight(bar_width);
    stroke(bar_color);
    line(x1, y1, x2, y2);
    // draw small points in each level location
    
    
    // draw the ball
    float ball_x = x1 + (x2 - x1) * value;
    float ball_y = y1 + (y2 - y1) * value;
    
    
    strokeWeight(3);
    stroke(0);
    if (adjusting == false && is_mouse_over() == false)
      fill(point_color);
    else
      fill(ball_secondary_color);
    ellipse(ball_x, ball_y, bar_width*3, bar_width*3);
  
    if(adjusting == true)
      adjust();
      
    if(mousePressed == false)
      adjusting = false;
            
  }
  
  
  // ajusts the value according ot the position of the mouse
  void adjust()
  {
    float new_x = mouseX;
    if (x1 < x2)
    {
      if (new_x < x1)
        new_x = x1;
      else if (new_x > x2)
        new_x = x2;      
    }
    else if (x2 < x1)
    {
      if (new_x > x1)
        new_x = x1;
      else if (new_x < x2)
        new_x = x2;      
    }
    
    float new_y = mouseY;
    if (y1 < y2)
    {
      if (new_y < y1)
        new_y = y1;
      else if (new_y > y2)
        new_y = y2;      
    }
    else if (y2 < y1)
    {
      if (new_y > y1)
        new_y = y1;
      else if (new_y < y2)
        new_y = y2;      
    }    
   
    float real_value = dist(x1, y1, new_x, new_y) / dist(x1, y1, x2, y2);
    float real_level = real_value * number_of_levels;
    float unreal_level = int(real_level) ;
    value =  unreal_level / number_of_levels;
    
  }
  
  // returns true if the user pressed on the point 
  boolean is_mouse_over()
  {
    float ball_x = x1 + (x2 - x1) * value;
    float ball_y = y1 + (y2 - y1) * value;
    
    return (dist(ball_x, ball_y, mouseX, mouseY) < bar_width * 3);
  }
  
  // if the mouse is pressed sees it's affecting the bar or not
  // if it's affectign then pyt it into adjusting mode
  void handle_mouse_pressed()
  {
    if (is_mouse_over() && mousePressed == true)
    {
      adjusting = true;
    }
  }
  
}

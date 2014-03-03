class Color
{
  float r;
  float g;
  float b;
  float opacity;
  
  color c;
  
  Color(float red, float green, float blue, float op)
  {
    r = red;
    g = green;
    b = blue;
    opacity = op;
    c = color(r, g, b, opacity);
    
  }
  
  
  // changes the color to another color C2 with percentage
  // 1 means that Color changes to C2 completely
  void change_to(Color C2, float percentage)
  {
    r += (C2.r - r) * percentage;
    g += (C2.g - g) * percentage;
    b += (C2.b - b) * percentage; 
   
    c = color(r, g, b, opacity);
  }
  
}

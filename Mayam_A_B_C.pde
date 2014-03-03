/*
* Author : Ahmed Fathy Aly
* for my my cousin Maram (Mayam)
* Mayam ABC is an interactive game to teach kids the alphapet
*/


Game_state state;
PImage b;
void setup()
{
  assign_data();
  size(int(WIDTH), int(HEIGHT));

  background(255);
  
  b= loadImage("background1.jpg");
  b.resize(int(WIDTH), int(HEIGHT));
  textFont(comic);
  
  state.start_round();
  

  
}

void draw()
{
  // the background sound
    background_music.play();
  if (wait_sound >0)
    wait_sound--;
  // The background
  image(b, width / 2, height/ 2);
 

  if (state.status == "playing")
  {
    // draw and update the character
    state.draw_stars();
    state.draw_questions();
    state.draw_choices();
  }
  else if (state.status == "celebrating")
  {
    state.draw_stars();
  state.draw_round_ended();   
  }
}

void mousePressed()
{
  // check which letter from choices was pressed
  for (int i = 0; i < state.choices.length; i++)
  {
    if (state.choices[i].is_mouse_over())
      state.handle_choice(state.choices[i]);
  }
  
}

void keyPressed()
{

}

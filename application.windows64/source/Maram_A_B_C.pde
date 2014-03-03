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
    background_music.play();

  if (wait_sound >0)
    wait_sound--;
  image(b, width / 2, height/ 2);
  // clear the background
  //background(255, 250);

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

Game_state state;
PImage b;
void setup()
{
  size(1000, 700);
  WIDTH = width;
  HEIGHT = height;
  background(255);
  
  assign_data();
  state.start_round();
  b= loadImage("background1.jpg");
  textFont(comic);

  
}

void draw()
{
  image(b, 0, 0);
  // clear the background
  //background(255, 250);
  if (state.status == "playing")
  {
    // draw and update the character
    state.draw_questions();
    state.draw_choices();
  }
  else if (state.status == "celebrating")
  {
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
float WIDTH;
float HEIGHT;
PFont comic;

// game data
Letter A;
Letter B;
Letter C;

// constans for the letters
Color default_colour = new Color(180, 50, 180, 250);
Color choice_color = new Color(180, 180, 50, 250);
float default_size = 100;

// initiates the variables
void assign_data()
{
  // load the font
  comic = loadFont("ComicSansMS-200.vlw");
  
  A = new Letter("A", WIDTH / 4, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  B = new Letter("B", WIDTH / 2, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  C = new Letter("C", WIDTH * 3 / 4, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  
  choices_colors = color_list_2(number_of_colours, choices_possibilities, probabilities);
  
  state = new Game_state();
  
}

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
      change_size_to(dest_size);
      if(bounce_more)
        bounce_rebound_vel = - HEIGHT / 80;
      returning_to_original = true;
        
    }
    else
    {
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
class Game_state
{
  // stuff about the words
  Letter[] question;
  Letter[] choices;
  Letter[] junk;
  int junk_size = 0;
  Letter pointer;
  
  // stuff that makes it like a state machine
  boolean in_play;
  String status;
  int celebrating_counter;
  int incorrect_counter;
  int level_change_counter;
  
  // a number which is totally useless 
  int first_letter;
  
  // The scroll bar that chooses the level
  Scroll_levels scroll_bar;
  int number_of_choices;
  int number_of_hidden;

  Game_state()
  {
    status = "playing";
    question = new Letter[6];
    for (int i = 0; i < question.length; i++)
        question[i] = new Letter(" ", 0, HEIGHT / 4, 1, new Color(250, 250, 250, 170));
    first_letter = 65;
    in_play = false;
    pointer = new Letter("^", 0, 0, default_size , new Color(240, 20, 20, 170));
    pointer.is_shaky = false;
    pointer.bounce_rebound_vel = - HEIGHT / 300;
    pointer.bounce_acc = HEIGHT / 4000;
    junk = new Letter[100];
    celebrating_counter = 0;
    scroll_bar = new Scroll_levels(9, WIDTH * 0.22, HEIGHT * 0.1, WIDTH * 0.75, HEIGHT * 0.1, color(30, 50, 120, 250), color(200, 30, 30), color(30, 50, 80, 100));
    scroll_bar.value = 0.5;
    number_of_choices = 4;
    number_of_hidden = 3;
    level_change_counter = 0;
  }
  
  // starts a round by
  // - generating random sequence of letters, some are hidden
  // search for the first hidden one
  // put a ^ under the first hidden letter
  // generate answers for the first letter
  void start_round()
  {
    // Calculate te difficulty
    number_of_choices = int(map(scroll_bar.value , 0, 1, 2, 7));
    number_of_hidden = int(map(scroll_bar.value , 0, 1, 1, 5));
    

    
    // generate a sequence
    question = generate_letters_sequence(6, number_of_hidden, first_letter);
    
    // find the first hidden letter
    Letter hidden = first_hidden(question);
    pointer.travel_to(hidden.x_pos, hidden.y_pos + 100);
 
    // generate answers for teh first letter
    choices = get_choices(hidden.character, number_of_choices);    
  }
  
  
  // returns the first letter that is hidden in an array of letters
  Letter first_hidden(Letter[] letters)
  {
    Letter result = new Letter();
    for (int i = 0;i < letters.length; i++)
    {
      if(letters[i].is_hidden)
      {
        result =  letters[i];
        break;
      }
    }
    return result;
  }
  
  // returns the index of first letter that is hidden in an array of letters
  int first_hidden_index(Letter[] letters)
  {
    int result = -1;
    for (int i = 0;i < letters.length; i++)
    {
      if(letters[i].is_hidden)
      {
        result =  i;
        break;
      }
    }
    return result;
  }  
  
  
  
  // handles what happens when a choice is choosed
  void handle_choice(Letter choice)
  {

    // if the choice is right
    if ( choice.character == first_hidden(question).character )
    {
      // add the qestion mark to the buffer and make it go away
      Letter question_mark = question[first_hidden_index(question)];
      Letter junked_question_mark  = new Letter(question_mark.character, question_mark.x_pos, question_mark.y_pos, question_mark.size, question_mark.colour);
      junked_question_mark.is_hidden = true;
      junk[junk_size] = junked_question_mark;
      junk[junk_size].change_size_to(-1);  
      junk[junk_size].travel_to(- 10, - 10);      
      junk_size++;
      
      // add the right choice to the questions
      int index = first_hidden_index(question);
      question[index] = new Letter(choice.character, choice.x_pos, choice.y_pos, choice.size, choice.colour);
      question[index].bounce_more = false;
      question[index].is_bouncy = false;      
      question[index].is_shaky = false;            
      question[index].travel_to(question_mark.x_pos, question_mark.y_pos);
      question[index].change_color_to(question_mark.colour);
     
      
      choice.character = " ";
      
      
      // put the choices to the junk and make them go away
      for (int i = 0; i < choices.length; i++)
      {
        Letter junked_choice = new Letter(choices[i].character, choices[i].x_pos, choices[i].y_pos, choices[i].size, choices[i].colour);
        choices[i].character = " ";
        junk[junk_size] = junked_choice;
        junk[junk_size].travel_to(random(3 * WIDTH) - WIDTH, random(HEIGHT) + HEIGHT + 200);
        junk[junk_size].speed_coeff = 0.015;    
        junk_size ++;
      }
      
      
      // if round didn't end
      if (first_hidden_index(question) != -1)
      {
        // move the pointer to the next position
        pointer.travel_to(first_hidden(question).x_pos, first_hidden(question).y_pos + 100);
        
        // generate choices
        choices = get_choices(first_hidden(question).character, number_of_choices);
        
      }
      // if the round ended
      else
      {
        // make all the question bouncy and big
        for (int i = 0; i < question.length; i++)
        {
          question[i].change_size_to(WIDTH / 5);          
//          if (question[i].is_travelling == false)
//            question[i].travel_to(question[i].x_pos, question[i].y_pos - HEIGHT / 15);
//          else
//           question[i].travel_to(question[i].x_dest, question[i].y_dest - HEIGHT / 15);
        }
        

        // move the poiter away        
        pointer.travel_to(5, 5);
        
        // change the status
        status = "celebrating";
        celebrating_counter = 100;
        
        // compute the first letter of the next sequence
        if(first_letter < 65 + 26 - 6)
        {
          first_letter += 5;

        }
        else
        {
          first_letter = 65;
          if (scroll_bar.value < 1)
              scroll_bar.value += 1.0/ scroll_bar.number_of_levels;
        }
      } 
    }
    else
    {
      // if the choice is wrong
      incorrect_counter = 50;
    }
  }
  
  
  
  // draws stuff on the screen when the round has ended
  void draw_round_ended()
  {
    // draw the stuff in the junk
    for(int i = 0; i < junk_size; i++)
    {    
      junk[i].update();
      junk[i].draw();
    }
    
    // draw the question
    for(int i = 0; i < state.question.length; i++)
    {    
      question[i].update();
      question[i].draw();
      question[i].handle_mouse_over(WIDTH / 5, WIDTH / 5);
    }

    // indicate that the player won
    textSize(WIDTH / 10);    
    fill(220, 20, 20, 170);    
    text("Won the round :D", WIDTH / 2, HEIGHT * 0.7);
    
    // check if celebratign time is over
    celebrating_counter--;
    if (celebrating_counter <= 0)
    {
      status = "playing";
      
      junk = new Letter[100];
      junk_size = 0;
      
      // add the questions to the buffer and make them travel
      for(int i =0; i < question.length; i++)
      {
        junk[junk_size] = new Letter(question[i].character, question[i].x_pos, question[i].y_pos, question[i].size, question[i].colour);
        junk[junk_size].travel_to(junk[junk_size].x_pos + WIDTH*1.1, junk[junk_size].y_pos);
        junk_size++;
      }
      
      start_round();

    }
  }
   
   
  // draws an array of letters represeting the question
  void draw_questions()
  {
    
    // draw the letters
    for(int i = 0; i < state.question.length; i++)
    {    
      question[i].update();
      question[i].draw();
      question[i].handle_mouse_over(default_size, default_size*1.5);
    }
  
    // update the junk
    for(int i = 0; i < junk_size; i++)
    {    
      junk[i].update();
      junk[i].draw();
    }    
  
    // draw the pointer
    pointer.update();
    pointer.draw();
    
    // Draw the scroll bar
    scroll_bar.draw();
    scroll_bar.handle_mouse_pressed();
    textSize(WIDTH / 20);
    text("Level : " + str(int(scroll_bar.number_of_levels * scroll_bar.value)+1), WIDTH * 0.1, HEIGHT * 0.1);
    if (level_change_counter > 0);
      level_change_counter --;
    if((number_of_choices != int(map(scroll_bar.value , 0, 1, 2, 7)) ||  number_of_hidden != int(map(scroll_bar.value , 0, 1, 1, 5))) && level_change_counter <= 0)
    {
      start_round();
      level_change_counter = 20;
    }
  }
  
  
  // draw the aray of choices
  void draw_choices()
  {
    
    for(int i = 0; i < state.choices.length; i++)
    {    
      choices[i].update();
      choices[i].draw();
      choices[i].handle_mouse_over(default_size, default_size * 4);
    } 

    // check if we have to draw incorrect
    if (incorrect_counter > 0)
    {
      textSize(WIDTH / 10);
      text("Wrong :(", mouseX, mouseY);
      incorrect_counter --;
    }    
  }
}
// generates a list of n Letter classes in a sequence
Letter[] generate_letters_sequence(int n, int number_hidden, int first)
{
  Letter[] result = new Letter[n];
  
  // get the first character
  char first_char = char(first);
  result[0] = new Letter(str(first_char), WIDTH / 10, HEIGHT * 0.3, default_size, default_colour);
  result[0].is_shaky = false;
  result[0].bounce_more = false;
  result[0].is_bouncy = false;
  
  

  // generate next character
  for (int i = 0; i < n - 1; i++)
  {
    result[i + 1] = new Letter(str(char(first + i + 1)), WIDTH / 10 + (i + 1)*width / n, HEIGHT * 0.3, default_size, default_colour); 
    //result[i + 1].travel_to(WIDTH / 10 + (i + 1)*width / n, HEIGHT * 0.3);
    result[i + 1].bounce_more = false;
    result[i + 1].is_shaky = false;    
    result[i + 1].is_bouncy = false;
  }
  
  hide_some(result,  number_hidden);
  return result;
}


// hides some objects in a list of Letters
void hide_some(Letter[] characters, int number_hidden)
{
  // hide some character
  int n = characters.length;
  int number_hidden_req = number_hidden;
  number_hidden_req = constrain(number_hidden_req, 0, n );
  int number_hid = 0;
  
  while(number_hid < number_hidden_req)
  {
    int index = int(random(n - 1) + 1);
    if (characters[index].is_hidden == false)
    {
      characters[index].is_hidden = true;
      characters[index].is_shaky = true;      
      number_hid ++;
    }
  }  
}


// creates a list of n Letter classes,
// one of them has charater c
Letter[] get_choices(String c, int n)
{
  if (n <= 1)
    n+= 2;

  Letter[] result = new Letter[n];
  // get the right character
  Color random_color = choices_colors[int(random(number_of_colours - 4))];
  result[0] = new Letter(c, WIDTH / 10, HEIGHT *3 / 4, default_size, new Color(random_color.r, random_color.g, random_color.b, random_color.opacity));
  
  // get some random choices
  int count = 0;
  while(count < n - 1)
  {
    int random_int = int(random(65, 65 + 26));
    char random_char = char(random_int);
    String random_char_str = str(random_char);
    if (random_char != c. charAt(0))
    {
      count++;
      random_color = choices_colors[int(random(number_of_colours - 4))];
      result[count] = new Letter(str(random_char), WIDTH / 10 + count*WIDTH / n, HEIGHT *3 / 4, default_size, new Color(random_color.r, random_color.g, random_color.b, random_color.opacity));
    } 
  }
  
  // replace the right choice(the first one) with any one other
  int second_index = int(random(n));
  String temp = result[0].character;
  result[0].character = result[second_index].character;
  result[second_index].character = temp;
  
  return result;
}
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
// returns a list of Color objects each according to its percentage
// @param probabilities: a list size n where probabilities[i] is probability
// that c[i] occurs
Color[] color_list_2(int n, Color[] c, float[] probabilities)
{
  Color[] result = new Color[n];
  int size = 0;
  
  for(int i = 0; i < c.length; i++)
  {
    // for each color, add it k times to the result where k is
    // teh number of its occurence
    float f_number_of_occurence = probabilities[i] * n;
    int number_of_occurence = int(f_number_of_occurence);
    for(int j = 0; j < number_of_occurence; j++)
    {
      result[size] = c[i];
      size++;
    }
  }
  
  // fill the rest with the first color
  for (int k = 0; k < (n - size); k++)
  {
     result[size] = c[0];
     size++;
  }
  
  return result;
}
 


float opacity = 240;
Color[] choices_possibilities = {new Color(255, 40, 170, opacity),new  Color(255, 0, 0, opacity),new  Color(255, 255, 0, opacity),
                  new Color(150, 255, 0, opacity),new  Color(255, 40, 170, opacity), new Color(255, 150, 0, opacity), new Color(151, 112, 250, opacity)};                  
float[] probabilities = { 0.1 ,0.9 / 6, 0.9 / 6, 0.9 / 6, 0.9 / 6, 0.9 / 6, 0.9 / 6};
Color[] choices_colors;
int number_of_colours = 100;

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


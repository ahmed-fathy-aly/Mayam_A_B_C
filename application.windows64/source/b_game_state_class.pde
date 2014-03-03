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
  int incorrect_x;
  int incorrect_y;
  
  // a number which is totally useless 
  int first_letter;
  
  // The scroll bar that chooses the level
  Scroll_levels scroll_bar;
  int number_of_choices;
  int number_of_hidden;
  
  // stuff about the score
  int score;
  Star[] score_stars;

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
    
    score = 2;
    score_stars = new Star[100];
    score_stars[0] = new Star(stars[int(random(stars.length))], random(WIDTH), random(HEIGHT));
    score_stars[1] = new Star(stars[int(random(stars.length))], random(WIDTH), random(HEIGHT));

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
      if(sound_on)
        fly_sound.play();
      wait_sound = 60;

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
        // add a star
        if (score < 99)
        {
          score_stars[score] = new  Star(stars[int(random(stars.length))], -WIDTH, -HEIGHT);
          score_stars[score].travel_to(random(WIDTH), random(HEIGHT*0.8));
          score ++;
        }
        // play applause sound
        if(sound_on)
            applause.play();

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
      // remove a star
      if (score > 0)
      {
        score_stars[score - 1].travel_to(random(WIDTH), - random(HEIGHT));
        score --;
      }
      if (sound_on)
        wrong_sound.play();
      incorrect_counter = 50;
      incorrect_x = (int)choice.x_pos;
      incorrect_y = (int)choice.y_pos;
      choice.travel_to(random(WIDTH), -random(HEIGHT));
      choice.angular_vel *= 5;
      choice.max_angle *= 100;
      choice.min_angle *= 100;
      choice.speed_coeff /= 5;
      choice.change_size_to(1);
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
    if (level_change_counter > 0)
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
      image(incorrect, incorrect_x, incorrect_y);
      incorrect_counter --;
    }    
  }
  
  // draw the stars
  void draw_stars()
  {
    for (int i = 0; i < score; i++)
    {
      score_stars[i].update();
      score_stars[i].draw();
    }
  }
}

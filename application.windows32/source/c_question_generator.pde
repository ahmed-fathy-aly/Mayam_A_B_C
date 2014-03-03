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
    result[i + 1] = new Letter(str(char(first + i + 1)), WIDTH / 10 + (i + 1)*WIDTH / n, HEIGHT * 0.3, default_size, default_colour); 
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

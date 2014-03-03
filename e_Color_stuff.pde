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


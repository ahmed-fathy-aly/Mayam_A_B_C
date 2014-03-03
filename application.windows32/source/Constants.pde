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
PImage incorrect;
PImage[] stars;

// sound stuff
boolean sound_on;
Maxim maxim;
AudioPlayer applause;
AudioPlayer[] alphapet;
AudioPlayer star_sound;
AudioPlayer fly_sound;
AudioPlayer background_music;
AudioPlayer wrong_sound;

int wait_sound;

// initiates the variables
void assign_data()
{

  String[] line = loadStrings("Scrreen resolution.txt");
  WIDTH = Integer.parseInt(line[0].substring(8));
  HEIGHT = Integer.parseInt(line[1].substring(8));

  // load the font
  comic = loadFont("ComicSansMS-Bold-120.vlw");
  
  A = new Letter("A", WIDTH / 4, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  B = new Letter("B", WIDTH / 2, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  C = new Letter("C", WIDTH * 3 / 4, HEIGHT / 2, 150, new Color(180, 50, 180, 250)); 
  
  choices_colors = color_list_2(number_of_colours, choices_possibilities, probabilities);
  stars = new PImage[7];
  stars[0] =  loadImage("star1.png");
  stars[1] =  loadImage("star2.png");
  stars[2] =  loadImage("star3.png");
  stars[3] =  loadImage("star4.png");
  stars[4] =  loadImage("star5.png");
  stars[5] =  loadImage("star6.png");
  stars[6] =  loadImage("star7.png");
  state = new Game_state();
  incorrect = loadImage("wrong.png");
  incorrect.resize(int(WIDTH / 3), int(HEIGHT / 3));

  
  for (PImage s: stars)
  {
    float extra = random(WIDTH / 20);
    s.resize(int(WIDTH / 20 + extra), int(WIDTH / 20 + extra));
  }

  imageMode(CENTER);
  
  // sound stuff
  sound_on = true;
  maxim = new Maxim(this);
  applause = maxim.loadFile("applause.wav");
  star_sound = maxim.loadFile("star.wav");
  wrong_sound = maxim.loadFile("wrong.wav");
  fly_sound = maxim.loadFile("fly.wav");
  background_music = maxim.loadFile("background.wav");
  background_music.setLooping(true);
  background_music.volume(10);
  fly_sound.setLooping(false);
  star_sound.setLooping(false);
  applause.setLooping(false);
  wrong_sound.setLooping(false);
  
  
  alphapet = new AudioPlayer[26];
  for (int i =0; i < 26; i++)
  {
    alphapet[i] = maxim.loadFile(str(char(65 + i)) + ".wav");
    alphapet[i].setLooping(false);
    alphapet[i].volume(10);
    
  }
  wait_sound = 0;
  
}


import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.IOException; 
import java.io.BufferedInputStream; 
import java.net.MalformedURLException; 
import java.net.URL; 
import javax.sound.sampled.AudioFormat; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.DataLine; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.SourceDataLine; 
import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Maram_A_B_C extends PApplet {

Game_state state;
PImage b;
public void setup()
{
  assign_data();
  size(PApplet.parseInt(WIDTH), PApplet.parseInt(HEIGHT));

  background(255);
  
  b= loadImage("background1.jpg");
  b.resize(PApplet.parseInt(WIDTH), PApplet.parseInt(HEIGHT));
  textFont(comic);
  
  state.start_round();
  

  
}

public void draw()
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

public void mousePressed()
{
  // check which letter from choices was pressed
  for (int i = 0; i < state.choices.length; i++)
  {
    if (state.choices[i].is_mouse_over())
      state.handle_choice(state.choices[i]);
  }
  
}

public void keyPressed()
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
public void assign_data()
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
  incorrect.resize(PApplet.parseInt(WIDTH / 3), PApplet.parseInt(HEIGHT / 3));

  
  for (PImage s: stars)
  {
    float extra = random(WIDTH / 20);
    s.resize(PApplet.parseInt(WIDTH / 20 + extra), PApplet.parseInt(WIDTH / 20 + extra));
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
    alphapet[i] = maxim.loadFile(str(PApplet.parseChar(65 + i)) + ".wav");
    alphapet[i].setLooping(false);
    alphapet[i].volume(10);
    
  }
  wait_sound = 0;
  
}

//The MIT License (MIT)

//Copyright (c) 2013 Mick Grierson, Matthew Yee-King, Marco Gillies

//Permission is hereby granted, free of charge, to any person obtaining a copy\u2028of this software and associated documentation files (the "Software"), to deal\u2028in the Software without restriction, including without limitation the rights\u2028to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\u2028copies of the Software, and to permit persons to whom the Software is\u2028furnished to do so, subject to the following conditions:
//The above copyright notice and this permission notice shall be included in\u2028all copies or substantial portions of the Software.

//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\u2028IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\u2028FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\u2028AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\u2028LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\u2028OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\u2028THE SOFTWARE.

















 
 
 
 

//import android.content.res.Resources;
// import android.app.Activity; 
// import android.os.Bundle; 
// import android.media.*;
// import android.media.audiofx.Visualizer;
// import android.content.res.AssetFileDescriptor;
// import android.hardware.*;


public class Maxim {

    private float sampleRate;

    public final float[] mtof = {
	0f, 8.661957f, 9.177024f, 9.722718f, 10.3f, 10.913383f, 11.562325f, 12.25f, 12.978271f, 13.75f, 14.567617f, 15.433853f, 16.351599f, 17.323914f, 18.354048f, 19.445436f, 20.601723f, 21.826765f, 23.124651f, 24.5f, 25.956543f, 27.5f, 29.135235f, 30.867706f, 32.703197f, 34.647827f, 36.708096f, 38.890873f, 41.203445f, 43.65353f, 46.249302f, 49.f, 51.913086f, 55.f, 58.27047f, 61.735413f, 65.406395f, 69.295654f, 73.416191f, 77.781746f, 82.406891f, 87.30706f, 92.498604f, 97.998856f, 103.826172f, 110.f, 116.540939f, 123.470825f, 130.81279f, 138.591309f, 146.832382f, 155.563492f, 164.813782f, 174.61412f, 184.997208f, 195.997711f, 207.652344f, 220.f, 233.081879f, 246.94165f, 261.62558f, 277.182617f, 293.664764f, 311.126984f, 329.627563f, 349.228241f, 369.994415f, 391.995422f, 415.304688f, 440.f, 466.163757f, 493.883301f, 523.25116f, 554.365234f, 587.329529f, 622.253967f, 659.255127f, 698.456482f, 739.988831f, 783.990845f, 830.609375f, 880.f, 932.327515f, 987.766602f, 1046.502319f, 1108.730469f, 1174.659058f, 1244.507935f, 1318.510254f, 1396.912964f, 1479.977661f, 1567.981689f, 1661.21875f, 1760.f, 1864.655029f, 1975.533203f, 2093.004639f, 2217.460938f, 2349.318115f, 2489.015869f, 2637.020508f, 2793.825928f, 2959.955322f, 3135.963379f, 3322.4375f, 3520.f, 3729.31f, 3951.066406f, 4186.009277f, 4434.921875f, 4698.63623f, 4978.031738f, 5274.041016f, 5587.651855f, 5919.910645f, 6271.926758f, 6644.875f, 7040.f, 7458.620117f, 7902.132812f, 8372.018555f, 8869.84375f, 9397.272461f, 9956.063477f, 10548.082031f, 11175.303711f, 11839.821289f, 12543.853516f, 13289.75f
    };

    private AudioThread audioThread;
    private PApplet processing;

    public Maxim (PApplet processing) {
	this.processing = processing;
	sampleRate = 44100f;
	audioThread = new AudioThread(sampleRate, 4096, false);
	audioThread.start();
	    
    }

    public float[] getPowerSpectrum() {
	return audioThread.getPowerSpectrum();
    }

    /** 
     *  load the sent file into an audio player and return it. Use
     *  this if your audio file is not too long want precision control
     *  over looping and play head position
     * @param String filename - the file to load
     * @return AudioPlayer - an audio player which can play the file
     */
    public AudioPlayer loadFile(String filename) {
	// this will load the complete audio file into memory
	AudioPlayer ap = new AudioPlayer(filename, sampleRate, processing);
	audioThread.addAudioGenerator(ap);
	// now we need to tell the audiothread
	// to ask the audioplayer for samples
	return ap;
    }

    /**
     * Create a wavetable player object with a wavetable of the sent
     * size. Small wavetables (<128) make for a 'nastier' sound!
     * 
     */
    public WavetableSynth createWavetableSynth(int size) {
	// this will load the complete audio file into memory
	WavetableSynth ap = new WavetableSynth(size, sampleRate);
	audioThread.addAudioGenerator(ap);
	// now we need to tell the audiothread
	// to ask the audioplayer for samples
	return ap;
    }
    // /**
    //  * Create an AudioStreamPlayer which can stream audio from the
    //  * internet as well as local files.  Does not provide precise
    //  * control over looping and playhead like AudioPlayer does.  Use this for
    //  * longer audio files and audio from the internet.
    //  */
    // public AudioStreamPlayer createAudioStreamPlayer(String url) {
    //     AudioStreamPlayer asp = new AudioStreamPlayer(url);
    //     return asp;
    // }
}




/**
 * This class can play audio files and includes an fx chain 
 */
public class AudioPlayer implements Synth, AudioGenerator {
    private FXChain fxChain;
    private boolean isPlaying;
    private boolean isLooping;
    private boolean analysing;
    private FFT fft;
    private int fftInd;
    private float[] fftFrame;
    private float[] powerSpectrum;

    //private float startTimeSecs;
    //private float speed;
    private int length;
    private short[] audioData;
    private float startPos;
    private float readHead;
    private float dReadHead;
    private float sampleRate;
    private float masterVolume;

    float x1, x2, y1, y2, x3, y3;

    public AudioPlayer(float sampleRate) {
	fxChain = new FXChain(sampleRate);
	this.sampleRate = sampleRate;
    }

    public AudioPlayer (String filename, float sampleRate, PApplet processing) {
	//super(filename);
	this(sampleRate);
	try {
	    // how long is the file in bytes?
	    //long byteCount = getAssets().openFd(filename).getLength();
	    File f = new File(processing.dataPath(filename));
	    long byteCount = f.length();
	    //System.out.println("bytes in "+filename+" "+byteCount);

	    // check the format of the audio file first!
	    // only accept mono 16 bit wavs
	    //InputStream is = getAssets().open(filename); 
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

	    // chop!!

	    int bitDepth;
	    int channels;
	    boolean isPCM;
	    // allows us to read up to 4 bytes at a time 
	    byte[] byteBuff = new byte[4];

	    // skip 20 bytes to get file format
	    // (1 byte)
	    bis.skip(20);
	    bis.read(byteBuff, 0, 2); // read 2 so we are at 22 now
	    isPCM = ((short)byteBuff[0]) == 1 ? true:false; 
	    //System.out.println("File isPCM "+isPCM);

	    // skip 22 bytes to get # channels
	    // (1 byte)
	    bis.read(byteBuff, 0, 2);// read 2 so we are at 24 now
	    channels = (short)byteBuff[0];
	    //System.out.println("#channels "+channels+" "+byteBuff[0]);
	    // skip 24 bytes to get sampleRate
	    // (32 bit int)
	    bis.read(byteBuff, 0, 4); // read 4 so now we are at 28
	    sampleRate = bytesToInt(byteBuff, 4);
	    //System.out.println("Sample rate "+sampleRate);
	    // skip 34 bytes to get bits per sample
	    // (1 byte)
	    bis.skip(6); // we were at 28...
	    bis.read(byteBuff, 0, 2);// read 2 so we are at 36 now
	    bitDepth = (short)byteBuff[0];
	    //System.out.println("bit depth "+bitDepth);
	    // convert to word count...
	    bitDepth /= 8;
	    // now start processing the raw data
	    // data starts at byte 36
	    int sampleCount = (int) ((byteCount - 36) / (bitDepth * channels));
	    audioData = new short[sampleCount];
	    int skip = (channels -1) * bitDepth;
	    int sample = 0;
	    // skip a few sample as it sounds like shit
	    bis.skip(bitDepth * 4);
	    while (bis.available () >= (bitDepth+skip)) {
		bis.read(byteBuff, 0, bitDepth);// read 2 so we are at 36 now
		//int val = bytesToInt(byteBuff, bitDepth);
		// resample to 16 bit by casting to a short
		audioData[sample] = (short) bytesToInt(byteBuff, bitDepth);
		bis.skip(skip);
		sample ++;
	    }

	    float secs = (float)sample / (float)sampleRate;
	    //System.out.println("Read "+sample+" samples expected "+sampleCount+" time "+secs+" secs ");      
	    bis.close();


	    // unchop
	    readHead = 0;
	    startPos = 0;
	    // default to 1 sample shift per tick
	    dReadHead = 1;
	    isPlaying = false;
	    isLooping = true;
	    masterVolume = 1;
	} 
	catch (FileNotFoundException e) {

	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void setAnalysing(boolean analysing_) {
	this.analysing = analysing_;
	if (analysing) {// initialise the fft
	    fft = new FFT();
	    fftInd = 0;
	    fftFrame = new float[1024];
	    powerSpectrum = new float[fftFrame.length/2];
	}
    }

    public float getAveragePower() {
	if (analysing) {
	    // calc the average
	    float sum = 0;
	    for (int i=0;i<powerSpectrum.length;i++){
		sum += powerSpectrum[i];
	    }
	    sum /= powerSpectrum.length;
	    return sum;
	}
	else {
	    System.out.println("call setAnalysing to enable power analysis");
	    return 0;
	}
    }
    public float[] getPowerSpectrum() {
	if (analysing) {
	    return powerSpectrum;
	}
	else {
	    System.out.println("call setAnalysing to enable power analysis");
	    return null;
	}
    }

    /** 
     *convert the sent byte array into an int. Assumes little endian byte ordering. 
     *@param bytes - the byte array containing the data
     *@param wordSizeBytes - the number of bytes to read from bytes array
     *@return int - the byte array as an int
     */
    private int bytesToInt(byte[] bytes, int wordSizeBytes) {
	int val = 0;
	for (int i=wordSizeBytes-1; i>=0; i--) {
	    val <<= 8;
	    val |= (int)bytes[i] & 0xFF;
	}
	return val;
    }

    /**
     * Test if this audioplayer is playing right now
     * @return true if it is playing, false otherwise
     */
    public boolean isPlaying() {
	return isPlaying;
    }

    /**
     * Set the loop mode for this audio player
     * @param looping 
     */
    public void setLooping(boolean looping) {
	isLooping = looping;
    }

    /**
     * Move the start pointer of the audio player to the sent time in ms
     * @param timeMs - the time in ms
     */
    public void cue(int timeMs) {
	//startPos = ((timeMs / 1000) * sampleRate) % audioData.length;
	//readHead = startPos;
	//System.out.println("AudioPlayer Cueing to "+timeMs);
	if (timeMs >= 0) {// ignore crazy values
	    readHead = (((float)timeMs / 1000f) * sampleRate) % audioData.length;
	    //System.out.println("Read head went to "+readHead);
	}
    }

    /**
     *  Set the playback speed,
     * @param speed - playback speed where 1 is normal speed, 2 is double speed
     */
    public void speed(float speed) {
	//System.out.println("setting speed to "+speed);
	dReadHead = speed;
    }

    /**
     * Set the master volume of the AudioPlayer
     */

    public void volume(float volume) {
	masterVolume = volume;
    }

    /**
     * Get the length of the audio file in samples
     * @return int - the  length of the audio file in samples
     */
    public int getLength() {
	return audioData.length;
    }
    /**
     * Get the length of the sound in ms, suitable for sending to 'cue'
     */
    public float getLengthMs() {
	return ((float) audioData.length / sampleRate * 1000f);
    }

    /**
     * Start playing the sound. 
     */
    public void play() {
	isPlaying = true;
    }

    /**
     * Stop playing the sound
     */
    public void stop() {
	isPlaying = false;
    }

    /**
     * implementation of the AudioGenerator interface
     */
    public short getSample() {
	if (!isPlaying) {
	    return 0;
	}
	else {
	    short sample;
	    readHead += dReadHead;
	    if (readHead > (audioData.length - 1)) {// got to the end
		//% (float)audioData.length;
		if (isLooping) {// back to the start for loop mode
		    readHead = readHead % (float)audioData.length;
		}
		else {
		    readHead = 0;
		    isPlaying = false;
		}
	    }

	    // linear interpolation here
	    // declaring these at the top...
	    // easy to understand version...
	    //      float x1, x2, y1, y2, x3, y3;
	    x1 = floor(readHead);
	    x2 = x1 + 1;
	    y1 = audioData[(int)x1];
	    y2 = audioData[(int) (x2 % audioData.length)];
	    x3 = readHead;
	    // calc 
	    y3 =  y1 + ((x3 - x1) * (y2 - y1));
	    y3 *= masterVolume;
	    sample = fxChain.getSample((short) y3);
	    if (analysing) {
		// accumulate samples for the fft
		fftFrame[fftInd] = (float)sample / 32768f;
		fftInd ++;
		if (fftInd == fftFrame.length - 1) {// got a frame
		    powerSpectrum = fft.process(fftFrame, true);
		    fftInd = 0;
		}
	    }

	    //return sample;
	    return (short)y3;
	}
    }

    public void setAudioData(short[] audioData) {
	this.audioData = audioData;
    }

    public short[] getAudioData() {
	return audioData;
    }

    public void setDReadHead(float dReadHead) {
	this.dReadHead = dReadHead;
    }

    ///
    //the synth interface
    // 

    public void ramp(float val, float timeMs) {
	fxChain.ramp(val, timeMs);
    } 



    public void setDelayTime(float delayMs) {
	fxChain.setDelayTime( delayMs);
    }

    public void setDelayFeedback(float fb) {
	fxChain.setDelayFeedback(fb);
    }

    public void setFilter(float cutoff, float resonance) {
	fxChain.setFilter( cutoff, resonance);
    }
}

/**
 * This class can play wavetables and includes an fx chain
 */
public class WavetableSynth extends AudioPlayer {

    private short[] sine;
    private short[] saw;
    private short[] wavetable;
    private float sampleRate;

    public WavetableSynth(int size, float sampleRate) {
	super(sampleRate);
	sine = new short[size];
	for (float i = 0; i < sine.length; i++) {
	    float phase;
	    phase = TWO_PI / size * i;
	    sine[(int)i] = (short) (sin(phase) * 32768);
	}
	saw = new short[size];
	for (float i = 0; i<saw.length; i++) {
	    saw[(int)i] = (short) (i / (float)saw.length *32768);
	}

	this.sampleRate = sampleRate;
	setAudioData(sine);
	setLooping(true);
    }

    public void setFrequency(float freq) {
	if (freq > 0) {
	    //System.out.println("freq freq "+freq);
	    setDReadHead((float)getAudioData().length / sampleRate * freq);
	}
    }

    public void loadWaveForm(float[] wavetable_) {
	if (wavetable == null || wavetable_.length != wavetable.length) {
	    // only reallocate if there is a change in length
	    wavetable = new short[wavetable_.length];
	}
	for (int i=0;i<wavetable.length;i++) {
	    wavetable[i] = (short) (wavetable_[i] * 32768);
	}
	setAudioData(wavetable);
    }
}

public interface Synth {
    public void volume(float volume);
    public void ramp(float val, float timeMs);  
    public void setDelayTime(float delayMs);  
    public void setDelayFeedback(float fb);  
    public void setFilter(float cutoff, float resonance);
    public void setAnalysing(boolean analysing);
    public float getAveragePower();
    public float[] getPowerSpectrum();
}

public class AudioThread extends Thread
{
    private int minSize;
    //private AudioTrack track;
    private short[] bufferS;
    private byte[] bOutput;
    private ArrayList audioGens;
    private boolean running;

    private FFT fft;
    private float[] fftFrame;
    private SourceDataLine sourceDataLine;
    private int blockSize;

    public AudioThread(float samplingRate, int blockSize) {
	this(samplingRate, blockSize, false);
    }

    public AudioThread(float samplingRate, int blockSize, boolean enableFFT)
    {
	this.blockSize = blockSize;
	audioGens = new ArrayList();
	// we'll do our dsp in shorts
	bufferS = new short[blockSize];
	// but we'll convert to bytes when sending to the sound card
	bOutput = new byte[blockSize * 2];
	AudioFormat audioFormat = new AudioFormat(samplingRate, 16, 1, true, false);
	DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
	    
	sourceDataLine = null;
	// here we try to initialise the audio system. try catch is exception handling, i.e. 
	// dealing with things not working as expected
	try {
	    sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
	    sourceDataLine.open(audioFormat, bOutput.length);
	    sourceDataLine.start();
	    running = true;
	} catch (LineUnavailableException lue) {
	    // it went wrong!
	    lue.printStackTrace(System.err);
	    System.out.println("Could not initialise audio. check above stack trace for more info");
	    //System.exit(1);
	}


	if (enableFFT) {
	    try {
		fft = new FFT();
	    }
	    catch(Exception e) {
		System.out.println("Error setting up the audio analyzer");
		e.printStackTrace();
	    }
	}
    }

    // overidden from Thread
    public void run() {
	running = true;
	while (running) {
	    //System.out.println("AudioThread : ags  "+audioGens.size());
	    for (int i=0;i<bufferS.length;i++) {
		// we add up using a 32bit int
		// to prevent clipping
		int val = 0;
		if (audioGens.size() > 0) {
		    for (int j=0;j<audioGens.size(); j++) {
			AudioGenerator ag = (AudioGenerator)audioGens.get(j);
			val += ag.getSample();
		    }
		    val /= audioGens.size();
		}
		bufferS[i] = (short) val;
	    }
	    // send it to the audio device!
	    sourceDataLine.write(shortsToBytes(bufferS, bOutput), 0, bOutput.length);
	}
    }
	
    public void addAudioGenerator(AudioGenerator ag) {
	audioGens.add(ag);
    }

    /**
     * converts an array of 16 bit samples to bytes
     * in little-endian (low-byte, high-byte) format.
     */
    private byte[] shortsToBytes(short[] sData, byte[] bData) {
	int index = 0;
	short sval;
	for (int i = 0; i < sData.length; i++) {
	    //short sval = (short) (fData[j][i] * ShortMaxValueAsFloat);
	    sval = sData[i];
	    bData[index++] = (byte) (sval & 0x00FF);
	    bData[index++] = (byte) ((sval & 0xFF00) >> 8);
	}
	return bData;
    }

    /**
     * Returns a recent snapshot of the power spectrum 
     */
    public float[] getPowerSpectrum() {
	// process the last buffer that was calculated
	if (fftFrame == null) {
	    fftFrame = new float[bufferS.length];
	}
	for (int i=0;i<fftFrame.length;i++) {
	    fftFrame[i] = ((float) bufferS[i] / 32768f);
	}
	return fft.process(fftFrame, true);
	//return powerSpectrum;
    }
}

/**
 * Implement this interface so the AudioThread can request samples from you
 */
public interface AudioGenerator {
    /** AudioThread calls this when it wants a sample */
    public short getSample();
}


public class FXChain  {
    private float currentAmp;
    private float dAmp;
    private float targetAmp;
    private boolean goingUp;
    private Filter filter;

    private float[] dLine;   

    private float sampleRate;

    public FXChain(float sampleRate_) {
	sampleRate = sampleRate_;
	currentAmp = 1;
	dAmp = 0;
	// filter = new MickFilter(sampleRate);
	filter = new RLPF(sampleRate);

	//filter.setFilter(0.1, 0.1);
    }

    public void ramp(float val, float timeMs) {
	// calc the dAmp;
	// - change per ms
	targetAmp = val;
	dAmp = (targetAmp - currentAmp) / (timeMs / 1000 * sampleRate);
	if (targetAmp > currentAmp) {
	    goingUp = true;
	}
	else {
	    goingUp = false;
	}
    }


    public void setDelayTime(float delayMs) {
    }

    public void setDelayFeedback(float fb) {
    }

    public void volume(float volume) {
    }


    public short getSample(short input) {
	float in;
	in = (float) input / 32768;// -1 to 1

	in =  filter.applyFilter(in);
	if (goingUp && currentAmp < targetAmp) {
	    currentAmp += dAmp;
	}
	else if (!goingUp && currentAmp > targetAmp) {
	    currentAmp += dAmp;
	}  

	if (currentAmp > 1) {
	    currentAmp = 1;
	}
	if (currentAmp < 0) {
	    currentAmp = 0;
	}  
	in *= currentAmp;  
	return (short) (in * 32768);
    }

    public void setFilter(float f, float r) {
	filter.setFilter(f, r);
    }
}


// /**
//  * Represents an audio source is streamed as opposed to being completely loaded (as WavSource is)
//  */
// public class AudioStreamPlayer {
// 	/** a class from the android API*/
// 	private MediaPlayer mediaPlayer;
// 	/** a class from the android API*/
// 	private Visualizer viz; 
// 	private byte[] waveformBuffer;
// 	private byte[] fftBuffer;
// 	private byte[] powerSpectrum;

// 	/**
// 	 * create a stream source from the sent url 
// 	 */
// 	public AudioStreamPlayer(String url) {
// 	    try {
// 		mediaPlayer = new MediaPlayer();
// 		//mp.setAuxEffectSendLevel(1);
// 		mediaPlayer.setLooping(true);

// 		// try to parse the URL... if that fails, we assume it
// 		// is a local file in the assets folder
// 		try {
// 		    URL uRL = new URL(url);
// 		    mediaPlayer.setDataSource(url);
// 		}
// 		catch (MalformedURLException eek) {
// 		    // couldn't parse the url, assume its a local file
// 		    AssetFileDescriptor afd = getAssets().openFd(url);
// 		    //mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
// 		    mediaPlayer.setDataSource(afd.getFileDescriptor());
// 		    afd.close();
// 		}

// 		mediaPlayer.prepare();
// 		//mediaPlayer.start();
// 		//System.out.println("Created audio with id "+mediaPlayer.getAudioSessionId());
// 		viz = new Visualizer(mediaPlayer.getAudioSessionId());
// 		viz.setEnabled(true);
// 		waveformBuffer = new byte[viz.getCaptureSize()];
// 		fftBuffer = new byte[viz.getCaptureSize()/2];
// 		powerSpectrum = new byte[viz.getCaptureSize()/2];
// 	    }
// 	    catch (Exception e) {
// 		System.out.println("StreamSource could not be initialised. Check url... "+url+ " and that you have added the permission INTERNET, RECORD_AUDIO and MODIFY_AUDIO_SETTINGS to the manifest,");
// 		e.printStackTrace();
// 	    }
// 	}

// 	public void play() {
// 	    mediaPlayer.start();
// 	}

// 	public int getLengthMs() {
// 	    return mediaPlayer.getDuration();
// 	}

// 	public void cue(float timeMs) {
// 	    if (timeMs >= 0 && timeMs < getLengthMs()) {// ignore crazy values
// 		mediaPlayer.seekTo((int)timeMs);
// 	    }
// 	}

// 	/**
// 	 * Returns a recent snapshot of the power spectrum as 8 bit values
// 	 */
// 	public byte[] getPowerSpectrum() {
// 	    // calculate the spectrum
// 	    viz.getFft(fftBuffer);
// 	    short real, imag;
// 	    for (int i=2;i<fftBuffer.length;i+=2) {
// 		real = (short) fftBuffer[i];
// 		imag = (short) fftBuffer[i+1];
// 		powerSpectrum[i/2] = (byte) ((real * real)  + (imag * imag));
// 	    }
// 	    return powerSpectrum;
// 	}

// 	/**
// 	 * Returns a recent snapshot of the waveform being played 
// 	 */
// 	public byte[] getWaveForm() {
// 	    // retrieve the waveform
// 	    viz.getWaveForm(waveformBuffer);
// 	    return waveformBuffer;
// 	}
// } 

/**
 * Use this class to retrieve data about the movement of the device
 */
public class Accelerometer  {
    //private SensorManager sensorManager;
    //private Sensor accelerometer;
    private float[] values;

    public Accelerometer() {
	//sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	//accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	//sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	values = new float[3];
	System.out.println("Java accelerometer will generate values of zero!");
    }

    public float[] getValues() {
	return values;
    }

    public float getX() {
	return values[0];
    }

    public float getY() {
	return values[1];
    }

    public float getZ() {
	return values[2];
    }

}

public interface Filter {
    public void setFilter(float f, float r);
    public float applyFilter(float in);
}

/** https://github.com/supercollider/supercollider/blob/master/server/plugins/FilterUGens.cpp */

public class RLPF implements Filter {
    float a0, b1, b2, y1, y2;
    float freq;
    float reson;
    float sampleRate;
    boolean changed;

    public RLPF(float sampleRate_) {
	this.sampleRate = sampleRate_;
	reset();
	this.setFilter(sampleRate / 4, 0.01f);
    }
    private void reset() {
	a0 = 0.f;
	b1 = 0.f;
	b2 = 0.f;
	y1 = 0.f;
	y2 = 0.f;
    }
    /** f is in the range 0-sampleRate/2 */
    public void setFilter(float f, float r) {
	// constrain 
	// limit to 0-1 
	f = constrain(f, 0, sampleRate/4);
	r = constrain(r, 0, 1);
	// invert so high r -> high resonance!
	r = 1-r;
	// remap to appropriate ranges
	f = map(f, 0f, sampleRate/4, 30f, sampleRate / 4);
	r = map(r, 0f, 1f, 0.005f, 2f);

	System.out.println("rlpf: f "+f+" r "+r);

	this.freq = f * TWO_PI / sampleRate;
	this.reson = r;
	changed = true;
    }

    public float applyFilter(float in) {
	float y0;
	if (changed) {
	    float D = tan(freq * reson * 0.5f);
	    float C = ((1.f-D)/(1.f+D));
	    float cosf = cos(freq);
	    b1 = (1.f + C) * cosf;
	    b2 = -C;
	    a0 = (1.f + C - b1) * .25f;
	    changed = false;
	}
	y0 = a0 * in + b1 * y1 + b2 * y2;
	y2 = y1;
	y1 = y0;
	if (Float.isNaN(y0)) {
	    reset();
	}
	return y0;
    }
}

/** https://github.com/micknoise/Maximilian/blob/master/maximilian.cpp */

class MickFilter implements Filter {

    private float f, res;
    private float cutoff, z, c, x, y, out;
    private float sampleRate;

    MickFilter(float sampleRate) {
	this.sampleRate = sampleRate;
    }

    public void setFilter(float f, float r) {
	f = constrain(f, 0, 1);
	res = constrain(r, 0, 1);
	f = map(f, 0, 1, 25, sampleRate / 4);
	r = map(r, 0, 1, 1, 25);
	this.f = f;
	this.res = r;    

	//System.out.println("mickF: f "+f+" r "+r);
    }
    public float applyFilter(float in) {
	return lores(in, f, res);
    }

    public float lores(float input, float cutoff1, float resonance) {
	//cutoff=cutoff1*0.5;
	//if (cutoff<10) cutoff=10;
	//if (cutoff>(sampleRate*0.5)) cutoff=(sampleRate*0.5);
	//if (resonance<1.) resonance = 1.;

	//if (resonance>2.4) resonance = 2.4;
	z=cos(TWO_PI*cutoff/sampleRate);
	c=2-2*z;
	float r=(sqrt(2.0f)*sqrt(-pow((z-1.0f), 3.0f))+resonance*(z-1))/(resonance*(z-1));
	x=x+(input-y)*c;
	y=y+x;
	x=x*r;
	out=y;
	return out;
    }
}


/*
 * This file is part of Beads. See http://www.beadsproject.net for all information.
 * CREDIT: This class uses portions of code taken from MPEG7AudioEnc. See readme/CREDITS.txt.
 */

/**
 * FFT performs a Fast Fourier Transform and forwards the complex data to any listeners. 
 * The complex data is a float of the form float[2][frameSize], with real and imaginary 
 * parts stored respectively.
 * 
 * @beads.category analysis
 */
public class FFT {

    /** The real part. */
    protected float[] fftReal;

    /** The imaginary part. */
    protected float[] fftImag;

    private float[] dataCopy = null;
    private float[][] features;
    private float[] powers;
    private int numFeatures;

    /**
     * Instantiates a new FFT.
     */
    public FFT() {
	features = new float[2][];
    }

    /* (non-Javadoc)
     * @see com.olliebown.beads.core.UGen#calculateBuffer()
     */
    public float[] process(float[] data, boolean direction) {
	if (powers == null) powers = new float[data.length/2];
	if (dataCopy==null || dataCopy.length!=data.length)
	    dataCopy = new float[data.length];
	System.arraycopy(data, 0, dataCopy, 0, data.length);

	fft(dataCopy, dataCopy.length, direction);
	numFeatures = dataCopy.length;
	fftReal = calculateReal(dataCopy, dataCopy.length);
	fftImag = calculateImaginary(dataCopy, dataCopy.length);
	features[0] = fftReal;
	features[1] = fftImag;
	// now calc the powers
	return specToPowers(fftReal, fftImag, powers);
    }

    public float[] specToPowers(float[] real, float[] imag, float[] powers) {
	float re, im;
	double pow;
	for (int i=0;i<powers.length;i++) {
	    //real = spectrum[i][j].re();
	    //imag = spectrum[i][j].im();
	    re = real[i];
	    im = imag[i];
	    powers[i] = (re*re + im * im);
	    powers[i] = (float) Math.sqrt(powers[i]) / 10;
	    // convert to dB
	    pow = (double) powers[i];
	    powers[i] = (float)(10 *  Math.log10(pow * pow)); // (-100 - 100)
	    powers[i] = (powers[i] + 100) * 0.005f; // 0-1
	}
	return powers;
    }

    /**
     * The frequency corresponding to a specific bin 
     * 
     * @param samplingFrequency The Sampling Frequency of the AudioContext
     * @param blockSize The size of the block analysed
     * @param binNumber 
     */
    public  float binFrequency(float samplingFrequency, int blockSize, float binNumber)
    {    
	return binNumber*samplingFrequency/blockSize;
    }

    /**
     * Returns the average bin number corresponding to a particular frequency.
     * Note: This function returns a float. Take the Math.round() of the returned value to get an integral bin number. 
     * 
     * @param samplingFrequency The Sampling Frequency of the AudioContext
     * @param blockSize The size of the fft block
     * @param freq  The frequency
     */

    public  float binNumber(float samplingFrequency, int blockSize, float freq)
    {
	return blockSize*freq/samplingFrequency;
    }

    /** The nyquist frequency for this samplingFrequency 
     * 
     * @params samplingFrequency the sample
     */
    public  float nyquist(float samplingFrequency)
    {
	return samplingFrequency/2;
    }

    /*
     * All of the code below this line is taken from Holger Crysandt's MPEG7AudioEnc project.
     * See http://mpeg7audioenc.sourceforge.net/copyright.html for license and copyright.
     */

    /**
     * Gets the real part from the complex spectrum.
     * 
     * @param spectrum
     *            complex spectrum.
     * @param length 
     *       length of data to use.
     * 
     * @return real part of given length of complex spectrum.
     */
    protected  float[] calculateReal(float[] spectrum, int length) {
	float[] real = new float[length];
	real[0] = spectrum[0];
	real[real.length/2] = spectrum[1];
	for (int i=1, j=real.length-1; i<j; ++i, --j)
	    real[j] = real[i] = spectrum[2*i];
	return real;
    }

    /**
     * Gets the imaginary part from the complex spectrum.
     * 
     * @param spectrum
     *            complex spectrum.
     * @param length 
     *       length of data to use.
     * 
     * @return imaginary part of given length of complex spectrum.
     */
    protected  float[] calculateImaginary(float[] spectrum, int length) {
	float[] imag = new float[length];
	for (int i=1, j=imag.length-1; i<j; ++i, --j)
	    imag[i] = -(imag[j] = spectrum[2*i+1]);
	return imag;
    }

    /**
     * Perform FFT on data with given length, regular or inverse.
     * 
     * @param data the data
     * @param n the length
     * @param isign true for regular, false for inverse.
     */
    protected  void fft(float[] data, int n, boolean isign) {
	float c1 = 0.5f; 
	float c2, h1r, h1i, h2r, h2i;
	double wr, wi, wpr, wpi, wtemp;
	double theta = 3.141592653589793f/(n>>1);
	if (isign) {
	    c2 = -.5f;
	    four1(data, n>>1, true);
	} 
	else {
	    c2 = .5f;
	    theta = -theta;
	}
	wtemp = Math.sin(.5f*theta);
	wpr = -2.f*wtemp*wtemp;
	wpi = Math.sin(theta);
	wr = 1.f + wpr;
	wi = wpi;
	int np3 = n + 3;
	for (int i=2,imax = n >> 2, i1, i2, i3, i4; i <= imax; ++i) {
	    /** @TODO this can be optimized */
	    i4 = 1 + (i3 = np3 - (i2 = 1 + (i1 = i + i - 1)));
	    --i4; 
	    --i2; 
	    --i3; 
	    --i1; 
	    h1i =  c1*(data[i2] - data[i4]);
	    h2r = -c2*(data[i2] + data[i4]);
	    h1r =  c1*(data[i1] + data[i3]);
	    h2i =  c2*(data[i1] - data[i3]);
	    data[i1] = (float) ( h1r + wr*h2r - wi*h2i);
	    data[i2] = (float) ( h1i + wr*h2i + wi*h2r);
	    data[i3] = (float) ( h1r - wr*h2r + wi*h2i);
	    data[i4] = (float) (-h1i + wr*h2i + wi*h2r);
	    wr = (wtemp=wr)*wpr - wi*wpi + wr;
	    wi = wi*wpr + wtemp*wpi + wi;
	}
	if (isign) {
	    float tmp = data[0]; 
	    data[0] += data[1];
	    data[1] = tmp - data[1];
	} 
	else {
	    float tmp = data[0];
	    data[0] = c1 * (tmp + data[1]);
	    data[1] = c1 * (tmp - data[1]);
	    four1(data, n>>1, false);
	}
    }

    /**
     * four1 algorithm.
     * 
     * @param data
     *            the data.
     * @param nn
     *            the nn.
     * @param isign
     *            regular or inverse.
     */
    private  void four1(float data[], int nn, boolean isign) {
	int n, mmax, istep;
	double wtemp, wr, wpr, wpi, wi, theta;
	float tempr, tempi;

	n = nn << 1;        
	for (int i = 1, j = 1; i < n; i += 2) {
	    if (j > i) {
		// SWAP(data[j], data[i]);
		float swap = data[j-1];
		data[j-1] = data[i-1];
		data[i-1] = swap;
		// SWAP(data[j+1], data[i+1]);
		swap = data[j];
		data[j] = data[i]; 
		data[i] = swap;
	    }      
	    int m = n >> 1;
	    while (m >= 2 && j > m) {
		j -= m;
		m >>= 1;
	    }
	    j += m;
	}
	mmax = 2;
	while (n > mmax) {
	    istep = mmax << 1;
	    theta = 6.28318530717959f / mmax;
	    if (!isign)
		theta = -theta;
	    wtemp = Math.sin(0.5f * theta);
	    wpr = -2.0f * wtemp * wtemp;
	    wpi = Math.sin(theta);
	    wr = 1.0f;
	    wi = 0.0f;
	    for (int m = 1; m < mmax; m += 2) {
		for (int i = m; i <= n; i += istep) {
		    int j = i + mmax;
		    tempr = (float) (wr * data[j-1] - wi * data[j]);  
		    tempi = (float) (wr * data[j]   + wi * data[j-1]);  
		    data[j-1] = data[i-1] - tempr;
		    data[j]   = data[i] - tempi;
		    data[i-1] += tempr;
		    data[i]   += tempi;
		}
		wr = (wtemp = wr) * wpr - wi * wpi + wr;
		wi = wi * wpr + wtemp * wpi + wi;
	    }
	    mmax = istep;
	}
    }
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
  
  // stuff for sound
  boolean played_sound;
  
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
    speed_coeff = 0.05f;  
    x_dest = x;
    y_dest = x;  
        
    is_changing_color = false;
    
    returning_to_original = false;
    
    // stuff for sound
    played_sound = false;
    
    
    
  }
  
  
  
  // draws the character in its position
  public void draw()
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
  public void update()
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
      colour.change_to(destination_color, 0.02f);
    }  
  }
  
  
  // when called, the letter keep exapnding or shrinking to that size
  public void change_size_to(float s)
  {
    is_changing_size = true;
    dest_size = s;
  }
  
  
  // changes the color to another color
  public void change_color_to(Color c2)
  {
    destination_color = new Color(c2.r, c2.g, c2.b, c2.opacity);
    is_changing_color = true;
  }
  
  // returns true if the mouse is over on the letter
  public boolean is_mouse_over()
  {
 
    float x_dist = abs(mouseX - x_pos) ;
    float y_dist = abs(mouseY - y_pos) ;
    
    return (x_dist < size * character.length() * 0.7f / 2 && y_dist < size * 1.1f / 2);
  }
  
  // manges stuff done when mouse is over
  // change size to a destination size and make the letter more bouncy
  public void handle_mouse_over(float original_size, float dest_size)
  {
    if(is_mouse_over())
    {
      if (sound_on && played_sound == false && is_hidden == false && wait_sound == 0)
      {
        alphapet[PApplet.parseInt(PApplet.parseChar(character.charAt(0))) - 65].play();
        played_sound = true;
        wait_sound = 40;
      }
      change_size_to(dest_size);
      if(bounce_more)
        bounce_rebound_vel = - HEIGHT / 80;
      returning_to_original = true;
        
    }
    else
    {
        if(played_sound == true)
          played_sound = false;
       if(returning_to_original)
      {
        change_size_to(original_size);
        bounce_rebound_vel =  - HEIGHT / 120;
        returning_to_original = false;
      }
    }    
  }
  
  public void travel_to(float x, float y)
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
    scroll_bar = new Scroll_levels(9, WIDTH * 0.22f, HEIGHT * 0.1f, WIDTH * 0.75f, HEIGHT * 0.1f, color(30, 50, 120, 250), color(200, 30, 30), color(30, 50, 80, 100));
    scroll_bar.value = 0.5f;
    number_of_choices = 4;
    number_of_hidden = 3;
    level_change_counter = 0;
    
    score = 2;
    score_stars = new Star[100];
    score_stars[0] = new Star(stars[PApplet.parseInt(random(stars.length))], random(WIDTH), random(HEIGHT));
    score_stars[1] = new Star(stars[PApplet.parseInt(random(stars.length))], random(WIDTH), random(HEIGHT));

  }
  
  // starts a round by
  // - generating random sequence of letters, some are hidden
  // search for the first hidden one
  // put a ^ under the first hidden letter
  // generate answers for the first letter
  public void start_round()
  {
    // Calculate te difficulty
    number_of_choices = PApplet.parseInt(map(scroll_bar.value , 0, 1, 2, 7));
    number_of_hidden = PApplet.parseInt(map(scroll_bar.value , 0, 1, 1, 5));
    

    
    // generate a sequence
    question = generate_letters_sequence(6, number_of_hidden, first_letter);
    
    // find the first hidden letter
    Letter hidden = first_hidden(question);
    pointer.travel_to(hidden.x_pos, hidden.y_pos + 100);
 
    // generate answers for teh first letter
    choices = get_choices(hidden.character, number_of_choices);    
  }
  
  
  // returns the first letter that is hidden in an array of letters
  public Letter first_hidden(Letter[] letters)
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
  public int first_hidden_index(Letter[] letters)
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
  public void handle_choice(Letter choice)
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
        junk[junk_size].speed_coeff = 0.015f;    
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
          score_stars[score] = new  Star(stars[PApplet.parseInt(random(stars.length))], -WIDTH, -HEIGHT);
          score_stars[score].travel_to(random(WIDTH), random(HEIGHT*0.8f));
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
              scroll_bar.value += 1.0f/ scroll_bar.number_of_levels;
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
  public void draw_round_ended()
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
    text("Won the round :D", WIDTH / 2, HEIGHT * 0.7f);
    
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
        junk[junk_size].travel_to(junk[junk_size].x_pos + WIDTH*1.1f, junk[junk_size].y_pos);
        junk_size++;
      }
      
      start_round();

    }
  }
   
   
  // draws an array of letters represeting the question
  public void draw_questions()
  {
    
    // draw the letters
    for(int i = 0; i < state.question.length; i++)
    {    
      question[i].update();
      question[i].draw();
      question[i].handle_mouse_over(default_size, default_size*1.5f);
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
    text("Level : " + str(PApplet.parseInt(scroll_bar.number_of_levels * scroll_bar.value)+1), WIDTH * 0.1f, HEIGHT * 0.1f);
    if (level_change_counter > 0)
      level_change_counter --;
    if((number_of_choices != PApplet.parseInt(map(scroll_bar.value , 0, 1, 2, 7)) ||  number_of_hidden != PApplet.parseInt(map(scroll_bar.value , 0, 1, 1, 5))) && level_change_counter <= 0)
    {
      start_round();
      level_change_counter = 20;
    }
  }
  
  
  // draw the aray of choices
  public void draw_choices()
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
  public void draw_stars()
  {
    for (int i = 0; i < score; i++)
    {
      score_stars[i].update();
      score_stars[i].draw();
    }
  }
}
// generates a list of n Letter classes in a sequence
public Letter[] generate_letters_sequence(int n, int number_hidden, int first)
{
  Letter[] result = new Letter[n];
  
  // get the first character
  char first_char = PApplet.parseChar(first);
  result[0] = new Letter(str(first_char), WIDTH / 10, HEIGHT * 0.3f, default_size, default_colour);
  result[0].is_shaky = false;
  result[0].bounce_more = false;
  result[0].is_bouncy = false;
  
  

  // generate next character
  for (int i = 0; i < n - 1; i++)
  {
    result[i + 1] = new Letter(str(PApplet.parseChar(first + i + 1)), WIDTH / 10 + (i + 1)*WIDTH / n, HEIGHT * 0.3f, default_size, default_colour); 
    //result[i + 1].travel_to(WIDTH / 10 + (i + 1)*width / n, HEIGHT * 0.3);
    result[i + 1].bounce_more = false;
    result[i + 1].is_shaky = false;    
    result[i + 1].is_bouncy = false;
  }
  
  hide_some(result,  number_hidden);
  return result;
}


// hides some objects in a list of Letters
public void hide_some(Letter[] characters, int number_hidden)
{
  // hide some character
  int n = characters.length;
  int number_hidden_req = number_hidden;
  number_hidden_req = constrain(number_hidden_req, 0, n );
  int number_hid = 0;
  
  while(number_hid < number_hidden_req)
  {
    int index = PApplet.parseInt(random(n - 1) + 1);
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
public Letter[] get_choices(String c, int n)
{
  if (n <= 1)
    n+= 2;

  Letter[] result = new Letter[n];
  // get the right character
  Color random_color = choices_colors[PApplet.parseInt(random(number_of_colours - 4))];
  result[0] = new Letter(c, WIDTH / 10, HEIGHT *3 / 4, default_size, new Color(random_color.r, random_color.g, random_color.b, random_color.opacity));
  
  // get some random choices
  int count = 0;
  while(count < n - 1)
  {
    int random_int = PApplet.parseInt(random(65, 65 + 26));
    char random_char = PApplet.parseChar(random_int);
    String random_char_str = str(random_char);
    if (random_char != c. charAt(0))
    {
      count++;
      random_color = choices_colors[PApplet.parseInt(random(number_of_colours - 4))];
      result[count] = new Letter(str(random_char), WIDTH / 10 + count*WIDTH / n, HEIGHT *3 / 4, default_size, new Color(random_color.r, random_color.g, random_color.b, random_color.opacity));
    } 
  }
  
  // replace the right choice(the first one) with any one other
  int second_index = PApplet.parseInt(random(n));
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
  
  int c;
  
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
  public void change_to(Color C2, float percentage)
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
public Color[] color_list_2(int n, Color[] c, float[] probabilities)
{
  Color[] result = new Color[n];
  int size = 0;
  
  for(int i = 0; i < c.length; i++)
  {
    // for each color, add it k times to the result where k is
    // teh number of its occurence
    float f_number_of_occurence = probabilities[i] * n;
    int number_of_occurence = PApplet.parseInt(f_number_of_occurence);
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
float[] probabilities = { 0.1f ,0.9f / 6, 0.9f / 6, 0.9f / 6, 0.9f / 6, 0.9f / 6, 0.9f / 6};
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
  int point_color;
  int ball_secondary_color;
  int bar_color;
  
  // stuff to adjust it
  boolean adjusting;
  
  
  Scroll_levels()
  {
  }
  
  
  Scroll_levels(int n_levels, float x_pos1, float y_pos1, float x_pos2, float y_pos2,  int colour_of_point, int mouse_over_color, int color_of_bar)
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
    current_level = PApplet.parseInt(number_of_levels / 2);
  }
  
  
  // draws the bar as a line with a point 
  public void draw()
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
  public void adjust()
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
    float unreal_level = PApplet.parseInt(real_level) ;
    value =  unreal_level / number_of_levels;
    
  }
  
  // returns true if the user pressed on the point 
  public boolean is_mouse_over()
  {
    float ball_x = x1 + (x2 - x1) * value;
    float ball_y = y1 + (y2 - y1) * value;
    
    return (dist(ball_x, ball_y, mouseX, mouseY) < bar_width * 3);
  }
  
  // if the mouse is pressed sees it's affecting the bar or not
  // if it's affectign then pyt it into adjusting mode
  public void handle_mouse_pressed()
  {
    if (is_mouse_over() && mousePressed == true)
    {
      adjusting = true;
    }
  }
  
}
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
    speed_coeff = 0.1f;  
    x_dest = x;
    y_dest = x;  
        

    
    
  }
  
  
  
  // draws the character in its position
  public void draw()
  {
    textAlign(CENTER, CENTER);
    
    pushMatrix();
    translate(x_pos, y_pos);
    rotate(angle);
    image(star, 0, 0);        
    popMatrix();
  
  }
  
  
  // updates the position and angle of the character
  public void update()
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
  

  public void travel_to(float x, float y)
  {
        if(sound_on)
          star_sound.play();
    is_travelling = true;
    x_dest = x;
    y_dest = y;

  }
  
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--hide-stop", "Maram_A_B_C" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

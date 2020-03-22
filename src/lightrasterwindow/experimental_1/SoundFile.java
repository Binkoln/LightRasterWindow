package lightrasterwindow.experimental_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundFile {
	Clip c;
	public SoundFile(String path) {
		if(!path.endsWith(".wav")) {System.err.println("LIB FATAL ERROR: Library support only wav files!!!");System.exit(-1);}
		try {
			AudioInputStream as = AudioSystem.getAudioInputStream(SoundFile.class.getResourceAsStream(path));
			c = AudioSystem.getClip();
			c.open(as);
			
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void play() {
		c.stop();
		c.setFramePosition(0);
		c.start();
	}
	
	public void setVolume(float var) {
		FloatControl volume = (FloatControl)c.getControl(FloatControl.Type.MASTER_GAIN);
		float range = volume.getMaximum() - volume.getMinimum();
		float gain = (range * var) + volume.getMinimum();
		volume.setValue(gain);
	}
	
	public void stop() {
		c.stop();
		c.setFramePosition(0);
	}
	
	public boolean isPlaying() {
		return c.isActive();
	}
}

package jp.nkn0t.mari0.util;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BGManager {
	public static final int MID = 2;
	public static final int WAV = 4;
	private Clip clip;
	private File bgmFile;
	private String extension;

	public BGManager(String fileName) {
		bgmFile = new File(fileName);
		extension = fileName.substring(fileName.indexOf('.') + 1, fileName.length());
	}

	public void play() {
		Sequencer sequencer;

		switch (getExtensionType()) {
			case MID :{
				try{
					Sequence s = MidiSystem.getSequence(bgmFile);
					sequencer  = MidiSystem.getSequencer(false);
					sequencer.open();
					sequencer.setSequence(s);

					sequencer.setLoopCount(2);

					sequencer.start();
				}catch(Exception e) {
					e.printStackTrace();
					return;
				}
				break;
			}

			case WAV : {
				try {
					AudioInputStream ais = AudioSystem.getAudioInputStream(bgmFile);
					clip = AudioSystem.getClip();
					clip.open(ais);
					clip.start();
					ais.close();
				} catch (IOException e){
					e.printStackTrace();
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private int getExtensionType() {
		if (extension.equals("mid")) {
			return MID;
		} else if (extension.equals("wav")){
			return WAV;
		}
		return 0;
	}

	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
		} else {
			return;
		}
	}
}
